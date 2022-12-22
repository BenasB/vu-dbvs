package com.dbvs.menuOptions;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.dbvs.Database;

public class DeleteProblem implements IMenuOption {

    private static final String QUERY = "DELETE FROM problems "
            + "WHERE id = ?";

    private Scanner in = new Scanner(System.in);

    @Override
    public String getDisplayName() {
        return "Remove an existing problem";
    }

    @Override
    public void execute() {
        new ListProblems().execute();

        System.out.print("\nProblem id to remove: ");
        int problemId = in.nextInt();

        System.out.println();

        try (PreparedStatement pstmt = Database.Connection.prepareStatement(QUERY)) {
            pstmt.setInt(1, problemId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Could not find problem with id " + problemId);

            System.out.println("Problem '" + problemId + "' removed successfully");
        } catch (SQLException ex) {
            System.out.println("Failed to remove an existing problem");
            System.out.println(ex.getMessage());
        }
    }

}
