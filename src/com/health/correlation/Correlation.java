package com.health.correlation;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.util.Pair;

public class Correlation {
	public static void test(){
		//PointBiserialCorrelationCoefficient------------------------------------------------
		Double[] X1 = new Double[15];
		Boolean[] Y1 = new Boolean[15];

		X1[0] = 57.4;	Y1[0] = true;
		X1[1] = 61.9;	Y1[1] = true;
		X1[2] = 25.3;	Y1[2] = false;
		X1[3] = 79.6;	Y1[3] = true;
		X1[4] = 24.3;	Y1[4] = false;
		X1[5] = 45.2;	Y1[5] = false;
		X1[6] = 43.1;	Y1[6] = false;
		X1[7] = 73.4;	Y1[7] = true;
		X1[8] = 52.5;	Y1[8] = false;
		X1[9] = 38.4;	Y1[9] = false;
		X1[10] = 66.5;	Y1[10] = true;
		X1[11] = 39.6;	Y1[11] = true;
		X1[12] = 55.4;	Y1[12] = true;
		X1[13] = 49.4;	Y1[13] = false;
		X1[14] = 72.1;	Y1[14] = true;
		
		Double Rpb = PointBiserialCorrelationCoefficient(X1, Y1);
		
		Log.i("Correlation", "Rpb = " + Rpb.toString());
		
		verificationPointBiserialCorrelationCoefficient(Rpb, X1.length);

		//PearsonCorrelationCoefficient------------------------------------------------------
		Double[] X2 = new Double[16];
		Double[] Y2 = new Double[16];
		
		X2[0] = 39.3;	Y2[0] = 15.0;
		X2[1] = 33.3;	Y2[1] = 13.0;
		X2[2] = 56.6;	Y2[2] = 20.9;
		X2[3] = 62.3;	Y2[3] = 19.0;
		X2[4] = 31.1;	Y2[4] = 13.6;
		X2[5] = 36.7;	Y2[5] = 15.0;
		X2[6] = 52.9;	Y2[6] = 17.1;
		X2[7] = 32.9;	Y2[7] = 13.5;
		X2[8] = 35.2;	Y2[8] = 14.2;
		X2[9] = 62.8;	Y2[9] = 21.3;
		X2[10] = 34.2;	Y2[10] = 13.5;
		X2[11] = 58.1;	Y2[11] = 17.0;
		X2[12] = 29.3;	Y2[12] = 13.0;
		X2[13] = 59.9;	Y2[13] = 18.2;
		X2[14] = 49.0;	Y2[14] = 19.2;
		X2[15] = 45.6;	Y2[15] = 16.5;
		
		Double pearsonCor = PearsonCorrelationCoefficient(X2, Y2);

		Log.i("Correlation", "pearsonCor = " + pearsonCor.toString());
		
		verificationPearsonCorrelationCoefficient(pearsonCor, X2.length);
		
		
		//PearsonContingencyCoefficient------------------------------------------------------
		Boolean[] X3 = new Boolean[200];
		Boolean[] Y3 = new Boolean[200];

		for(int i = 0; i < 100; i++){
			X3[i] = true;
		}
		for(int i = 100; i < 200; i++){
			X3[i] = false;
		}
		for(int i = 0; i < 59; i++){
			Y3[i] = true;
		}
		for(int i = 59; i < 100; i++){
			Y3[i] = false;
		}
		for(int i = 100; i < 136; i++){
			Y3[i] = true;
		}
		for(int i = 136; i < 200; i++){
			Y3[i] = false;
		}
		
		Double f = PearsonContingencyCoefficient(X3, Y3);
		
		Log.i("Correlation", "f = " + f.toString());
		
		verificationPearsonContingencyCoefficient(f, X3.length);
	}
	
	private static Double dispersion(Double[] X){
		return avg(getSquaredDeviation(X));
	}
	
	private static Double uncorrectedStandardDeviation(Double[] X){ //сигма
		return Math.sqrt(dispersion(X));
	}
	
