describe('Dashboard for course execution 2023', () => {

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
  
    it ('Update the charts and statistics of all courses', () => { 

        cy.intercept('GET', '**/teachers/dashboards/executions/*').as(
            'getDashboard'
        );
        
        cy.demoTeacherLogin();

        cy.contains('Change course').click();

        cy.changeCourseExecution('1st Semester 2022/2023');
        
        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.wait('@getDashboard');

        cy.contains('Statistics for this course execution');

        cy.get('.items').children('.project-name').contains('Number of Questions').parent().siblings().first().contains('50');
        cy.get('.items').children('.project-name').contains('Number of Questions Answered (Unique)').parent().siblings().first().contains('10');
        cy.get('.items').children('.project-name').contains('Number of Questions Answered (Unique, Average Per Student)').parent().siblings().first().contains('6');

        cy.get('.items').children('.project-name').contains('Number of Students').parent().siblings().first().contains('24');
        cy.get('.items').children('.project-name').contains('Students who Solved >= 75% Questions').parent().siblings().first().contains('1');
        cy.get('.items').children('.project-name').contains('Students who Solved >= 3 Quizzes').parent().siblings().first().contains('8');

        cy.get('.items').children('.project-name').contains('Number of Quizzes').parent().siblings().first().contains('6');
        cy.get('.items').children('.project-name').contains('Number of Quizzes Solved (Unique)').parent().siblings().first().contains('50');
        cy.get('.items').children('.project-name').contains('Number of Quizzes Solved (Unique, Average Per Student)').parent().siblings().first().contains('10');
        

        cy.get('canvas').eq(0).scrollIntoView().wait(5000).screenshot('test/2022/studentStatsTestGraphs');
    
        // PNGJS lets me load the picture from disk
        const PNG = require('pngjs').PNG;
        // pixelmatch library will handle comparison
        const pixelmatch = require('pixelmatch');
    
        cy.readFile(
            './tests/e2e/resources/2022/studentStatsGraphs.png', 'base64'
        ).then(expectedGraphs => {
        cy.readFile(
            './tests/e2e/screenshots/access_stats/2022teacherDashboardStats.js/test/2022/studentStatsTestGraphs.png', 'base64'
        ).then(testGraphs => {
    
            // load both pictures
            const expected = PNG.sync.read(Buffer.from(expectedGraphs, 'base64'));
            const test = PNG.sync.read(Buffer.from(testGraphs, 'base64'));
    
            const { width, height } = expected;
            const diff = new PNG({ width, height });
    
            // calling pixelmatch return how many pixels are different
            const numDiffPixels = pixelmatch(expected.data, test.data, diff.data, width, height, { threshold: 0.05 });
    
            // calculating a percent diff
            const diffPercent = (numDiffPixels / (width * height) * 100);
    
            expect(diffPercent).to.be.below(5);

            //cy.contains('Logout').click();

            Cypress.on('uncaught:exception', (err, runnable) => {
                // returning false here prevents Cypress from
                // failing the test
                return false;
              });
            })
        });

        cy.get('canvas').eq(1).scrollIntoView().wait(5000).screenshot('test/2022/quizzesStatsTestGraphs');
    
        cy.readFile(
            './tests/e2e/resources/2022/quizzesStatsGraphs.png', 'base64'
        ).then(expectedGraphs => {
        cy.readFile(
            './tests/e2e/screenshots/access_stats/2022teacherDashboardStats.js/test/2022/quizzesStatsTestGraphs.png', 'base64'
        ).then(testGraphs => {
    
            // load both pictures
            const expected = PNG.sync.read(Buffer.from(expectedGraphs, 'base64'));
            const test = PNG.sync.read(Buffer.from(testGraphs, 'base64'));
    
            const { width, height } = expected;
            const diff = new PNG({ width, height });
    
            // calling pixelmatch return how many pixels are different
            const numDiffPixels = pixelmatch(expected.data, test.data, diff.data, width, height, { threshold: 0.05 });
    
            // calculating a percent diff
            const diffPercent = (numDiffPixels / (width * height) * 100);
    
            expect(diffPercent).to.be.below(5);

            //cy.contains('Logout').click();

            Cypress.on('uncaught:exception', (err, runnable) => {
                // returning false here prevents Cypress from
                // failing the test
                return false;
              });
            })
        });

        cy.get('canvas').eq(2).scrollIntoView().wait(5000).screenshot('test/2022/questionStatsTestGraphs');
    
        cy.readFile(
            './tests/e2e/resources/2022/questionStatsGraphs.png', 'base64'
        ).then(expectedGraphs => {
        cy.readFile(
            './tests/e2e/screenshots/access_stats/2022teacherDashboardStats.js/test/2022/questionStatsTestGraphs.png', 'base64'
        ).then(testGraphs => {
    
            // load both pictures
            const expected = PNG.sync.read(Buffer.from(expectedGraphs, 'base64'));
            const test = PNG.sync.read(Buffer.from(testGraphs, 'base64'));
    
            const { width, height } = expected;
            const diff = new PNG({ width, height });
    
            // calling pixelmatch return how many pixels are different
            const numDiffPixels = pixelmatch(expected.data, test.data, diff.data, width, height, { threshold: 0.05 });
    
            // calculating a percent diff
            const diffPercent = (numDiffPixels / (width * height) * 100);
    
            expect(diffPercent).to.be.below(5);

            cy.contains('Logout').click();

            Cypress.on('uncaught:exception', (err, runnable) => {
                // returning false here prevents Cypress from
                // failing the test
                return false;
              });
            })
        });
    });
  });