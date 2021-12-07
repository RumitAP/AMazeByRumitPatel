package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.RumitPatel.R;
import edu.wm.cs.cs301.RumitPatel.generation.Maze;
import edu.wm.cs.cs301.RumitPatel.generation.Order;
import edu.wm.cs.cs301.RumitPatel.generation.StubOrder;

public class GeneratingActivity extends AppCompatActivity implements Order {

    private String builder;
    private Builder build;
    private String Rooms = "Yes";
    private boolean hasRooms;
    private String level;
    private int intLevel;
    private String mode;
    private String robot;
    private String driver;
    private ProgressBar progressBar;
    private int progress = 0;
    Handler handler=new Handler();
    private TextView textView;
    private Spinner spinner2;
    private Spinner spinner;
    private Intent intent;
    private String Logv = "GeneratingActivity:";
    private int seed;
    /**
     * UI for generating activity
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        Intent getIntent = getIntent();
        builder = getIntent.getStringExtra("Builder");
        Rooms = getIntent.getStringExtra("Rooms");
        level = getIntent.getStringExtra("Level");
        mode = getIntent.getStringExtra("Mode");
        seed = getIntent.getExtras().getInt("seed");

        //to get the int level for skill level
        if (level == "1") {
            intLevel = 1;
        } else if (level == "2") {
            intLevel = 2;
        } else if (level == "3") {
            intLevel = 3;
        } else if (level == "4") {
            intLevel = 4;
        }else if (level == "5") {
            intLevel = 5;
        }else if (level == "6") {
            intLevel = 6;
        }else if (level == "7") {
            intLevel = 7;
        }else if (level == "8") {
            intLevel = 8;
        }else if (level == "9") {
            intLevel = 9;
        }else if (level == "0") {
            intLevel = 0;
        }

        //to get the builder build for builder
        if (builder == "DFS") {
            build = Builder.DFS;
        } else if(builder == "Prim"){
            build = Builder.Prim;
        } else if (builder == "Boruvka") {
            build = Builder.DFS;
        }

        //to get if perfect maze or not
        if (Rooms == "Yes") {
            hasRooms = false;
        } else {
            hasRooms = true;
        }
        
        
        progressBar = findViewById(R.id.ProgressBar);
        textView = findViewById(R.id.textviewid);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                startProgress();

            }
        });
        thread.start();


        spinner = (Spinner) findViewById(R.id.robotSpinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.robot, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner2 = (Spinner) findViewById(R.id.driverSpinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.driver, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

    }

    /**
     * progress bar progression on background thread
     */
    public void startProgress(){

        for (progress=0; progress < 100; progress++){
            try {
                Thread.sleep(50);
                Log.v(Logv,"Loading:" + progress);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(String.valueOf(progress+"%"));
                }
            });
        }
        driver = spinner2.getSelectedItem().toString();
        robot = spinner.getSelectedItem().toString();
        Play();

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
     * if no driver was chosen, then asks the user to choose a driver.
     * If one isnt chosen in 5 seconds, driver is set to manual.
     *  Then based  off of selected inputs, will hand off to
     * Play Manual or Play Animation to start the maze.
     */
    public void Play() {
        if(driver.equals("Select Here")) {
            Log.v(Logv, "Driver not selected");
            Snackbar mySnackBar = Snackbar.make(textView, "Please choose a driver.",
                    10000);
            mySnackBar.show();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver = spinner2.getSelectedItem().toString();


        } else {
            Snackbar mySnackBar = Snackbar.make(textView, "Game will start shortly" +
                    ".", 5000);
            mySnackBar.show();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver = spinner2.getSelectedItem().toString();
        }

        if (driver.equals("Select Here")){
            driver = "Manual";
        }

        if(driver.equalsIgnoreCase("Manual")){
            Toast.makeText(GeneratingActivity.this, "Manual",
                    Toast.LENGTH_SHORT).show();
            Log.v(Logv, "Manual selected");
            intent = new Intent(this, PlayManuallyActivity.class);
            startActivity(intent);
            finish();

        }else if(driver.equalsIgnoreCase("Wizard")){
            Toast.makeText(GeneratingActivity.this, "Wizard",
                    Toast.LENGTH_SHORT).show();
            Log.v(Logv, "Wizard selected");
            intent = new Intent(this, PlayAnimationActivity.class);
            intent.putExtra("Robot",robot);
            intent.putExtra("Driver", driver);
            startActivity(intent);
            finish();
        }else if(driver.equalsIgnoreCase("Wallfollower")){
            Toast.makeText(GeneratingActivity.this, "Wallfollower",
                    Toast.LENGTH_SHORT).show();
            Log.v(Logv, "Wallfollower selected");
            intent = new Intent(this, PlayAnimationActivity.class);
            intent.putExtra("robot",robot);
            intent.putExtra("driver", driver);
            startActivity(intent);
            finish();

        }

    }

    @Override
    public int getSkillLevel() {
        return intLevel;
    }

    @Override
    public Builder getBuilder() {
        return build;
    }

    @Override
    public boolean isPerfect() {
        return hasRooms;
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {

    }

    @Override
    public void updateProgress(int percentage) {

    }
}
