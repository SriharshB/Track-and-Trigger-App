package com.aditi.reform2;

import com.google.firebase.database.Exclude;

public class Items {
    private String itemname;
    private String itemquantity;
    private String itemdescription;
    private String itemimage;
    private String mKey;


    public Items() {

    }

    public Items(String itemname, String itemquantity, String itemdescription, String itemimage) {
        this.itemname = itemname;
        this.itemquantity = itemquantity;
        this.itemdescription = itemdescription;
        this.itemimage = itemimage;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(String itemquantity) {
        this.itemquantity = itemquantity;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemimage() {
        return itemimage;
    }

    public void setItemimage(String itemimage) {
        this.itemimage = itemimage;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String mKey) {
        this.mKey = mKey;
    }
}