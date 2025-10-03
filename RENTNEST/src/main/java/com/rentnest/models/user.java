package com.rentnest.models;

public class user {
    private int id;
    private String username;
    private String email;
    private String passwordHash;

    public user(int id, String username, String email, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
