package com.example.onlinertc;

public class UserNumber {
    private static UserNumber instance;
    private String number;

    private UserNumber() {}

    public static UserNumber getInstance() {
        if (instance == null) {
            instance = new UserNumber();
        }
        return instance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
