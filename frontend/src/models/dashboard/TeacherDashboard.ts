import QuestionStats from './QuestionStats';
import QuizStats from './QuizStats';
import StudentStats from './TeacherDashboardStudentStats';

export default class TeacherDashboard {
  id!: number;
  quizStats!: QuizStats[];
  studentStats!: StudentStats[];
  questionStats!: QuestionStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.questionStats = jsonObj.questionStats;
      this.quizStats = jsonObj.quizStats;
      this.studentStats = jsonObj.studentStats;
    }
  }
}