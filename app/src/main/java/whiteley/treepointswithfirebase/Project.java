package whiteley.treepointswithfirebase;

import android.util.Base64;

import com.google.firebase.database.DatabaseReference;

import java.net.URLEncoder;

public class Project {
    private String projectName;
    private String municipality;
    private Integer numberOfTrees;
    private String projectId;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Project() {
    }

    Project(String projectName, String municipality, Integer numberOfTrees, String projectId) {
        this.projectName = projectName;
        this.municipality = municipality;
        this.numberOfTrees = numberOfTrees;
        this.projectId = projectId;
    }

    public String getProjectName(){
        return projectName;
    }
    public void setProjectName(String projectName){
        this.projectName=projectName;
    }

    public String getMunicipality(){
        return municipality;
    }
    public void setMunicipality(String municipality){
        this.municipality=municipality;
    }

    public Integer getNumberOfTrees() {
        return numberOfTrees;
    }
    public void setNumberOfTrees(Integer numberOfTrees){
        this.numberOfTrees=numberOfTrees;
    }

    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String  projectId){
        this.projectId=projectId;
    }

    public void pushToFirebase (final DatabaseReference databaseReference){
        if(projectId == null) {
            projectId= URLEncoder.encode(projectName);
        }
        databaseReference.child("Projects").child(projectId).child("projectName").setValue(projectName);
        databaseReference.child("Projects").child(projectId).child("municipality").setValue(municipality);
        databaseReference.child("Projects").child(projectId).child("numberOfTrees").setValue(numberOfTrees);
        databaseReference.child("Projects").child(projectId).child("projectId").setValue(projectId);
    }

}