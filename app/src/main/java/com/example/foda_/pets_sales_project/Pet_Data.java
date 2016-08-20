package com.example.foda_.pets_sales_project;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Query;

/**
 * Created by foda_ on 2016-07-16.
 */
public class Pet_Data implements Parcelable{
    public String OwnerEmail;
    public String Name;
    public String Type;
    public String Gender;
    public String Description;
    public String Age;
    public String Price;
    public String URLImage;
    public String Date;
    public String Time;
    public String UserImage;
    public String UserRate;

    public  static Query parameter;
    public Pet_Data() {
    }

    public Pet_Data(String OwnerEmail, String Name, String Type, String Gender,
                    String Description, String Age, String Price, String URLImage, String Date, String Time,String UserImage,String UserRate) {
        this.OwnerEmail = OwnerEmail;
        this.Name = Name;
        this.Type = Type;
        this.Gender = Gender;
        this.Description = Description;
        this.Age = Age;
        this.Price = Price;
        this.URLImage = URLImage;
        this.Date = Date;
        this.Time = Time;
        this.UserImage=UserImage;
        this.UserRate=UserRate;
    }

    protected Pet_Data(Parcel in) {
        OwnerEmail = in.readString();
        Name = in.readString();
        Type = in.readString();
        Gender = in.readString();
        Description = in.readString();
        Age = in.readString();
        Price = in.readString();
        URLImage = in.readString();
        Date = in.readString();
        Time = in.readString();
        UserImage = in.readString();
        UserRate = in.readString();
    }

    public static final Creator<Pet_Data> CREATOR = new Creator<Pet_Data>() {
        @Override
        public Pet_Data createFromParcel(Parcel in) {
            return new Pet_Data(in);
        }

        @Override
        public Pet_Data[] newArray(int size) {
            return new Pet_Data[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OwnerEmail);
        dest.writeString(Name);
        dest.writeString(Type);
        dest.writeString(Gender);
        dest.writeString(Description);
        dest.writeString(Age);
        dest.writeString(Price);
        dest.writeString(URLImage);
        dest.writeString(Date);
        dest.writeString(Time);
        dest.writeString(UserImage);
        dest.writeString(UserRate);
    }
}
