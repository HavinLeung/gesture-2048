package lab1_203_12.uwaterloo.ca.lab2;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;

public class MainActivity extends AppCompatActivity {
    LineGraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //line taken from Arunkumar on stackoverflow
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        LinearLayout x = (LinearLayout) (findViewById(R.id.layout));

        //initialize graph
        graph = new LineGraphView( getApplicationContext(), 100, Arrays.asList("x", "y", "z"));

        graph.setVisibility(View.VISIBLE);

        //initialize all labels
        final TextView CSVDIR = new TextView(getApplicationContext());

        TextView AccelerometerLabel = new TextView(getApplicationContext());
        AccelerometerLabel.setText("The Accelerometer reading is:");
        TextView AccelerometerReading = new TextView(getApplicationContext());
        TextView AccelerometerRecordLabel = new TextView(getApplicationContext());
        AccelerometerRecordLabel.setText("The Record-High Accelerometer reading is:");
        TextView AccelerometerRecord = new TextView(getApplicationContext());

        SensorManager sensorManager =(SensorManager) getSystemService(SENSOR_SERVICE);


        //accelerometer sensor
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final ThreeSensorEventListener l = new ThreeSensorEventListener(AccelerometerReading,AccelerometerRecord,graph);
        sensorManager.registerListener(l, accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);

        //csv file button
        Button createCSV = new Button(getApplicationContext());

        createCSV.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                Date date = new Date();

                File dir = getExternalFilesDir(null);
                File file = new File(dir,dateFormat.format(date)+".csv");
                try {
                    PrintWriter writer = new PrintWriter(file);
                    CSVDIR.setText(file.getAbsolutePath());
                    int max = l.xyzarray.size();
                    float[] xyz;
                    for(int i=0;i<max;i++){
                        xyz=l.xyzarray.get(i); //get array, print into CSV file
                        writer.println(String.format("%f,%f,%f",xyz[0],xyz[1],xyz[2]));
                    }
                    writer.close();
                }catch(IOException e){
                    Log.e("CSV ERROR",e.getMessage());
                    CSVDIR.setText("FAILED!");
                }
            }
        });

        createCSV.setText("Create CSV");
        //Button to quit
        Button quit = new Button(getApplicationContext());
        quit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                System.exit(0);
            }
        });
        quit.setText("QUIT");


        //Button to clear high
        Button clearRecord = new Button(getApplicationContext());
        clearRecord.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                l.resetRecord();
            }
        });
        clearRecord.setText("Clear Record-High Numbers");

        //all the addView
        x.addView(CSVDIR);
        x.addView(graph);
        x.addView(createCSV);
        x.addView(clearRecord);
        x.addView(quit);
        x.addView(AccelerometerLabel);
        x.addView(AccelerometerReading);
        x.addView(AccelerometerRecordLabel);
        x.addView(AccelerometerRecord);





    }
}
