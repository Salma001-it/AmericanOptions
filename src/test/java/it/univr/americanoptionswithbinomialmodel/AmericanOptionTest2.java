package it.univr.americanoptionswithbinomialmodel;


import java.util.function.DoubleUnaryOperator;

import it.univr.trees.approximatingmodels.CoxRossRubinsteinModel;
import it.univr.trees.approximatingmodels.JarrowRuddModel;
import it.univr.trees.approximatingmodels.LeisenReimerModel;

//Il seguente codice serve per la verifica del primo istante temporale tk in cui in almeno un nodo convenga esercitare l'opzione;
//Inoltre viene implementato un test che verifichi, per ogni modello, l'istante in cui conviene esercitare l'opzione,

public class AmericanOptionTest2 {

	public static void main(String[] args) {

		//model parameters
		double spotPrice = 1;
		double riskFreeRate = 0.02;
		double volatility = 0.7;

		//option parameters
		double strike = 1;
		double maturity = 3.5;
		
		//Opzione Put
		DoubleUnaryOperator payoffFunction = (x) -> Math.max(strike - x, 0);

		AmericanOption ourOption = new AmericanOption(maturity, payoffFunction);
		
		CoxRossRubinsteinModel ourModelForFunction = new CoxRossRubinsteinModel(spotPrice, riskFreeRate, volatility, maturity, 300);

		AmericanOptionData ourOptionData = ourOption.getOptionData(ourModelForFunction);
		int exerciseTimeIndex = 50;
		
		char [] BestStrategy=ourOptionData.getExerciseOrWaitAtGivenTimeIndex(exerciseTimeIndex);

			int i=0;

			//continua ad aumentare i fino a quando la strategia non è diversa da W
			while(BestStrategy[i]=='W') {
				i++;
			}
			System.out.print("Il primo istante temporale tk in cui conviene esercitare l'opzione è:");
			System.out.println();
			System.out.print(i);
		
		//Mentre, per l'opzione call, si può notare come, modificando la funzione, ovvero imponendo x-strike, il codice dia errore.

		//Relativamente all'ultimo punto, questa è l'opzione inventata
		//Rimane un'opzione put
		DoubleUnaryOperator payoffFunction_Inventata = (x) -> Math.max(strike - Math.pow(x,3), 0);
		//La implementiamo per i diversi modelli
		CoxRossRubinsteinModel ourModelForFunctionCoxRossRubinsteinModel = new CoxRossRubinsteinModel(spotPrice, riskFreeRate, volatility, maturity, 300);
		JarrowRuddModel ourModelForFunctionJarrowRuddModel = new JarrowRuddModel(spotPrice, riskFreeRate, volatility, maturity, 300);
		LeisenReimerModel ourModelForFunctionLeisenReimerModel = new LeisenReimerModel(spotPrice, riskFreeRate, volatility, maturity, 300,strike);

		AmericanOption Option = new AmericanOption(maturity, payoffFunction_Inventata);
		
		AmericanOptionData OptionDataCoxRossRubinsteinModel = Option.getOptionData(ourModelForFunctionCoxRossRubinsteinModel);
		AmericanOptionData OptionDataJarrowRuddModel = Option.getOptionData(ourModelForFunctionJarrowRuddModel);
		AmericanOptionData OptionDataLeisenReimerModell = Option.getOptionData(ourModelForFunctionLeisenReimerModel);

		
		char [] BestStrategyInventedCoxRossRubinsteinModel=OptionDataCoxRossRubinsteinModel.getExerciseOrWaitAtGivenTimeIndex(exerciseTimeIndex);
		char [] BestStrategyInventedJarrowRuddModel=OptionDataJarrowRuddModel.getExerciseOrWaitAtGivenTimeIndex(exerciseTimeIndex);
		char [] BestStrategyInventedLeisenReimerModel=OptionDataLeisenReimerModell.getExerciseOrWaitAtGivenTimeIndex(exerciseTimeIndex);

		int c=0;
		int a=0;
		int b=0;

		while(BestStrategyInventedCoxRossRubinsteinModel[a]=='W') {
			a++;
		}
		while(BestStrategyInventedJarrowRuddModel[b]=='W') {
			b++;
		}
		while(BestStrategyInventedLeisenReimerModel[c]=='W') {
			c++;
		}

				System.out.println();
				System.out.print("Il primo istante temporale tk in cui conviene esercitare l'opzione inventata per CoxRossRubinsteinModel è: " +a);
				System.out.println();
				System.out.print("Il primo istante temporale tk in cui conviene esercitare l'opzione inventata per JarrowRuddModel è: "+b);
				System.out.println();
				System.out.print("Il primo istante temporale tk in cui conviene esercitare l'opzione inventata per LeisenReimerModel è: "+c);
				System.out.println();

		//Si può anche qui verficare, che per una call dia errore

		

	}

}
