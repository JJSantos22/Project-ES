<template>
    <div class="container">
      <div v-if="questionStats[0] != undefined" class="stats-container">
        <div class="items">
          <div ref="numAvailable" class="icon-wrapper">
            <animated-number :number="questionStats[0].numAvailable" />
          </div>
          <div class="project-name">
            <p>Number of Questions</p>
          </div>
        </div>
        <div class="items">
          <div ref="answeredQuestionsUnique" class="icon-wrapper">
            <animated-number :number="questionStats[0].answeredQuestionsUnique" />
          </div>
          <div class="project-name">
            <p>Number of Questions Answered (Unique)</p>
          </div>
        </div>
        <div class="items">
          <div ref="averageQuestionsAnswered" class="icon-wrapper">
            <animated-number :number="questionStats[0].averageQuestionsAnswered" />
          </div>
          <div class="project-name">
            <p>Number of Questions Answered (Unique, Average Per Student)</p>
          </div>
        </div>
      </div>
    </div>
</template>
  
  <script lang="ts">
  import { Component, Prop, Vue } from 'vue-property-decorator';
  import RemoteServices from '@/services/RemoteServices';
  import QuestionStats from '@/models/dashboard/QuestionStats';
  import AnimatedNumber from '@/components/AnimatedNumber.vue';
 
  @Component({
    components: {
      AnimatedNumber
    },
  })
  export default class QuestionStatsView extends Vue {
  
    @Prop() readonly dashboardId!: number;
    show: string | null = null;
    questionStats: QuestionStats[] = [];
  
    async created() {
      await this.$store.dispatch('loading');
      try {
        let teacherDashboard = await RemoteServices.getTeacherDashboard();
  
        this.questionStats = teacherDashboard.questionStats;

        this.show = 'Global';
      
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