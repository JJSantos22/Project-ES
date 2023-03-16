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
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard

import java.lang.reflect.Field
import spock.mock.DetachedMockFactory
import java.time.LocalDateTime
import spock.lang.Unroll

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def teacher
    def teacherDashboardId

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
    }

    def createQuizStat() {
        def quizStat = new QuizStats(teacherDashboard, externalCourseExecution)
        quizStatsRepository.save(quizStat)
        return quizStat
    }

    def "update a dashboard"() {


        given: "a teacher dashboard"
        def courseExecution2020 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2020, 1, 1, 1, 1))
        courseRepository.save(courseExecution2020)
        def quizStat = new QuizStats()
        quizStat.setCourseExecution(courseExecution2020)
        quizStat.setNumQuizzes(10)
        quizStat.setNumUniqueAnsweredQuizzes(12)
        quizStat.setAverageQuizzesSolved(15)
        


        and:
        teacher.addCourse(courseExecution2020)
       

        when:
        teacherDashboardService.createTeacherDashboard(courseExecution2020.getId(), teacher.getId())
        def teacherDashboard = teacherDashboardRepository.findAll().get(0)
        def quizStats = new ArrayList<>()
        quizStats.add(quizStat)
        teacherDashboard.setQuizStats(quizStats)
        

        then:
        teacherDashboard.getCourseExecution().getId() == courseExecution2020.getId()
        teacherDashboard.getQuizStats().get(0).getNumQuizzes() == 10
        teacherDashboard.getQuizStats().get(0).getNumUniqueAnsweredQuizzes() == 12
        teacherDashboard.getQuizStats().get(0).getAverageQuizzesSolved() == 15
        

        when:
        
        teacherDashboardService.updateTeacherDashboard(teacherDashboard.getId())

        then:
        teacherDashboard.getCourseExecution().getId() == courseExecution2020.getId()
        teacherDashboard.getQuizStats().get(0).getNumQuizzes() == 0
        teacherDashboard.getQuizStats().get(0).getNumUniqueAnsweredQuizzes() == 0
        teacherDashboard.getQuizStats().get(0).getAverageQuizzesSolved() == 0
    }

     @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}