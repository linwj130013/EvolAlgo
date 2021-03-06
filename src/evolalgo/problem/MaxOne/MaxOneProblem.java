package evolalgo.problem.MaxOne;

import evolalgo.Phenotype;
import evolalgo.Individual;
import evolalgo.IndividualImpl;
import evolalgo.problem.IProblem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Development method for bit strings.
 * @author Odd
 */
public class MaxOneProblem implements IProblem{
    
    int[] targetArray;
    int length;
    
    public MaxOneProblem(String targetString, int length){
        this.length = length;
        if(targetString.equals("")){
            targetArray = null;
        }else{
            targetArray = new int[targetString.length()];
            for (int i = 0; i < targetString.length(); i++){
                targetArray[i] = (int) targetString.charAt(i);
            }
        }
    }

    @Override
    public void developPheno(Individual individual) throws Exception{
        if (individual.phenotype() != null) return;
        String geno = individual.getGenes().toString();
        String genostring = geno.toString();
        MaxOnePheno phenotype = new MaxOnePheno(genostring.length());
        
        for(int i = 0; i < phenotype.pheno.length; i++){
            phenotype.pheno[i] = (int) genostring.charAt(i);
        }
        individual.setPhenotype(phenotype);
    }
    
    @Override
    public void calculateFitness(List<Individual> population) throws Exception{
        for(int i = 0; i < population.size(); i++){
            try{
                population.get(i).fitness();
            }catch (Exception e){
                int fitnessFactor = 0;
                MaxOnePheno pheno = (MaxOnePheno) population.get(i).phenotype();
                int[] phenoArray = pheno.pheno;
                //Blow up targetString and compare each bit
                if(targetArray != null && targetArray.length != phenoArray.length){
                    throw new Exception("Phenotype and bit string not of equal length!");
                }

                for(int j = 0; j < phenoArray.length; j++){
                    if(targetArray != null && phenoArray[j] == targetArray[j]){
                        fitnessFactor++;
                    }else if(phenoArray[j] == '1') fitnessFactor++;
                }population.get(i).setFitness((double) fitnessFactor/ (double) phenoArray.length);
            }
        }
    }
    
    @Override
    public List<Individual> createPopulation(int individuals) {
        List<Object> genotypes = new ArrayList();
        for(int i = 0; i < individuals; i++){
            String bitstring = "";
            Random rand = new Random();
            for(int j = 0; j < length; j++){
                if(rand.nextBoolean() == true) bitstring += "1";
                else bitstring += "0";
            }
            genotypes.add(bitstring);
        }
        
        //Setting up list of individuals
        List<Individual> population = new ArrayList();
        
        for(Object o: genotypes){
            population.add(new IndividualImpl(o));
        }
        return population;
    }
}
class MaxOnePheno implements Phenotype{
    public int[] pheno;
    
    public MaxOnePheno(int size){
        pheno = new int[size];
    }

    @Override
    public String toString() {
        return "";
    }
}