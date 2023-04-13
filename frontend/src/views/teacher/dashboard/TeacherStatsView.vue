<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <student-stats-view :dashboardId="dashboardId"></student-stats-view>
      <quiz-stats-view :dashboardId="dashboardId"></quiz-stats-view>
      <question-stats-view :dashboardId="dashboardId"></question-stats-view>
    </div>
    <div v-if="questionData.length>1 && quizData.length>1 && studentsData.length>1">
      <h2>Comparison with previous course executions</h2>
      <div class="stats-container">
        <charts :stats="studentsData" :labels="studentLabels"></charts>
        <charts :stats="quizData" :labels="quizLabels"></charts>
        <charts :stats="questionData" :labels="questionLabels"></charts>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import TeacherDashboard from '@/models/dashboard/TeacherDashboard';
import Charts from '@/components/Charts.vue';
import QuizStatsView from '@/views/teacher/dashboard/QuizStatsView.vue';
import QuizStats from '@/models/dashboard/QuizStats';
import StudentStatsView from '@/views/teacher/dashboard/StudentStatsView.vue';
import StudentStats from '@/models/dashboard/TeacherDashboardStudentStats';
import QuestionStatsView from '@/views/teacher/dashboard/QuestionStatsView.vue';
import QuestionStats from '@/models/dashboard/QuestionStats';

@Component({
  components: { StudentStatsView, QuizStatsView, QuestionStatsView, Charts },
})

export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;

  studentStats:  StudentStats[] = [];
  quizStats: QuizStats[] = [];
  questionStats: QuestionStats[] = [];

  studentsData: any[] = [];
  quizData: any[] = [];
  questionData: any[] = [];

  studentLabels : String[] = [];
  quizLabels : String[] = [];
  questionLabels : String[] = [];

  async created() {
    await this.$store.dispatch('loading');

    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
      this.studentStats = this.teacherDashboard.studentStats;
      this.quizStats = this.teacherDashboard.quizStats;
      this.questionStats = this.teacherDashboard.questionStats;

      for (let i = 0; i < Math.min.apply(Math, [this.questionStats.length, this.quizStats.length, this.studentStats.length]); i++) {
        if (this.studentStats[i] != undefined && this.studentStats[i]){
          this.studentsData.push([
            this.studentStats[i]?.courseExecutionYear, 
            this.studentStats[i]?.numStudents, 
            this.studentStats[i]?.numMore75CorrectQuestions, 
            this.studentStats[i]?.numAtLeast3Quizzes
          ]);
        }

        if (this.quizStats[i] != undefined  && this.quizStats[i]){
          this.quizData.push([
            this.quizStats[i]?.courseExecutionYear,
            this.quizStats[i]?.numQuizzes,
            this.quizStats[i]?.numUniqueAnsweredQuizzes,
            this.quizStats[i]?.averageQuizzesSolved,
          ]);
        }

        if (this.questionStats[i] != undefined && this.questionStats[i]){
          this.questionData.push([
            this.questionStats[i]?.courseExecutionYear,
            this.questionStats[i]?.numAvailable,
            this.questionStats[i]?.answeredQuestionsUnique,
            this.questionStats[i]?.averageQuestionsAnswered,
          ]);
        }
      }

      this.studentLabels = [
        'Total Number of Studentes',
        'Students who Solved >= 75% Questions',
        'Students who Solved >= 3 Quizzes'
      ];

      this.quizLabels = [
        'Quizzes: Total Available',
        'Quizzes: (Unique)',
        'Quizzes: Solved (Unique, Average Per Student)',
      ];

      this.questionLabels = [
        'Questions: Total Available',
        'Questions: Total Answered (Unique)',
        'Questions: Answered (Unique, Average Per Student)',
      ];

    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}

</script>

<style lang="scss" scoped>
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }

  .bar-chart {
    background-color: rgba(255, 255, 255, 0.90);
    height: 400px;
  }
}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 100px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: start;
}

.project-name p {
  font-size: 24px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(0px);
  transition: all 0.5s;
}

.items:hover {
  border: 3px solid black;

  & .project-name p {
    transform: translateY(-10px);
  }

  & .icon-wrapper i {
    transform: translateY(5px);
  }
}
</style>
