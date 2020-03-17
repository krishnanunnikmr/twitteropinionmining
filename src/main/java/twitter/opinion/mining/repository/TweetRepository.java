package twitter.opinion.mining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.opinion.mining.model.Tweet;

import java.util.List;


public interface TweetRepository extends JpaRepository<Tweet,Integer>{
    List<Tweet> findAllByOrderByPostTimeDesc();
    List<Tweet> findTop100ByTweetUser_UserIdInOrderByPostTimeDesc(List<String> tweetUserId);
	void deleteById(Integer id);
	Tweet findFirstByTweetId(Integer id);
   
}
