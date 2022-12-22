package com.dbvs.menuOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbvs.Database;

public class ListSolutions implements IMenuOption {

    private static final String QUERY = "SELECT * FROM solutions";

    @Override
    public String getDisplayName() {
        return "List all solutions";
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
        System.out.println("Here are all of the solutions");
        System.out.println("Id | Submitted at | Source file | Problem id | Submitted by");
        System.out.println("--------------------------------------------------");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | "
                    + rs.getDate("submitted_at") + " | "
                    + rs.getString("source_file") + " | "
                    + rs.getInt("problem_id") + " | "
                    + rs.getString("submitted_by"));

        }
    }
}
