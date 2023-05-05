package com.bee.cnscnewsandupdate.login_and_register_system;

public class ReadWriteUserDetails {
    public String doB, gender, mobile;

    public ReadWriteUserDetails(){};


    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile) {
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}
