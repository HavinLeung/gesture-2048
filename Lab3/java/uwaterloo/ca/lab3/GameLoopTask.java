package uwaterloo.ca.lab3;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class GameLoopTask extends TimerTask {

    public enum Movement {UP, DOWN, LEFT, RIGHT, NOMOVEMENT};
    private int secondsElapsed = 0;
    private Activity activity;
    private RelativeLayout relativeLayout;
    private Context context;
    private Movement movement = Movement.NOMOVEMENT;

    public GameBlock getGameBlock() {
        return gameBlock;
    }

    private GameBlock gameBlock;

    public GameLoopTask(Activity activity, RelativeLayout relativeLayout, Context context) {
        this.activity = activity;
        this.relativeLayout = relativeLayout;
        this.context = context;
        createBlock();
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (secondsElapsed % 20 == 0) {
                    Log.d(TAG, "run: " + secondsElapsed / 20);
                }
                secondsElapsed++;
                gameBlock.move();
            }
        });
    }

    private void createBlock() {
        gameBlock = new GameBlock(context, 0, 0);
        relativeLayout.addView(gameBlock);
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
        gameBlock.setMovement(this.movement);
        Log.d(TAG, "setMovement: " + this.movement);
    }
}
