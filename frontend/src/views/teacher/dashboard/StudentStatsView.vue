<template>
    <div class="container">
      <div v-if="studentStats[0] != undefined" class="stats-container">
        <div class="items">
            <div ref="totalStudents" class="icon-wrapper">
              <animated-number :number="studentStats[0].numStudents" />
            </div>
            <div class="project-name">
              <p>{{ labels[0] }}</p>
            </div>
        </div>
        <div class="items">
            <div ref="totalStudents" class="icon-wrapper">
              <animated-number :number="studentStats[0].numMore75CorrectQuestions" />
            </div>
            <div class="project-name">
              <p>{{ labels[1] }}</p>
            </div>
        </div>
        <div class="items">
            <div ref="totalStudents" class="icon-wrapper">
              <animated-number :number="studentStats[0].numAtLeast3Quizzes" />
            </div>
            <div class="project-name">
              <p>{{ labels[2] }}</p>
            </div>
        </div>  
        </div>
      </div>
  </template>
  
  <script lang="ts">
  import { Component, Prop, Vue } from 'vue-property-decorator';
  import RemoteServices from '@/services/RemoteServices';
  import AnimatedNumber from '@/components/AnimatedNumber.vue';
  import StudentStats from '@/models/dashboard/TeacherDashboardStudentStats';
  
  @Component({
    components: { AnimatedNumber },
  })
  
  export default class StudentStatsView extends Vue {
    @Prop() readonly dashboardId!: number;
    show: string | null = null;
    studentStats:  StudentStats[] = [];
  
    labels : String[] = [];

    async created() {
      await this.$store.dispatch('loading');
      try {
        let teacherDashboard = await RemoteServices.getTeacherDashboard();
        this.studentStats = teacherDashboard.studentStats;

        this.labels = ['Number of Students',
        'Number of Students who Solved >= 75% Questions',
        'Number of Students who Solved >= 3 Quizzes'
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