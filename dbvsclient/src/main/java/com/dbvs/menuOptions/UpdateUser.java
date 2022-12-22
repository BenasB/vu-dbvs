package com.dbvs.menuOptions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbvs.Database;

public class UpdateUser implements IMenuOption {

    private static final String QUERY_SELECT = "SELECT first_name, last_name, email FROM users "
            + "WHERE username = ?";

    private static final String QUERY_UPDATE = "UPDATE users SET first_name = ?, last_name = ?, email = ? "
            + "WHERE username = ?";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "Update an existing user";
    }

    @Override
    public void execute() {
        new ListUsers().execute();

        System.out.println("\nUsername to update: ");
        String username = in.nextLine();

        System.out.println();

        try {
            PreparedStatement selectStatement = Database.Connection.prepareStatement(QUERY_SELECT);
            selectStatement.setString(1, username);
            ResultSet selectResultSet = selectStatement.executeQuery();

            if (!selectResultSet.next())
                throw new SQLException("User with username '" + username + "' does not exist");

            String firstName = selectResultSet.getString("first_name");
            String lastName = selectResultSet.getString("last_name");
            String email = selectResultSet.getString("email");

            System.out.print("New first name (previously was: '" + firstName + "'): ");
            String newFirstName = in.nextLine();
            System.out.print("New last name (previously was: '" + lastName + "'): ");
            String newLastName = in.nextLine();
            System.out.print("New email (previously was: '" + email + "'): ");
            String newEmail = in.nextLine();

            PreparedStatement updateStatement = Database.Connection.prepareStatement(QUERY_UPDATE);
            updateStatement.setString(1, newFirstName);
            updateStatement.setString(2, newLastName);
            updateStatement.setString(3, newEmail);
            updateStatement.setString(4, username);

            int affectedRows = updateStatement.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException();

            System.out.println("User '" + username + "' updated successfully");
        } catch (SQLException ex) {
            System.out.println("Failed to update an existing user");
            System.out.println(ex.getMessage());
        }
    }

}
