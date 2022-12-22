package com.dbvs;

public class App {

    private static Menu menu = new Menu();

    public static void main(String[] args) {
        System.out.println();
        new Database();
        menu.display();
    }
}
