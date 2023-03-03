package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class TeacherDashboard implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherDashboard", orphanRemoval = true)
    private ArrayList<QuestionStats> questionStats = new ArrayList<QuestionStats>(); 

    private ArrayList<QuizStats> quizStats;

    public TeacherDashboard() {
    }

    public TeacherDashboard(CourseExecution courseExecution, Teacher teacher) {
        setCourseExecution(courseExecution);
        setTeacher(teacher);
    }
    
    public void remove() {
        teacher.getDashboards().remove(this);
        teacher = null;
    }

    public Integer getId() {
        return id;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        this.teacher.addDashboard(this);
    }

    public ArrayList<QuestionStats> getQuestionStats() {
        return questionStats;
    }

    public void addQuestionStats(QuestionStats questionStats) {
        this.questionStats.add(questionStats);
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    public void addQuizStats(QuizStats quizStats) {
        this.quizStats.add(quizStats);
    }

    public void removeQuizStats(QuizStats quizStats) {
        this.quizStats.remove(quizStats);
    }

    public ArrayList<QuizStats> getQuizStats() {
        return quizStats;
    }

    public void setQuizStats(ArrayList<QuizStats> quizStats) {
        this.quizStats = quizStats;
    }

    public void update(){ 
        for (QuestionStats q: this.getQuestionStats())
            q.update();
        for (QuizStats q: this.getQuizStats())
            q.update();          
    }

    @Override
    public String toString() {
        return "Dashboard{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", teacher=" + teacher +
                '}';
    }


}
