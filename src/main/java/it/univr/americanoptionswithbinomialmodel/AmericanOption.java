package it.univr.americanoptionswithbinomialmodel;

import java.util.function.DoubleUnaryOperator;

import it.univr.trees.approximatingmodels.ApproximatingBinomialModel;
import it.univr.usefulmethodsarrays.UsefulMethodsForArrays;

public class AmericanOption {
	
	private double maturity;
	private DoubleUnaryOperator payoffFunction;
	

	/**
	 * It constructs an object which represents the implementation of the American option.
	 * @param maturity, the maturity of the option
	 * @param payoffFunction, the function which identifies the payoff. The payoff is f(S_T) for payoffFunction
	 * 			f and underlying value S_T at maturity. The payoffFunction is represented by a DoubleUnaryOperator
	 */
	public AmericanOption(double maturity, DoubleUnaryOperator payoffFunction) {
		this.maturity = maturity;
		this.payoffFunction = payoffFunction;
	}

	/**
	 * It returns the discounted value of the option written on the continuous time model approximated by
	 * the object of type ApproximatingBinomialModel given in input. This is done by going backward.
	 * At any node at time t_{i}, the value of the option is computed as the maximum between the payoff
	 * function evaluated at that node and the discounted conditional expectation at that node of the values
	 * of the option at time t_{i+1}.
	 * 
	 * 
	 * @param approximatingBinomialModel, the underlying
	 * @return the value of the option written on the underlying
	 */
	public double getValue(ApproximatingBinomialModel approximatingBinomialModel) {

		//Qui la parte che ho aggiunto
		int numberOfTimeSteps=(int) Math.round(maturity/approximatingBinomialModel.getTimeStep());

		double[] optionValues = approximatingBinomialModel.getTransformedValuesAtGivenTimeIndex(numberOfTimeSteps, payoffFunction);
		for(int timeIndex=numberOfTimeSteps-1;timeIndex>=0;timeIndex--) {
			double[]conditionalExpectation=approximatingBinomialModel.getConditionalExpectation(optionValues, timeIndex);
			optionValues=UsefulMethodsForArrays.getMaxValuesBetweenTwoArrays(conditionalExpectation,approximatingBinomialModel.getTransformedValuesAtGivenTimeIndex(timeIndex, payoffFunction));
		}

		/*
		 * IL VOSTRO CODICE VA QUI. DOVETE CALCOLARE IL VALORE DELLA OPZIONE AMERICANA COL METODO BACKWARD, PRENDENDO SPUNTO
		 * PER ESEMPIO DA 	QUANTO VISTO NEL METODO getValue IN EuropeanNonPathDependentOption.
		 */
		  
		return optionValues[0];
	}

	/**
	 * It returns an object which is a container for four matrices containing respectively, for each time index, the
	 * values of the option, what one would get if exercising at the current time, the expected values of the option
	 * at future times and characters indicating if it is in expectation better to wait or exercise. 
	 * 
	 * @param approximatingBinomialModel, the underlying
	 * @return AmericanOptionData, the container for the informations above.
	 */
	public AmericanOptionData getOptionData(ApproximatingBinomialModel approximatingBinomialModel) {
		
		/*
		 * IL VOSTRO CODICE VA QUI: DOVETE RIEMPIRE LE QUATTRO MATRICI QUI SOPRA CON I VALORI APPROPRIATI. 
		 * LA RIGA k RAPPRESENTA I VALORI AL TEMPO t_k.
		 */
		
		double timeStep = approximatingBinomialModel.getTimeStep();
		int numberOfTimeSteps = (int) Math.round(maturity/timeStep);//numero di sottointervalli
		//I seguenti vettori conterranno, per ogni riga e colonna, numberOfTimeSteps + 1 elementi.
		double[][] americanOptionValues = new double[numberOfTimeSteps + 1][numberOfTimeSteps + 1];
		double[][] exerciseValues = new double[numberOfTimeSteps + 1][numberOfTimeSteps + 1];
		double[][] waitValues = new double[numberOfTimeSteps + 1][numberOfTimeSteps + 1];
		char[][] exerciseOrWait = new char[numberOfTimeSteps + 1][numberOfTimeSteps + 1];
		double[] probabilities=approximatingBinomialModel.getUpAndDownProbabilities();

    	double[]transformedValueMaturity=approximatingBinomialModel.getTransformedValuesAtGivenTime(maturity, payoffFunction);
    	
    	for (int i = numberOfTimeSteps; i >= 0; i--) {
    		int numberOfDowns=numberOfTimeSteps-i;
    		exerciseValues[numberOfTimeSteps][numberOfDowns] = transformedValueMaturity[numberOfDowns]; // abbiamo i valori a cui è già stata applicata la funzione
    		americanOptionValues[numberOfTimeSteps][numberOfDowns] = exerciseValues[numberOfTimeSteps][numberOfDowns]; //i valori finali sono uguali 
    	}

    	//parte da k-1, perché l'ultima colonna di valori è occupata dai valori a maturità
	    for (int k = numberOfTimeSteps - 1; k >= 0; k--) {
	        for (int i = k; i>=0; i--) { 
	        	int numberOfDowns=k-i;
        	//valori di partenza finali
	        	double[]transformedValue=approximatingBinomialModel.getTransformedValuesAtGivenTimeIndex(k, payoffFunction);
	      	
	            exerciseValues[k][numberOfDowns] = transformedValue[numberOfDowns]; //Valore dell'opzione. Ovvero il valore della funzione applicata al sottostante
	            waitValues[k][numberOfDowns] =(probabilities[0] * americanOptionValues[k + 1][numberOfDowns] + probabilities[1] * americanOptionValues[k + 1][numberOfDowns + 1]) *  Math.exp(-approximatingBinomialModel.getRiskFreeRate() * timeStep); 
	            americanOptionValues[k][numberOfDowns] = Math.max(exerciseValues[k][numberOfDowns], waitValues[k][numberOfDowns]); 
	            exerciseOrWait[k][numberOfDowns] = (exerciseValues[k][numberOfDowns] > waitValues[k][numberOfDowns]) ? 'E' : 'W'; 
	        } 
	    }


	
		return new AmericanOptionData (americanOptionValues,exerciseValues, waitValues, exerciseOrWait, timeStep, numberOfTimeSteps);
	}
}
