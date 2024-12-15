package com.ismail.personalsafety.ModelClasses;

public  class User {
    public String userName;
    public String userDistrict;
    public String userPhoneNum;
    public String userEmail;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String userDistrict, String userPhoneNum, String userEmail) {
        this.userName = userName;
        this.userDistrict = userDistrict;
        this.userPhoneNum = userPhoneNum;
        this.userEmail = userEmail;
    }
}
