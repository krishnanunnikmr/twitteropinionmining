package twitter.opinion.mining.form;

public class CommentForm {
    //@Size(min = 1,max = 255,message = "Tweets must be between 1 and 255 characters")
    private String content;
    private Integer tweetid;

    /**
	 * @return the tweetid
	 */
	public Integer getTweetid() {
		return tweetid;
	}

	/**
	 * @param tweetid the tweetid to set
	 */
	public void setTweetid(Integer tweetid) {
		this.tweetid = tweetid;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
