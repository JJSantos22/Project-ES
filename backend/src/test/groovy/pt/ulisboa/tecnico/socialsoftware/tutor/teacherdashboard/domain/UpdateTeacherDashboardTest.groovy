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
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import spock.lang.Unroll

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def question1
    def question2
    def question3
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


    def "update the number of available questions"() {
        given:
        def question1 = new Question()
        def question2 = new Question()
        def question3 = new Question()
        questionStats.setNumAvailable(0)
        dashboard.addQuestionStats(questionStats)

        and:

        externalCourseExecution.course.addQuestion(question1)  
        externalCourseExecution.course.addQuestion(question2) 
        externalCourseExecution.course.addQuestion(question3) 

        when:
        dashboard.update()

        def result = dashboard.getQuestionStats()

        then:
        result.get(0).getNumAvailable() == 3
    }

    def "Update the number of unique questions answered"(){
        given:
        def question1 = new Question()
        def question2 = new Question()
        def question3 = new Question()
        questionStats.setAnsweredQuestionsUnique(0)
        question1.setNumberOfAnswers(3)
        question2.setNumberOfAnswers(1)
        question3.setNumberOfAnswers(0)

        and:

        externalCourseExecution.course.addQuestion(question1)  
        externalCourseExecution.course.addQuestion(question2) 
        externalCourseExecution.course.addQuestion(question3) 

        dashboard.addQuestionStats(questionStats)

        when:
        dashboard.update()

        def result = dashboard.getQuestionStats()

        then:
        result.get(0).getAnsweredQuestionsUnique() == 2
    }

    def "Update average number of unique answered questions by a student"(){
        given:
        def question1 = new Question()
        def question2 = new Question()
        def question3 = new Question()
        def student = new Student(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, false, AuthUser.Type.TECNICO)
        externalCourseExecution.addUser(student)
        def quiz = new Quiz()
        def quizQuestion1 = new QuizQuestion(quiz, question1, 1)
        quiz.addQuizQuestion(quizQuestion1)
        def quizQuestion2 = new QuizQuestion(quiz, question2, 2)
        quiz.addQuizQuestion(quizQuestion2)
        def quizQuestion3 = new QuizQuestion(quiz, question3, 3)
        quiz.addQuizQuestion(quizQuestion3)
        def answer = new QuizAnswer(student, quiz)
        student.addQuizAnswer(answer)

        and:

        quiz.setCourseExecution(externalCourseExecution)
        questionStats.setAverageQuestionsAnswered(0)
        dashboard.addQuestionStats(questionStats)

        when:
        dashboard.update()

        def result = dashboard.getQuestionStats()

        then:
        result.get(0).getAverageQuestionsAnswered() == 3
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}