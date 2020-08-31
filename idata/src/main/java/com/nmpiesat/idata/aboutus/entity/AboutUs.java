package com.nmpiesat.idata.aboutus.entity;

public class AboutUs {
    private String id;
    private String platformIntroduction;		// 平台简介
    private String telephone;		// 电话
    private String postcode;		// 邮编
    private String email;		// 邮箱

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatformIntroduction() {
        return platformIntroduction;
    }

    public void setPlatformIntroduction(String platformIntroduction) {
        this.platformIntroduction = platformIntroduction;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AboutUs{" +
                "id='" + id + '\'' +
                ", platformIntroduction='" + platformIntroduction + '\'' +
                ", telephone='" + telephone + '\'' +
                ", postcode='" + postcode + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
