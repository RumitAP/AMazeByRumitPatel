package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.RumitPatel.R;

public class WinningActivity extends AppCompatActivity {
    private String pathLength;
    private String shortestPath;
    private TextView Length;
    private TextView Path;
    private TextView textView;

    /**
     * UI for Winning activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);


        Intent getIntent = getIntent();
        pathLength = getIntent.getStringExtra("Path Length");
        shortestPath = getIntent.getStringExtra("Distance To Exit From Start");

        Path = findViewById(R.id.shortestPath1);
        Length = findViewById(R.id.pathLegnth1);

        Path.setText("Shortest Path: " + shortestPath);
        Length.setText("Your Path: " + pathLength);

        Snackbar mySnackBar = Snackbar.make(textView, "Pressing back button will let you restart.",
                30000);
        mySnackBar.show();



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
