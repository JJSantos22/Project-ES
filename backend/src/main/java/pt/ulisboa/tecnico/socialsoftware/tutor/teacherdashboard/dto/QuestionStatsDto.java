package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats;
import java.io.Serializable;

public class QuestionStatsDto implements Serializable {

    private Integer id;
    private int numAvailable;
    private int answeredQuestionsUnique;
    private float averageQuestionsAnswered; 
    private int year;

    public QuestionStatsDto(){    
    }

    public QuestionStatsDto(QuestionStats questionStats){
        this.id = questionStats.getId();
        this.numAvailable = questionStats.getNumAvailable();
        this.answeredQuestionsUnique = questionStats.getAnsweredQuestionsUnique();
        this.averageQuestionsAnswered = questionStats.getAverageQuestionsAnswered();  
        /* this.year = questionStats.getCourseExecution().getYear(); */
    } 

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public int getNumAvailable(){
        return numAvailable;
    }

    public int getAnswerQuestionsUnique(){
        return answeredQuestionsUnique;
    }

    public float getAverageQuestionsAnswered(){
        return averageQuestionsAnswered;
    }

    public int getYear(){
        return year;
    }

    public void setNumAvailable(int numAvailable){
        this.numAvailable = numAvailable;
    }

    public void setAnsweredQuestionsUnique(int answeredQuestionsUnique){
        this.answeredQuestionsUnique = answeredQuestionsUnique;
    }

    public void setAverageQuestionsUnique(float averageQuestionsUnique){
        this.averageQuestionsAnswered = averageQuestionsUnique;
    }

    public void setYear(int year){
        this.year = year;
    }

    @Override
    public String toString() {
        return "QuestionStatsDto{" +
                "id=" + id +
                "numAvailable=" + numAvailable +
                ", answeredQuestionsUnique=" + answeredQuestionsUnique +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                /* ", year=" + year + */
                '}';
    }
    
}