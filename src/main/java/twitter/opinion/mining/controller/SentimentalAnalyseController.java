package twitter.opinion.mining.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import com.hankcs.hanlp.classification.classifiers.IClassifier;

import twitter.opinion.mining.form.TweetForm;
import twitter.opinion.mining.response.pojo.AnalysedData;

@Controller
public class SentimentalAnalyseController {
	
	@Autowired
	@Qualifier("sentimentalClassifier") 
	private IClassifier iClassifier;

	@PostMapping("/analyse")
	String loadform(Principal principal, Model model,@Validated TweetForm form){

		List<AnalysedData> analysedDatas=new ArrayList<AnalysedData>();
		Map<String, Double> datas=iClassifier.predict(form.getContent());
		datas.forEach((c,v)->{
			analysedDatas.add(new AnalysedData(c, v));
		});
		
		model.addAttribute("analysedData", analysedDatas);

		return "sentimental";
	}


}
