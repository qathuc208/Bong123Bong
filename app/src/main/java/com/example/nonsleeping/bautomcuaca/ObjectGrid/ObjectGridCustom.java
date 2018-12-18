package com.example.nonsleeping.bautomcuaca.ObjectGrid;

public class ObjectGridCustom {
    private String mMoney;
    private int mIdImage;

    public ObjectGridCustom(String mMoney, int mIdImage) {
        this.mIdImage = mIdImage;
        this.mMoney = mMoney;
    }

    public String getmMoney() {
        return mMoney;
    }

    public void setmMoney(String mMoney) {
        this.mMoney = mMoney;
    }

    public int getmIdImage() {
        return mIdImage;
    }

    public void setmIdImage(int mIdImage) {
        this.mIdImage = mIdImage;
    }
}
