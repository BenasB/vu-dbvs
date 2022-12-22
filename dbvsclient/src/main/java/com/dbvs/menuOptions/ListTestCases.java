package com.dbvs.menuOptions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbvs.Database;

public class ListTestCases implements IMenuOption {

    private static final String QUERY = "SELECT * FROM test_cases "
            + "WHERE problem_id = ?";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "List test cases of a problem";
    }

    @Override
    public void execute() {
        new ListProblems().execute();

        System.out.print("Specify a problem id: ");
        int problemId = in.nextInt();

        try (PreparedStatement pstmt = Database.Connection.prepareStatement(QUERY)) {
            pstmt.setInt(1, problemId);
            ResultSet rs = pstmt.executeQuery();
            displayProblem(rs, problemId);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayProblem(ResultSet rs, int problemId) throws SQLException {
        System.out.println();
        System.out.println("Here are all of the test cases for problem id " + problemId);
        System.out.println("Input | Expected output");
        System.out.println("-----------------------");
        while (rs.next()) {
            System.out.println(rs.getString("input") + " | "
                    + rs.getString("expected_output"));
        }
    }
}
