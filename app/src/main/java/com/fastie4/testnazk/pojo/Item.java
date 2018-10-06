package com.fastie4.testnazk.pojo;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {
    @SerializedName("id")
    public String id;

    @SerializedName("firstname")
    public String firstname;

    @SerializedName("lastname")
    public String lastname;

    @SerializedName("placeOfWork")
    public String placeOfWork;

    @SerializedName("position")
    public String position;

    @SerializedName("linkPDF")
    public String linkPDF;

    public boolean isFavourite;

    public String note;

    public Item() {}

    protected Item(Parcel in) {
        id = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        placeOfWork = in.readString();
        position = in.readString();
        linkPDF = in.readString();
        isFavourite = in.readByte() != 0;
        note = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeString(placeOfWork);
        parcel.writeString(position);
        parcel.writeString(linkPDF);
        parcel.writeByte((byte) (isFavourite ? 1 : 0));
        parcel.writeString(note);
    }
}
