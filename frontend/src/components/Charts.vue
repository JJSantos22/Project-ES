<template>
    <div class="stats-container">
      <div class="items">
        <Bar
          id="chart-id"
          :chart-options="chartOptions"
          :chart-data="chartData"
        ></Bar>
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import { Component, Prop, Vue } from 'vue-property-decorator';
  import { Bar } from 'vue-chartjs/legacy';
  
  import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
  } from 'chart.js';
  
  ChartJS.register(
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale
  );
  
  @Component({
    components: {
      Bar,
    },
  })
  export default class Charts extends Vue {
    @Prop() readonly stats!: any[];
    @Prop() readonly labels!: String[];
  
    show: string | null = null;
  
    chartData: any = null;
    chartOptions: any = null;
  
    async created() {
      await this.$store.dispatch('loading');
      try {
        console.log(this.stats);
        this.show = 'Global';
        this.chartData = {
          labels: [
            /*this.stats[2]?.courseExecutionYear,
            this.stats[1]?.courseExecutionYear,
            this.stats[0]?.courseExecutionYear + ' (Current)',*/
            2020,
            2021,
            2022
          ],
          datasets: [
            {
              label: this.labels[0],
              backgroundColor: '#C0392B',
              data: [
                this.stats[2]?.numStudents,
                this.stats[1]?.numStudents,
                this.stats[0]?.numStudents,
                
              ],
            },
            {
              label: this.labels[1],
              backgroundColor: '#2980B9',
              data: [
                this.stats[2]?.numMore75CorrectQuestions,
                this.stats[1]?.numMore75CorrectQuestions,
                this.stats[0]?.numMore75CorrectQuestions,
                
              ],
            },
            {
              label: this.labels[2],
              backgroundColor: '#1ABC9C',
              data: [
                this.stats[2]?.numAtLeast3Quizzes,
                this.stats[1]?.numAtLeast3Quizzes,
                this.stats[0]?.numAtLeast3Quizzes,
              ],
            },
          ],
        };
  
        this.chartOptions = {
          responsive: true,
          
        };
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
      background-color: rgb(255, 255, 255);
      color: #1976d2;
      border-radius: 5px;
      flex-basis: 25%;
      margin: 20px;
      cursor: pointer;
      transition: all 0.6s;
    }
  
    .bar-chart {
      background-color: rgb(255, 255, 255);
      height: 400px;
    }
  }
  </style> 