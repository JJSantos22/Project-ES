package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;

import javax.persistence.*;


@Entity
public class QuizStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    private  int numQuizzes = 0;
    private int uniqueQuizzesSolved = 0;
    private float averageQuizzesSolved = 0;

    public QuizStats (){
    }

    public QuizStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setTeacherDashboard(teacherDashboard);
        setCourseExecution(courseExecution);
    }

    public int getNumQuizzes() {
        return numQuizzes;
    }
    
    public void setNumQuizzes(int numQuizzes) {
        this.numQuizzes = numQuizzes;
    }
    
    public int getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public void setUniqueQuizzesSolved(int uniqueQuizzesSolved) {
        this.uniqueQuizzesSolved = uniqueQuizzesSolved;
    }

    public float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void setAverageQuizzesSolved(float averageQuizzesSolved) {
        this.averageQuizzesSolved = averageQuizzesSolved;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public TeacherDashboard getTeacherDashboard() {
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
    }

    @Override
    public String toString() {
        return "QuizStats{" +
                "courseExecution=" + courseExecution +
                ", teacherDashboard=" + teacherDashboard +
                ", numQuizzes=" + numQuizzes +
                ", uniqueQuizzesSolved=" + uniqueQuizzesSolved +
                ", averageQuizzesSolved=" + averageQuizzesSolved +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
    }

}