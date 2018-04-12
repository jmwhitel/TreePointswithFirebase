package whiteley.treepointswithfirebase;

public class Tree {
    private String status;
    private String rating;
    private String grade;
    private Float latitude;
    private Float longitude;
    private String species;
    private String notes;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Tree() {
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



}
