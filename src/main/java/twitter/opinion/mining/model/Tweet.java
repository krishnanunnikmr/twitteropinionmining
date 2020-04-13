package twitter.opinion.mining.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tweetId;

    @CreationTimestamp  //@prepersistしてpostTime=new Timestamp(sys.currentTimeMilis());
                        //updatetimestamp
    private Timestamp postTime;

    @ManyToOne
    private User tweetUser;

    @NotNull
    private String content;
    
    //@NotNull
    private String category;

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public User getTweetUser() {
        return tweetUser;
    }

    public void setTweetUser(User tweetUser) {
        this.tweetUser = tweetUser;
    }

    public Tweet() {
    }

    public Tweet(String content , User tweetUser,String category) {
        this.content = content;
        this.tweetUser=tweetUser;
        this.category=category;
    }

    public Integer getTweetId() {
        return tweetId;
    }

    public void setTweetId(Integer tweetId) {
        this.tweetId = tweetId;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
