package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import java.util.*;

public class TeacherDashboardDto {
    private Integer id;
    private Integer numberOfStudents;
    private ArrayList<QuizStatsDto> quizStats;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        // For the number of students, we consider only active students
        this.numberOfStudents = teacherDashboard.getCourseExecution().getNumberOfActiveStudents();
        this.quizStats = new ArrayList<QuizStatsDto>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public ArrayList<QuizStatsDto> getQuizStatsDto() {
        return quizStats;
    }

    public void setQuizStatsDto(ArrayList<QuizStatsDto> quizStats) {
        this.quizStats = quizStats;
    }

    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", numberOfStudents=" + numberOfStudents +
                ", quizStats=" + quizStats +
                '}';
    }
}
