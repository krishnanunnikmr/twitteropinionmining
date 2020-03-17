package twitter.opinion.mining.form;

import javax.validation.constraints.Size;

public class UserForm {
    @Size(max = 25,message = "Only letters or numbers can be used")
    private String screenName;

    private String biography;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
