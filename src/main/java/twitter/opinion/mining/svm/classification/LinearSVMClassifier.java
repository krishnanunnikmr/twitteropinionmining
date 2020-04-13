package twitter.opinion.mining.svm.classification;

import com.hankcs.hanlp.classification.classifiers.AbstractClassifier;
import com.hankcs.hanlp.classification.corpus.Document;
import com.hankcs.hanlp.classification.corpus.IDataSet;
import com.hankcs.hanlp.classification.features.*;
import com.hankcs.hanlp.classification.models.AbstractModel;
import com.hankcs.hanlp.classification.tokenizers.ITokenizer;
import com.hankcs.hanlp.classification.utilities.MathUtility;
import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;

import de.bwaldvogel.liblinear.*;

import java.util.*;

import static com.hankcs.hanlp.classification.utilities.Predefine.logger;

public class LinearSVMClassifier extends AbstractClassifier
{
    LinearSVMModel model;

    public LinearSVMClassifier()
    {
    }

    public LinearSVMClassifier(LinearSVMModel model)
    {
        this.model = model;
    }

    public Map<String, Double> predict(String text) throws IllegalArgumentException, IllegalStateException
    {
        if (model == null)
        {
            throw new IllegalStateException("Model Object Is Null");
        }
        if (text == null)
        {
            throw new IllegalArgumentException("Preduction text is  null");
        }
        Document document = new Document(model.wordIdTrie, model.tokenizer.segment(text));

        return predict(document);
    }

    @Override
    public double[] categorize(Document document) throws IllegalArgumentException, IllegalStateException
    {
        FeatureNode[] x = buildDocumentVector(document, model.featureWeighter);
        double[] probs = new double[model.svmModel.getNrClass()];
        Linear.predictProbability(model.svmModel, x, probs);
        return probs;
    }

    @Override
    public void train(IDataSet dataSet)
    {
        if (dataSet.size() == 0) throw new IllegalArgumentException("Data Set IS Null");
        //  Select feature
        DfFeatureData featureData = selectFeatures(dataSet);
        //  Constructing weight calculation logic
        IFeatureWeighter weighter = new TfIdfFeatureWeighter(dataSet.size(), featureData.df);
        // Constructing SVM problems
        Problem problem = createLiblinearProblem(dataSet, featureData, weighter);
        //Free up memory
        BinTrie<Integer> wordIdTrie = featureData.wordIdTrie;
        featureData = null;
        ITokenizer tokenizer = dataSet.getTokenizer();
        String[] catalog = dataSet.getCatalog().toArray();
        dataSet = null;
        System.gc();
        // Solving SVM problems
        Model svmModel = solveLibLinearProblem(problem);
        // Keep useful data
        model = new LinearSVMModel();
        model.tokenizer = tokenizer;
        model.wordIdTrie = wordIdTrie;
        model.catalog = catalog;
        model.svmModel = svmModel;
        model.featureWeighter = weighter;
    }

    public AbstractModel getModel()
    {
        return model;
    }

    private Model solveLibLinearProblem(Problem problem)
    {
        de.bwaldvogel.liblinear.Parameter lparam = new Parameter(SolverType.L1R_LR,                                                              
                                                                 500.,
                                                                 0.01);
        return de.bwaldvogel.liblinear.Linear.train(problem, lparam);
    }

    private Problem createLiblinearProblem(IDataSet dataSet, BaseFeatureData baseFeatureData, IFeatureWeighter weighter)
    {
        Problem problem = new Problem();
        int n = dataSet.size();
        problem.l = n;
        problem.n = baseFeatureData.featureCategoryJointCount.length;
        problem.x = new FeatureNode[n][];
        problem.y = new double[n]; //The latest version of liblinear's y array is a floating point number
        Iterator<Document> iterator = dataSet.iterator();
        for (int i = 0; i < n; i++)
        {
            // Constructing document vectors
            Document document = iterator.next();
            problem.x[i] = buildDocumentVector(document, weighter);
            //Set the sample's y value
            problem.y[i] = document.category;
        }

        return problem;
    }

    private FeatureNode[] buildDocumentVector(Document document, IFeatureWeighter weighter)
    {
        int termCount = document.tfMap.size();  //Number of words
        FeatureNode[] x = new FeatureNode[termCount];
        Iterator<Map.Entry<Integer, int[]>> tfMapIterator = document.tfMap.entrySet().iterator();
        for (int j = 0; j < termCount; j++)
        {
            Map.Entry<Integer, int[]> tfEntry = tfMapIterator.next();
            int feature = tfEntry.getKey();
            int frequency = tfEntry.getValue()[0];
            x[j] = new FeatureNode(feature + 1,  // liblinear Require subscripts to increase from 1
                                   weighter.weight(feature, frequency));
        }
        // Normalize the vector
        double normalizer = 0;
        for (int j = 0; j < termCount; j++)
        {
            double weight = x[j].getValue();
            normalizer += weight * weight;
        }
        normalizer = Math.sqrt(normalizer);
        for (int j = 0; j < termCount; j++)
        {
            double weight = x[j].getValue();
            x[j].setValue(weight / normalizer);
        }

        return x;
    }


    protected DfFeatureData selectFeatures(IDataSet dataSet)
    {
        ChiSquareFeatureExtractor chiSquareFeatureExtractor = new ChiSquareFeatureExtractor();

        //FeatureStats-Object contains all features in the document and their statistics
        DfFeatureData featureData = new DfFeatureData(dataSet); //Executive statistics

        logger.start("Use chi-square detection to select features ...");
        //We pass these statistics into the feature selection algorithm to get the features and their scores
        Map<Integer, Double> selectedFeatures = chiSquareFeatureExtractor.chi_square(featureData);

        // Delete useless features from the training data and rebuild the feature map
        String[] wordIdArray = dataSet.getLexicon().getWordIdArray();
        int[] idMap = new int[wordIdArray.length];
        Arrays.fill(idMap, -1);
        featureData.wordIdTrie = new BinTrie<Integer>();
        featureData.df = new int[selectedFeatures.size()];
        int p = -1;
        for (Integer feature : selectedFeatures.keySet())
        {
            ++p;
            featureData.wordIdTrie.put(wordIdArray[feature], p);
            featureData.df[p] = MathUtility.sum(featureData.featureCategoryJointCount[feature]);
            idMap[feature] = p;
        }
        logger.finish(",Number of selected features:%d / %d = %.2f%%\n", selectedFeatures.size(),
                      featureData.featureCategoryJointCount.length,
                      MathUtility.percentage(selectedFeatures.size(), featureData.featureCategoryJointCount.length));
        logger.start("Reduce training data...");
        int n = dataSet.size();
        dataSet.shrink(idMap);
        logger.finish("Reduced% d samples,% d samples remaining\n", n - dataSet.size(), dataSet.size());

        return featureData;
    }
}
