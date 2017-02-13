package com.instainsight.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ashwinir on 20-12-2016.
 */

public class EncryptDecryptBean implements Parcelable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Data")
    private String Data;
    @JsonProperty("Authenticator")
    private String Authenticator;

    @JsonProperty("Data")
    public String getData() {
        return Data;
    }

    @JsonProperty("Data")
    public void setData(String data) {
        this.Data = data;
    }

    @JsonProperty("Authenticator")
    public String getAuthenticator() {
        return Authenticator;
    }

    @JsonProperty("Authenticator")
    public void setAuthenticator(String authenticator) {
        this.Authenticator = authenticator;
    }

    @Override
    public String toString() {
        return "EncryptDecryptBean{" +
                "data='" + Data + '\'' +
                ", authenticator='" + Authenticator + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
