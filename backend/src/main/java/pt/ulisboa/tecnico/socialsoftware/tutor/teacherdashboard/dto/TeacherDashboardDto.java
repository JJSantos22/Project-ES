package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherDashboardDto {
    private Integer id;
    private List<QuestionStatsDto> questionStatsDto;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        this.questionStatsDto = teacherDashboard.getQuestionStats().stream().map(QuestionStatsDto::new).collect(Collectors.toList());
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

    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", questionStatsDto=" + questionStatsDto +
                "}";
    }
}
