package com.dbvs.menuOptions;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbvs.Database;

public class DeleteUser implements IMenuOption {

    private static final String QUERY = "DELETE FROM users "
            + "WHERE username = ?";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "Remove an existing user";
    }

    @Override
    public void execute() {
        new ListUsers().execute();

        System.out.print("\nUsername to remove: ");
        String username = in.nextLine();

        System.out.println();

        try (PreparedStatement pstmt = Database.Connection.prepareStatement(QUERY)) {
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Could not find '" + username + "' user");

            System.out.println("User '" + username + "' removed successfully");
        } catch (SQLException ex) {
            System.out.println("Failed to remove an existing user");
            System.out.println(ex.getMessage());
        }
    }

}
