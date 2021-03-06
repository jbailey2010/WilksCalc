package com.devingotaswitch.wilkscalculator.wilksutils;

public class GeneralUtils {
	/**
	 * Makes sure a string is a double
	 */
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Makse sure a string is an int
	 */
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Converts pounds to kilograms
	 */
	public static double lbToKg(double pound) {
		return pound / 2.20462;
	}

	/**
	 * Converts kilograms to pounds
	 */
	public static double kgToLb(double kg) {
		return kg * 2.20462;
	}
}
