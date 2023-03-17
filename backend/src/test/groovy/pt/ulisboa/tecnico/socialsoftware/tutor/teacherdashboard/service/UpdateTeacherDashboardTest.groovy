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
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.AnswerDetails
import java.time.LocalDateTime


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
        def courseExecution1 = new CourseExecution(externalCourseExecution.getCourse(), COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY.plusYears(2))
        courseExecutionRepository.save(courseExecution1)
        def s1 = createStudent(USER_1_NAME, USER_1_USERNAME, USER_1_EMAIL, externalCourseExecution) 
        def s2 = createStudent(USER_2_NAME, USER_2_USERNAME, USER_2_EMAIL, externalCourseExecution)
        def s3 = createStudent(USER_3_NAME, USER_3_USERNAME, USER_3_EMAIL, courseExecution1)

        and: "three questions for each courseExecution"
        def q1 = createQuestion(1, externalCourseExecution.getCourse())
        def q2 = createQuestion(2, externalCourseExecution.getCourse())
        def q3 = createQuestion(3, externalCourseExecution.getCourse())
        def q5 = createQuestion(1, courseExecution1.getCourse())
        def q6 = createQuestion(2, courseExecution1.getCourse())
        def q7 = createQuestion(3, courseExecution1.getCourse())
        def q9 = createQuestion(5, courseExecution1.getCourse())

        and: "two submitted questions"
        def q4 = createQuestion(4, externalCourseExecution.getCourse())
        def q8 = createQuestion(4, courseExecution1.getCourse())
        
        
        and: "two quizzes"
        def quiz = createQuiz(externalCourseExecution)
        def qq1 = createQuizQuestion(quiz, q1, 0)
        def qq2 = createQuizQuestion(quiz, q2, 1)
        def qq3 = createQuizQuestion(quiz, q3, 2)
        def quiz2 = createQuiz(courseExecution1)
        def qq4 = createQuizQuestion(quiz2, q5, 0)
        def qq5 = createQuizQuestion(quiz2, q6, 1)
        def qq6 = createQuizQuestion(quiz2, q7, 2)
        def qq7 = createQuizQuestion(quiz2, q9, 3)

        and: "students answer questions"
        def quizAs1 = createQuizAnswer(s1, quiz)        
        def quizAs2 = createQuizAnswer(s2, quiz)
        createQuestionAnswer(qq1, quizAs1, 0, true)
        createQuestionAnswer(qq2, quizAs1, 1, false)
        createQuestionAnswer(qq2, quizAs2, 0, true)
        
        teacher.addCourse(externalCourseExecution)
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        def quizAs3 = createQuizAnswer(s3, quiz2)
        createQuestionAnswer(qq4, quizAs3, 0, true)
        createQuestionAnswer(qq5, quizAs3, 1, false)
        
        teacher.addCourse(courseExecution1)
        teacherDashboardService.createTeacherDashboard(courseExecution1.getId(), teacher.getId())

        when: "the stats are created"
        def result = teacherDashboardRepository.findAll().get(0)
        def result1 = teacherDashboardRepository.findAll().get(1)

        then: "the stats will start with 0"
        teacherDashboardRepository.count() == 2L
        result.getQuestionStats().get(0).getAnsweredQuestionsUnique() == 0
        result.getQuestionStats().get(0).getAverageQuestionsAnswered() == 0
        result.getQuestionStats().get(0).getNumAvailable() == 0
        result1.getQuestionStats().get(0).getAnsweredQuestionsUnique() == 0
        result1.getQuestionStats().get(0).getAverageQuestionsAnswered() == 0
        result1.getQuestionStats().get(0).getNumAvailable() == 0
        result1.getQuestionStats().get(1).getAnsweredQuestionsUnique() == 0
        result1.getQuestionStats().get(1).getAverageQuestionsAnswered() == 0
        result1.getQuestionStats().get(1).getNumAvailable() == 0

        when: "the stats are updated"
        teacherDashboardService.updateAllTeacherDashboards()
        result = teacherDashboardRepository.findAll().get(0)
        result1 = teacherDashboardRepository.findAll().get(1)

        then: "the stats are correct"
        teacherDashboardRepository.count() == 2L
        result.getQuestionStats().get(0).getAnsweredQuestionsUnique() == 2
        result.getQuestionStats().get(0).getAverageQuestionsAnswered() == (float)3/2
        result.getQuestionStats().get(0).getNumAvailable() == 3
        result1.getQuestionStats().get(0).getAnsweredQuestionsUnique() == 2
        result1.getQuestionStats().get(0).getAverageQuestionsAnswered() == (float)2
        result1.getQuestionStats().get(0).getNumAvailable() == 4
        result1.getQuestionStats().get(1).getAnsweredQuestionsUnique() == 2
        result1.getQuestionStats().get(1).getAverageQuestionsAnswered() == (float)3/2
        result1.getQuestionStats().get(1).getNumAvailable() == 3
    }

    def "updateAll creates a new Dashboard"(){

        given: "Given a course and a teacher"
        def courseExecution2020 = new CourseExecution(externalCourse, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LocalDateTime.of(2020, 1, 1, 1, 1))
        courseRepository.save(courseExecution2020)

        when:
        teacher.addCourse(courseExecution2020)

        then:
        teacher.getDashboards().size() == 0

        when:
        teacherDashboardService.updateAllTeacherDashboards()

        then:
        teacher.getDashboards().size() == 1;
    }

@TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}