
package evolalgo;

import java.text.DecimalFormat;

/**
 * Implementation of individual.
 * @author Odd
 */
public class IndividualImpl implements Individual{
    
    private Object genotype;
    private Phenotype phenotype;
    private int age;
    private double fitness;
    
    public IndividualImpl(Object genotype){
        this.genotype = genotype;
        this.age = 0;
        this.fitness = -1.0;
        this.phenotype = null;
    }

    @Override
    public void setPhenotype(Phenotype phenotype) {
        this.phenotype = phenotype;
    }

    @Override
    public Phenotype phenotype(){
        return phenotype;
    }

    @Override
    public Object getGenes() {
        return genotype;
    }

    @Override
    public void growOlder() {
        age++;
    }

    @Override
    public int age() {
        return age;
    }
    
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        return "Individual: Fitness "+ df.format(fitness) +"\nPhenotype:" + 
                phenotype.toString() + "\nGenome: " + genotype.toString();
    }

    @Override
    public void setFitness(double fitness) throws Exception{
        if(fitness < 0.0 || fitness > 1.0) throw new Exception("Error setting "
                + "fitness, value is out of range! Requires [0.0-1.0], got " + 
                fitness + ")");
        this.fitness = fitness;
    }

    @Override
    public double fitness() throws Exception {
        if(fitness >= 0.0 && fitness <= 1.0){
            return fitness;
        }else{
            throw new Exception("Fitness-error! Value is " + fitness +"");
        }
    }
}
