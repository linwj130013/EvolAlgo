
package evolalgo.adultselectors;

import evolalgo.Individual;
import java.util.List;

/**
 * As mentioned in lecture notes, we use simple selection and pick parents only
 * by best fitness. This interface is implemented by all selection methods
 * @author Odd
 */
public interface IAdultSelection {
    
    /**
     * Return a population list with only adults. What happened to the previous
     * adults and/or children depends on the class that implements this interface.
     * @param population
     * @return A list populated with adults
     * @throws Exception 
     */
    public List<Individual> getAdults(List<Individual> population) 
            throws Exception;
    public int getNumberOfChildren(List<Individual> population);
}
