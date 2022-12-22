package com.dbvs;

import java.util.Scanner;

import com.dbvs.menuOptions.Exit;
import com.dbvs.menuOptions.IMenuOption;
import com.dbvs.menuOptions.RegisterUser;
import com.dbvs.menuOptions.SolveProblem;

public class Menu {
    private IMenuOption[] options = { new RegisterUser(), new SolveProblem(), new Exit() };
    private Scanner in = new Scanner(System.in);

    public void display() {
        while (true) {
            for (int i = 1; i <= options.length; i++) {
                System.out.println("[" + i + "] " + options[i - 1].getDisplayName());
            }

            System.out.print("Select an option: ");
            int selectedOption = in.nextInt();
            if (selectedOption <= 0 || selectedOption > options.length)
                return;

            options[selectedOption - 1].execute();
            System.out.println();
        }
    }
}
