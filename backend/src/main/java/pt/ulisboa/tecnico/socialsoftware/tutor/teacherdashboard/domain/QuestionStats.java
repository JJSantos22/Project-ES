package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class QuestionStats implements DomainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private int numAvailable;
    private int answeredQuestionsUnique;
    private float averageQuestionsAnswered;
    
    @ManyToOne
    private TeacherDashboard teacherDashboard;

    @OneToOne
    private CourseExecution courseExecution;

    public QuestionStats() {}

    public QuestionStats(TeacherDashboard teacherDashboard, CourseExecution courseExecution) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
    }

    public Integer getId() {
        return id;
    }

    public int getNumAvailable() {
        return this.numAvailable;
    }

    public int getAnsweredQuestionsUnique() {
        return this.answeredQuestionsUnique;
    }

    public float getAverageQuestionsAnswered() {
        return this.averageQuestionsAnswered;
    }
    
    public CourseExecution getCourseExecution() {
        return this.courseExecution;
    }

    public TeacherDashboard getTeacherDashboard() {
        return this.teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public void setAnsweredQuestionsUnique(int answeredQuestionsUnique) {
        this.answeredQuestionsUnique = answeredQuestionsUnique;
    }

    public void setAverageQuestionsAnswered(float averageQuestionsAnswered) {
        this.averageQuestionsAnswered = averageQuestionsAnswered;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public void accept(Visitor visitor) {
        // Only to generate XML
    }

    public void update(){
        setNumAvailable(this.courseExecution.getNumberOfQuestions());
        setAnsweredQuestionsUnique(this.getTotalAnsweredQuestionsUnique());
        setAverageQuestionsAnswered(this.getAverageStudentsUniqueAnswers());
    }

    public int getTotalAnsweredQuestionsUnique(){
        return (int) (this.courseExecution.getCourse()
                .getQuestions().stream().distinct()
                .filter(question -> question.getNumberOfAnswers()>0).count());
    }

    public float getAverageStudentsUniqueAnswers(){
        int studentsTotalUniqueAnswers = 0;
        Set<Question> questions = new HashSet<>();
        int n_students = 0;
        for (Student student:this.courseExecution.getStudents()){
            for (QuizAnswer quizAnswer:student.getQuizAnswers()){
                if (quizAnswer.getQuiz().getCourseExecution().equals(this.courseExecution)){
                    for (QuestionAnswer questionAnswer:quizAnswer.getQuestionAnswers()){
                        questions.add(questionAnswer.getQuestion());
                    }
                }
            }
            studentsTotalUniqueAnswers += questions.size();
            questions.clear();
            n_students++;
        }

        float result = (float) studentsTotalUniqueAnswers/n_students;
        return result;
    }

    public void remove() {
        this.teacherDashboard.getQuestionStats().remove(this);
        this.teacherDashboard = null;
    }

    @Override
    public String toString() {
        return "QuestionStats{" +
                "id=" + id +
                ", numAvailable=" + numAvailable +
                ", answeredQuestionsUnique=" + answeredQuestionsUnique +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                '}';
    }
}