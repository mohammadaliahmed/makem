package com.appsinventiv.makemoney;

/**
 * Created by maliahmed on 12/19/2017.
 */

public class Users {
    String name,phone;

    public Users() {
    }

    public Users(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
