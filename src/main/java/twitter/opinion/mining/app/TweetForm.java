package twitter.opinion.mining.app;

import javax.validation.constraints.Size;


public class TweetForm {
    //@Size(min = 1,max = 255,message = "Tweets must be between 1 and 255 characters")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
