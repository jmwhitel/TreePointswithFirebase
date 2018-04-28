package whiteley.treepointswithfirebase;

import com.google.firebase.database.DatabaseReference;

public class Tree {
    //TODO: Update lat and long to float
    private String key;
    private String status;
    private String rating;
    private String grade;
    private Float latitude;
    private Float longitude;
    private String species;
    private String notes;
    private String comments;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Tree() {
    }

    Tree(String status, String rating, String grade, Float latitude, Float longitude, String species, String notes) {
        this.status = status;
        this.rating = rating;
        this.grade = grade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.species = species;
        this.notes = notes;
    }


    public String getStatus(){
        return status;
    }

    public String getRating(){
        return rating;
    }

    public String getGrade() {
        return grade;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getSpecies() {
        return species;
    }

    public String getNotes() {
        return notes;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String Key){
        this.key = key;
    }

}
