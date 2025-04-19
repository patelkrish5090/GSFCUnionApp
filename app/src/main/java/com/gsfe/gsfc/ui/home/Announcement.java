package com.gsfe.gsfc.ui.home;

import android.os.Parcel;
import android.os.Parcelable;

public class Announcement implements Parcelable {
    private String id;
    private String date;
    private String title;
    private String details;
    private String link;

    public Announcement() {
    }

    public Announcement(String id, String date, String title, String details, String link) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.details = details;
        this.link = link;
    }

    protected Announcement(Parcel in) {
        date = in.readString();
        title = in.readString();
        details = in.readString();
        link = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getLink(){return link;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(link);
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };
}