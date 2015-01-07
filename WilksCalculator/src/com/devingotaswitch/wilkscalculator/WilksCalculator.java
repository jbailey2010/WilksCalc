package com.devingotaswitch.wilkscalculator;


import java.text.DecimalFormat;








import com.devingotaswitch.wilkscalculator.wilksutils.GeneralUtils;
import com.devingotaswitch.wilkscalculator.wilksutils.UserStats;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WilksCalculator extends Activity {
	public Context cont;
	public UserStats stats;
	SideNavigationView sideNavigationView;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wilks_calculator);
		ActionBar ab = getActionBar();
		cont = this;
		ab.setDisplayShowTitleEnabled(false);
		ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {
		    @Override
		    public void onSideNavigationItemClick(int itemId) {
		    	switch (itemId) {
	            case R.id.score_classifications:
	            	wilksPopup();
	                break;
	            case R.id.siff_score:
	            	siffPopup();	
	                break;
	            default:
	                return;
		    	}
		    }
		};
		sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
	    sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
	    sideNavigationView.setMenuClickCallback(sideNavigationCallback);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		checkUserStats();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wilks_calculator, menu);
		
		return true;
	}
	
	/**
	 * Runs the on selection part of the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{  
		switch (item.getItemId()) 
		{
			case android.R.id.home:
		        sideNavigationView.toggleMenu();
		        return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Makes the one text one image popup with the Wilks score classifications
	 */
	public void wilksPopup()
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.text_image_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		TextView close = (TextView)dialog.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		TextView text = (TextView)dialog.findViewById(R.id.text_above_image);
		ImageView image = (ImageView)dialog.findViewById(R.id.image_below_text);
		image.setImageResource(R.drawable.wilks);
		text.setText("Below are the wilks score experience levels, as described by Rippetoe and Kilgore");
	}
	
	/**
	 * Makes the one text one image popup with the Siff scores
	 */
	public void siffPopup()
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.text_image_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		TextView close = (TextView)dialog.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		TextView text = (TextView)dialog.findViewById(R.id.text_above_image);
		ImageView image = (ImageView)dialog.findViewById(R.id.image_below_text);
		image.setImageResource(R.drawable.siff);
		text.setText("Below are the Siff scores, which are roughly the percentage of the world record performance in that field");
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
		DecimalFormat df = new DecimalFormat("#.###");
		if(!stats.isKG)
		{
			if(!stats.getClassifs().contains("Un-trained")){
				output.setText("Big 3 Total: " + df.format(stats.total)
						+ "\nWilks Score: " + df.format(stats.wilksScore)
						+ "\nClassification: " + stats.getClassifs());
			}
			else{
				output.setText("Big 3 Total: " + df.format(stats.total)
						+ "\nWilks Score: " + df.format(stats.wilksScore));
			}
		}
		else
		{
			if(stats.getClassifs() != null && !stats.getClassifs().contains("Un-trained")){
				output.setText("Big 3 Total: " + 
						df.format(GeneralUtils.lbToKg(stats.total))+ "\nWilks Score: " +  
						df.format(stats.wilksScore)
						+ "\nClassification: " + stats.getClassifs());
			}
			else{
				output.setText("Big 3 Total: " + 
						df.format(GeneralUtils.lbToKg(stats.total))+ "\nWilks Score: " +  
						df.format(stats.wilksScore));
			}
		}
		output.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				wilksPopup();
			}
		});
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
		output.setOnClickListener(null);
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
				handleClear();
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
					stats.updateStats(!isPounds, isMale, Math.abs(weight), Math.abs(squat), Math.abs(dead), Math.abs(bench));
					fillDataStored();
				}
				else
				{
					Toast.makeText(cont, "Invalid input, please type numbers in every field", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * Handles the confirmation/canceling of clearing
	 */
	public void handleClear()
	{
		final Dialog dialog = new Dialog(cont, R.style.RoundCornersFull);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.clear_popup);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	    dialog.getWindow().setAttributes(lp);
		dialog.show();
		Button cancel = (Button)dialog.findViewById(R.id.clear_cancel);
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button confirm = (Button)dialog.findViewById(R.id.clear_confirm);
		confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				stats.clearData();
				fillDataEmpty();
			}
		});
	}
}
