package com.dbvs.menuOptions;

public class RegisterUser implements IMenuOption {

    @Override
    public String getDisplayName() {
        return "Register a new user";
    }

    @Override
    public void execute() {
        System.out.println("Registering user");
    }

}
