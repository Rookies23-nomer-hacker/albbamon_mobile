package com.example.albbamon.model;

import com.google.gson.annotations.SerializedName;

public class LoginUserModel {

        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;


        public String getEmail() { return email; }
        public String getPassword() { return password; }

        public LoginUserModel(String email, String password) {
            this.email = email;
            this.password = password;

        }


}