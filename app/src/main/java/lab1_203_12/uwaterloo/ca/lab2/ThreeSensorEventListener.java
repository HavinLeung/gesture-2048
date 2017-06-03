package lab1_203_12.uwaterloo.ca.lab2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import java.util.LinkedList;

import ca.uwaterloo.sensortoy.LineGraphView;

/**
 * Created by havinleung on 2017-05-16.
 */

public class ThreeSensorEventListener implements SensorEventListener{
    private TextView output;
    private TextView record;
    private double currentmax;
    private LineGraphView graph;
    public LinkedList<float[]> xyzarray = new LinkedList<>();

    public ThreeSensorEventListener(TextView outputView, TextView record) {
        output = outputView;
        this.record = record;
        currentmax =0;

    }

    public ThreeSensorEventListener(TextView outputView, TextView record, LineGraphView x) {
        output = outputView;
        this.record = record;
        currentmax =0;
        graph=x;
    }
    public void resetRecord(){
        currentmax=0;
        record.setText("(0,0,0)");
    }

    public void onAccuracyChanged(Sensor s, int i) { }


    public void onSensorChanged(SensorEvent se) {
        float x=se.values[0];
        float y=se.values[1];
        float z=se.values[2];
        float[] xyz = {x,y,z};
        double max = Math.sqrt(x*x+y*y+z*z);
         if(graph!=null){
             graph.addPoint(se.values);
             if(xyzarray.size()>99){
                 xyzarray.remove();
             }
             xyzarray.add(xyz);

         }
        String current = String.format("(%.2f,%.2f,%.2f)",x,y,z);
        output.setText(current);
        if(max>currentmax){
            record.setText(current);
            currentmax = max;

        }
    }

}
