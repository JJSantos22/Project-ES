package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import java.lang.reflect.Field
import spock.mock.DetachedMockFactory
import java.time.LocalDateTime

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


    def "create a dashboard with multiple coursesExecutions existing"() {

        given: "A course with four courses executions"
        def courseExecution2017 = new CourseExecution(externalCourse, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2017, 1, 1, 1, 1))
        courseRepository.save(courseExecution2017)
        def courseExecution2018 = new CourseExecution(externalCourse, COURSE_1_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2018, 1, 1, 1, 1))
        courseRepository.save(courseExecution2018)
        def courseExecution2019 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2019, 1, 1, 1, 1))
        courseRepository.save(courseExecution2019)
        def courseExecution2020 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2020, 1, 1, 1, 1))
        courseRepository.save(courseExecution2020)

        when:
        //teacher.addCourse(courseExecution2019)
        teacher.addCourse(courseExecution2020)

        then:
        teacher.getCourseExecutions().size() == 1;

        when:
        teacherDashboardService.createTeacherDashboard(courseExecution2020.getId(), teacher.getId())

        then: "an empty dashboard is created"
        teacherDashboardRepository.count() == 1L

        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == courseExecution2020.getId()
        result.getQuizStats().size() == 3
        result.getQuizStats().get(0).getCourseExecution().getEndDate().getYear()== 2020
        result.getQuizStats().get(1).getCourseExecution().getEndDate().getYear()== 2019
        result.getQuizStats().get(2).getCourseExecution().getEndDate().getYear()== 2018

        result.getStudentStats().size() == 3
        result.getStudentStats().get(0).getCourseExecution().getEndDate().getYear()== 2020
        result.getStudentStats().get(1).getCourseExecution().getEndDate().getYear()== 2019
        result.getStudentStats().get(2).getCourseExecution().getEndDate().getYear()== 2018

    }

    def "create a dashboard with less than three coursesExecutions existing"() {
        
        given: "A course with two courses executions"       
        def courseExecution2020 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2020, 1, 1, 1, 1))
        courseRepository.save(courseExecution2020)

        when:
        teacher.addCourse(courseExecution2020)
        teacher.addCourse(externalCourseExecution)

        then:
        teacher.getCourseExecutions().size() == 2;

        when:
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "an empty dashboard is created"
        teacherDashboardRepository.count() == 1L
        
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getQuizStats().size() == 2
        result.getQuizStats().get(0).getCourseExecution().getEndDate().getYear()== DateHandler.now().getYear()
        result.getQuizStats().get(1).getCourseExecution().getEndDate().getYear()== 2020

        result.getStudentStats().size() == 2
        result.getStudentStats().get(0).getCourseExecution().getEndDate().getYear()== DateHandler.now().getYear()
        result.getStudentStats().get(1).getCourseExecution().getEndDate().getYear()== 2020
        
    }
    
    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
