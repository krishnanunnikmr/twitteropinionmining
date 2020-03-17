package twitter.opinion.mining.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {

    @Size(min = 4,max = 20,message = "User ID must be between 4 and 20 characters")
    @Pattern(regexp = "[a-zA-Z0-9]",message = "Only letters or numbers can be used")
    private String userId;

    @Size(min = 4,max = 20,message = "Password must be between 4 and 20 characters")
    @Pattern(regexp = "[a-zA-Z0-9]",message = "Only letters or numbers can be used")
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
