package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import spock.lang.Unroll

@DataJpaTest
class GetFunctionsQuestionStatsTest extends SpockTest {
    def teacher
    def dashboard
    def questionStats
    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
        questionStats = new QuestionStats(dashboard, externalCourseExecution)
        questionStatsRepository.save(questionStats)

    }

    def "Test for getTeacherDashboard"() {
        
        given:

        and:

        when:
        questionStats.setTeacherDashboard(dashboard)

        def result = questionStats.getTeacherDashboard()

        then:
        result.equals(dashboard)

    }

    def "Test for getCourseExecution"() {
        
        given:

        and:

        when:
        questionStats.setCourseExecution(externalCourseExecution)

        def result = questionStats.getCourseExecution()

        then:
        result.equals(externalCourseExecution)

    }

    def "Test for getId"() {
        
        given:
        

        and:

        when:
        

        def result = teacherDashboardRepository.findAll().get(0)        

        then:
        result.getId() != 0

    }


    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}