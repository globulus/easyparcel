package net.globulus.easyparcelsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Goal goal = new Goal();
		goal.goalType = 5;
		goal.calories = 10f;
		goal.carbs = null;
		goal.endDate = new Date();
		goal.fat = 11f;
		goal.name = "aaa";
		goal.parameter1 = 22;

		Bundle bundle = new Bundle();
		bundle.putParcelable("aaa", goal);

		Goal goal2 = bundle.getParcelable("aaa");

		String a = "a";
	}
}
