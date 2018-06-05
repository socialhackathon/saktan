package io.jachoteam.omurbek.saktan;

public class HotlineModel {
    public String name;
    public String phone;

    public HotlineModel() {
    }

    public HotlineModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
