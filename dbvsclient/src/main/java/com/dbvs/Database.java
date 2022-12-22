package com.dbvs;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection Connection;

    public Database() {
        java.io.Console console = System.console();
        String server = console.readLine("Server: ");
        String database = console.readLine("Database: ");
        String username = console.readLine("Username: ");
        String password = new String(console.readPassword("Password: "));

        try {
            Connection = DriverManager.getConnection("jdbc:postgresql://" + server + "/" + database, username,
                    password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Successfuly connected to the database");
    }
}
