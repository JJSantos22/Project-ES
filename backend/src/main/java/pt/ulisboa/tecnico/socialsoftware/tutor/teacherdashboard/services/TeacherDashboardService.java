package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.repository.CourseExecutionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.QuizStatsDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto.TeacherDashboardDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.TeacherDashboardRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.TeacherRepository;

import java.util.*;
import java.util.stream.Collectors;
import static java.util.Comparator.comparingInt;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class TeacherDashboardService {

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherDashboardRepository teacherDashboardRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TeacherDashboardDto getTeacherDashboard(int courseExecutionId, int teacherId) {
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId)
                .orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TutorException(USER_NOT_FOUND, teacherId));

        if (!teacher.getCourseExecutions().contains(courseExecution))
            throw new TutorException(TEACHER_NO_COURSE_EXECUTION);

        Optional<TeacherDashboard> dashboardOptional = teacher.getDashboards().stream()
                .filter(dashboard -> dashboard.getCourseExecution().getId().equals(courseExecutionId))
                .findAny();

        return dashboardOptional.
                map(TeacherDashboardDto::new).
                orElseGet(() -> createAndReturnTeacherDashboardDto(courseExecution, teacher));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TeacherDashboardDto createTeacherDashboard(int courseExecutionId, int teacherId) {
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId)
                .orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TutorException(USER_NOT_FOUND, teacherId));

        if (teacher.getDashboards().stream().anyMatch(dashboard -> dashboard.getCourseExecution().equals(courseExecution)))
            throw new TutorException(TEACHER_ALREADY_HAS_DASHBOARD);

        if (!teacher.getCourseExecutions().contains(courseExecution))
            throw new TutorException(TEACHER_NO_COURSE_EXECUTION);

        return createAndReturnTeacherDashboardDto(courseExecution, teacher);
    }

    private TeacherDashboardDto createAndReturnTeacherDashboardDto(CourseExecution courseExecution, Teacher teacher) {

        TeacherDashboard teacherDashboard = new TeacherDashboard(courseExecution, teacher);

        teacherDashboardRepository.save(teacherDashboard);

        ArrayList<QuizStatsDto> quizStatsDto = getQuizStatsDto(teacherDashboard.getId());

        if (quizStatsDto.isEmpty()) {
            //System.out.println(quizStatsDto);
        }


        TeacherDashboardDto teacherDashboardDto = new TeacherDashboardDto(teacherDashboard);

        teacherDashboardDto.setQuizStatsDto(quizStatsDto);

        return teacherDashboardDto;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeTeacherDashboard(Integer dashboardId) {
        if (dashboardId == null)
            throw new TutorException(DASHBOARD_NOT_FOUND, -1);

        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(dashboardId).orElseThrow(() -> new TutorException(DASHBOARD_NOT_FOUND, dashboardId));
        teacherDashboard.remove();
        teacherDashboardRepository.delete(teacherDashboard);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ArrayList<QuizStatsDto> getQuizStatsDto(int teacherDashboardId) {

        ArrayList<QuizStatsDto> quizStatsDto = new ArrayList<QuizStatsDto>();

        TeacherDashboard teacherDashboard = teacherDashboardRepository.findById(teacherDashboardId)
                .orElseThrow(() -> new TutorException(DASHBOARD_NOT_FOUND, teacherDashboardId));

        Set<CourseExecution> courseExecutions = teacherDashboard.getCourseExecution().getCourse().getCourseExecutions();

        if (courseExecutions.size() > 3) {
            List<CourseExecution> threeMostRecentCourseExecutions = courseExecutions.stream()
            .sorted(comparingInt(CourseExecution::getYear).reversed())
            .limit(3)
            .collect(Collectors.toList());

            List<QuizStats> quizStats = teacherDashboard.getQuizStats().stream().
            filter(quizStat -> threeMostRecentCourseExecutions.contains(quizStat.getCourseExecution())).collect(Collectors.toList());

            teacherDashboard.setQuizStats(quizStats);

            quizStatsDto = (ArrayList<QuizStatsDto>) quizStats.stream().map(QuizStatsDto::new).collect(Collectors.toList());
        }
        else if (courseExecutions.size() > 0) {
            List<QuizStats> quizStats = teacherDashboard.getQuizStats().stream().
            filter(quizStat -> courseExecutions.contains(quizStat.getCourseExecution())).collect(Collectors.toList());

            teacherDashboard.setQuizStats(quizStats);

            quizStatsDto = (ArrayList<QuizStatsDto>) quizStats.stream().map(QuizStatsDto::new).collect(Collectors.toList());
        }

        return quizStatsDto;
    }
}