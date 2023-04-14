describe('Dashboard for course execution 2023', () => {

    function compareImages(expect_url, test_url) {

        // PNGJS lets me load the picture from disk
        const PNG = require('pngjs').PNG;
        // pixelmatch library will handle comparison
        const pixelmatch = require('pixelmatch');

        const expected_path = './tests/e2e/resources/' + expect_url;
        const test_path = './tests/e2e/screenshots/access_stats/dashboardStats2023.js/test/2023/' + test_url;
        cy.readFile(
            expected_path, 'base64'
        ).then(expectedGraphs => {
            cy.readFile(
                test_path, 'base64'
            ).then(testGraphs => {

                // load both pictures
                const expected_img = PNG.sync.read(Buffer.from(expectedGraphs, 'base64'));
                const test_img = PNG.sync.read(Buffer.from(testGraphs, 'base64'));

                const { width, height } = expected_img;
                const diff = new PNG({ width, height });

                // calling pixelmatch return how many pixels are different
                const numDiffPixels = pixelmatch(expected_img.data, test_img.data, diff.data, width, height, { threshold: 0.05 });

                // calculating a percent diff
                const diffPercent = (numDiffPixels / (width * height) * 100);

                expect(diffPercent).to.be.below(5);
            })
        })
    }

    function CompareNumbers(label, value) {
        cy.get('.items').children('.project-name').contains(label).parent().siblings().first().contains(value);
    }

    beforeEach(() => {
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
        cy.resetData();
    });

    it('Update the statistics', () => {
        cy.demoTeacherLogin();
        cy.contains('Change course').click();
        cy.changeCourseExecution('1st Semester 2023/2024');
        cy.get('[data-cy="dashboardMenuButton"]').click();
        cy.contains('Statistics for this course execution').click();

        CompareNumbers('Number of Questions', '25');
        CompareNumbers('Number of Questions Answered (Unique)', '5');
        CompareNumbers('Number of Questions Answered (Unique, Average Per Student)', '3');
        CompareNumbers('Number of Students', '12');
        CompareNumbers('Students who Solved >= 75% Questions', '8');
        CompareNumbers('Students who Solved >= 3 Quizzes', '4');
        CompareNumbers('Number of Quizzes', '8');
        CompareNumbers('Number of Quizzes Solved (Unique)', '12');
        CompareNumbers('Number of Quizzes Solved (Unique, Average Per Student)', '4');

        cy.logout();
    })


    it('shows the charts ', () => {

        cy.demoTeacherLogin();
        cy.contains('Change course').click();
        cy.changeCourseExecution('1st Semester 2023/2024');

        cy.get('[data-cy="dashboardMenuButton"]').click();

        cy.contains('Statistics for this course execution').click();

        cy.get('canvas').eq(1).scrollIntoView().wait(5000).screenshot('./test/2023/quizzesStatsTestGraphs');
        cy.get('canvas').eq(1).scrollIntoView().wait(5000).screenshot('./test/2023/questionStatsTestGraphs');
        cy.get('canvas').eq(1).scrollIntoView().wait(5000).screenshot('./test/2023/studentsStatsTestGraphs');

        compareImages('2023/quizzesStatsTestGraphs.png', 'quizzesStatsTestGraphs.png');
        compareImages('2023/questionsStatsTestGraphs.png', 'questionStatsTestGraphs.png');
        compareImages('2023/studentsStatsTestGraphs.png', 'studentsStatsTestGraphs.png');

        cy.logout();
    })
});



