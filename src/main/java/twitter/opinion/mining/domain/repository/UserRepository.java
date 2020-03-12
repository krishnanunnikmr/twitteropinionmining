package twitter.opinion.mining.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import twitter.opinion.mining.domain.model.User;

public interface UserRepository extends JpaRepository<User,String>{
    User findFirstByUserId(String userId);


}
