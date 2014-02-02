package com.devingotaswitch.wilkscalculator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WilksCalculator extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wilks_calculator);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wilks_calculator, menu);
		return true;
	}

}
