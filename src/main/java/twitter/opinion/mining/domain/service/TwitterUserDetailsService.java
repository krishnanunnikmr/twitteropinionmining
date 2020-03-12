package twitter.opinion.mining.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import twitter.opinion.mining.domain.model.User;
import twitter.opinion.mining.domain.repository.UserRepository;


@Service
public class TwitterUserDetailsService implements UserDetailsService{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findFirstByUserId(username);
        if(user==null){
            throw new UsernameNotFoundException(username+" is not found.");
        }
        return new TwitterUserDetails(user);
    }
}
