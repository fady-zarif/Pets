package com.example.foda_.pets_sales_project;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foda_ on 2016-07-20.
 */
public class UserInfo implements Parcelable {
    public String Country;
    public String Email;
    public String Name;
    public String Phone;
    public String Rating;
    public String ImageUrl;

    public UserInfo() {
    }

    public UserInfo(String Country, String Email, String Name, String Phone,
                    String Rating, String ImageUrl) {

        this.Country=Country;
        this.Email=Email;
        this.Phone=Phone;
        this.Name=Name;
        this.Rating=Rating;
        this.ImageUrl=ImageUrl;
    }

    protected UserInfo(Parcel in) {
        Country = in.readString();
        Email = in.readString();
        Name = in.readString();
        Phone = in.readString();
        Rating = in.readString();
        ImageUrl = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Country);
        dest.writeString(Email);
        dest.writeString(Name);
        dest.writeString(Phone);
        dest.writeString(Rating);
        dest.writeString(ImageUrl);
    }
}
