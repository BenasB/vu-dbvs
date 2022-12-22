package com.dbvs.menuOptions;

public class Exit implements IMenuOption {

    @Override
    public String getDisplayName() {
        return "Exit";
    }

    @Override
    public void execute() {
        System.exit(0);
    }

}
