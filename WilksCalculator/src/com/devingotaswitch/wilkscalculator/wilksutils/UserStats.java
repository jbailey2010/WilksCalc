package com.devingotaswitch.wilkscalculator.wilksutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * A library to handle the wilks calculation, file I/O...etc.
 * @author Jeff
 *
 */
public class UserStats 
{
	public boolean isKG;
	public boolean isMan;
	public double weight;
	public double deadlift;
	public double squat;
	public double bench;
	public double total;
	public double wilksScore;
	public Context cont;
	public HashMap<String, String> classifications;
	
	/**
	 * Takes in all of the user input measurements, uses them to calculate the wilks score, and saves the data
	 */
	public UserStats(boolean isM, boolean kg, double w, double dl, double s, double b, Context c)
	{
		classifications = new HashMap<String, String>();
		cont = c;
		isMan = isM;
		isKG = kg;
		weight = w;
		deadlift = dl;
		squat = s;
		bench = b;
		total = deadlift + squat + bench;
		wilksScore = this.calculateWilks();
		populateClassifs();
		saveData();
	}
	
	/**
	 * A pseudo dummy constructor to be read to if data exists
	 */
	public UserStats(Context c)
	{
		classifications = new HashMap<String, String>();
		cont = c;
		isMan = false;
		isKG = false;
		weight = 0.0;
		deadlift = 0.0;
		squat = 0.0;
		bench = 0.0;
		total = 0.0;
		populateClassifs();
	}
	
	public void populateClassifs()
	{
		classifications.put("52/116", "Un-trained");
		classifications.put("52/193", "Novice");
		classifications.put("52/227", "Intermediate");
		classifications.put("52/321", "Advanced");
		classifications.put("52/416", "Elite");
		
		classifications.put("56/116", "Un-trained");
		classifications.put("56/193", "Novice");
		classifications.put("56/230", "Intermediate");
		classifications.put("56/320", "Advanced");
		classifications.put("56/415", "Elite");
		
		classifications.put("60/117", "Un-trained");
		classifications.put("60/195", "Novice");
		classifications.put("60/231", "Intermediate");
		classifications.put("60/321", "Advanced");
		classifications.put("60/414", "Elite");
		
		classifications.put("67/118", "Un-trained");
		classifications.put("67/197", "Novice");
		classifications.put("67/236", "Intermediate");
		classifications.put("67/326", "Advanced");
		classifications.put("67/416", "Elite");
		
		classifications.put("75/119", "Un-trained");
		classifications.put("75/198", "Novice");
		classifications.put("75/236", "Intermediate");
		classifications.put("75/326", "Advanced");
		classifications.put("75/416", "Elite");
	
		classifications.put("82/120", "Un-trained");
		classifications.put("82/201", "Novice");
		classifications.put("82/239", "Intermediate");
		classifications.put("82/329", "Advanced");
		classifications.put("82/418", "Elite");
	
		classifications.put("90/121", "Un-trained");
		classifications.put("90/201", "Novice");
		classifications.put("90/241", "Intermediate");
		classifications.put("90/329", "Advanced");
		classifications.put("90/416", "Elite");
	
		classifications.put("100/121", "Un-trained");
		classifications.put("100/203", "Novice");
		classifications.put("100/243", "Intermediate");
		classifications.put("100/330", "Advanced");
		classifications.put("100/415", "Elite");
	
		classifications.put("110/123", "Un-trained");
		classifications.put("110/204", "Novice");
		classifications.put("110/242", "Intermediate");
		classifications.put("110/329", "Advanced");
		classifications.put("110/412", "Elite");
	
		classifications.put("125/122", "Un-trained");
		classifications.put("125/203", "Novice");
		classifications.put("125/241", "Intermediate");
		classifications.put("125/326", "Advanced");
		classifications.put("125/408", "Elite");
	
		classifications.put("145/121", "Un-trained");
		classifications.put("145/202", "Novice");
		classifications.put("145/240", "Intermediate");
		classifications.put("145/324", "Advanced");
		classifications.put("145/405", "Elite");
		
		classifications.put("145+/124", "Un-trained");
		classifications.put("145+/206", "Novice");
		classifications.put("145+/245", "Intermediate");
		classifications.put("145+/330", "Advanced");
		classifications.put("145+/413", "Elite");
	}
	
