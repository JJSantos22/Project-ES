package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats;
import java.io.Serializable;

public class QuestionStatsDto implements Serializable {

    private Integer numAvailable;
    private Integer answeredQuestionsUnique;
    private Float averageQuestionsAnswered; 
    private Integer year;

    public QuestionStatsDto(){    
    }

    public QuestionStatsDto(QuestionStats questionStats){
        this.numAvailable = questionStats.getNumAvailable();
        this.answeredQuestionsUnique = questionStats.getAnsweredQuestionsUnique();
        this.averageQuestionsAnswered = questionStats.getAverageQuestionsAnswered();  
        this.year = questionStats.getCourseExecution().getYear();
    }

    public Integer getNumAvailable(){
        return numAvailable;
    }

    public Integer getAnswerQuestionsUnique(){
        return answeredQuestionsUnique;
    }

    public Float getAverageQuestionsAnswered(){
        return averageQuestionsAnswered;
    }

    public int getYear(){
        return year;
    }

    public void setNumAvailable(Integer numAvailable){
        this.numAvailable = numAvailable;
    }

    public void setAnsweredQuestionsUnique(Integer answeredQuestionsUnique){
        this.answeredQuestionsUnique = answeredQuestionsUnique;
    }

    public void setAverageQuestionsUnique(Float averageQuestionsUnique){
        this.averageQuestionsAnswered = averageQuestionsUnique;
    }

    public void setYear(Integer year){
        this.year = year;
    }

    @Override
    public String toString() {
        return "QuestionStatsDto{" +
                "numAvailable=" + numAvailable +
                ", answeredQuestionsUnique=" + answeredQuestionsUnique +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                ", year=" + year +
                '}';
    }
    
}