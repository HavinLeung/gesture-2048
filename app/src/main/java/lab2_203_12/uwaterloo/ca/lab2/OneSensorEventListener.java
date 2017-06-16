package lab2_203_12.uwaterloo.ca.lab2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/**
 * Created by havinleung on 2017-05-16.
 */

public class OneSensorEventListener implements SensorEventListener{
    private TextView output;
    private TextView record;
    private double currentmax;

    public OneSensorEventListener(TextView outputView, TextView record) {
        output = outputView;
        this.record = record;
        currentmax =0;

    }
    public void resetRecord(){
        currentmax=0;
        record.setText(String.format("(0)"));
    }

    public void onAccuracyChanged(Sensor s, int i) { }

    public void onSensorChanged(SensorEvent se) {
        float x=se.values[0];

        String current = String.format("(%.2f)",x);
        output.setText(current);
        if(x>currentmax){
            record.setText(current);
            currentmax = x;

        }
    }

}
