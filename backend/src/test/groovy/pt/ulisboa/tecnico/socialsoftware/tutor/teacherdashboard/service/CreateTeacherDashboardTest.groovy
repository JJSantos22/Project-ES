package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import spock.lang.Unroll

@DataJpaTest
    class CreateTeacherDashboardTest extends SpockTest {
    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
    }

    def "create an empty dashboard"() {
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        when: "a dashboard is created"
        teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "an empty dashboard is created"
        teacherDashboardRepository.count() == 1L
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getTeacher().getId() == teacher.getId()

        and: "the teacher has a reference for the dashboard"
        teacher.getDashboards().size() == 1
        teacher.getDashboards().contains(result)
    }

    def "cannot create multiple dashboards for a teacher on a course execution"() {
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        and: "an empty dashboard for the teacher"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        when: "a second dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L

        and: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_ALREADY_HAS_DASHBOARD
    }

    def "cannot create a dashboard for a user that does not belong to the course execution"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "exception is thrown"        
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_NO_COURSE_EXECUTION
    }

    @Unroll
    def "cannot create a dashboard with courseExecutionId=#courseExecutionId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecutionId, teacher.getId())

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND

        where:
        courseExecutionId << [0, 100]
    }

    @Unroll
    def "cannot create a dashboard with teacherId=#teacherId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacherId)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.USER_NOT_FOUND

        where:
        teacherId << [0, 100]
    }

    def "create multiple dashboards and verify if they have stats and if they were well stored"(){
        given: "A Teacher and his course executions"
        def courseExecution2 = new CourseExecution(externalCourseExecution.getCourse(), COURSE_1_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY.minusYears(2))
        courseExecutionRepository.save(courseExecution2)
        def courseExecution3 = new CourseExecution(externalCourseExecution.getCourse(), COURSE_2_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY.minusYears(1))
        courseExecutionRepository.save(courseExecution3)
        def courseExecution4 = new CourseExecution(externalCourseExecution.getCourse(), COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY.minusYears(5))
        courseExecutionRepository.save(courseExecution4)
        teacher.addCourse(externalCourseExecution)
        teacher.addCourse(courseExecution2)
        teacher.addCourse(courseExecution3)
        teacher.addCourse(courseExecution4)

        when: "We create a dashboard for each course execution"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())
        teacherDashboardService.createTeacherDashboard(courseExecution2.getId(), teacher.getId())
        teacherDashboardService.createTeacherDashboard(courseExecution3.getId(), teacher.getId())
        teacherDashboardService.createTeacherDashboard(courseExecution4.getId(), teacher.getId())
        

        then: "There are 4 dashboards stored in the database"
        teacherDashboardRepository.count() == 4L
        

        and: "They were well stored"
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getTeacher().getId() == teacher.getId()
        def result2 = teacherDashboardRepository.findAll().get(1)
        result2.getId() != 0
        result2.getCourseExecution().getId() == courseExecution2.getId()
        result2.getTeacher().getId() == teacher.getId()
        def result3 = teacherDashboardRepository.findAll().get(2)
        result3.getId() != 0
        result3.getCourseExecution().getId() == courseExecution3.getId()
        result3.getTeacher().getId() == teacher.getId()
        def result4 = teacherDashboardRepository.findAll().get(3)
        result4.getId() != 0
        result4.getCourseExecution().getId() == courseExecution4.getId()
        result4.getTeacher().getId() == teacher.getId()


        and: "The dashboards have the correct number of questionStats in their QuestionStats list"
        result.getQuestionStats().size() == 3
        result2.getQuestionStats().size() == 2
        result3.getQuestionStats().size() == 3
        result4.getQuestionStats().size() == 1

        and: "All questionStats have the correct course executions"
        result.getQuestionStats().get(0).getCourseExecution().getEndDate() == externalCourseExecution.getEndDate()
        result.getQuestionStats().get(1).getCourseExecution().getEndDate() == courseExecution3.getEndDate()
        result.getQuestionStats().get(2).getCourseExecution().getEndDate() == courseExecution2.getEndDate()
        result2.getQuestionStats().get(0).getCourseExecution().getEndDate() == courseExecution2.getEndDate()
        result2.getQuestionStats().get(1).getCourseExecution().getEndDate() == courseExecution4.getEndDate()
        result3.getQuestionStats().get(0).getCourseExecution().getEndDate() == courseExecution3.getEndDate()
        result3.getQuestionStats().get(1).getCourseExecution().getEndDate() == courseExecution2.getEndDate()
        result3.getQuestionStats().get(2).getCourseExecution().getEndDate() == courseExecution4.getEndDate()
        result4.getQuestionStats().get(0).getCourseExecution().getEndDate() == courseExecution4.getEndDate()

        when: "We add a new teacher dashboard to the teacher"
        teacher.addDashboard(teacherDashboardRepository.findAll().get(0))
        teacher.addDashboard(teacherDashboardRepository.findAll().get(1))
        teacher.addDashboard(teacherDashboardRepository.findAll().get(2))
        teacher.addDashboard(teacherDashboardRepository.findAll().get(3))

        then: "The teacher has 4 dashboards"
        teacher.getDashboards().size() == 4
    }

    def "create 2 dashboard and verify if they have stats and if they were well stored"(){
        given: "A Teacher and his course executions"
        def courseExecution2 = new CourseExecution(externalCourseExecution.getCourse(), COURSE_1_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY.minusYears(2))
        courseExecutionRepository.save(courseExecution2)
        teacher.addCourse(externalCourseExecution)
        teacher.addCourse(courseExecution2)

        when: "We create a dashboard for each course execution"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())
        teacherDashboardService.createTeacherDashboard(courseExecution2.getId(), teacher.getId())
        

        then: "There are 2 dashboards stored in the database"
        teacherDashboardRepository.count() == 2L
        

        and: "They were well stored"
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getTeacher().getId() == teacher.getId()
        def result2 = teacherDashboardRepository.findAll().get(1)
        result2.getId() != 0
        result2.getCourseExecution().getId() == courseExecution2.getId()
        result2.getTeacher().getId() == teacher.getId()

        and: "The dashboards have the correct number of questionStats in their QuestionStats list"
        result.getQuestionStats().size() == 2
        result2.getQuestionStats().size() == 1

        and: "All questionStats have the correct course executions"
        result.getQuestionStats().get(0).getCourseExecution().getEndDate() == externalCourseExecution.getEndDate()
        result.getQuestionStats().get(1).getCourseExecution().getEndDate() == courseExecution2.getEndDate()
        result2.getQuestionStats().get(0).getCourseExecution().getEndDate() == courseExecution2.getEndDate()
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
