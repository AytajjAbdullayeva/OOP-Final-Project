package entity;

import logging.Logger;

import java.io.Serializable;
//import util.Logger;

public class Passenger implements Serializable {
    private String firstName;
    private String lastName;

    public Passenger(String firstName, String lastName) {
        Logger.DebugLog("Creating Passenger: " + firstName + " " + lastName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
