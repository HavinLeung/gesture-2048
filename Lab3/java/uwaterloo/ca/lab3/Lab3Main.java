package uwaterloo.ca.lab3;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;


public class Lab3Main extends AppCompatActivity {

private int GAMEBOARD_DIMENSION = 1440;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_main);
        //line taken from Arunkumar on stackoverflow
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        RelativeLayout x = (RelativeLayout) (findViewById(R.id.layout));
        x.getLayoutParams().height = GAMEBOARD_DIMENSION;
        x.getLayoutParams().width = GAMEBOARD_DIMENSION;
        x.setBackgroundResource(R.drawable.gameboard);
        //initialize all labels
        final TextView gestureName = new TextView(getApplicationContext());
        gestureName.setTextSize(42);
        //get sensormanager
        SensorManager sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);

        //start timer here
        GameLoopTask gameLoopTask = new GameLoopTask(this, x, getApplicationContext());
        Timer gameLoop = new Timer();
        gameLoop.schedule(gameLoopTask, 50, 50);

        //create FSM object
        myFSM FSM = new myFSM(gestureName, gameLoopTask);

        //accelerometer sensor
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final ThreeSensorEventListener l = new ThreeSensorEventListener(FSM);
        sensorManager.registerListener(l, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
        x.addView(gestureName);
    }
}
