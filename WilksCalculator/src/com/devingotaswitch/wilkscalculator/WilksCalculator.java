package com.devingotaswitch.wilkscalculator;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;

public class WilksCalculator extends Activity {
	public Context cont;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		cont = this;
		//ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_wilks_calculator);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wilks_calculator, menu);
		return true;
	}

}
