package twitter.opinion.mining.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import twitter.opinion.mining.response.pojo.AnalysedData;

@Controller
public class SentimentalAnalyseController {

	@GetMapping("/analyse")
	String loadform(Principal principal, Model model){

		/*
		 * List<String[]> data=new ArrayList<String[]>();
		 * 
		 * String[] head=new String[2]; head[0]="Sentimental Analysis";
		 * head[1]="Rating"; data.add(head);
		 * 
		 * head=new String[2]; head[0]="Good"; head[1]="21"; data.add(head);
		 * 
		 * head=new String[2]; head[0]="Great"; head[1]="20"; data.add(head); head=new
		 * String[2]; head[0]="Terrible"; head[1]="36"; data.add(head); head=new
		 * String[2]; head[0]="Alright"; head[1]="25"; data.add(head);
		 */

		
		List<AnalysedData> analysedDatas=new ArrayList<AnalysedData>();
		analysedDatas.add(new AnalysedData("Negative", 5));
		analysedDatas.add(new AnalysedData("Nuetral", 5));
		analysedDatas.add(new AnalysedData("Positive", 5));

		
		
		model.addAttribute("analysedData", analysedDatas);

		return "sentimental";
	}


}
