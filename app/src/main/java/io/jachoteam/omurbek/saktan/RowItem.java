package io.jachoteam.omurbek.saktan;

/**
 * Created by omurbek on 10/22/2017.
 */

public class RowItem {

    public String name, phone, image;

    public RowItem(String name, String phone, String image) {
        this.name = name;
        this.phone = phone;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return name+ " " + phone + " "+ image;
    }
}
