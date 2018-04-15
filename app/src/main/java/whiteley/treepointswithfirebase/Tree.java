package whiteley.treepointswithfirebase;

public class Tree {
    //TODO: Update lat and long to float
    private String Key;
    private String Status;
    private String Rating;
    private String Grade;
    private String Latitude;
    private String Longitude;
    private String Species;
    private String Notes;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Tree() {
    }

    Tree(String Status, String Rating, String Grade, String Latitude, String Longitude, String Species, String Notes) {
        this.Status = Status;
        this.Rating = Rating;
        this.Grade = Grade;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Species = Species;
        this.Notes = Notes;
    }


    public String getStatus(){
        return Status;
    }

    public String getRating(){
        return Rating;
    }

    public String getGrade() {
        return Grade;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getSpecies() {
        return Species;
    }

    public String getNotes() {
        return Notes;
    }

    public String getKey(){
        return Key;
    }

    public void setKey(String Key){
        this.Key = Key;
    }


}