	public String getClassifs()
	{
		double mass = weight;
		double lifted = wilksScore;
		if(!isKG)
		{
			mass = GeneralUtils.lbToKg(mass);
		}
		Set<String> keys = classifications.keySet();
		String massKey = this.closestUnderWeight(mass, keys);
		String liftedKey = this.closestUnderMass(lifted, keys, massKey);
		return classifications.get(massKey + "/" + liftedKey);
	}
	
	public String closestUnderMass(double x, Set<String> keys, String massKey)
	{
		String ret = "";
		final Iterator<String> itr = keys.iterator();
	    Object lastElement = itr.next();
	    Object lastGood = lastElement;
	    while(itr.hasNext()) {
	    	if((((String) lastElement).split("/")[0]).equals("massKey"))
	    	{
	    		lastGood = lastElement;
	    	}
	        lastElement=itr.next();
	    }
	    Integer lastValidWilks = Integer.parseInt(((String)lastGood).split("/")[1]);
	    double currBest = 1000000000;
	    
		for(String keyComb : keys)
		{
			String[] keySet = keyComb.split("/");
			if(keySet[0].equals(massKey))
			{
				String wilksIter = keySet[1];
			    Integer wilksCurrent = Integer.parseInt(wilksIter);
			    if(Math.abs(wilksCurrent - x) < currBest)
			    {
			    	ret = String.valueOf(wilksCurrent);
			    	currBest = Math.abs(wilksCurrent - x);
			    }
			}
		}
		if(ret.equals(""))
		{
			return String.valueOf(lastValidWilks);
		}
		return ret;
	}
	
	public String closestUnderWeight(double x, Set<String> keys)
	{
		String ret = "";
		double currBest = 1000000000;
		for(String iter : keys)
		{
			String[] set = iter.split("/");
			if(iter.contains("+"))
			{
				Integer ceil = Integer.parseInt(set[0].substring(0, set[0].length() - 1));
				if(ceil < x)
				{
					return set[0];
				}
				else
				{
					continue;
				}
			}
			Integer currMass = Integer.parseInt(set[0]);
			if(Math.abs(x - currMass) < currBest)
			{
				currBest = Math.abs(x - currMass);
				ret = String.valueOf(currMass);
			}
		}
		return ret;
	}
	
	/**
	 * Checks to see if everything is saved as a flag to know if needed to read from file or not
	 */
	public static boolean isSaved(Context cont)
	{
		SharedPreferences editor = cont.getSharedPreferences("Wilks Calculator Jeff", 0);
		return editor.getBoolean("Data Saved", false);
	}
	
