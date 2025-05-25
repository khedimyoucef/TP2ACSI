package org.creche;

public class Child {
    private int id;
    private int userId;
    private String name;
    private String birthdate;
    private String allergies;
    private String specialNeeds;

    public Child() {}

    public Child(int userId, String name, String birthdate, String allergies, String specialNeeds) {
        this.userId = userId;
        this.name = name;
        this.birthdate = birthdate;
        this.allergies = allergies;
        this.specialNeeds = specialNeeds;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public String getAllergies() { return allergies; }
    public String getSpecialNeeds() { return specialNeeds; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public void setSpecialNeeds(String specialNeeds) { this.specialNeeds = specialNeeds; }
}

