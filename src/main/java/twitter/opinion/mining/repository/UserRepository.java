package twitter.opinion.mining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.opinion.mining.model.User;

public interface UserRepository extends JpaRepository<User,String>{
    User findFirstByUserId(String userId);


}
