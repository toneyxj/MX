package com.moxi.studentclient.model;

/**
 * Created by qiaokeli on 16/12/1.
 */

public class AnwerBean {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private int id;
    private char value;
    private boolean isSelect;


}
