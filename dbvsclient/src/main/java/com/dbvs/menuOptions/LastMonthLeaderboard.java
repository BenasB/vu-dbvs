package com.dbvs.menuOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbvs.Database;

public class LastMonthLeaderboard implements IMenuOption {

    private static final String QUERY = "SELECT * FROM leaderboard_solutions_monthly";

    @Override
    public String getDisplayName() {
        return "Last month's leaderboard";
    }

    @Override
    public void execute() {
        try (Statement stmt = Database.Connection.createStatement();
                ResultSet rs = stmt.executeQuery(QUERY)) {
            displayUser(rs);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void displayUser(ResultSet rs) throws SQLException {
        System.out.println();
        System.out.println("Here is last month's leaderboard");
        System.out.println("Username | Correct solutions");
        System.out.println("-----------------");
        while (rs.next()) {
            System.out.println(rs.getString("username") + " | "
                    + rs.getInt("correct solutions"));

        }
    }
}
