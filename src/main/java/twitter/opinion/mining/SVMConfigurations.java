package twitter.opinion.mining;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hankcs.hanlp.classification.classifiers.IClassifier;

import twitter.opinion.mining.svm.classification.LinearSVMClassifier;
import twitter.opinion.mining.util.SVMUtil;

@Configuration
public class SVMConfigurations {

	@Value( "${path.svm.dataset}")
	private String datasetPath;
	
	
	@Bean
	IClassifier iClassifier() throws IOException {
		return new LinearSVMClassifier(SVMUtil.trainAndLoadModel(datasetPath));
	}
	
	
}
