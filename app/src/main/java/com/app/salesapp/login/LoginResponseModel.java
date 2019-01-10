package com.app.salesapp.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponseModel {

    @SerializedName("token")
    public String token;
    @SerializedName("full_name")
    public String fullName;
    @SerializedName("phone")
    public String phone;
    @SerializedName("email")
    public String email;
    @SerializedName("photo")
    public String photo;
    @SerializedName("level")
    public String level;
    @SerializedName("current_program")
    public CurrentProgram currentProgram;
    @SerializedName("list_program")
    public List<ListProgram> listProgram;

    public class CurrentProgram {
        @SerializedName("program_id")
        public String programId;
        @SerializedName("name")
        public String name;
    }

    public class ListProgram {
        @SerializedName("program_id")
        public String programId;
        @SerializedName("name")
        public String name;
    }

    @SuppressWarnings({"unused", "used by Retrofit"})
    public LoginResponseModel() {
    }
}
