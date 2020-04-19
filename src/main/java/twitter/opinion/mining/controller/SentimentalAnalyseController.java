package twitter.opinion.mining.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hankcs.hanlp.classification.classifiers.IClassifier;

import twitter.opinion.mining.model.Tweet;
import twitter.opinion.mining.response.pojo.AnalysedData;
import twitter.opinion.mining.service.TweetService;

@Controller
public class SentimentalAnalyseController {
	
	@Autowired
	@Qualifier("sentimentalClassifier") 
	private IClassifier iClassifier;
	
	@Autowired
	private TweetService tweetService;

	@GetMapping("/analyse/{id}")
	String loadform(Principal principal, Model model,@PathVariable Integer id){

		List<AnalysedData> analysedDatas=new ArrayList<AnalysedData>();
		Tweet tweet =tweetService.find(id);
		StringBuffer sf=new StringBuffer();
		tweet.getComments().forEach(come->{
			sf.append(come.getComment());
		});
		Map<String, Double> datas=iClassifier.predict(sf.toString());
		datas.forEach((c,v)->{
			analysedDatas.add(new AnalysedData(c, v));
		});
		
		model.addAttribute("analysedData", analysedDatas);

		return "sentimental";
	}


}
