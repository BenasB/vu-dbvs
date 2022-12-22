package com.dbvs.menuOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbvs.Database;

public class ListUsers implements IMenuOption {

    private static final String QUERY = "SELECT * FROM users";

    @Override
    public String getDisplayName() {
        return "List all users";
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
        System.out.println("Here are all of the users");
        System.out.println("Username | First name | Last name | Email | Points");
        System.out.println("--------------------------------------------------");
        while (rs.next()) {
            System.out.println(rs.getString("username") + " | "
                    + rs.getString("first_name") + " | "
                    + rs.getString("last_name") + " | "
                    + rs.getString("email") + " | "
                    + rs.getString("points"));

        }
    }
}
