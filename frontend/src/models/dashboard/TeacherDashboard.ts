import QuizStats from './QuizStats';
import StudentStats from './TeacherDashboardStudentStats';

export default class TeacherDashboard {
  id!: number;
  quizStats!: QuizStats[];
  studentStats!: StudentStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.quizStats = jsonObj.quizStats;
      this.studentStats = jsonObj.studentStats;
    }
  }
}
