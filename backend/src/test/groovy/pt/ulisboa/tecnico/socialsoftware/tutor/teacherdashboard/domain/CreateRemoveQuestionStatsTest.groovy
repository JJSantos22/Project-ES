package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import spock.lang.Unroll

@DataJpaTest
class CreateRemoveQuestionStatsTest extends SpockTest {

    def teacher
    def dashboard

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
        dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
    }

    def "create a QuestionStats instance"() {
        given: 
        
        def instance = new QuestionStats(dashboard, externalCourseExecution)

        when: 
        dashboard.addQuestionStats(instance)

        then:     
        dashboard.getQuestionStats().size() == 1
    }

    def "remove a QuestionStats instance"() {
        given: 
        def instance = new QuestionStats(dashboard, externalCourseExecution)
        dashboard.addQuestionStats(instance)

        when: 
        dashboard.getQuestionStats().get(0).remove()

        then:     
        dashboard.getQuestionStats().size() == 0
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
