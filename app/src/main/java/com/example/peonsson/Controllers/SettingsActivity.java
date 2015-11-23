package com.example.peonsson.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by Peonsson on 2015-11-22.
 */
public class SettingsActivity extends Activity {

    Button saveButton;
    SeekBar seekBar;
    TextView seekBarTxt;
    Switch isSortedAZSwitch;

    private boolean isSortedAZ;

    private long timeInHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        seekBarTxt = (TextView)findViewById(R.id.seekBarTxt);
        isSortedAZSwitch = (Switch)findViewById(R.id.sortSwitch);
        isSortedAZ = getIntent().getBooleanExtra("isSortedAZ", false);
        timeInHours = getIntent().getLongExtra("timeInHours", -1);

        System.out.println("GOT SettingsActivity isSortedAZ: " + isSortedAZ);
        System.out.println("GOT SettingsActivity timeInHours: " + timeInHours);

        String display = timeInHours + " hours";
        seekBarTxt.setText(display);
        isSortedAZSwitch.setChecked(isSortedAZ);

        //seekBar
        Double d = timeInHours/0.24;
        int progress = d.intValue();
        seekBar = (SeekBar)findViewById(R.id.seekBarSwitch);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double hours = progress * 0.24;
                String time = String.format("%.0f", hours);
                seekBarTxt.setText(time + " hours");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //save button
        saveButton = (Button)findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                System.out.println("SENT SettingsActivity isSortedAZ: " + isSortedAZSwitch.isChecked());
                intent.putExtra("isSortedAZ", isSortedAZSwitch.isChecked());

                System.out.println("SENT SettingsActivity timeInHours: " + seekBar.getProgress() * 0.24);
                intent.putExtra("timeInHours", seekBar.getProgress() * 0.24);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
