package com.dbvs.menuOptions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbvs.Database;

public class SearchProblems implements IMenuOption {

    private static final String QUERY = "SELECT * FROM problems "
            + "WHERE lower(description) LIKE ?";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "Search problems";
    }

    @Override
    public void execute() {
        System.out.print("Enter a fragment of a description you are looking for: ");
        String fragment = in.next();

        try (PreparedStatement pstmt = Database.Connection.prepareStatement(QUERY)) {
            pstmt.setString(1, "%" + fragment.toLowerCase() + "%");
            ResultSet rs = pstmt.executeQuery();
            displayProblem(rs, fragment);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayProblem(ResultSet rs, String fragment) throws SQLException {
        System.out.println();
        System.out.println("Here are all of the problems that have '" + fragment + "'");
        System.out.println("Id | Description | Required points | Reward points | Time limit | Memory limit");
        System.out.println("------------------------------------------------------------------------------");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | "
                    + rs.getString("description") + " | "
                    + rs.getString("required_points") + " | "
                    + rs.getString("reward_points") + " | "
                    + rs.getString("time_limit") + " | "
                    + rs.getString("memory_limit"));

        }
    }
}
