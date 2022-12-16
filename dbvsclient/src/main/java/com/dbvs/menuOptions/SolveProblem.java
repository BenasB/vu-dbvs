package com.dbvs.menuOptions;

public class SolveProblem implements IMenuOption {

    @Override
    public String getDisplayName() {
        return "Solve a problem";
    }

    @Override
    public void execute() {
        System.out.println("Solving a problem");
    }

}
