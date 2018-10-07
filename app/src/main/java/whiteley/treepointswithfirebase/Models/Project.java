package whiteley.treepointswithfirebase.Models;

import com.google.firebase.database.DatabaseReference;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Project {
    private String projectName;
    private String municipality;
    private Integer numberOfTrees;
    private String projectId;
    private String speciesListString;
    private List<String> speciesList;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Project() {
    }

    Project(String projectName, String municipality, Integer numberOfTrees, String projectId, String speciesListString, List speciesList) {
        this.projectName = projectName;
        this.municipality = municipality;
        this.numberOfTrees = numberOfTrees;
        this.projectId = projectId;
        this.speciesListString = speciesListString;
        this.speciesList = speciesList;
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

    public String getSpeciesListString() {
        return speciesListString;
    }
    public void setSpeciesListString(String speciesListString) {
        this.speciesListString=speciesListString;
        this.speciesList = new ArrayList<String>(Arrays.asList(speciesListString.split("\\s*,\\s*")));

    }

    public List<String> getSpeciesList() {
        return speciesList;
    }
    public void setSpeciesList(List<String> speciesList) {
        this.speciesList = speciesList;
    }

    public void addSpecies(String speciesString) {
        this.speciesList.add(0, speciesString);
    }

    public void pushToFirebase (final DatabaseReference databaseReference){
        if(projectId == null) {
            projectId= URLEncoder.encode(projectName);
        }
        databaseReference.child("Projects").child(projectId).child("projectName").setValue(projectName);
        databaseReference.child("Projects").child(projectId).child("municipality").setValue(municipality);
        databaseReference.child("Projects").child(projectId).child("numberOfTrees").setValue(numberOfTrees);
        databaseReference.child("Projects").child(projectId).child("projectId").setValue(projectId);
        databaseReference.child("Projects").child(projectId).child("speciesList").setValue(speciesList);
    }

}