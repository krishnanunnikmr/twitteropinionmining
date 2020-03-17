package twitter.opinion.mining.util;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import twitter.opinion.mining.controller.FileUploadController;
import twitter.opinion.mining.model.User;
import twitter.opinion.mining.service.TwitterUserDetails;


public class Util {
    private static String noIcon;

    public static String[] imageExtensions ={"jpg","jpeg","png","gif"};

    public static User getLoginuserFromPrincipal(Principal principal){
        Authentication authentication=(Authentication)principal;
        TwitterUserDetails userDetails=TwitterUserDetails.class.cast(authentication.getPrincipal());
        return userDetails.getuser();
        /*TwitterCloneUserDetails userDetails=(TwitterCloneUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getuser();*/
    }
    public static void updateAuthenticate(Principal principal, User newUser) {
        Authentication oldAuth= (Authentication) principal;
        Authentication newAuth=new UsernamePasswordAuthenticationToken(new TwitterUserDetails(newUser),oldAuth.getCredentials(),oldAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public static String getNoIcon() {
        if(noIcon==null)
            noIcon= MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,"serveFile","noicon.png").build().toString();
        return noIcon;
    }
}
