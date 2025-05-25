package org.creche;

public class Admission {
    private int id;
    private int userId;
    private String status;

    public Admission() {}

    public Admission(int userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setStatus(String status) { this.status = status; }
}
