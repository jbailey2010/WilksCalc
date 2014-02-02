package com.devingotaswitch.wilkscalculator.wilksutils;

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
	
	/**
	 * Takes in all of the user input measurements, uses them to calculate the wilks score, and saves the data
	 */
	public UserStats(boolean isM, boolean kg, double w, double dl, double s, double b, Context c)
	{
		cont = c;
		isMan = isM;
		isKG = kg;
		weight = w;
		deadlift = dl;
		squat = s;
		bench = b;
		total = deadlift + squat + bench;
		wilksScore = this.calculateWilks();
		saveData();
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
		if(newLift > old)
		{
			Toast.makeText(cont, "Congrats on the " + key + " PR!", Toast.LENGTH_SHORT).show();
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
			a = -216.0475144;
			b = 16.2606339;
			c = -0.002388645;
			d = -0.00113732;
			e = 0.00000701863;
			f = 0.00000001291;
		}
		else
		{
			a = 594.31747775582;
			b = -27.23842536557;
			c = 0.82112226871;
			d = -0.00930733913;
			e = 0.00004731582;
			f = -0.00000009054;
		}
		double x = weight;
		double denom = a + b*x + c*Math.pow(x,2.0) + d*Math.pow(x,3) + e*Math.pow(x,4) + f*Math.pow(x,5);
		wilks = total * (500.0 / denom);
		return wilks;
	}

}