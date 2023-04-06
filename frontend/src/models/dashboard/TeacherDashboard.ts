import QuizStats from './QuizStats';

export default class TeacherDashboard {
  id!: number;
  quizStats!: QuizStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.quizStats = jsonObj.quizStats;
    }
  }
}
