
package twitter.opinion.mining.svm.classification;

import com.hankcs.hanlp.classification.features.IFeatureWeighter;
import com.hankcs.hanlp.classification.models.AbstractModel;

import de.bwaldvogel.liblinear.Model;


public class LinearSVMModel extends AbstractModel
{
    /**
     *Number of training samples
     */
    public int n = 0;
    /**
     * Number of categories
     */
    public int c = 0;
    /**
     * Number of features
     */
    public int d = 0;
    /**
     * Feature weight calculation tool
     */
    public IFeatureWeighter featureWeighter;
    /**
     *SVM classification model
     */
    public Model svmModel;
}