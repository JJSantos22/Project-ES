package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.io.Serializable;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats;

public class StudentStatsDto implements Serializable {

    private Integer numStudents;
    private Integer numMore75CorrectQuestions;
    private Integer numAtLeast3Quizzes;
    private Integer year;

    public StudentStatsDto() {
    }

    public StudentStatsDto(StudentStats studentStats/* , CourseExecution courseExecution */) {
        setNumStudents(studentStats.getNumStudents());
        setNumMore75CorrectQuestions(studentStats.getNumMore75CorrectQuestions());
        setNumAtLeast3Quizzes(studentStats.getNumAtLeast3Quizzes());
        // setYear(courseExecution.getYear());
    }

    public Integer getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(Integer numStudents) {
        this.numStudents = numStudents;
    }

    public Integer getNumMore75CorrectQuestions() {
        return numMore75CorrectQuestions;
    }

    public void setNumMore75CorrectQuestions(Integer numMore75CorrectQuestions) {
        this.numMore75CorrectQuestions = numMore75CorrectQuestions;
    }

    public Integer getNumAtLeast3Quizzes() {
        return numAtLeast3Quizzes;
    }

    public void setNumAtLeast3Quizzes(Integer numAtLeast3Quizzes) {
        this.numAtLeast3Quizzes = numAtLeast3Quizzes;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}