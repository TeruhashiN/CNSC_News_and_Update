package com.bee.cnscnewsandupdate.login_and_register_system;

public class ReadWriteUserDetails {
    public String doB, gender, mobile, educasstId;

    public ReadWriteUserDetails(){};


    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile, String educasstId) {
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
        this.educasstId = educasstId;
    }
}
