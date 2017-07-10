package lab2_203_12.uwaterloo.ca.lab2;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int GAMEBOARD_DIMENSION = 1440;

    @Override @TargetApi(12)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //line taken from Arunkumar on stackoverflow
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        RelativeLayout x = (RelativeLayout) (findViewById(R.id.layout));
        x.getLayoutParams().height = GAMEBOARD_DIMENSION;
        x.getLayoutParams().width = GAMEBOARD_DIMENSION;
        x.setBackgroundResource(R.drawable.gameboard);

        //initialize all labels
        final TextView gestureName = new TextView(getApplicationContext());
        gestureName.setTextSize(32);

        final TextView gameState = new TextView(getApplicationContext());
        gameState.setTextSize(120);
        gameState.setTextColor(Color.RED);
        //get sensormanager
        SensorManager sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);

        //create game object
        final Game2048 game = new Game2048(this,gameState,x);

        //create FSM object
        myFSM FSM = new myFSM(gestureName,game);

        //accelerometer sensor
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final ThreeSensorEventListener l = new ThreeSensorEventListener(FSM);
        sensorManager.registerListener(l, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);




        //all the addView
        x.addView(gestureName);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        p.leftMargin = 200; // in PX
        p.topMargin = 200; // in PX
        gameState.setLayoutParams(p);
        x.addView(gameState);

        //create up/down/left/right buttons for debugging
//        Button buttonUP = new Button(getApplicationContext());
//
//        buttonUP.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                game.moveUp();
//            }
//        });
//        buttonUP.setText("UP");
//        buttonUP.setY(1450);
//        x.addView(buttonUP);




    }
}
//TODO: refactor variables names
//TODO: make block child textview better centered
