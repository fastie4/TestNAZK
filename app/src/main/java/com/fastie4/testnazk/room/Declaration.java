package com.fastie4.testnazk.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Declaration {
    @NonNull
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "place_of_work")
    private String placeOfWork;

    @ColumnInfo(name = "position")
    private String position;

    @ColumnInfo(name = "link_pdf")
    private String linkPdf;

    @ColumnInfo(name = "note")
    private String note;

    public Declaration() {
    }

    public Declaration(@NonNull String id, String firstName, String lastName, String placeOfWork,
                       String position, String linkPdf, String note) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.placeOfWork = placeOfWork;
        this.position = position;
        this.linkPdf = linkPdf;
        this.note = note;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLinkPdf() {
        return linkPdf;
    }

    public void setLinkPdf(String linkPdf) {
        this.linkPdf = linkPdf;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
