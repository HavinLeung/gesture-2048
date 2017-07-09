package lab2_203_12.uwaterloo.ca.lab2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.LinkedList;

/**
 * Created by havinleung on 2017-05-16.
 */

public class ThreeSensorEventListener implements SensorEventListener{
    private myFSM FSM;
    LinkedList<float[]> xyzarray = new LinkedList<>();
    float[] xyz_prev={0,0,0};
    float[] xyz_curr={0,0,0};
    float[] xyz_slope={0,0,0};
    private static final int FILTER_CONSTANT = 5;


    public ThreeSensorEventListener(myFSM y) {
        FSM = y;
    }


    public void onAccuracyChanged(Sensor s, int i) { }


    public void onSensorChanged(SensorEvent se) {
        for(int i=0;i<3;i++){
            xyz_prev[i] = xyz_curr[i]; //store previous values to calculate slope
            xyz_curr[i] = (se.values[i]-xyz_curr[i])/FILTER_CONSTANT;
            xyz_slope[i] = xyz_curr[i] - xyz_prev[i];
        }
        FSM.iterateFSM(xyz_curr,xyz_slope);
    }

}
