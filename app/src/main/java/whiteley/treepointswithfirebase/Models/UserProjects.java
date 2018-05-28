package whiteley.treepointswithfirebase.Models;

public class UserProjects {

    private String ProjectName;

    public UserProjects(String projectName) {
        ProjectName = projectName;
    }

    public UserProjects() {
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }
}
