package com.mx.cqbookstore.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/30.
 */

public class Boughtbean implements Parcelable {

    String OrderID;
    String UserID;
    String OredrCode;
    Double Capital;
    String PayTime;
    String BookID;
    String Name;
    String Author;
    String CoverPath;
    String PublishDate;
    String Publisher;

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getOredrCode() {
        return OredrCode;
    }

    public void setOredrCode(String oredrCode) {
        OredrCode = oredrCode;
    }

    public Double getCapital() {
        return Capital;
    }

    public void setCapital(Double capital) {
        Capital = capital;
    }

    public String getPayTime() {
        return PayTime;
    }

    public void setPayTime(String payTime) {
        PayTime = payTime;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getCoverPath() {
        return CoverPath;
    }

    public void setCoverPath(String coverPath) {
        CoverPath = coverPath;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.OrderID);
        dest.writeString(this.UserID);
        dest.writeString(this.OredrCode);
        dest.writeValue(this.Capital);
        dest.writeString(this.PayTime);
        dest.writeString(this.BookID);
        dest.writeString(this.Name);
        dest.writeString(this.Author);
        dest.writeString(this.CoverPath);
        dest.writeString(this.PublishDate);
        dest.writeString(this.Publisher);
    }

    public Boughtbean() {
    }

    protected Boughtbean(Parcel in) {
        this.OrderID = in.readString();
        this.UserID = in.readString();
        this.OredrCode = in.readString();
        this.Capital = (Double) in.readValue(Double.class.getClassLoader());
        this.PayTime = in.readString();
        this.BookID = in.readString();
        this.Name = in.readString();
        this.Author = in.readString();
        this.CoverPath = in.readString();
        this.PublishDate = in.readString();
        this.Publisher = in.readString();
    }

    public static final Creator<Boughtbean> CREATOR = new Creator<Boughtbean>() {
        @Override
        public Boughtbean createFromParcel(Parcel source) {
            return new Boughtbean(source);
        }

        @Override
        public Boughtbean[] newArray(int size) {
            return new Boughtbean[size];
        }
    };
}
