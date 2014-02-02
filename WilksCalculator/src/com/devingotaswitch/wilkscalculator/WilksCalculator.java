package com.devingotaswitch.wilkscalculator;


import com.devingotaswitch.wilkscalculator.wilksutils.GeneralUtils;
import com.devingotaswitch.wilkscalculator.wilksutils.UserStats;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class WilksCalculator extends Activity {
	public Context cont;
	public UserStats stats;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar ab = getActionBar();
		cont = this;
		//ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_wilks_calculator);
		
		checkUserStats();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wilks_calculator, menu);
		return true;
	}
	
	
	/**
	 * If data is stored, read it and display, otherwise dummy stuff
	 */
	public void checkUserStats()
	{
		boolean isStored = UserStats.isSaved(cont);
		if(isStored)
		{
			stats = new UserStats(cont);
			stats.readData();
			fillDataStored();
		}
		else
		{
			fillDataEmpty();
		}
	}
	
	/**
	 * Read stuff from file then output it as the same so it's all hunky dory
	 */
	public void fillDataStored()
	{
		RadioButton male = (RadioButton)findViewById(R.id.gender_male);
		RadioButton female = (RadioButton)findViewById(R.id.gender_female);
		RadioButton pounds = (RadioButton)findViewById(R.id.weight_lbs);
		RadioButton kgs = (RadioButton)findViewById(R.id.weight_kgs);
		if(stats.isMan)
		{
			male.setChecked(true);
		}
		else
		{
			female.setChecked(true);
		}
		if(stats.isKG)
		{
			kgs.setChecked(true);
		}
		else
		{
			pounds.setChecked(true);
		}
		EditText weight = (EditText)findViewById(R.id.weight_view);
		EditText deadlift_view = (EditText)findViewById(R.id.deadlift_view);
		EditText squat_view = (EditText)findViewById(R.id.squat_view);
		EditText bench_view = (EditText)findViewById(R.id.bench_view);
		weight.setText(String.valueOf(stats.weight));
		deadlift_view.setText(String.valueOf(stats.deadlift));
		squat_view.setText(String.valueOf(stats.squat));
		bench_view.setText(String.valueOf(stats.bench));
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(weight.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(deadlift_view.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(squat_view.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(bench_view.getWindowToken(), 0);
		TextView output = (TextView)findViewById(R.id.output_view);
		if(!stats.isKG)
		{
			output.setText("Big 3 Total: " + stats.total + "\nWilks Score: " + stats.wilksScore);
		}
		else
		{
			output.setText("Big 3 Total: " + GeneralUtils.lbToKg(stats.total)+ "\nWilks Score: " +  
					stats.wilksScore);
		}
			setButtonsOnClick(male, female, pounds, kgs, weight, deadlift_view, squat_view, bench_view);
	}

	/**
	 * Fills the data with preset stuff/hints
	 */
	public void fillDataEmpty()
	{
		RadioButton male = (RadioButton)findViewById(R.id.gender_male);
		RadioButton female = (RadioButton)findViewById(R.id.gender_female);
		RadioButton pounds = (RadioButton)findViewById(R.id.weight_lbs);
		RadioButton kgs = (RadioButton)findViewById(R.id.weight_kgs);
		male.setChecked(true);
		female.setChecked(false);
		pounds.setChecked(true);
		kgs.setChecked(false);
		EditText weight = (EditText)findViewById(R.id.weight_view);
		EditText deadlift_view = (EditText)findViewById(R.id.deadlift_view);
		EditText squat_view = (EditText)findViewById(R.id.squat_view);
		EditText bench_view = (EditText)findViewById(R.id.bench_view);
		weight.getText().clear();
		deadlift_view.getText().clear();
		squat_view.getText().clear();
		bench_view.getText().clear();
		weight.setHint("Weight");
		deadlift_view.setHint("Deadlift Max");
		squat_view.setHint("Squat Max");
		bench_view.setHint("Bench Max");
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(weight.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(deadlift_view.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(squat_view.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(bench_view.getWindowToken(), 0);
		TextView output = (TextView)findViewById(R.id.output_view);
		output.setText(" ");
		setButtonsOnClick(male, female, pounds, kgs, weight, deadlift_view, squat_view, bench_view);
	}

	public void setButtonsOnClick(final RadioButton male, RadioButton female,
			final RadioButton pounds, RadioButton kgs, final EditText weight_view,
			final EditText deadlift_view, final EditText squat_view, final EditText bench_view) {
		Button clear = (Button)findViewById(R.id.clear);
		Button submit = (Button)findViewById(R.id.submit);
		clear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				stats.clearData();
				fillDataEmpty();
			}
		});
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				boolean isWeight = GeneralUtils.isDouble(weight_view.getText().toString());
				boolean isDead = GeneralUtils.isDouble(deadlift_view.getText().toString());
				boolean isSquat = GeneralUtils.isDouble(squat_view.getText().toString());
				boolean isBench = GeneralUtils.isDouble(bench_view.getText().toString());
				if(isWeight && isDead && isSquat && isBench)
				{
					boolean isMale = false;
					if(male.isChecked())
					{
						isMale = true;
					}
					boolean isPounds = false;
					if(pounds.isChecked())
					{
						isPounds = true;
					}
					double weight = Double.parseDouble(weight_view.getText().toString());
					double dead = Double.parseDouble(deadlift_view.getText().toString());
					double squat = Double.parseDouble(squat_view.getText().toString());
					double bench = Double.parseDouble(bench_view.getText().toString());
					stats = new UserStats(isMale, !isPounds, weight, dead, squat, bench, cont);
					fillDataStored();
				}
			}
		});
	}
	
}
