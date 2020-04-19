package twitter.opinion.mining.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @CreationTimestamp  
    private Timestamp postTime;

    @ManyToOne
    @NotNull
    private User commentUser;
    
	@ManyToOne
    @NotNull
    private Tweet tweet;

    @NotNull
    private String comment;
    
    public Comment() {}
    
    public Comment(Integer commentId, Timestamp postTime, @NotNull User commentUser, @NotNull Tweet tweet,
			@NotNull String comment) {
		super();
		this.commentId = commentId;
		this.postTime = postTime;
		this.commentUser = commentUser;
		this.tweet = tweet;
		this.comment = comment;
	}

	/**
	 * @return the commentId
	 */
	public Integer getCommentId() {
		return commentId;
	}

	/**
	 * @param commentId the commentId to set
	 */
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	/**
	 * @return the postTime
	 */
	public Timestamp getPostTime() {
		return postTime;
	}

	/**
	 * @param postTime the postTime to set
	 */
	public void setPostTime(Timestamp postTime) {
		this.postTime = postTime;
	}

	/**
	 * @return the commentUser
	 */
	public User getCommentUser() {
		return commentUser;
	}

	/**
	 * @param commentUser the commentUser to set
	 */
	public void setCommentUser(User commentUser) {
		this.commentUser = commentUser;
	}

	/**
	 * @return the tweet
	 */
	public Tweet getTweet() {
		return tweet;
	}

	/**
	 * @param tweet the tweet to set
	 */
	public void setTweet(Tweet tweet) {
		this.tweet = tweet;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


 
}
