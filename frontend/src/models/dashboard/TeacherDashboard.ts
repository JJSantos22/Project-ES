import StudentStats from './TeacherDashboardStudentStats';

export default class TeacherDashboard {
  id!: number;
  studentStats!: StudentStats[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.studentStats = jsonObj.studentStats;
    }
  }
}
