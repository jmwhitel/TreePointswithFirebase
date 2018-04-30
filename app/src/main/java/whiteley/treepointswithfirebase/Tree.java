package whiteley.treepointswithfirebase;

import java.util.ArrayList;

public class Tree {
    private String status;
    private String grade;
    private Float latitude;
    private Float longitude;
    private String species;
    private ArrayList notes;
    private String comments;
    private ArrayList dbh;
    private String geohash;
    private String health;
    private Integer treeId;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Tree() {
    }

    Tree(String status, String health, String grade, Float latitude, Float longitude, String species, ArrayList notes, String comments, ArrayList dbh, String geohash) {
        this.status = status;
        this.health = health;
        this.grade = grade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.species = species;
        this.notes = notes;
        this.comments = comments;
        this.dbh = dbh;
        this.geohash = geohash;
    }


    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }

    public String getHealth(){
        return health;
    }
    public void setHealth(String health) {
        this.health = health;
    }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade){
        this.grade =grade;
    }

    public Float getLatitude() {
        return latitude;
    }
    public void setLatitude(Float latitude) {
        this.latitude=latitude;
    }

    public Float getLongitude() {
        return longitude;
    }
    public void setLongitude(Float longitude){
        this.longitude=longitude;
    }

    public void setLocation(Float latitude, Float longitude){
        this.latitude=latitude;
        this.longitude=longitude;
        //TODO: use this to add geohash functionality: https://github.com/kungfoo/geohash-java
    }

    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species=species;
    }

    public ArrayList getNotes() {
        return notes;
    }
    public void setNotes(ArrayList notesArray){
        this.notes=notesArray;
    }
    public void addNote(String note){
        this.notes.add(note);
    }
    public void removeNote(String note){
        this.notes.remove(note);
    }

    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList getDBH(){
        return this.dbh;
    }
    public void setDbh(ArrayList dbh){
        this.dbh = dbh;
    }
    public void addDbh(Float dbh){
        this.dbh.add(dbh);
    }
    public void removeDbh(Float dbh){
        this.dbh.remove(dbh);
    }

    public String getGeohash() {
        return geohash;
    }
    public void setGeohash(String geohash){
        this.geohash=geohash;
//        if(  this.latitude !=null && this.longitude != null){
    //TODO: use this to add geohash functionality: https://github.com/kungfoo/geohash-java
//        }
    }


    public Integer getTreeId(){
        return treeId;
    }
    public void setTreeId(Integer treeId){
        this.treeId = treeId;
    }


    public void pushToFirebase (){

    }

}

