package twitter.opinion.mining;

import static com.hankcs.hanlp.utility.Predefine.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

import com.hankcs.hanlp.classification.classifiers.IClassifier;

import twitter.opinion.mining.svm.classification.LinearSVMClassifier;
import twitter.opinion.mining.svm.classification.LinearSVMModel;
import twitter.opinion.mining.util.SVMUtil;

class SvmTest {

	
	public static final String CORPUS_FOLDER = "/home/user/git/twitteropinionmining/src/main/resources/data/textclassification";
	 public static final String MODEL_PATH = "/home/user/git/twitteropinionmining/src/main/resources/data/svm-classification-model.ser";
	
	   
	@Test
	void test() throws IOException {
		IClassifier classifier = new LinearSVMClassifier(trainOrLoadModel());
		System.out.println(SVMUtil.predictTheCategory(classifier, "drug"));
		System.out.println(SVMUtil.predictTheCategory(classifier, "krishnanunni"));
		
		
	}

	  
	    private static LinearSVMModel trainOrLoadModel() throws IOException
	    {
	        LinearSVMModel model = (LinearSVMModel) readObjectFrom(MODEL_PATH);
	        if (model != null) return model;

	        File corpusFolder = new File(CORPUS_FOLDER);
	        if (!corpusFolder.exists() || !corpusFolder.isDirectory())
	        {
	            	        System.exit(1);
	        }

	        IClassifier classifier = new LinearSVMClassifier();  //Create a classifier. For more advanced functions, please refer to the interface definition of IClassifier
	        classifier.train(CORPUS_FOLDER);                     //The trained model supports persistence, so you don't need to train next time
	        model = (LinearSVMModel) classifier.getModel();
	        saveObjectTo(model, MODEL_PATH);
	        return model;
	    }

	 
	    public static boolean saveObjectTo(Object o, String path)
	    {
	        try
	        {
	            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
	            oos.writeObject(o);
	            oos.close();
	        }
	        catch (IOException e)
	        {
	            logger.warning("Saving object" + o + "To" + path + "An exception occurred" + e);
	            return false;
	        }

	        return true;
	    }

	
	    public static Object readObjectFrom(String path)
	    {
	        ObjectInputStream ois = null;
	        try
	        {
	            ois = new ObjectInputStream(new FileInputStream(path));
	            Object o = ois.readObject();
	            ois.close();
	            return o;
	        }
	        catch (Exception e)
	        {
	            logger.warning("In from" + path + "An exception occurred while reading the object" + e);
	        }

	        return null;
	    }
}
