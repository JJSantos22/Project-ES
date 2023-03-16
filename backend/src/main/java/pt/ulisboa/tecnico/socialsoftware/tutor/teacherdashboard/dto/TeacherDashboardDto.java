package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;

public class TeacherDashboardDto {
    private Integer id;
    private List<StudentStatsDto> studentStatsDtoList;

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();
        this.studentStatsDtoList = teacherDashboard.getStudentStats().stream().map(StudentStatsDto::new)
                .collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<StudentStatsDto> getStudentStatsDtoList() {
        return this.studentStatsDtoList;
    }

    public void setStudentStatsDtoList(List<StudentStatsDto> studentStatsDtoList) {
        this.studentStatsDtoList = studentStatsDtoList;
    }

    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                "studentStatsDtoList= " + studentStatsDtoList +
                "}";
    }
}
