package com.dbvs;

import java.sql.DriverManager;

public class Database {
    public static Database Instance;

    private static final String username = "docker"; // bebu8390
    private static final String database = "docker"; // studentu
    private static final String server = "localhost"; // pgsql3.mif

    public Database() {
        if (Instance == null)
            Instance = this;

        java.io.Console console = System.console();
        console.printf("Username: %s\n", username);
        String password = new String(console.readPassword("Password: "));

        try {
            DriverManager.getConnection("jdbc:postgresql://" + server + "/" + database, username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Successfuly connected to the database");
    }
}
