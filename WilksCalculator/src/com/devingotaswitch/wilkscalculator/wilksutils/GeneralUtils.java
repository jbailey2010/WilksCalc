package com.devingotaswitch.wilkscalculator.wilksutils;

public class GeneralUtils 
{
	/**
	 * Makes sure a string is a double
	 */
	public static boolean isDouble(String s){
		try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
}
