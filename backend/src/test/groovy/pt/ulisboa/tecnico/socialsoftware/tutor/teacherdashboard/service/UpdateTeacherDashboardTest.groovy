package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.User
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizQuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuestionAnswerRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.repository.QuizAnswerRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.OptionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.AnswerDetails


import spock.lang.Unroll

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {
    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
    }

    def createQuiz (courseExecution) {
        def quiz = new Quiz()
        quiz.setKey(1)
        quiz.setTitle("Quiz Title")
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(courseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)
    }

    def createQuizQuestion(quiz, question, seq) {
        def quizQuestion = new QuizQuestion(quiz, question, seq)
        quizQuestionRepository.save(quizQuestion)
    } 

    def createStudent(name, user, mail, courseExecution) {
        def student = new Student(name, user, mail, false, AuthUser.Type.TECNICO)
        student.addCourse(courseExecution)
        userRepository.save(student)
    }

    def createQuestion(key, available=true, course) {
        def question = new Question()
        question.setTitle("Question Title")
        question.setCourse(course)
        if(available == true){
            question.setStatus(Question.Status.AVAILABLE)
        }
        else{
            question.setStatus(Question.Status.SUBMITTED)
        }
        question.setKey(key)
        def questionDetails = new MultipleChoiceQuestion()
        question.setQuestionDetails(questionDetails)
        questionRepository.save(question)

        def option = new Option()
        option.setContent("Option Content")
        option.setCorrect(true)
        option.setSequence(0)
        option.setQuestionDetails(questionDetails)
        optionRepository.save(option)
        def optionKO = new Option()
        optionKO.setContent("Option Content")
        optionKO.setCorrect(false)
        optionKO.setSequence(1)
        optionKO.setQuestionDetails(questionDetails)
        optionRepository.save(optionKO)

        return question;
    }

    def createQuizAnswer (user, quiz, date = DateHandler.now()) {
        def quizAnswer = new QuizAnswer()
        quizAnswer.setCompleted(true)
        quizAnswer.setCreationDate(date)
        quizAnswer.setAnswerDate(date)
        quizAnswer.setStudent(user)
        quizAnswer.setQuiz(quiz)
        quizAnswerRepository.save(quizAnswer)
    }

    def createQuestionAnswer (quizQuestion, quizAnswer, sequence, correct, answered = true) {
        def questionAnswer = new QuestionAnswer ()
        questionAnswer.setTimeTaken(1)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(quizQuestion)
        questionAnswer.setSequence(sequence)
        questionAnswerRepository.save(questionAnswer)

        def answerDetails
        def correctOption = quizQuestion.getQuestion().getQuestionDetails().getCorrectOption()
        def incorrectOption = quizQuestion.getQuestion().getQuestionDetails().getOptions().stream().filter(option -> option != correctOption).findAny().orElse(null)
        if (answered && correct) answerDetails = new MultipleChoiceAnswer(questionAnswer, correctOption)
        else if (answered && !correct) answerDetails = new MultipleChoiceAnswer(questionAnswer, incorrectOption)
        else {
            questionAnswerRepository.save(questionAnswer)
            return questionAnswer
        }
        questionAnswer.setAnswerDetails(answerDetails)
        answerDetailsRepository.save(answerDetails)
        return questionAnswer
    }

    def "update a question stats with 3 available questions and 2 students"() {
        given: "two students"
        def s1 = createStudent(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, externalCourseExecution) 
        def s2 = createStudent(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, externalCourseExecution)

        and: "three questions"
        def q1 = createQuestion(1, externalCourseExecution.getCourse())
        def q2 = createQuestion(2, externalCourseExecution.getCourse())
        def q3 = createQuestion(3, externalCourseExecution.getCourse())

        and: "a submitted question"
        def q4 = createQuestion(4, externalCourseExecution.getCourse())
        
        and: "a quiz"
        def quiz = createQuiz(externalCourseExecution)
        def qq1 = createQuizQuestion(quiz, q1, 0)
        def qq2 = createQuizQuestion(quiz, q2, 1)
        def qq3 = createQuizQuestion(quiz, q3, 2)

        and: "students answer questions"
        def quizAs1 = createQuizAnswer(s1, quiz)        
        def quizAs2 = createQuizAnswer(s2, quiz)
        createQuestionAnswer(qq1, quizAs1, 0, true)
        createQuestionAnswer(qq2, quizAs1, 1, false)
        createQuestionAnswer(qq2, quizAs2, 0, true)
        
        teacher.addCourse(externalCourseExecution)
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        when: "the stats are created"
        def result = teacherDashboardRepository.findAll().get(0)

        then: "the stats start with 0"
        teacherDashboardRepository.count() == 1L
        result.getQuestionStats().get(0).getAnsweredQuestionsUnique() == 0
        result.getQuestionStats().get(0).getAverageQuestionsAnswered() == 0
        result.getQuestionStats().get(0).getNumAvailable() == 0

        when: "the stats are updated"
        teacherDashboardService.updateAllTeacherDashboards()
        result = teacherDashboardRepository.findAll().get(0)

        then: "the stats are correct"
        teacherDashboardRepository.count() == 1L
        result.getQuestionStats().get(0).getAnsweredQuestionsUnique() == 2
        result.getQuestionStats().get(0).getAverageQuestionsAnswered() == (float)3/2
        result.getQuestionStats().get(0).getNumAvailable() == 3
    }

@TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}