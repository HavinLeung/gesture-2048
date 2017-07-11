package ece155.uwaterloo.ca.game2048;

import android.widget.TextView;

/**
 * Created by havinleung on 2017-06-04.
 */

public class myFSM {
    //class  variables
    private TextView myTV;
    private Game2048 game;

    private enum possible_Gestures{LEFT,RIGHT,UP,DOWN, NONE};
    private enum FSM_States{WAIT,RISE,PEAK,TROUGH,STABLE,RECOGNIZED};
    private FSM_States state = FSM_States.WAIT; //initial state is "wait"
    private possible_Gestures gesture = possible_Gestures.NONE;
    private boolean wait=false;
    private int waitCounter = 0;
    private int counter = 0;
    private static final int COUNTER_LIMIT = 100;
    private static final double[] THRESHOLD_VALUES ={1.0,1.2,0.5,0.4}; //thresholds for RISE,PEAK,DETERMINED



    //class constructor
    myFSM(TextView x, Game2048 y){
        myTV =x;
        game=y;
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
    private void waitCheck(){
        if(waitCounter<=10){
            waitCounter++;
        }
        else{
            wait = false;
            waitCounter = 0;
        }
    }

    //call iterateFSM per sensorEventChange. Pass in current value and slope value
    protected void iterateFSM(float[] current, float[] slope){
        switch (state){
            case WAIT:
                if(wait){
                    waitCheck();
                    break;
                }
                if(Math.abs(current[0])>THRESHOLD_VALUES[0]){
                    state = FSM_States.RISE;
                    gesture = (current[0]>0) ? possible_Gestures.RIGHT : possible_Gestures.LEFT ;
                }
                if(Math.abs(current[2])>THRESHOLD_VALUES[0]){
                    state = FSM_States.RISE;
                    gesture = (current[2]>0) ? possible_Gestures.UP : possible_Gestures.DOWN ;
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
                    if (Math.abs(current[2]) > THRESHOLD_VALUES[1]) {
                        state = FSM_States.PEAK;
                    }
                    break;
                }
                break;


            case PEAK:
                counterCheck();
                switch (gesture){
                    case UP:
//                        if(slope[1]<0){
//                            state=FSM_States.TROUGH;
//                        }
//                        break;
                    case DOWN:
//                        if(slope[1]>0){
//                            state=FSM_States.TROUGH;
//                        }
                        if(Math.abs(current[2])<THRESHOLD_VALUES[1]){
                            state=FSM_States.TROUGH;
                        }
                        break;
                    case RIGHT:
//                        if(slope[0]<0){
//                            state=FSM_States.TROUGH;
//                        }
//                        break;
                    case LEFT:
//                        if(slope[0]>0){
//                            state=FSM_States.TROUGH;
//                        }
                        if(Math.abs(current[0])<THRESHOLD_VALUES[1]){
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
                       if (Math.abs(current[2]) > THRESHOLD_VALUES[2]) {
                            state = FSM_States.STABLE;
                }
                        break;
                }
                break;


            case STABLE:
                counterCheck();
                switch(gesture) {
                    case UP:
                    case DOWN:
                    if (Math.abs(current[2]) < THRESHOLD_VALUES[3]) {
                        state = FSM_States.RECOGNIZED;
                    }
                    case LEFT:
                    case RIGHT:
                        if (Math.abs(current[0]) < THRESHOLD_VALUES[3]) {
                            state = FSM_States.RECOGNIZED;
                        }
                }
                break;


            case RECOGNIZED:
                switch (gesture){
                    case LEFT:
                        myTV.setText("Gesture: LEFT");
                        game.moveLeft();
                        break;
                    case RIGHT:
                        myTV.setText("Gesture: RIGHT");
                        game.moveRight();
                        break;
                    case UP:
                        myTV.setText("Gesture: UP");
                        game.moveUp();
                        break;
                    case DOWN:
                        myTV.setText("Gesture: DOWN");
                        game.moveDown();
                        break;
                }
                game.drawGraphics();
                myTV.bringToFront();
                counter=0;
                gesture = possible_Gestures.NONE;
                state = FSM_States.WAIT;
                wait = true;
                break;
        }
    }
}
