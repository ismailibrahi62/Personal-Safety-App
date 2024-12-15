package com.ismail.personalsafety.ModelClasses;

public class Request {
    private String emergencyType;
    private String userName;
    private String district;
    private String userPhoneNum;
    private String imageUrl;

    // Empty constructor required for Firebase
    public Request() {
    }

    public Request(String emergencyType, String userName, String district, String userPhoneNum, String imageUrl) {
        this.emergencyType = emergencyType;
        this.userName = userName;
        this.district = district;
        this.userPhoneNum = userPhoneNum;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getEmergencyType() {
        return emergencyType;
    }

    public void setEmergencyType(String emergencyType) {
        this.emergencyType = emergencyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
