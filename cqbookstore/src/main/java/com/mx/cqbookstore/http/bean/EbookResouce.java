package com.mx.cqbookstore.http.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/20.
 */

public class EbookResouce implements Parcelable {

    private String ID;

    private String CoverPath;

    private String Name;

    private String Author;

    private Double Price;

    private String PublishDate;

    private Integer SaleCount;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCoverPath() {
        return CoverPath;
    }

    public void setCoverPath(String coverPath) {
        CoverPath = coverPath;
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

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
    }

    public Integer getSaleCount() {
        return SaleCount;
    }

    public void setSaleCount(Integer saleCount) {
        SaleCount = saleCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.CoverPath);
        dest.writeString(this.Name);
        dest.writeString(this.Author);
        dest.writeValue(this.Price);
        dest.writeString(this.PublishDate);
        dest.writeValue(this.SaleCount);
    }

    public EbookResouce() {
    }

    protected EbookResouce(Parcel in) {
        this.ID = in.readString();
        this.CoverPath = in.readString();
        this.Name = in.readString();
        this.Author = in.readString();
        this.Price = (Double) in.readValue(Double.class.getClassLoader());
        this.PublishDate = in.readString();
        this.SaleCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<EbookResouce> CREATOR = new Creator<EbookResouce>() {
        @Override
        public EbookResouce createFromParcel(Parcel source) {
            return new EbookResouce(source);
        }

        @Override
        public EbookResouce[] newArray(int size) {
            return new EbookResouce[size];
        }
    };
}
