package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question.Status;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;

import javax.persistence.*;

@Entity
public class QuestionStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    private int numAvailable, answeredQuestionsUnique;
    private float averageQuestionsAnswered;

    public QuestionStats () {}

    public QuestionStats (TeacherDashboard dashboard, CourseExecution execution) {
        setDashboard(dashboard);
        setCourseExecution(execution);      
    }

    public void setDashboard (TeacherDashboard dashboard) {
        this.teacherDashboard = dashboard;
        dashboard.addQuestionStats(this);
    }

    public void setCourseExecution (CourseExecution execution) {
        this.courseExecution = execution;
    }

    public CourseExecution getCourseExecution () {
        return this.courseExecution;
    }

    public TeacherDashboard getTeacherDashboard () {
        return this.teacherDashboard;
    }

    public Integer getId() {
        return this.id;
    }

    public void remove () {
        teacherDashboard.getQuestionStats().remove(this);
        courseExecution = null;
        teacherDashboard = null;
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

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public void setAnsweredQuestionsUnique(int answeredQuestionsUnique) {
        this.answeredQuestionsUnique = answeredQuestionsUnique;
    }

    public void setAverageQuestionsAnswered(float averageQuestionsAnswered) {
        this.averageQuestionsAnswered = averageQuestionsAnswered;
    }

    public void update() {
        this.numAvailable = (int) courseExecution.getQuizzes().stream()
            .flatMap(q -> q.getQuizQuestions().stream())
            .map(QuizQuestion::getQuestion)
            .filter(q -> q.getStatus() == Status.AVAILABLE)
            .distinct()
            .count();
        
        this.answeredQuestionsUnique = (int) courseExecution.getQuizzes().stream()
                .flatMap(q -> q.getQuizAnswers() .stream()
                    .flatMap(qa -> qa.getQuestionAnswers().stream()
                        .map(QuestionAnswer::getQuestion)))
            .distinct()
            .count();

        int students = courseExecution.getStudents().size();

        long uniqueAllStudents = courseExecution.getStudents().stream().mapToLong(student -> 
            student.getQuizAnswers().stream().flatMap(
                qa -> qa.getQuestionAnswers().stream().map(QuestionAnswer::getQuestion)
            ).distinct().count()).sum();

        this.averageQuestionsAnswered = students > 0 ? (float) uniqueAllStudents / students : 0.0f;
    }

    @Override
    public void accept(Visitor visitor) {
        
    }

    @Override
    public String toString() {
        return "QuestionStats{" +
            "id=" + id +
            ", teacherDashboard=" + teacherDashboard.getId() +
            ", courseExecution=" + courseExecution.getId() +
            ", numAvailable=" + numAvailable +
            ", answeredQuestionsUnique=" + answeredQuestionsUnique +
            ", averageQuestionsAnswered=" + averageQuestionsAnswered +
            '}';
      }
}