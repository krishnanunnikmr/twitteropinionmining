package twitter.opinion.mining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.opinion.mining.model.Comment;
import twitter.opinion.mining.model.Tweet;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Integer>{
  
   
}
