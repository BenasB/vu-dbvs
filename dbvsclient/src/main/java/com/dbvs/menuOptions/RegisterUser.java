package com.dbvs.menuOptions;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbvs.Database;

public class RegisterUser implements IMenuOption {

    private static final String QUERY = "INSERT INTO users(username, first_name, last_name, email) "
            + "VALUES(?, ?, ?, ?)";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "Register a new user";
    }

    @Override
    public void execute() {
        System.out.println("Username: ");
        String username = in.nextLine();
        System.out.println("First name: ");
        String firstName = in.nextLine();
        System.out.println("Last name: ");
        String lastName = in.nextLine();
        System.out.println("Email: ");
        String email = in.nextLine();

        System.out.println();

        try (PreparedStatement pstmt = Database.Connection.prepareStatement(QUERY)) {
            pstmt.setString(1, username);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Failed to register a new user");
                return;
            }
            System.out.println("New user '" + username + "' created successfully");
        } catch (SQLException ex) {
            System.out.println("Failed to register a new user");
            System.out.println(ex.getMessage());
        }
    }

}
