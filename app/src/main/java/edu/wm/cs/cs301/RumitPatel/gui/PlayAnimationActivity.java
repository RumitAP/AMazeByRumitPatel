package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import edu.wm.cs.cs301.RumitPatel.generation.CardinalDirection;
import edu.wm.cs.cs301.RumitPatel.generation.Distance;

/**
 * Used these for threads: https://developer.android.com/reference/android/os/Looper
 * https://developer.android.com/reference/java/lang/Thread
 */

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
    private boolean pause = false;
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
    private MazePanel panel;

    private RobotDriver driver;
    private String sensorConfiguration = "1111";
    private Robot robot;
    private StatePlaying statePlaying = new StatePlaying();
    private int startingDistToExit;

    CardinalDirection cd = null;
    DistanceSensor l;
    DistanceSensor r;
    DistanceSensor f;
    DistanceSensor b;


    /**
     * UI for animation activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        panel = findViewById(R.id.goToWinning);
        statePlaying.setMazeConfiguration(GeneratingActivity.maze);
        statePlaying.setPlayAnimation(this);

        int[] start = GeneratingActivity.maze.getStartingPosition();
        startingDistToExit =GeneratingActivity.maze.getDistanceToExit(start[0], start[1]);

        Intent getIntent = getIntent();

        if (getIntent.getStringExtra("driver").equalsIgnoreCase("Wizard")) {
            driver = new Wizard();
            sensorConfiguration = "1111";
            robot = new ReliableRobot();
        }
        else {
            driver = new Wallfollower();
            switch (getIntent.getStringExtra("robot")) {
                case "Shaky":
                    sensorConfiguration = "0000";
                    break;
                case "Soso":
                    sensorConfiguration = "1010";
                    break;
                case "Mediocre":
                    sensorConfiguration = "1110";
                    break;
                case "Premium":
                default:
                    sensorConfiguration = "1111";
            }
            Log.v(Logv, getIntent.getStringExtra("robot") + " -- using configuration: " + sensorConfiguration);
            robot = sensorConfiguration.contains("0") ? new UnreliableRobot() : new ReliableRobot();
        }
        robot.setController(statePlaying);
        addDistanceSensor(0, Robot.Direction.FORWARD);
        addDistanceSensor(1, Robot.Direction.LEFT);
        addDistanceSensor(2, Robot.Direction.RIGHT);
        addDistanceSensor(3, Robot.Direction.BACKWARD);
        driver.setMaze(GeneratingActivity.maze);
        driver.setRobot(robot);



        //Create progress bar and corresponding thread
        progressBar = findViewById(R.id.ProgressBar1);
        textView = findViewById(R.id.textviewid1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
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
                    statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLESOLUTION, 0);
                    Log.v(Logv, "Map On");
                }else {
                    statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLESOLUTION, 0);
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
                statePlaying.keyDown(Constants.UserInput.ZOOMIN, 0);
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
                statePlaying.keyDown(Constants.UserInput.ZOOMOUT, 0);
                Log.v(Logv, "Zooming Out");
            }
        });


        //Play pause button plays and pauses the animation
        playPauseButton = (Button) findViewById(R.id.playPauseButton); // or whatever ID you set it to
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (playPauseButton.getText().equals(getText(R.string.playing))) {
                    playPauseButton.setText(R.string.paused);
                    pause = true;
                    Log.v(Logv, "Paused");
                }
                else {
                    playPauseButton.setText(R.string.playing);
                    pause = false;
                    Log.v(Logv, "Playing");
                }
                driver.togglePaused();
            }
        });


        seekBar = findViewById(R.id.speedSeekBar);
        seekBar.setMax(9);
        speed = 0;
        updateDriverDelay();
        /**
         * Changes the speed of the animation through interaction with the speedbar.
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
       int speedchange = 0;
       @Override
       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
           speedchange = progress;
           speed = speedchange;
           updateDriverDelay();
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
           updateDriverDelay();
       }
   });

        statePlaying.sensorSetColor(R.id.frontSensor, frontSensorOperational);
        statePlaying.sensorSetColor(R.id.backSensor, backSensorOperational);
        statePlaying.sensorSetColor(R.id.leftSensor, leftSensorOperational);
        statePlaying.sensorSetColor(R.id.rightSensor, rightSensorOperational);

        statePlaying.start(panel);

        Thread driverThread = new Thread(() -> {
            Looper.prepare();

            try {
                if (driver.drive2Exit()) {
                    goToWinning();
                } else {
                    goToLosing();
                }
            }
            catch (InterruptedException ignore) { }
            catch (Exception e) {
                Log.v("PlayAnimationActivity", "error driving to exit");
                e.printStackTrace();
                //goToLosing();
            }
            finally {
                if (robot instanceof UnreliableRobot) {
                    for (Robot.Direction d : ((UnreliableRobot)robot).getUnreliableDirections()) {
                        robot.stopFailureAndRepairProcess(d);
                    }
                }
            }
        });
        driverThread.start();


    }



    /**
     * progress bar progression on background thread
     */
    public void startProgress(){
//        energyConsumption = 0;
//        for (energy=3500; energy > 0; energy--){
//            try {
//                Thread.sleep(50);
//                progressBar.setProgress(energy);
//                energyConsumption += 1;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    textView.setText(String.valueOf(energy+"%"));
//                }
//            });
//        }

    }

