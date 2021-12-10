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
    private StatePlaying statePlaying = new StatePlaying();

    /**
     * Seeting up all buttons, ImageButtons, and toggles.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually);

        panel = findViewById(R.id.shortcut);
        statePlaying.setMazeConfiguration(GeneratingActivity.maze);
        statePlaying.setPlayManual(this);
        statePlaying.start(panel);

        wholeMazeToggle = findViewById(R.id.wholeMazeToggle);

        /**
         * Listener for the toggle which toggles the Maze view on and off
         */
        wholeMazeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wholeMazeToggle.isChecked()){
                    //statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, 0);
                    Log.v(Logv, "Map On");
                }else {
                    //statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, 0);
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
                    //statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLESOLUTION, 0);
                    Log.v(Logv, "Solution On");
                }else {
                    //statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    statePlaying.keyDown(Constants.UserInput.TOGGLESOLUTION, 0);
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
                    statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
                    Log.v(Logv, "Visible Walls: On");
                }else {
                    statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, 0);
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
                statePlaying.keyDown(Constants.UserInput.ZOOMIN, 0);

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
                statePlaying.keyDown(Constants.UserInput.ZOOMOUT, 0);

            }
        });

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
                statePlaying.keyDown(Constants.UserInput.LEFT, 0);
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
                statePlaying.keyDown(Constants.UserInput.RIGHT, 0);
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
                statePlaying.keyDown(Constants.UserInput.UP, 0);
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
                statePlaying.keyDown(Constants.UserInput.JUMP, 0);
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
    public void Winning() {
        intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Clicks",clicks);
        intent.putExtra("Starting Distance", startingDistToExit);
        startActivity(intent);
        finish();
    }


}
