package whiteley.treepointswithfirebase.Models;

public class User {

    private String UserName;
    private String EmailAddress;

    public User(String UserName, String EmailAddress) {
       this.UserName = UserName;
       this.EmailAddress = EmailAddress;
    }

    public User(){

    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        UserName = UserName;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserName='" + UserName + '\'' +
                ", EmailAddress='" + EmailAddress + '\'' +
                '}';
    }
}

