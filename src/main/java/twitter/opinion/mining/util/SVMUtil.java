package twitter.opinion.mining.util;

import static com.hankcs.hanlp.utility.Predefine.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.util.ResourceUtils;

import com.hankcs.hanlp.classification.classifiers.IClassifier;

import twitter.opinion.mining.exception.StorageException;
import twitter.opinion.mining.svm.classification.LinearSVMClassifier;
import twitter.opinion.mining.svm.classification.LinearSVMModel;



public class SVMUtil {



	public static String predictTheCategory(IClassifier classifier, String text)
	{
		return classifier.classify(text);
	}

	public static LinearSVMModel trainAndLoadModel(String datasetPath) throws IOException
	{
		File corpusFolder = ResourceUtils.getFile(datasetPath);		
		if (!corpusFolder.exists() || !corpusFolder.isDirectory())
		{
			throw new StorageException("Invalid Directory Path");
		}		
		IClassifier classifier = new LinearSVMClassifier();  
		classifier.train(corpusFolder.getAbsolutePath());                     
		LinearSVMModel model = (LinearSVMModel) classifier.getModel();
		return model;
	}






}
