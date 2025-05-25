package org.creche;
import java.sql.Timestamp;
public class Admission {
    private int id;
    private int childId;
    private int userId;
    private String status;
    private Timestamp requestedAt;
    public Admission() {}

    public Admission(int userId,int childId, String status) {
        this.userId = userId;
        this.status = status;
        this.childId = childId; 
    }

    // Getters and setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getStatus() { return status; }
    public int getChildId() { return childId; }
    
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setStatus(String status) { this.status = status; }
    public void setChildId(int childId) { this.childId = childId; }

    public Timestamp getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Timestamp requestedAt) { this.requestedAt = requestedAt; }
}
