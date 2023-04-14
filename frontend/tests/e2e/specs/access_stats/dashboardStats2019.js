describe('Dashboard', () => {
  let date;

  beforeEach(() => {

    cy.deleteQuestionsAndAnswers();
    cy.reset2023CourseExecution();
    cy.resetData();

    cy.demoTeacherLogin();
    cy.get('[data-cy="dashboardMenuButton"]').click();
    cy.contains('Statistics for this course execution').click();
    cy.logout();

    cy.demoTeacherLogin();
    cy.populate_2023();
    cy.populate_2022();
    cy.populate_2019();
    cy.logout();

});

afterEach(() => {
    cy.deleteQuestionsAndAnswers();
    cy.reset2023CourseExecution();
    cy.resetData();
});

it('teacher accesses dashboard from Course Execution 2019', () => {
  cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
      'getDashboard'
  );

  cy.demoTeacherLogin();

  cy.contains('Change course').click();

  cy.changeCourseExecution('1st Semester 2019/2020');

  cy.get('[data-cy="dashboardMenuButton"]').click();
  cy.wait('@getDashboard');

  cy.contains('Statistics for this course execution');

  cy.get('.items').children('.project-name').contains('Number of Questions').parent().siblings().first().contains('75');
  cy.get('.items').children('.project-name').contains('Number of Questions Answered (Unique)').parent().siblings().first().contains('15');
  cy.get('.items').children('.project-name').contains('Number of Questions Answered (Unique, Average Per Student)').parent().siblings().first().contains('9');
  cy.get('.items').children('.project-name').contains('Number of Students').parent().siblings().first().contains('10');
  cy.get('.items').children('.project-name').contains('Students who Solved >= 75% Questions').parent().siblings().first().contains('13');
  cy.get('.items').children('.project-name').contains('Students who Solved >= 3 Quizzes').parent().siblings().first().contains('12');
  cy.get('.items').children('.project-name').contains('Number of Quizzes').parent().siblings().first().contains('9');
  cy.get('.items').children('.project-name').contains('Number of Quizzes Solved (Unique)').parent().siblings().first().contains('75');
  cy.get('.items').children('.project-name').contains('Number of Quizzes Solved (Unique, Average Per Student)').parent().siblings().first().contains('15');
  
  cy.get('canvas').should('not.exist');

  cy.contains('Logout').click();

    Cypress.on('uncaught:exception', (err, runnable) => {
      // returning false here prevents Cypress from
      // failing the test
      return false;
    });
})
});