	/**
	 * Saves everything to file for easy reading later
	 */
	public void saveData()
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("Wilks Calculator Jeff", 0).edit();
		editor.putBoolean("Data Saved", true);
		editor.putBoolean("isKG", isKG);
		editor.putLong("weight", Double.doubleToRawLongBits(weight));
		editor.putBoolean("isMan", isMan);
		editor.putLong("squat", Double.doubleToRawLongBits(squat));
		editor.putLong("deadlift", Double.doubleToRawLongBits(deadlift));
		editor.putLong("bench", Double.doubleToRawLongBits(bench));
		editor.putLong("total", Double.doubleToRawLongBits(total));
		editor.putLong("wilksScore", Double.doubleToRawLongBits(wilksScore));
		editor.commit();
	}
	
	/**
	 * Reads the data from file for usage in the GUI
	 */
	public void readData()
	{
		SharedPreferences reader = cont.getSharedPreferences("Wilks Calculator Jeff", 0);
		isMan = reader.getBoolean("isMan", true);
		isKG = reader.getBoolean("isKG", false);
		weight = Double.longBitsToDouble(reader.getLong("weight", Double.doubleToLongBits(100.0)));
		squat = Double.longBitsToDouble(reader.getLong("squat", Double.doubleToLongBits(100.0)));
		deadlift = Double.longBitsToDouble(reader.getLong("deadlift", Double.doubleToLongBits(100.0)));
		bench = Double.longBitsToDouble(reader.getLong("bench", Double.doubleToLongBits(100.0)));
		total = Double.longBitsToDouble(reader.getLong("total", Double.doubleToLongBits(100.0)));
		wilksScore = Double.longBitsToDouble(reader.getLong("wilksScore", Double.doubleToLongBits(100.0)));
	}
	
	/**
	 * Deletes every bit of data stored to file
	 */
	public void clearData()
	{
		SharedPreferences.Editor editor = cont.getSharedPreferences("Wilks Calculator Jeff", 0).edit();
		editor.clear().commit();
	}
	
	/**
	 * Updates all of the data, syncing wilks and total, and then saves the data to file again
	 */
	public void updateStats(boolean isK, boolean isM, double newWeight, double newSquat, double newDead, double newBench)
	{
		updateIsKg(isK);
		updateGender(isM);
		updateWeight(newWeight);
		updateSquat(newSquat);
		updateDeadlift(newDead);
		updateBench(newBench);
		updateTotalWilks();
		saveData();
	}
	
	/**
	 * Updates is kilograms
	 */
	public void updateIsKg(boolean isK)
	{
		isKG = isK;
	}
	
	/**
	 * Updates gender and wilks
	 */
	public void updateGender(boolean isM)
	{
		isMan = isM;
	}
	
	/**
	 * Updates weight saved and wilks
	 */
	public void updateWeight(double newWeight)
	{
		weight = newWeight;
	}
	
	/**
	 * Updates the score as need be for bench
	 */
	public void updateBench(double newBench)
	{
		isPR(bench, newBench, "bench");
		bench = newBench;
	}
	
	/**
	 * Updates the score as need be for squats
	 */
	public void updateSquat(double newSquat)
	{
		isPR(squat, newSquat, "squat");
		squat = newSquat;
	}
	
	/**
	 * Updates the score as need be for deadlifts
	 */
	public void updateDeadlift(double newDead)
	{
		isPR(deadlift, newDead, "deadlift");
		deadlift = newDead;
	}
	
	/**
	 * SYnces the total and wilks score post update
	 */
	public void updateTotalWilks()
	{
		total = deadlift + squat + bench;
		wilksScore = calculateWilks();
	}
	
	/**
	 * If the newly input data is better than the old, it congratulates you
	 */
	public void isPR(double old, double newLift, String key)
	{
		if(newLift > old && old > 0)
		{
			Toast.makeText(cont, "Congrats on the " + key + " PR!", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Does the actual wilks calculation, returning the score itself (not the coefficient)
	 */
	public double calculateWilks()
	{
		double wilks = 0.0;
		double a = 0;
		double b = 0;
		double c = 0;
		double d = 0;
		double e = 0;
		double f = 0;
		if(isMan)
		{
			a=-216.0475144;
			b=16.2606339;
			c=-0.002388645;
			d=-0.00113732;
			e=7.01863E-06;
			f=-1.291E-08;
		}
		else
		{
			a=594.31747775582;
			b=-27.23842536447;
			c=0.82112226871;
			d=-0.00930733913;
			e=0.00004731582;
			f=-0.00000009054;
		}
		double x = weight;
		double totNormalized = total;
		if(!isKG)
		{
			x = GeneralUtils.lbToKg(x);
			totNormalized = GeneralUtils.lbToKg(total);
		}
		double denom = a + b*x + c*Math.pow(x,2.0) + d*Math.pow(x,3.0) + e*Math.pow(x,4.0) + f*Math.pow(x,5.0);
		wilks = totNormalized * (500.0 / denom);
		return wilks;
	}

}
