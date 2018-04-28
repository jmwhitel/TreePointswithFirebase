package whiteley.treepointswithfirebase;

import java.util.ArrayList;

public class Tree {
    //TODO: Update lat and long to float
    private String key;
    private String status;
    private String grade;
    private Float latitude;
    private Float longitude;
    private String species;
    private String notes;
    private String comments;
    private ArrayList dbh;
    private String geohash;
    private String health;
    private Integer treeId;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Tree() {
    }

    Tree(String status, String rating, String grade, Float latitude, Float longitude, String species, String notes) {
        this.status = status;
        this.health = rating;
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
        return health;
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
        this.key = Key;
    }


    public void pushToFirebase (){

    }

}
