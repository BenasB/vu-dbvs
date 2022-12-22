package com.dbvs.menuOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import com.dbvs.Database;

public class SolveProblem implements IMenuOption {

    private static final String QUERY_SOLUTION = "INSERT INTO solutions "
            + "(problem_id, submitted_by, source_file) "
            + "VALUES(?, ?, ?)";

    private static final String QUERY_TEST_CASES = "SELECT Id FROM test_cases "
            + "WHERE problem_id = ?";

    private static final String QUERY_TEST_RESULT = "INSERT INTO test_results "
            + "(is_success, time_spent, memory_used, solution_id, test_case_id) "
            + "VALUES(?, ?, ?, ?, ?)";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "Solve a problem";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.dbvs.menuOptions.IMenuOption#execute()
     */
    @Override
    public void execute() {
        new ListUsers().execute();
        new ListProblems().execute();

        System.out.print("Problem ID you want to solve: ");
        int problemId = in.nextInt();
        System.out.print("Username that solves this problem: ");
        String username = in.next();
        System.out.println("Source file: ");
        String sourceFile = in.next();

        System.out.println();

        Connection conn = Database.Connection;
        try {
            conn.setAutoCommit(false);

            // Insert solution
            PreparedStatement solutionStatement = conn.prepareStatement(QUERY_SOLUTION,
                    Statement.RETURN_GENERATED_KEYS);
            solutionStatement.setInt(1, problemId);
            solutionStatement.setString(2, username);
            solutionStatement.setString(3, sourceFile);

            int affectedRows = solutionStatement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Failed to solve a problem");

            // Get newly created solution's ID
            ResultSet generatedIdSet = solutionStatement.getGeneratedKeys();
            if (!generatedIdSet.next())
                throw new SQLException("Failed to retrieve generated solution ID");

            int newSolutionId = generatedIdSet.getInt(1);

            // Query test cases of specified problem id
            PreparedStatement casesStatement = conn.prepareStatement(QUERY_TEST_CASES);
            casesStatement.setInt(1, problemId);
            ResultSet casesSet = casesStatement.executeQuery();
            Queue<Integer> casesQueue = new LinkedList<>();
            while (casesSet.next()) {
                casesQueue.add(casesSet.getInt(1));
            }

            // Create fake test results for each test case
            while (!casesQueue.isEmpty()) {
                PreparedStatement resultStatement = conn.prepareStatement(QUERY_TEST_RESULT);
                resultStatement.setBoolean(1, true);
                resultStatement.setFloat(2, 1.0f);
                resultStatement.setInt(3, 128);
                resultStatement.setInt(4, newSolutionId);
                resultStatement.setInt(5, casesQueue.remove());

                affectedRows = resultStatement.executeUpdate();

                if (affectedRows == 0)
                    throw new SQLException("Failed to create a test result");
            }

            conn.commit();
            System.out.println("New solution with ID '" + newSolutionId + "' to problem ID '" + problemId
                    + "' created successfully");
        } catch (SQLException ex) {
            System.out.println("Failed to solve a problem");
            System.out.println(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