//    private void sensorSetColor( TextView sense, boolean sensor){
//        if (sensor == true) {
//            sense.setTextColor(Color.parseColor("#33cc33"));
//            Toast.makeText(PlayAnimationActivity.this, "Sensor is operational!",
//                    Toast.LENGTH_SHORT).show();
//            Log.v(Logv, "Sensor is operational!");
//        }else {
//            sense.setTextColor(Color.parseColor("#ff0000"));
//            Toast.makeText(PlayAnimationActivity.this, "Sensor is NOT operational!",
//                    Toast.LENGTH_SHORT).show();
//            Log.v(Logv, "Sensor is NOT operational!");
//        }
//    }

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
        intent.putExtra("Energy Consumption", String.valueOf(driver.getEnergyConsumption()));
        intent.putExtra("Clicks", String.valueOf(driver.getPathLength()));
        intent.putExtra("shortest", String.valueOf(GeneratingActivity.maze.getMazedists().getMaxDistance()));
        intent.putExtra("Starting Distance", startingDistToExit);
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
        intent.putExtra("Energy Consumption", String.valueOf(driver.getEnergyConsumption()));
        intent.putExtra("Clicks", String.valueOf(driver.getPathLength()));
        intent.putExtra("shortest", String.valueOf(GeneratingActivity.maze.getMazedists().getMaxDistance()));
        intent.putExtra("Starting Distance", startingDistToExit);
        Toast.makeText(PlayAnimationActivity.this, "Losing",
                Toast.LENGTH_SHORT).show();
        Log.v(Logv, "Losing");
        startActivity(intent);

    }

    /**
     * Instantiates and adds a Sensor
     * @param index the index of the sensorConfiguration to grab the sensorType for
     * @param direction the direction to add the sensor
     */
    private void addDistanceSensor(int index, Robot.Direction direction) {
        // the type of sensor to instantiate:
        // '0' for unreliable, otherwise reliable
        char sensorType = sensorConfiguration.charAt(index);
        ReliableSensor sensor = sensorType == '0' ? new UnreliableSensor() : new ReliableSensor();
        sensor.setController(statePlaying);
        robot.addDistanceSensor(sensor, direction);

        if (robot instanceof UnreliableRobot && sensorType == '0') {
            robot.startFailureAndRepairProcess(direction, 4, 2);
        }
    }

    private void updateDriverDelay() {
        driver.setDelay((9 - speed) * 100);
    }




}
