package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import edu.wm.cs.cs301.RumitPatel.R;
import edu.wm.cs.cs301.RumitPatel.generation.Maze;

import android.util.Log;
import android.widget.Toast;

public class PlayManuallyActivity extends AppCompatActivity {
    private ToggleButton wholeMazeToggle;
    private ToggleButton solutionToggle;
    private ToggleButton visibleToggle;
    private Button zoomInButton;
    private Button zoomOutButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton forwardButton;
    private ImageButton jumpButton;
    private int startingDistToExit;
    Intent intent;
    private Button shortcut;
    private Maze maze;
    private MazePanel panel;

    private int clicks = 0;
    private String Logv = "Play Manually Activity: ";

    /**
     * Seeting up all buttons, ImageButtons, and toggles.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually);

        wholeMazeToggle = findViewById(R.id.wholeMazeToggle);

        /**
         * Listener for the toggle which toggles the Maze view on and off
         */
        wholeMazeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wholeMazeToggle.isChecked()){
                    Toast.makeText(PlayManuallyActivity.this, "Show Map: On",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Map On");
                }else {
                    Toast.makeText(PlayManuallyActivity.this, "Show Map: Off",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Map Off");
                }

            }

        });

        solutionToggle = findViewById(R.id.solutionToggle);

        /**
         * If clicked, will toggle the solution to maze on and off.
         */
        solutionToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (solutionToggle.isChecked()){
                    Toast.makeText(PlayManuallyActivity.this, "Show Solution: On",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Solution On");
                }else {
                    Toast.makeText(PlayManuallyActivity.this, "Show Solution: Off",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Solution Off");
                }

            }

        });

        visibleToggle = findViewById(R.id.visibleToggle);

        /**
         * If clicked, will toggle visible walls on and off.
         */
        visibleToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibleToggle.isChecked()){
                    Toast.makeText(PlayManuallyActivity.this, "Visible Walls: On",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Visible Walls: On");
                }else {
                    Toast.makeText(PlayManuallyActivity.this, "Visible Walls: Off",
                            Toast.LENGTH_SHORT).show();
                    Log.v(Logv, "Visible Walls: Off");
                }

            }
        });

        zoomInButton = findViewById(R.id.zoom_in);
        /**
         * if clicked, will zoom in to the maze.
         */
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "Zoom In",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Zooming In");
            }
        });


        zoomOutButton = findViewById(R.id.zoom_out);
        /**
         * if clicked, will zoom out of the maze.
         */
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "Zoom Out",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Zooming Out");
            }
        });


        panel = findViewById(R.id.shortcut);

        /**
         * if clicked, will take you to the winning screen.
         */
        /**
        shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "Going to Winning State",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Going to Winning State");
                Winning();
            }
        });
         */


        leftButton = findViewById(R.id.imageLeft);
        /**
         * If clicked, will turn you left.
         */
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "Going Left WOOOO!",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Going Left");
            }
        });

        rightButton = findViewById(R.id.imageRight);
        /**
         * if clicked, will turn you right.
         */
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "Going Right WOOOO!",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Going Right");
            }
        });

        forwardButton = findViewById(R.id.imageForward);
        /**
         * if clicked, will move you forward and increment "clicks"
         */
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks += 1;
                Toast.makeText(PlayManuallyActivity.this, "Going Forward WOOOO!",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Going Forward");
            }
        });

        jumpButton = findViewById(R.id.imageJump);
        /**
         * if clicked, will move you forward through an obstacle and increment "clicks"
         */
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks += 1;
                Toast.makeText(PlayManuallyActivity.this, "Jumping WOOOO!",
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Jumping");
            }
        });



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
     * takes user to the winning screen.
     */
    private void Winning() {
        intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Clicks",clicks);
        intent.putExtra("Starting Distance", startingDistToExit);
        startActivity(intent);
        finish();
    }


}