	private static Double correctedStandardDeviation(Double[] X){ //s
		return Math.sqrt(sum(getSquaredDeviation(X))/(double)(X.length - 1));
	}
	
	private static Double[] getSquaredDeviation(Double[] X){
		Double avgX = avg(X);
		Double[] squaredDeviation = new Double[X.length];
		
		for(int i = 0; i < X.length; i++){
			squaredDeviation[i] = Math.pow(X[i] - avgX, 2);
		}
		
		return squaredDeviation;
	}
	
	private static Double[] getMultiplicationOfDeviation(Double[] X, Double[] Y){
		Double avgX = avg(X);
		Double avgY = avg(Y);
		
		Double[] multiplicationOfDeviation = new Double[X.length];
		
		for(int i = 0; i < X.length; i++){
			multiplicationOfDeviation[i] = (X[i] - avgX)*(Y[i] - avgY);
		}
		
		return multiplicationOfDeviation;
	}
	
	private static Double correlationCoefficient(Double[] X, Double[] Y){
		return covariationCoefficient(X, Y) /  (uncorrectedStandardDeviation(X) * uncorrectedStandardDeviation(Y));
	}
	
	private static Double covariationCoefficient(Double[] X, Double[] Y){
		return avg(getMultiplicationOfDeviation(X, Y));
	}
	
	private static Double PearsonCorrelationCoefficient(Double[] X, Double[] Y){
		Double r = sum(getMultiplicationOfDeviation(X, Y))/Math.sqrt(sum(getSquaredDeviation(X))*sum(getSquaredDeviation(Y)));
		if(X.length < 100){
			r = r * (1 + (1 - Math.pow(r, 2))/(2 * (X.length - 3)));
		}
		return r;
	}
	
