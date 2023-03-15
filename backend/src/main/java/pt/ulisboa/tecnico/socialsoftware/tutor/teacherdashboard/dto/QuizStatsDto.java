package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.io.Serializable;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;

public class QuizStatsDto implements Serializable {

    private Integer id;

    private int numQuizzes;

    private int numUniqueAnsweredQuizzes;

    private int year;

    private float averageQuizzesSolved;

    public QuizStatsDto() {
    }

    public QuizStatsDto(QuizStats quizStats) {
        setId(quizStats.getId());
        setNumQuizzes(quizStats.getNumQuizzes());
        setNumUniqueAnsweredQuizzes(quizStats.getNumUniqueAnsweredQuizzes());
        setAverageQuizzesSolved(quizStats.getAverageQuizzesSolved());
        setYear(quizStats.getCourseExecution().getYear());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumQuizzes() {
        return numQuizzes;
    }

    public void setNumQuizzes(int numQuizzes) {
        this.numQuizzes = numQuizzes;
    }

    public int getNumUniqueAnsweredQuizzes() {
        return numUniqueAnsweredQuizzes;
    }

    public void setNumUniqueAnsweredQuizzes(int numUniqueAnsweredQuizzes) {
        this.numUniqueAnsweredQuizzes = numUniqueAnsweredQuizzes;
    }

    public float getAverageQuizzesSolved() {
        return averageQuizzesSolved;
    }

    public void setAverageQuizzesSolved(float averageQuizzesSolved) {
        this.averageQuizzesSolved = averageQuizzesSolved;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}