package whiteley.treepointswithfirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    private String status;
    private String grade;
    private Double latitude;
    private Double longitude;
    private String species;
    private String notes ;
    private String comments;
    private Double dbhTotal;
    private String dbh ;
    private String geohash;
    private String health;
    private Integer treeNumber;
    private String treeId;
    private String projectName;
    private String projectId;
    private ArrayList<String> images = new ArrayList();
    private DatabaseReference treeDbRef;
    private DatabaseReference imageDbRef;


    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Tree() {
    }

    Tree(String status, String health, String grade, Double latitude, Double longitude, String species, String notes, String comments, String dbh, Double dbhTotal, String geohash, String projectId, String projectName, Integer treeNumber) {
        this.status = status;
        this.health = health;
        this.grade = grade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.species = species;
        this.notes = notes;
        this.comments = comments;
        this.dbh = dbh;
        this.dbhTotal = dbhTotal;
        this.geohash = geohash;
        this.projectId =projectId;
        this.projectName = projectName;
        this.treeNumber = treeNumber;
    }

    public String getProjectName(){
        return projectName;
    }
    public void setProjectName(String projectName){
        this.projectName=projectName;
    }

    public String getProjectId(){
        return projectId;
    }
    public void setProjectId(String projectId){
        this.projectId=projectId;
    }

    public Integer getTreeNumber() {
        return treeNumber;
    }
    public void setTreeNumber(Integer treeNumber){
        this.treeNumber=treeNumber;
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

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude=latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude){
        this.longitude=longitude;
    }

    public void setLocation(Double latitude, Double longitude){
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


    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getDbh() {return this.dbh;};
    public void setDbh(String dbh) {
        this.dbh=dbh;
        this.dbhTotal=sumDbhValues(dbh);
    };
    private  Double sumDbhValues(String dbh){
        Double sumOfDbh = 0.0;
        List<String> dbhList = Arrays.asList(dbh.split(","));
        for (String value : dbhList) {
            sumOfDbh += Double.valueOf(value);
        }
        return sumOfDbh;
    }

    public String getNotes() {return this.notes;};
    public void setNotes(String notes) {this.notes=notes;};

    public String getGeohash() {
        return geohash;
    }
    public void setGeohash(String geohash){
        this.geohash=geohash;


//        if(  this.latitude !=null && this.longitude != null){
    //TODO: use this to add geohash functionality: https://github.com/kungfoo/geohash-java
//        }
    }


    public String getTreeId(){
        return treeId;
    }
    public void setTreeId(String treeId){
        this.treeId = treeId;
    }

    public void setImages(ArrayList imagesEncoded){
        this.images = imagesEncoded;
    }
    public void addImage(String imageEncoded){
        this.images.add(imageEncoded);
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("grade", grade);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("species", species);
        map.put("notes", notes);
        map.put("comments", comments);
        map.put("dbh", dbh);
        map.put("geohash", geohash);
        map.put("health", health);
        map.put("projectName", projectName);
        map.put("projectId", projectId);
        map.put("treeNumber", treeNumber);
        return map;
    }
    public Boolean pushToFirebase (final DatabaseReference databaseReference){
            if(treeNumber == null) {
               databaseReference.child("Projects").child(projectId).child("numberOfTrees").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer numberOfTrees = Integer.parseInt(dataSnapshot.getValue().toString()) ;

                        treeNumber = numberOfTrees +1;
                        numberOfTrees =numberOfTrees+1;
                        treeId= projectId+"_"+treeNumber;
                       // databaseReference.child("Projects").child(projectId).child("numberOfTrees").setValue(numberOfTrees);
                        Map<String, Object> treeValues = toMap();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/Trees/" + treeId, treeValues);
                        childUpdates.put("/Images/" + treeId, images);
                        childUpdates.put("/Projects/"+projectId+"/numberOfTrees",numberOfTrees);
                        databaseReference.updateChildren(childUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return true;
            } else {
                Map<String, Object> treeValues = toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Trees/" + treeId, treeValues);
                childUpdates.put("/Images/" + treeId, images);
                databaseReference.updateChildren(childUpdates);
                return true;
            }
    }

}


