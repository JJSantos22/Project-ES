describe('Dashboard for course execution 2023', () => {

    beforeEach(() => {
        cy.deleteQuestionsAndAnswers();
        cy.deleteCourseExecutions("1st Semester 2021/2022");
        cy.deleteCourseExecutions("1st Semester 2020/2021");
        //cy.deleteCourseExecutions("1st Semester 2019/2020");
        
    });
  
    afterEach(() => {
        //cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm("1st Semester 2022/2023")   
    });
  
    it ('open stuff', () => {
  
        cy.demoTeacherLogin();
        cy.logout();
        
        cy.demoTeacherLogin();
        
        cy.createCourseExecutionOnDemoCourse("1st Semester 2021/2022")
        cy.createCourseExecutionOnDemoCourse("1st Semester 2020/2021")
        //cy.createCourseExecutionOnDemoCourse("1st Semester 2019/2020")
        
        cy.get('[data-cy="dashboardMenuButton"]').click();

        //cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm("1st Semester 2022/2023")   
    
        cy.setStudentStats(1, 2, 3, "1st Semester 2022/2023")
        cy.setStudentStats(3, 4, 6, "1st Semester 2021/2022")
        cy.setStudentStats(5, 7, 8, "1st Semester 2020/2021")
        //cy.setStudentStats(5, 7, 8, "1st Semester 2019/2020")
        
        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.contains('Statistics for this course execution').click();

        cy.logout();
    })
  }); 