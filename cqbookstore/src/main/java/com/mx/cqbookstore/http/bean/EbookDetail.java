package com.mx.cqbookstore.http.bean;

/**
 * Created by Administrator on 2016/12/23.
 */

public class EbookDetail {
    private String ID;

    private String Name;

    private String ISBN;

    private String Publisher;

    private String PublishDate;

    private String Author;

    private String CoverPath;

    private Integer State;

    private String Language;

    private String Words;

    private Double Price;

    private String UserID;

    private Integer IfCollect;

    private Integer IsPay;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getPublishDate() {
        return PublishDate;
    }

    public void setPublishDate(String publishDate) {
        PublishDate = publishDate;
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

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getWords() {
        return Words;
    }

    public void setWords(String words) {
        Words = words;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Integer getIfCollect() {
        return IfCollect;
    }

    public void setIfCollect(Integer ifCollect) {
        IfCollect = ifCollect;
    }

    public Integer getIsPay() {
        return IsPay;
    }

    public void setIsPay(Integer isPay) {
        IsPay = isPay;
    }
}
