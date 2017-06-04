package lab1_203_12.uwaterloo.ca.lab2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.LinkedList;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by havinleung on 2017-05-16.
 */

public class ThreeSensorEventListener implements SensorEventListener{
    private LineGraphView graph;
    private myFSM FSM;
    LinkedList<float[]> xyzarray = new LinkedList<>();
    float[] xyz_prev={0,0,0};
    float[] xyz_curr={0,0,0};
    float[] xyz_slope={0,0,0};
    private static final int FILTER_CONSTANT = 5;


    public ThreeSensorEventListener(LineGraphView x,myFSM y) {
        graph = x;
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
        if(graph!=null){
            graph.addPoint(xyz_curr);
            if(xyzarray.size()>99){
                xyzarray.remove();
            }
            float[] xyz_copy = new float[3];
            System.arraycopy(xyz_curr,0,xyz_copy,0,3);
            xyzarray.add(xyz_copy);
        }

    }

}
