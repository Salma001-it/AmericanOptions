package it.univr.americanoptionswithbinomialmodel;

import java.util.Arrays;

import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class AmericanOptionData {
	
	private char[][] exerciseOrWait; 
	private double[][] optionValues; 
	private double[][] exerciseValues; 
	private double[][] waitValues; 
	private TimeDiscretization exerciseTimes;
	
	/**
	 * It constructs a container for the informations about American options
	 * 
	 * @param exerciseOrWait: matrix of characters indicating at each node if it is in expectation better
	 * 						  to wait (continue) or exercise the option
	 * @param optionValues:   matrix of doubles indicating at each node the value of the option
	 * @param exerciseValues: matrix of doubles indicating at each node the amount one gets exercising of the option
	 * @param waitValues: :   matrix of doubles indicating at each node the expected value of the option at future times
	 * @param exerciseTimes:  the time step of the time discretization
	 * @param numberOfTimeSteps: the number of times
	 */
	public AmericanOptionData(double[][] optionValues,
			double[][] exerciseValues, double[][] waitValues, char[][] exerciseOrWait, double timeStep, int numberOfTimeSteps) {
		this.optionValues = optionValues;
		this.exerciseValues = exerciseValues;
		this.waitValues = waitValues;
		this.exerciseOrWait = exerciseOrWait;
		exerciseTimes = new TimeDiscretizationFromArray(0.0, numberOfTimeSteps, timeStep);
	}
	
	
	
	/**
	 * 
	 * @param timeIndex, the time index at which we want to get the information
	 * @return an array of doubles representing at each node (starting from the node where the value of the underlying
	 * 		   is higher) the value of the option
	 */
	public double[] getOptionValuesAtGivenTimeIndex(int timeIndex){
		return Arrays.copyOfRange(optionValues[timeIndex], 0, timeIndex + 1);
	}
	
	
	/**
	 * 
	 * @param timeIndex, the time index at which we want to get the information
	 * @return an array of doubles representing at each node (starting from the node where the value of the underlying
	 * 		   is higher) the amount of money we would get if we exercise the option
	 */
	public double[] getExerciseValuesAtGivenTimeIndex(int timeIndex){
		return Arrays.copyOfRange(exerciseValues[timeIndex], 0, timeIndex + 1);
	}
	
	/**
	 * 
	 * @param timeIndex, the time index at which we want to get the information
	 * @return an array of doubles representing at each node (starting from the node where the values of the underlying
	 * 		   is higher) the discounted expectation of what we would get if we wait and do not exercise the option
	 */
	public double[] getWaitValuesAtGivenTimeIndex(int timeIndex){
		return Arrays.copyOfRange(waitValues[timeIndex], 0, timeIndex + 1);
	}
	
	
	/**
	 * 
	 * @param timeIndex, the time index at which we want to get the information
	 * @return an array of characters with 'w' and 'e' in the position of nodes where it is in expectation better
	 * 		  to wait and exercise, respectively, from the node where the value of the underlying is higher.
	 */
	public char[] getExerciseOrWaitAtGivenTimeIndex(int timeIndex){
		return Arrays.copyOfRange(exerciseOrWait[timeIndex], 0, timeIndex + 1);
	}	
	
	
	/**
	 * @param time, the time at which we want to get the information
	 * @return an array of doubles representing at each node (starting from the node where the value of the underlying
	 * 		   is higher) the value of the option
	 */
	public double[] getOptionValuesAtGivenTime(double time){
		int timeIndex = exerciseTimes.getTimeIndex(time);
		return getOptionValuesAtGivenTimeIndex(timeIndex);
	}
	
	/**
	 * @param time, the time at which we want to get the information
	 * @return an array of doubles representing at each node (starting from the node where the values of the underlying
	 * 		   is higher) the amount of money we would get if we exercise the option
	 */
	public double[] getExerciseValuesAtGivenTime(double time){
		int timeIndex = exerciseTimes.getTimeIndex(time);
		return getExerciseValuesAtGivenTimeIndex(timeIndex);
	}
	
	/**
	 * @param time, the time at which we want to get the information
	 * @return an array of doubles representing at each node (starting from the node where the values of the underlying
	 * 		   is higher) the discounted expectation of what we would get if we wait and do not exercise the option
	 */
	public double[] getWaitValuesAtGivenTime(double time){
		int timeIndex = exerciseTimes.getTimeIndex(time);
		return getWaitValuesAtGivenTimeIndex(timeIndex);
	}
	
	
	/**
	 * 
	 * @param time, the time at which we want to get the information
	 * @return an array of characters with 'w' and 'e' in the position of nodes where it is in expectation better to wait
	 * 		  and exercise, respectively, from the node where the value of the underlying is higher.
	 */
	public char[] getExerciseOrWaitAtGivenTime(double time){
		int timeIndex = exerciseTimes.getTimeIndex(time);
		return getExerciseOrWaitAtGivenTimeIndex(timeIndex);
	}
}