	private static void verificationPearsonCorrelationCoefficient(Double r, Integer n){
		if(n >= 100){
    		Double t = (Math.abs(r) / (Math.sqrt(1 - r * r))) * Math.sqrt(n - 2);
    		Log.i("Correlation", "t = " + t.toString());
    		
    		List<Pair<Double, Double>> q = new ArrayList<Pair<Double, Double>>();
    		//                             0.05     0.01
    		q.add(new Pair<Double, Double>(12.7062,63.657));
    		q.add(new Pair<Double, Double>(4.3027,9.9248 ));
    		q.add(new Pair<Double, Double>(3.1824,5.8409 ));
    		q.add(new Pair<Double, Double>(2.7764,4.6041 ));
    		q.add(new Pair<Double, Double>(2.5706,4.0321 ));
    		q.add(new Pair<Double, Double>(2.4469,3.7074 ));
    		q.add(new Pair<Double, Double>(2.3646,3.4995 ));
    		q.add(new Pair<Double, Double>(2.3060,3.3554 ));
    		q.add(new Pair<Double, Double>(2.2622,3.2498 ));
    		q.add(new Pair<Double, Double>(2.2281,3.1693 ));
    		q.add(new Pair<Double, Double>(2.2010,3.1058 ));
    		q.add(new Pair<Double, Double>(2.1788,3.0545 ));
    		q.add(new Pair<Double, Double>(2.1604,3.0123 ));
    		q.add(new Pair<Double, Double>(2.1448,2.9768 ));
    		q.add(new Pair<Double, Double>(2.1314,2.9467 ));
    		q.add(new Pair<Double, Double>(2.1199,2.9208 ));
    		q.add(new Pair<Double, Double>(2.1098,2.8982 ));
    		q.add(new Pair<Double, Double>(2.1009,2.8784 ));
    		q.add(new Pair<Double, Double>(2.0930,2.8609 ));
    		q.add(new Pair<Double, Double>(2.0860,2.8453 ));
    		q.add(new Pair<Double, Double>(2.0796,2.8314 ));
    		q.add(new Pair<Double, Double>(2.0739,2.8188 ));
    		q.add(new Pair<Double, Double>(2.0687,2.8073 ));
    		q.add(new Pair<Double, Double>(2.0639,2.7969 ));
    		q.add(new Pair<Double, Double>(2.0595,2.7874 ));
    		q.add(new Pair<Double, Double>(2.0555,2.7787 ));
    		q.add(new Pair<Double, Double>(2.0518,2.7707 ));
    		q.add(new Pair<Double, Double>(2.0484,2.7633 ));
    		q.add(new Pair<Double, Double>(2.0452,2.7564 ));
    		q.add(new Pair<Double, Double>(2.0423,2.75	 ));
    		q.add(new Pair<Double, Double>(2.0395,2.744  ));
    		q.add(new Pair<Double, Double>(2.0369,2.7385 ));
    		q.add(new Pair<Double, Double>(2.0345,2.7333 ));
    		q.add(new Pair<Double, Double>(2.0322,2.7284 ));
    		q.add(new Pair<Double, Double>(2.0301,2.7238 ));
    		q.add(new Pair<Double, Double>(2.0281,2.7195 ));
    		q.add(new Pair<Double, Double>(2.0262,2.7154 ));
    		q.add(new Pair<Double, Double>(2.0244,2.7116 ));
    		q.add(new Pair<Double, Double>(2.0227,2.7079 ));
    		q.add(new Pair<Double, Double>(2.0211,2.7045 ));
    		q.add(new Pair<Double, Double>(2.0195,2.7012 ));
    		q.add(new Pair<Double, Double>(2.0181,2.6981 ));
    		q.add(new Pair<Double, Double>(2.0167,2.6951 ));
    		q.add(new Pair<Double, Double>(2.0154,2.6923 ));
    		q.add(new Pair<Double, Double>(2.0141,2.6896 ));
    		q.add(new Pair<Double, Double>(2.0129,2.687  ));
    		q.add(new Pair<Double, Double>(2.0117,2.6846 ));
    		q.add(new Pair<Double, Double>(2.0106,2.6822 ));
    		q.add(new Pair<Double, Double>(2.0096,2.68	 ));
    		q.add(new Pair<Double, Double>(2.0086,2.6778 ));
    		q.add(new Pair<Double, Double>(1.9840,2.6259 ));
    		q.add(new Pair<Double, Double>(1.9623,2.5808 ));
    		q.add(new Pair<Double, Double>(1.9600,2.5758 ));
    		
    		if(t <= q.get(n - 2 - 1).first){
    			Log.i("Correlation", "коэффициент незначим");
    		}
    
    		if(t > q.get(n - 2 - 1).second){
    			Log.i("Correlation", "коэффициент значим");
    			Double r1 = r - q.get(n - 2 - 1).second * ((1 - r * r)/Math.sqrt(n - 1));
    			Double r2 = r + q.get(n - 2 - 1).second * ((1 - r * r)/Math.sqrt(n - 1));
    			Log.i("Correlation", "доверительный интервал (99.5%) " + r1.toString() + " - " + r2.toString());
    		}
		} else {
			Double u = Math.log((1 + r) / (1 - r)) / 2;
			Double uan005 = 1.960 / Math.sqrt(n - 3);
			Double uan001 = 2.576 / Math.sqrt(n - 3);
    		Log.i("Correlation", "u = " + u.toString());
    		Log.i("Correlation", "uan005 = " + uan005.toString());
    		Log.i("Correlation", "uan001 = " + uan001.toString());
			
    		if(u <= uan005){
    			Log.i("Correlation", "коэффициент незначим");
    		}

    		if(u > uan001){
    			Log.i("Correlation", "коэффициент значим");
    			Double u1 = u - uan005;
    			Double u2 = u + uan005;
    			Double r1 = (Math.exp(2 * u1) - 1) / (Math.exp(2 * u1) + 1);
    			Double r2 = (Math.exp(2 * u2) - 1) / (Math.exp(2 * u2) + 1);
        		Log.i("Correlation", "u1 = " + u1.toString());
        		Log.i("Correlation", "u2 = " + u2.toString());
    			Log.i("Correlation", "доверительный интервал (99.5%) " + r1.toString() + " - " + r2.toString());
    		}
		}
	}
	
