package com.alexandresocha.gigaquiz;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class QuestionCategorie implements Parcelable {
    public static final int PROGRAMMING = 1;
    public static final int GEOGRAPHY = 2;
    public static final int MATH = 3;

    private int id;
    private String name;

    public QuestionCategorie(){}

    public QuestionCategorie(String name) {
        this.name = name;
    }

    protected QuestionCategorie(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<QuestionCategorie> CREATOR = new Creator<QuestionCategorie>() {
        @Override
        public QuestionCategorie createFromParcel(Parcel in) {
            return new QuestionCategorie(in);
        }

        @Override
        public QuestionCategorie[] newArray(int size) {
            return new QuestionCategorie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionCategorie that = (QuestionCategorie) o;
        return getId() == that.getId() && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
