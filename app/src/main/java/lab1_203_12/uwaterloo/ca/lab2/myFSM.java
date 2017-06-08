package lab1_203_12.uwaterloo.ca.lab2;

import android.widget.TextView;

/**
 * Created by havinleung on 2017-06-04.
 */

public class myFSM {
    //class  variables
    private TextView myTV;

    private enum possible_Gestures{LEFT,RIGHT,UP,DOWN, NONE};
    private enum FSM_States{WAIT,RISE,PEAK,TROUGH,STABLE,RECOGNIZED};
    private FSM_States state = FSM_States.WAIT; //initial state is "wait"
    private possible_Gestures gesture = possible_Gestures.NONE;

    private int counter = 0;
    private static final int COUNTER_LIMIT = 50;
    private static final double[] THRESHOLD_VALUES ={0.6,1,0.8,0.2}; //thresholds for RISE,PEAK,DETERMINED



    //class constructor
    myFSM(TextView x){
        myTV =x;
    }
    //class functions
    private void counterCheck(){
        counter++;
        if(counter>=COUNTER_LIMIT){//reset if at limit
            state = FSM_States.WAIT;
            gesture = possible_Gestures.NONE;
            counter =0;
        }
    }

    //call iterateFSM per sensorEventChange. Pass in current value and slope value
    protected void iterateFSM(float[] current, float[] slope){
        switch (state){
            case WAIT:
                if(Math.abs(current[0])>THRESHOLD_VALUES[0]){
                    state = FSM_States.RISE;
                    gesture = (slope[0]>0) ? possible_Gestures.RIGHT : possible_Gestures.LEFT ;
                }
                if(Math.abs(current[1])>THRESHOLD_VALUES[0]){
                    state = FSM_States.RISE;
                    gesture = (slope[1]>0) ? possible_Gestures.UP : possible_Gestures.DOWN ;
                }
                break;


            case RISE:
                counterCheck();
                switch(gesture) {
                    case LEFT:
                    case RIGHT:
                    if (Math.abs(current[0]) > THRESHOLD_VALUES[1]) {
                        state = FSM_States.PEAK;
                    }
                    break;

                    case UP:
                    case DOWN:
                    if (Math.abs(current[1]) > THRESHOLD_VALUES[1]) {
                        state = FSM_States.PEAK;
                    }
                    break;
                }
                break;


            case PEAK:
                counterCheck();
                switch (gesture){
                    case UP:
                        if(slope[1]<0){
                            state=FSM_States.TROUGH;
                        }
                        break;
                    case DOWN:
                        if(slope[1]>0){
                            state=FSM_States.TROUGH;
                        }
                        break;
                    case RIGHT:
                        if(slope[0]<0){
                            state=FSM_States.TROUGH;
                        }
                        break;
                    case LEFT:
                        if(slope[0]>0){
                            state=FSM_States.TROUGH;
                        }
                        break;
                }
                break;


            case TROUGH:
                counterCheck();
                switch(gesture) {
                    case LEFT:
                    case RIGHT:
                        if (Math.abs(current[0]) > THRESHOLD_VALUES[2]) {
                            state = FSM_States.STABLE;
                        }
                        break;

                    case UP:
                    case DOWN:
                        if (Math.abs(current[1]) > THRESHOLD_VALUES[2]) {
                            state = FSM_States.STABLE;
                        }
                        break;
                }
                break;


            case STABLE:
                counterCheck();
                if(Math.abs(current[1])<THRESHOLD_VALUES[3]){
                    state=FSM_States.RECOGNIZED;
                }
                break;


            case RECOGNIZED:
                switch (gesture){
                    case LEFT:
                        myTV.setText("Gesture: LEFT");
                        break;
                    case RIGHT:
                        myTV.setText("Gesture: RIGHT");
                        break;
                    case UP:
                        myTV.setText("Gesture: UP");
                        break;
                    case DOWN:
                        myTV.setText("Gesture: DOWN");
                        break;
                }
                counter=0;
                gesture = possible_Gestures.NONE;
                state = FSM_States.WAIT;
                break;
        }
    }
}