	public static Double PointBiserialCorrelationCoefficient(Double[] X, Boolean[] Y){
		Double[] X0;
		Double[] X1;
		Integer n0 = 0;
		Integer n1 = 0;
		Integer n = X.length;
		
		for(int i = 0; i < X.length; i++){
			if(Y[i]){
				n1++;
			} else {
				n0++;
			}
		}

		X0 = new Double[n0];
		X1 = new Double[n1];

		for(int i = 0, j0 = 0, j1 = 0; i < X.length; i++){
			if(Y[i]){
				X1[j1++] = X[i];
			} else {
				X0[j0++] = X[i];
			}
		}
		
		return ((avg(X1) - avg(X0)) / correctedStandardDeviation(X)) * Math.sqrt(((double)n1 * (double)n0)/((double)n * ((double)n - 1)));
	}
	
	public static String verificationPointBiserialCorrelationCoefficient(Double r, Integer n){
		String result = "";
		Double t = (r / (Math.sqrt(1 - r * r))) * Math.sqrt(n - 2);
		Log.i("Correlation", "t = " + t.toString());
		
		List<Pair<Double, Double>> q = new ArrayList<Pair<Double, Double>>();
		//                             0.05     0.01
		q.add(new Pair<Double, Double>(12.7062,63.657));
		q.add(new Pair<Double, Double>(4.3027,9.9248 ));
		q.add(new Pair<Double, Double>(3.1824,5.8409 ));
		q.add(new Pair<Double, Double>(2.7764,4.6041 ));
		q.add(new Pair<Double, Double>(2.5706,4.0321 ));
		q.add(new Pair<Double, Double>(2.4469,3.7074 ));
		q.add(new Pair<Double, Double>(2.3646,3.4995 ));
		q.add(new Pair<Double, Double>(2.3060,3.3554 ));
		q.add(new Pair<Double, Double>(2.2622,3.2498 ));
		q.add(new Pair<Double, Double>(2.2281,3.1693 ));
		q.add(new Pair<Double, Double>(2.2010,3.1058 ));
		q.add(new Pair<Double, Double>(2.1788,3.0545 ));
		q.add(new Pair<Double, Double>(2.1604,3.0123 ));
		q.add(new Pair<Double, Double>(2.1448,2.9768 ));
		q.add(new Pair<Double, Double>(2.1314,2.9467 ));
		q.add(new Pair<Double, Double>(2.1199,2.9208 ));
		q.add(new Pair<Double, Double>(2.1098,2.8982 ));
		q.add(new Pair<Double, Double>(2.1009,2.8784 ));
		q.add(new Pair<Double, Double>(2.0930,2.8609 ));
		q.add(new Pair<Double, Double>(2.0860,2.8453 ));
		q.add(new Pair<Double, Double>(2.0796,2.8314 ));
		q.add(new Pair<Double, Double>(2.0739,2.8188 ));
		q.add(new Pair<Double, Double>(2.0687,2.8073 ));
		q.add(new Pair<Double, Double>(2.0639,2.7969 ));
		q.add(new Pair<Double, Double>(2.0595,2.7874 ));
		q.add(new Pair<Double, Double>(2.0555,2.7787 ));
		q.add(new Pair<Double, Double>(2.0518,2.7707 ));
		q.add(new Pair<Double, Double>(2.0484,2.7633 ));
		q.add(new Pair<Double, Double>(2.0452,2.7564 ));
		q.add(new Pair<Double, Double>(2.0423,2.75	 ));
		q.add(new Pair<Double, Double>(2.0395,2.744  ));
		q.add(new Pair<Double, Double>(2.0369,2.7385 ));
		q.add(new Pair<Double, Double>(2.0345,2.7333 ));
		q.add(new Pair<Double, Double>(2.0322,2.7284 ));
		q.add(new Pair<Double, Double>(2.0301,2.7238 ));
		q.add(new Pair<Double, Double>(2.0281,2.7195 ));
		q.add(new Pair<Double, Double>(2.0262,2.7154 ));
		q.add(new Pair<Double, Double>(2.0244,2.7116 ));
		q.add(new Pair<Double, Double>(2.0227,2.7079 ));
		q.add(new Pair<Double, Double>(2.0211,2.7045 ));
		q.add(new Pair<Double, Double>(2.0195,2.7012 ));
		q.add(new Pair<Double, Double>(2.0181,2.6981 ));
		q.add(new Pair<Double, Double>(2.0167,2.6951 ));
		q.add(new Pair<Double, Double>(2.0154,2.6923 ));
		q.add(new Pair<Double, Double>(2.0141,2.6896 ));
		q.add(new Pair<Double, Double>(2.0129,2.687  ));
		q.add(new Pair<Double, Double>(2.0117,2.6846 ));
		q.add(new Pair<Double, Double>(2.0106,2.6822 ));
		q.add(new Pair<Double, Double>(2.0096,2.68	 ));
		q.add(new Pair<Double, Double>(2.0086,2.6778 ));
		q.add(new Pair<Double, Double>(1.9840,2.6259 ));
		q.add(new Pair<Double, Double>(1.9623,2.5808 ));
		q.add(new Pair<Double, Double>(1.9600,2.5758 ));
		
		if(t <= q.get(n > q.size() ? q.size() - 1 : n - 2 - 1).second){
			result = "Связь отсутствует";
			Log.i("Correlation", "коэффициент незначим");
		}

		if(t > q.get(n > q.size() ? q.size() - 1 : n - 2 - 1).second){
			Double rabs = Math.abs(r);
			if(rabs >= 0.0 && rabs < 0.2){
				result = "Практически нет связи";
			} else if (rabs >= 0.2 && rabs < 0.5){
				result = "Связь слабая";
			} else if (rabs >= 0.5 && rabs < 0.75){
				result = "Связь средняя";
			} else if (rabs >= 0.75 && rabs < 0.95){
				result = "Связь тесная";
			} else if (rabs >= 0.95 && rabs < 1.0){
				result = "Связь очень сильная";
			} else if (rabs == 1.0){
				result = "Функциональная зависимость";
			}
			Log.i("Correlation", "коэффициент значим");
			Double r1 = r - q.get(n > q.size() ? q.size() - 1 : n - 2 - 1).second * ((1 - r * r)/Math.sqrt(n - 1));
			Double r2 = r + q.get(n > q.size() ? q.size() - 1 : n - 2 - 1).second * ((1 - r * r)/Math.sqrt(n - 1));
			Log.i("Correlation", "доверительный интервал (99.5%) " + r1.toString() + " - " + r2.toString());
		}
		
		return result;
	}
	
	private static Double PearsonContingencyCoefficient(Boolean[] X, Boolean[] Y){
		Integer a = 0;
		Integer b = 0;
		Integer c = 0;
		Integer d = 0;
		
		for(int i = 0; i < X.length; i++){
			if(X[i] && Y[i]){
				a++;
			} else if(!X[i] && Y[i]){
				b++;
			} else if(X[i] && !Y[i]){
				c++;
			} else if(!X[i] && !Y[i]){
				d++;
			}
		}
		
		return ((double)(a * d - b * c)) / Math.sqrt((a + b) * (b + d) * (a + c) * (c + d));
	}

	private static void verificationPearsonContingencyCoefficient(Double r, Integer n){
		Double x2 = r * r * (double)n;
		Log.i("Correlation", "x2 = " + x2.toString());
		
		if(x2 <= 3.841){
			Log.i("Correlation", "коэффициент незначим");
		}
		
		if(x2 > 6.635){
			Log.i("Correlation", "коэффициент значим");
		}
	}
		
	private static Double avg(Double[] X){
		return sum(X)/(double)X.length;
	}
	
	private static Double sum(Double[] X){
		Double result = 0.0;
		
		for(int i = 0; i < X.length; i++){
			result += X[i];
		}
		
		return result;
	}
}
