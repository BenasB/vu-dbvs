package com.dbvs;

import java.util.Scanner;

import com.dbvs.menuOptions.Exit;
import com.dbvs.menuOptions.IMenuOption;
import com.dbvs.menuOptions.LastMonthLeaderboard;
import com.dbvs.menuOptions.ListProblems;
import com.dbvs.menuOptions.ListSolutions;
import com.dbvs.menuOptions.ListTestCases;
import com.dbvs.menuOptions.ListUsers;
import com.dbvs.menuOptions.RegisterUser;
import com.dbvs.menuOptions.SolveProblem;
import com.dbvs.menuOptions.UpdateUser;

public class Menu {
    private IMenuOption[] options = { new ListUsers(), new RegisterUser(), new UpdateUser(), new LastMonthLeaderboard(),
            new ListProblems(), new ListTestCases(), new ListSolutions(), new SolveProblem(), new Exit() };
    private Scanner in = new Scanner(System.in);

    public void display() {
        while (true) {
            System.out.println();
            for (int i = 1; i <= options.length; i++) {
                System.out.println("[" + i + "] " + options[i - 1].getDisplayName());
            }

            System.out.print("Select an option: ");
            int selectedOption = in.nextInt();
            if (selectedOption <= 0 || selectedOption > options.length) {
                System.out.println("Option not found");
                continue;
            }

            options[selectedOption - 1].execute();
        }
    }
}
