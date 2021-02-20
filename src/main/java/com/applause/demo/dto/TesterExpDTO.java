package com.applause.demo.dto;

public class TesterExpDTO {
    private String firstName;
    private String lastName;
    private int exp;

    public TesterExpDTO(String firstName, String lastName, int exp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.exp = exp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
