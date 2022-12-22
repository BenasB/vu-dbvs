package com.dbvs;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection Connection;

    private static final String USERNAME = "docker"; // bebu8390
    private static final String DATABASE = "docker"; // studentu
    private static final String SERVER = "localhost"; // pgsql3.mif

    public Database() {
        java.io.Console console = System.console();
        console.printf("Username: %s\n", USERNAME);
        String password = new String(console.readPassword("Password: "));

        try {
            Connection = DriverManager.getConnection("jdbc:postgresql://" + SERVER + "/" + DATABASE, USERNAME,
                    password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Successfuly connected to the database");
    }
}
