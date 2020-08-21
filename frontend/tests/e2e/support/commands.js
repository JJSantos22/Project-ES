// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
/// <reference types="Cypress" />

Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click({force: true});
  cy.get('[data-cy="courseExecutionNameInput"]').type(name);
  cy.get('[data-cy="courseExecutionAcronymInput"]').type(acronym);
  cy.get('[data-cy="courseExecutionAcademicTermInput"]').type(academicTerm);
  cy.get('[data-cy="saveButton"]').click();
  cy.wait(1000);
});

Cypress.Commands.add('closeErrorMessage', (name, acronym, academicTerm) => {
  cy.contains('Error')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add('deleteCourseExecution', acronym => {
  cy.contains(acronym)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 13)
    .find('[data-cy="deleteCourse"]')
    .click();
});

Cypress.Commands.add(
  'createFromCourseExecution',
  (name, acronym, academicTerm) => {
    cy.contains(name)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 13)
      .find('[data-cy="createFromCourse"]')
      .click();
    cy.get('[data-cy="courseExecutionAcronymInput"]').type(acronym);
    cy.get('[data-cy="courseExecutionAcademicTermInput"]').type(academicTerm);
    cy.get('[data-cy="saveButton"]').click();
  }
);

Cypress.Commands.add('seeTournamentsLists', type => {
  cy.get('[data-cy="Tournament"]').click();
  cy.get(`[data-cy="${type}"]`).click();
  cy.wait(100);
});

Cypress.Commands.add('createTournament', numberOfQuestions => {
  cy.get('[data-cy="createButton"]')
    .should('be.visible')
    .click();
  cy.tournamentCreation(numberOfQuestions);
  cy.get('[data-cy="saveButton"]').click();
  cy.wait(100);
});

Cypress.Commands.add('createPrivateTournament', numberOfQuestions => {
  cy.get('[data-cy="createButton"]')
    .should('be.visible')
    .click({ force: true });

  cy.get('[data-cy="SwitchPrivacy"]').click({ force: true });
  cy.wait(500);
  cy.get('[data-cy="Password"]').type('123', { force: true });
  cy.tournamentCreation(numberOfQuestions);
  cy.get('[data-cy="saveButton"]').click();
  cy.wait(100);
});

Cypress.Commands.add('tournamentCreation', numberOfQuestions => {
  cy.time('Start Time', 22, 0);
  cy.wait(100);
  cy.time('End Time', 25, 1);
  cy.get('[data-cy="NumberOfQuestions"]').type(numberOfQuestions, {
    force: true
  });
  cy.selectTopic('Software Architecture');
});

Cypress.Commands.add('createOpenTournament', numberOfQuestions => {
  cy.createTournament(numberOfQuestions);
  cy.updateTournamentStartTime();
});

Cypress.Commands.add('time', (date, day, type) => {
  let get = '';
  if (type === 0) {
    get = '#startTimeInput-picker-container-DatePicker';
  } else {
    get = '#endTimeInput-picker-container-DatePicker';
  }

  cy.get('label')
    .contains(date)
    .click({force: true});

  cy.get(get + ' > .calendar > .datepicker-controls > .text-right > .datepicker-button > svg > path')
    .click({force: true});

  cy.wait(500);
  cy.get(get + ' > .calendar > .month-container > :nth-child(1) > .datepicker-days > :nth-child(' + day + ') > .datepicker-day-text')
    .click({force: true});
});

Cypress.Commands.add('selectTopic', topic => {
  cy.get('[data-cy="Topics"]')
    .should('have.length', 1)
    .children()
    .should('have.length', 4)
    .contains(topic)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 2)
    .find('[data-cy="addTopic"]')
    .click();
});

Cypress.Commands.add('joinTournament', tournament => {
  cy.selectTournamentWithAction(tournament, "JoinTournament");
});

Cypress.Commands.add('joinPrivateTournament', tournament => {
  cy.joinTournament(tournament);
  cy.get('[data-cy="Password"]').type('123');
  cy.get('[data-cy="joinPrivateTournament"]').click();
});

Cypress.Commands.add('solveTournament', tournament => {
  cy.selectTournamentWithAction(tournament, "SolveQuiz");
});

Cypress.Commands.add('leaveTournament', tournament => {
  cy.selectTournamentWithAction(tournament, "LeaveTournament");
});

Cypress.Commands.add('editTournament', tournament => {
  cy.selectTournamentWithAction(tournament, "EditTournament");

  cy.time('Start Time', 24, 0);
  cy.wait(100);
  cy.time('End Time', 26, 1);
  cy.get('[data-cy="NumberOfQuestions"]')
    .clear({
      force: true
    })
    .type(5, {
      force: true
    });
  cy.selectTopic('Web Application');
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('cancelTournament', tournament => {
  cy.selectTournamentWithAction(tournament, "CancelTournament");
});

Cypress.Commands.add('removeTournament', tournament => {
  cy.selectTournamentWithAction(tournament, "RemoveTournament");
});

Cypress.Commands.add('selectTournamentWithAction', (tournament, action) => {

  cy.get(`:nth-child(${tournament}) > :nth-child(1) > [data-cy="${action}"]`)
    .click({ force: true });
});

Cypress.Commands.add('addUserThroughForm', (acronym, name, email, type) => {
  cy.contains(acronym)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 13)
    .find('[data-cy="addExternalUser"]')
    .click();

  cy.get('[data-cy="userNameInput"]').type(name);
  cy.get('[data-cy="userEmailInput"]').type(email);
  cy.get('[data-cy="userRoleSelect"]').parent().parent().click();
  cy.get('.v-menu__content .v-list').children().contains(type).first().click();
  cy.get('[data-cy="saveButton"]').click();
  cy.wait(3000);
});


Cypress.Commands.add('deleteUser', (mail, acronym) => {
  cy.contains(acronym)
    .parent()
    .children()
    .find('[data-cy="viewUsersButton"]')
    .click();

  cy.contains(mail).parent().children().eq(0).click();
  cy.get('[data-cy="deleteSelectedUsersButton"').click();
  cy.contains('No data available');
  cy.get('[data-cy="cancelButton"').click()
});

Cypress.Commands.add('checkStudentCount', (acronym, count) => {
  cy.contains(acronym).parent().children().eq(9).contains(count);
});

Cypress.Commands.add('checkTeacherCount', (acronym, count) => {
  cy.contains(acronym).parent().children().eq(7).contains(count);
});

Cypress.Commands.add('closeUserCreationDialog', () => {
  cy.get('[data-cy="cancelButton"]').click();
});
