package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import edu.wm.cs.cs301.RumitPatel.R;

import androidx.appcompat.app.AppCompatActivity;

public class LosingActivity extends AppCompatActivity {
    private String energyConsumption;
    private String pathLength;
    private String shortestPath;
    private TextView Consumption;
    private TextView Length;
    private TextView Path;

    /**
     * UI for Losing activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        Intent getIntent = getIntent();
        energyConsumption = getIntent.getStringExtra("Energy Consumption");
        pathLength = getIntent.getStringExtra("Path Length");
        shortestPath = getIntent.getStringExtra("Distance To Exit From Start");



        //CHANGE FOR PROJECT 7
        ImageView losingImage = findViewById(R.id.losingImage);
        if (4500 >= 3500) {
            losingImage.setBackgroundResource(R.drawable.battery);
        }
        else {
            losingImage.setBackgroundResource(R.drawable.broken);
        }

        Consumption = findViewById(R.id.energyConsumption);
        Path = findViewById(R.id.shortestPath);
        Length = findViewById(R.id.pathLegnth);
        Consumption.setText("Energy Consumption: "+"2000");
        Path.setText("Shortest Path: " + "100");
        Length.setText("Your Path: " + "200");





    }


        /**
         * take user to AMazeActivity if back button is pressed
         */
        @Override
        public void onBackPressed () {
            Intent intent = new Intent(this, AMazeActivity.class);

            startActivity(intent);
            finish();
        }
    }
