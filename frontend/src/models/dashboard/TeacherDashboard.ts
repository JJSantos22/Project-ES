import QuestionStats from './QuestionStats';

export default class TeacherDashboard {
  id!: number;
  questionStats!: QuestionStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.questionStats = jsonObj.questionStats;
    }
  }
}