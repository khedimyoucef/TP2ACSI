package org.creche;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String role;
    private String email; 
    public User() {}

    public User(String username, String passwordHash, String role, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getEmail() { return email; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; } 
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public boolean checkPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hash){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
