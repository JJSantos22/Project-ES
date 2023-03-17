package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherDashboardDto {
    private Integer id;
    private List<QuestionStatsDto> questionStatsDto;
    private ArrayList<QuizStatsDto> quizStats = new ArrayList<>();
    private List<StudentStatsDto> studentStatsDtoList;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        this.questionStatsDto = teacherDashboard.getQuestionStats().stream().map(QuestionStatsDto::new).collect(Collectors.toList());
        teacherDashboard.getQuizStats().forEach(quizStats -> this.quizStats.add(new QuizStatsDto(quizStats)));
        this.studentStatsDtoList = teacherDashboard.getStudentStats().stream().map(StudentStatsDto::new)
                .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<QuestionStatsDto> getQuestionStatsDto() {
        return questionStatsDto;
    }

    public void setQuestionStatsDto(List<QuestionStatsDto> questionStatsDto) {
        this.questionStatsDto = questionStatsDto;
    }
    
    public List<StudentStatsDto> getStudentStatsDtoList() {
        return this.studentStatsDtoList;
    }

    public void setStudentStatsDtoList(List<StudentStatsDto> studentStatsDtoList) {
        this.studentStatsDtoList = studentStatsDtoList;
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
                ", questionStatsDto=" + questionStatsDto +
                ", quizStats=" + quizStats +
                "studentStatsDtoList= " + studentStatsDtoList +
                '}';
    }
}