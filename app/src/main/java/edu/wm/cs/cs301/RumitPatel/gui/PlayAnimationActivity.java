package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.RumitPatel.R;

public class PlayAnimationActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private TextView frontSensor;
    private TextView leftSensor;
    private TextView rightSensor;
    private TextView backSensor;
    private boolean frontSensorOperational = true;
    private boolean leftSensorOperational = true;
    private boolean rightSensorOperational = true;
    private boolean backSensorOperational = true;
    private int energy;
    Handler handler=new Handler();
    private String Logv = "Animation Activity:";
    private Button zoomInButton;
    private Button zoomOutButton;
    private ToggleButton wholeMazeToggle;
    private Button playPauseButton;
    private SeekBar seekBar;
    private int speed;
    private Intent intent;
    private boolean hasStoppedAtExit = true;
    private int energyConsumption = 0;
    private Button go2Winning;
    private Button go2Losing;


    /**
     * UI for animation activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        //Create progress bar and corresponding thread
        progressBar = findViewById(R.id.ProgressBar1);
        textView = findViewById(R.id.textviewid1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                startProgress();
            }
        });
        thread.start();


        wholeMazeToggle = findViewById(R.id.wholeMazeToggle1);

        /**
         * Listener for the toggle which toggles the Maze view on and off
         */
        wholeMazeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wholeMazeToggle.isChecked()){
                    Toast.makeText(PlayAnimationActivity.this, "Show Map: On",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Map On");
                }else {
                    Toast.makeText(PlayAnimationActivity.this, "Show Map: Off",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Map Off");
                }

            }

        });


        zoomInButton = findViewById(R.id.zoom_in1);
        /**
         * if clicked, will zoom in to the maze.
         */
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "Zoom In",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Zooming In");
            }
        });


        zoomOutButton = findViewById(R.id.zoom_out1);
        /**
         * if clicked, will zoom out of the maze.
         */
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "Zoom Out",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Zooming Out");
            }
        });


        //Play pause button plays and pauses the animation
        playPauseButton = (Button) findViewById(R.id.playPauseButton); // or whatever ID you set it to
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (playPauseButton.getText().equals("Play")) {
                    playPauseButton.setText("Pause");
                    Toast.makeText(PlayAnimationActivity.this, "Paused",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Paused");
                }
                else {
                    playPauseButton.setText("Play");
                    Toast.makeText(PlayAnimationActivity.this, "Playing",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Playing");
                }
            }
        });


        seekBar = findViewById(R.id.speedSeekBar);
        seekBar.setMax(9);
        /**
         * Changes the speed of the animation through interaction with the speedbar.
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
       int speedchange = 0;
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
           speedchange = progress;
           speed = speedchange;
       }

       @Override
       public void onStartTrackingTouch(SeekBar seekBar) {

       }

       /**
        * used the following website: https://developer.android.com/guide/topics/ui/notifiers/toasts
        * @param seekBar
        */
       @Override
       public void onStopTrackingTouch(SeekBar seekBar) {
           Toast.makeText(PlayAnimationActivity.this, "Speed is: " + speedchange,
                   Toast.LENGTH_SHORT).show();
           Log.v(Logv, "Speed is: " + speedchange);
           speed = speedchange;
       }
   });

        frontSensor = findViewById(R.id.frontSensor);
        backSensor = findViewById(R.id.backSensor);
        leftSensor = findViewById(R.id.leftSensor);
        rightSensor = findViewById(R.id.rightSensor);
        sensorSetColor(frontSensor, frontSensorOperational);
        sensorSetColor(backSensor, backSensorOperational);
        sensorSetColor(leftSensor, leftSensorOperational);
        sensorSetColor(rightSensor, rightSensorOperational);


        go2Winning = findViewById(R.id.goToWinning);
        /**
         * if clicked, will take you to the winning screen.
         */
        go2Winning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "Going to Winning State",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Going to Winning State");
                goToWinning();
            }
        });

        go2Losing = findViewById(R.id.goToLosing);
        /**
         * if clicked, will take you to the winning screen.
         */
        go2Losing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "Going to Losing State",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Going to Losing State");
                goToLosing();
            }
        });


    }



    /**
     * progress bar progression on background thread
     */
    public void startProgress(){
        energyConsumption = 0;
        for (energy=3500; energy > 0; energy--){
            try {
                Thread.sleep(50);
                Log.v(Logv,"EnergyLevel:" + energy);
                progressBar.setProgress(energy);
                energyConsumption += 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(String.valueOf(energy+"%"));
                }
            });
        }

    }

    private void sensorSetColor( TextView sense, boolean sensor){
        if (sensor == true) {
            sense.setTextColor(Color.parseColor("#33cc33"));
            Toast.makeText(PlayAnimationActivity.this, "Sensor is operational!",
                    Toast.LENGTH_SHORT).show();
            Log.v(Logv, "Sensor is operational!");
        }else {
            sense.setTextColor(Color.parseColor("#ff0000"));
            Toast.makeText(PlayAnimationActivity.this, "Sensor is NOT operational!",
                    Toast.LENGTH_SHORT).show();
            Log.v(Logv, "Sensor is NOT operational!");
        }
    }

    /**
     * take user to AMazeActivity if back button is pressed
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, AMazeActivity.class);

        startActivity(intent);
        finish();
    }

    /**
     * pass information to the WinningClass
     * https://developer.android.com/guide/components/intents-filters#:~:text=An%20Intent%20object%20carries%20information,action%20to%20take%20and%20the
     */
    public void goToWinning() {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Energy Consumption", energyConsumption);
        intent.putExtra("Path Length", 0);
        intent.putExtra("Distance To Exit From Start", 20);
        Toast.makeText(PlayAnimationActivity.this, "Winning",
                Toast.LENGTH_SHORT).show();
        Log.v(Logv, "Winning");
        startActivity(intent);

    }

    /**
     * pass information to the Losing Activity class
     * https://developer.android.com/guide/components/intents-filters#:~:text=An%20Intent%20object%20carries%20information,action%20to%20take%20and%20the
     */
    public void goToLosing() {
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("Energy Consumption", 3500);
        intent.putExtra("Path Length", 0);
        intent.putExtra("Distance To Exit From Start", 20);
        Toast.makeText(PlayAnimationActivity.this, "Losing",
                Toast.LENGTH_SHORT).show();
        Log.v(Logv, "Losing");
        startActivity(intent);

    }

}
