package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;

import javax.persistence.*;


@Entity
public class QuestionStats implements DomainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private int numAvailable;
    private int answeredQuestionsUnique;
    private Float averageQuestionsAnswered;
    
    @ManyToOne
    private TeacherDashboard teacherDashboard;

    @OneToOne
    private CourseExecution courseExecution;

    public QuestionStats() {}

    public QuestionStats(TeacherDashboard teacherDashboard, CourseExecution courseExecution) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
    }

    public int getNumAvailable() {
        return this.numAvailable;
    }

    public int getAnsweredQuestionsUnique() {
        return this.answeredQuestionsUnique;
    }

    public Float getAverageQuestionsAnswered() {
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

    public void setAverageQuestionsAnswered(Float averageQuestionsAnswered) {
        this.averageQuestionsAnswered = averageQuestionsAnswered;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public void accept(Visitor visitor) {
        // Only to generate XML
    }

    

}