package twitter.opinion.mining.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import twitter.opinion.mining.app.TwitterController;
import twitter.opinion.mining.domain.model.User;
import twitter.opinion.mining.domain.repository.UserRepository;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User create(User user) throws UserIdAlreadyExistsException{
		
		  User tmp=userRepository.findFirstByUserId(user.getUserId()); 
		  if(tmp!=null) throw new
		  UserIdAlreadyExistsException(user.getUserId()+" is already exists.");
		 
        return (User) userRepository.save(user);
    }
    public User update(User user) {
		
    	  User tmp=userRepository.findFirstByUserId(user.getUserId()); 
		  if(null==tmp) throw new
		  UserIdNotFoundException(user.getUserId() +"is not found.");
		 

        userRepository.save(user);
        return user;
    }
    public void delete(String id){
        userRepository.deleteById(id);
    }
    public User find(String id){
        return userRepository.findFirstByUserId(id);
    }

    public List<User> getUnFollowing10Users(User loginUser, TwitterController twitterController){
        TwitterController.log.info("loginuser is: " + loginUser.toString());

        List<User> alluser= findAll();
        List<User> following=loginUser.getFollowing();
        List<User> unFollowing10Users=alluser.stream()
                                    .limit(10)
                                    .filter(u->!(following.contains(u) || u.equals(loginUser) ))
                                    .collect(Collectors.toList());
        return unFollowing10Users;
    }

}
