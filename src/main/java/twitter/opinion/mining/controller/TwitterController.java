package twitter.opinion.mining.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hankcs.hanlp.classification.classifiers.IClassifier;

import twitter.opinion.mining.exception.UserIdAlreadyExistsException;
import twitter.opinion.mining.exception.UserIdNotFoundException;
import twitter.opinion.mining.form.RegisterForm;
import twitter.opinion.mining.form.TweetForm;
import twitter.opinion.mining.form.UserForm;
import twitter.opinion.mining.model.Tweet;
import twitter.opinion.mining.model.User;
import twitter.opinion.mining.service.*;
import twitter.opinion.mining.service.upload.FileSystemStorageService;
import twitter.opinion.mining.util.SVMUtil;
import twitter.opinion.mining.util.Util;

import java.security.Principal;
import java.util.*;



@Controller
//@SessionAttributes(value = {"userinfo"})
public class TwitterController {

    public static final Logger log = LoggerFactory.getLogger(TwitterController.class);

    private TweetService tweetService;
    private UserService userService;
    private TwitterUserDetailsService userDetailsService;
    private FileSystemStorageService fileSystemStorageService;
    private IClassifier iClassifier;

 
    @Autowired
    public TwitterController(TweetService tweetService,
                                  UserService userService,
                                  TwitterUserDetailsService userDetailsService,
                                  FileSystemStorageService fileSystemStorageService,
                                  @Qualifier("categoryClassifier") IClassifier iClassifier) {
        this.tweetService = tweetService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.fileSystemStorageService = fileSystemStorageService;
        this.iClassifier=iClassifier;
    }

    @GetMapping(value = "/")
    String timeline(Principal principal, Model model) {
        model.addAttribute("tweetForm", new TweetForm());    //attribute can be omitted.

        //default attribute name is Classname whose first letter is lower case.


        User loginUser = Util.getLoginuserFromPrincipal(principal);
        model.addAttribute("userinfo", loginUser);

        model.addAttribute("tweets", tweetService.getTimeLineforLoginUser(loginUser));

        model.addAttribute("recommend", userService.getUnFollowing10Users(loginUser, this));

        log.info("util.noicon: "+Util.getNoIcon());


        return "timeline";
    }


    @PostMapping(value = "/")
    String tweet(Principal principal, @Validated TweetForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Set<String> err = new HashSet<>();
            bindingResult.getAllErrors().forEach(e -> err.add(e.getDefaultMessage()));
            model.addAttribute("errors", err);
            return timeline(principal, model);
            //return "redirect:/";
        }
        Tweet tweet = 
        		new Tweet(form.getContent(),Util.getLoginuserFromPrincipal(principal)
        				,predictTheCategory(form));

        
        
        try {
            tweetService.save(tweet);
        } catch (Exception e) {
            Set<String> err = new HashSet<>();
            err.add("an error occured. try again.");
            model.addAttribute("errors", err);
            log.info(e.toString());
            e.printStackTrace();
            //return timeline(principal,model);
            return "redirect:/";
        }

        return "redirect:/";
    }

    //register
    @GetMapping(value = "/register")
    String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping(value = "/register")
    String register(@Validated RegisterForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("user:" + form.getUserId());
            log.info("pass:" + form.getPassword());
            log.info("scr:" + form.getScreenName());
            Set<String> err = new HashSet<>();
            bindingResult.getAllErrors().forEach(e -> err.add(e.getDefaultMessage()));
            model.addAttribute("errors", err);
            return "register";
        }

        log.info("user:" + form.getUserId());
        log.info("pass:" + form.getPassword());
        log.info("scr:" + form.getScreenName());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User(form.getUserId(), encoder.encode(form.getPassword()), form.getScreenName());
        try {
            userService.create(user);
        } catch (UserIdAlreadyExistsException e) {
            Set<String> errors = new HashSet<>();
            errors.add(e.getMessage());
            model.addAttribute("errors", errors);
            return "register";
        } catch (Exception e) {

            Set<String> errors = new HashSet<>();
            errors.add("unexpected error occured. try again.");
            model.addAttribute("errors", errors);

            log.info(e.toString());
            return "register";
        }
        return "redirect:/loginForm";
    }

    @GetMapping("/update")
    String updateUserDataPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        //model.addAttribute("uploadForm",new UploadFileForm());
        return "mypage";
    }

    @PostMapping("/update")
    String updateUserData(Principal principal, @Validated UserForm form, BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            Set<String> err = new HashSet<>();
            bindingResult.getAllErrors().forEach(e -> err.add(e.getDefaultMessage()));
            model.addAttribute("errors", err);
            return updateUserDataPage(model);
            //return "mypage"; 
        }

        try {
            User newUser = userService.find(Util.getLoginuserFromPrincipal(principal).getUserId());
            if (!Objects.equals(form.getScreenName(), ""))
                newUser.setScreenName(form.getScreenName());
            if (!Objects.equals(form.getBiography(), ""))
                newUser.setBiography(form.getBiography());
            userService.update(newUser);

            Util.updateAuthenticate(principal, newUser);

            model.addAttribute("userinfo", newUser);
        } catch (UserIdNotFoundException e) {
            Set<String> errors = new HashSet<>();
            errors.add(e.getMessage());
            model.addAttribute("errors", errors);
            return "mypage";
        } catch (Exception e) {
            Set<String> errors = new HashSet<>();
            errors.add("unexpected error occured. try again.");
            model.addAttribute("errors", errors);
            log.info(e.getMessage());
            return "mypage";
        }
        return "redirect:/";
    }

    @PostMapping(value = "/follow/{userid}")
    String follow(Principal principal, @PathVariable("userid") String userid, RedirectAttributes attributes){
        User loginUser=Util.getLoginuserFromPrincipal(principal);
        try {
            User target = userService.find(userid);
            loginUser.getFollowing().add(target);
            userService.update(loginUser); 
            Util.updateAuthenticate(principal, loginUser);  
        }catch (Exception e) {
            Set<String> errors = new HashSet<>();
            errors.add("unexpected error occured. try again.");
            log.info(e.toString());
            attributes.addFlashAttribute("errors", errors);
            log.info(e.getMessage());
        }
        return "redirect:/";
    }


    //------Util--------------------------------------

    @GetMapping("/debug")
    String debug() {
        List<User> users = userService.findAll();
        for (User u : users) {
            log.info(u.toString());
        }
        return "redirect:/";
    }
    
    private String predictTheCategory(TweetForm form) {
    	String category="Other";
    	try {
    		 category=SVMUtil.predictTheCategory(iClassifier, form.getContent());
    		 log.info("predicted Category" + category);
    	}
    	catch(Exception e) {
    		category="Other";
    	}
    	return category;
    }

}
