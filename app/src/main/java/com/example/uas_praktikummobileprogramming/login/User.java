package com.example.uas_praktikummobileprogramming.login;

public class User {
    public String name;
    public String email;

    public User() {
        // Diperlukan oleh Firebase
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
