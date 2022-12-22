package com.dbvs.menuOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbvs.Database;

public class ListProblems implements IMenuOption {

    private static final String QUERY = "SELECT * FROM problems";

    @Override
    public String getDisplayName() {
        return "List all problems";
    }

    @Override
    public void execute() {
        try (Statement stmt = Database.Connection.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY)) {
            displayProblem(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayProblem(ResultSet rs) throws SQLException {
        System.out.println();
        System.out.println("Here are all of the problems");
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
