package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student



import spock.lang.Unroll


@DataJpaTest
class QuestionStatsRepositoryTest extends SpockTest {
    def teacher
    def dashboard
    def questionStats
    def questionStats2
    def questionStats3


    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
    }

    
    @Unroll
    def "Test QuestionStat Repository"() {
        given: 
            questionStats = new QuestionStats(dashboard, externalCourseExecution)
    
            questionStats.setNumAvailable(45)
            questionStats.setAnsweredQuestionsUnique(10)
            questionStats.setAverageQuestionsAnswered(4.5)

            float average = 19.7

            questionStats2 = new QuestionStats(dashboard, externalCourseExecution)
    
            questionStats2.setNumAvailable(87)
            questionStats2.setAnsweredQuestionsUnique(22)
            questionStats2.setAverageQuestionsAnswered(average)



            questionStatsRepository.save(questionStats)
            questionStatsRepository.save(questionStats2)
        
        when: 
            questionStats3 = questionStatsRepository.findById(questionStats2.getId()).get()
            
        
        then:
            questionStats3.getNumAvailable() == 87
            questionStats3.getAnsweredQuestionsUnique() == 22
            questionStats3.getAverageQuestionsAnswered() == average
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}

