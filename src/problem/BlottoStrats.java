
package problem;

import evolalgo.IIndividual;
import evolalgo.IPhenotype;
import evolalgo.IProblem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Development method for the Blotto Strategies
 * @author Odd
 */
public class BlottoStrats implements IProblem{
    private int numBattles;
    private double redeploymentRate;
    private double defectionRate;
    /**
     * 
     * @param B Number of battles
     * @param R The re-deployment factor when fights are won
     * @param L Defection factor when fights are lost
     */
    public BlottoStrats(int B, double R, double L){
        numBattles = B;
        redeploymentRate = R;
        defectionRate = L;
    }

    /**
     * Creates phenotype
     * @param geno the genotype bitstring, where segments of 4 bits account for
     * men used in each battle
     * @return the phenotype
     * @throws Exception 
     */
    @Override
    public void developPheno(IIndividual individual) throws Exception{
        String geno = individual.getGenes().toString();
        if(geno instanceof String){
            int segments = geno.toString().length() / 4;
            double[] phenotype = new double[segments];
            double total = 0.0;
            for(int i = 0; i < segments; i++){
                int location = i * 4;
                String binary = geno.toString().substring(location, location + 4);
                phenotype[i] = Integer.parseInt(binary, 2);
                total += phenotype[i];
            }
            double weight = 1 / total;
            for(int i = 0; i < segments; i++){
                phenotype[i] = phenotype[i] * weight;
            }
            IPhenotype pheno = new BlottoPheno(phenotype);
            individual.setPhenotype(pheno);
        }else{
            throw new Exception("Error in genotype");
        }
    }

    @Override
    public void calculateFitness(List<IIndividual> population) 
            throws Exception {
        //Have every individual fight every other individual and increase fitness as they score
        for (IIndividual fighter: population){
            BlottoPheno fighterPheno = (BlottoPheno) fighter.phenotype();
            try{
                fighter.fitness();
            }catch (Exception e){
                fighter.setFitness(0.0);
            }
            for(IIndividual opponent: population){
                if(!fighter.equals(opponent) && !fighterPheno.fought.contains(opponent)){
                    BlottoPheno opponentPheno = (BlottoPheno) opponent.phenotype();
                    try{
                        opponent.fitness();
                    }catch (Exception e){
                        opponent.setFitness(0.0);
                    }
                    int fighterWins = 0;
                    int opponentWins = 0;
                    double[] fighterWeights = new double[numBattles];
                    System.arraycopy(fighterPheno.pheno, 0, fighterWeights, 0, numBattles);
                    double[] opponentWeights = new double[numBattles];
                    System.arraycopy(opponentPheno.pheno, 0, opponentWeights, 0, numBattles);
                    for(int battle = 0; battle < numBattles; battle++){
                        if(fighterWeights[battle] > opponentWeights[battle]){
                            fighterWins += 1;
                            double redistribution = fighterWeights[battle] - opponentWeights[battle];
                            int remainingBattles = numBattles - (battle + 1);
                            redistribution = redistribution / remainingBattles;
                            for (int i = battle + 1; i < numBattles; i++){
                                fighterWeights[i] += redeploymentRate * redistribution;
                                opponentWeights[i] *= 1 - defectionRate;
                            }
                        }else if(fighterWeights[battle] > opponentWeights[battle]){
                            opponentWins += 1;
                            double redistribution = opponentWeights[battle] - fighterWeights[battle];
                            int remainingBattles = numBattles - (battle + 1);
                            redistribution = redistribution / remainingBattles;
                            for (int i = battle + 1; i < numBattles; i++){
                                opponentWeights[i] += redeploymentRate * redistribution;
                                fighterWeights[i] *= 1 - defectionRate;
                            }
                        }
                    }
                    if (fighterWins == opponentWins){
                        fighter.setFitness(fighter.fitness() + 1);
                        opponent.setFitness(opponent.fitness() + 1);
                    }else if(fighterWins > opponentWins){
                        fighter.setFitness(fighter.fitness() + 2);
                    }else if(fighterWins < opponentWins){
                        opponent.setFitness(opponent.fitness() + 2);
                    }
                    
                    //Note that they've fought eachother
                    fighterPheno.fought.add(opponent);
                    opponentPheno.fought.add(fighter);
                }
            }
        }
        double maxFitness = numBattles * 2;
        for (IIndividual individual: population){
            individual.setFitness(individual.fitness() / maxFitness);
        }
    }
    
    @Override
    public List<Object> createPopulation(int individuals) {
        List<Object> list = new ArrayList();
        for(int i = 0; i < individuals; i++){
            String bitstring = "";
            Random rand = new Random();
            for(int j = 0; j < (numBattles * 4); j++){
                if(rand.nextBoolean() == true) bitstring += "1";
                else bitstring += "0";
            }
            list.add(bitstring);
        }
        return list;
    }
}

class BlottoPheno implements IPhenotype{
    public double[] pheno;
    public List<IIndividual> fought;
    
    public BlottoPheno(double[] pheno){
        this.pheno = pheno;
        fought = new ArrayList<IIndividual>();
    }
}