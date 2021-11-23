package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SeekBar;
import android.widget.Toast;
import android.view.View;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.cs301.RumitPatel.R;

public class AMazeActivity extends AppCompatActivity {
    private Button Explore;
    private Button Revisit;
    private int level;
    private String str_level;
    private String builder;
    private String rooms;
    private String Logv = "AMazeActivity";

    /**
     * this is from the slides. It sets the driver, builder, and level.
     * @param savedInstanceState
     */

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        Spinner spinner = (Spinner) findViewById(R.id.builderSpinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.builder, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Spinner spinner2 = (Spinner) findViewById(R.id.roomSpinner);
        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.rooms, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        SeekBar seekbar = findViewById(R.id.levelSeekBar);
        seekbar.setMax(9);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int difficultyChange = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                difficultyChange = progress;
                level = difficultyChange;
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
                Toast.makeText(AMazeActivity.this, "Level is :" + difficultyChange,
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv, "Level is:" + difficultyChange);
                level = difficultyChange;
            }
        }
        );

        Explore = findViewById(R.id.explorebutton);
        Revisit = findViewById(R.id.revisitbutton);

        /**
         * generate new maze when "explore button" is clicked
         */
        Explore.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                level = seekbar.getProgress();
                str_level = String.valueOf(level);
                builder = spinner.getSelectedItem().toString();
                rooms = spinner2.getSelectedItem().toString();

                Toast.makeText(AMazeActivity.this, "Builder: " +  builder +
                        "/nLevel: " +  str_level + "/nRooms?: " +  rooms,
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv,"Exploring");

                explore();
            }
        });
        /**
         * for project 6, proceed the same way as Explore per the instructions
         */
        Revisit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                level = seekbar.getProgress();
                str_level = String.valueOf(level);
                builder = spinner.getSelectedItem().toString();
                rooms = spinner2.getSelectedItem().toString();

                Toast.makeText(AMazeActivity.this, "Builder: " +  builder +
                                "/nLevel: " +  str_level + "/nRooms?: " +  rooms,
                        Toast.LENGTH_SHORT).show();
                Log.v(Logv,"Revisiting");

                revisit();
            }
        });

    }

    /**
     * pass information to Generating class.
     * https://developer.android.com/guide/components/intents-filters#:~:text=An%20Intent%20object%20carries%20information,action%20to%20take%20and%20the
     */
    public void explore() {
        Intent intent = new Intent(this, GeneratingActivity.class);
        intent.putExtra("Builder", builder);
        intent.putExtra("level", str_level);
        intent.putExtra("Rooms", rooms);
        intent.putExtra("Mode","explore");
        Toast.makeText(AMazeActivity.this, "Exploring",
                Toast.LENGTH_SHORT).show();
        Log.v(Logv, "Creating new maze.");
        startActivity(intent);

    }

    /**
     * For Project 6, proceed the same way as explore per his instructions.
     * https://developer.android.com/guide/components/intents-filters#:~:text=An%20Intent%20object%20carries%20information,action%20to%20take%20and%20the
     */
    public void revisit() {
        Intent intent = new Intent(this, GeneratingActivity.class);
        intent.putExtra("Builder", builder);
        intent.putExtra("Level", str_level);
        intent.putExtra("Rooms", rooms);
        intent.putExtra("Mode","explore");
        Toast.makeText(AMazeActivity.this, "Revisiting",
                Toast.LENGTH_SHORT).show();
        Log.v(Logv, "Loading old maze.");
        startActivity(intent);
    }


}
