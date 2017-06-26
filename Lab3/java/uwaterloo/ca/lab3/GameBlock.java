package uwaterloo.ca.lab3;


import android.content.Context;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class GameBlock extends android.support.v7.widget.AppCompatImageView {
    private final float IMAGE_SCALE = 0.70f;
    private GameLoopTask.Movement movement = GameLoopTask.Movement.NOMOVEMENT;
    private final float XMIN = 0;
    private final float XMAX = 1080;
    private final float YMIN = 0;
    private final float YMAX = 1080;
    private float velocity = 0;
    private final float acceleration = 20;
    private float currentX;
    private float currentY;
    private float nextX;
    private float nextY;

    private boolean isMoving;

    public GameBlock(Context context, int coordX, int coordY) {
        super(context);
        this.setImageResource(R.drawable.gameblock);
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        //change block to have centre be where scaled from instead of top left
        this.setPivotX(this.getWidth() / 2);
        this.setPivotY(this.getHeight() / 2);
        currentX = coordX;
        currentY = coordY;
        nextX = 0;
        nextY = 0;
        isMoving = false;
        movement = GameLoopTask.Movement.NOMOVEMENT;
        this.setX(currentX);
        this.setY(currentY);
    }


    public void setMovement(GameLoopTask.Movement movement) {
        if (!isMoving) {
            this.movement = movement;
            isMoving = true;
            Log.w(TAG, "setMovement: changing MOVEMENT" + movement + " " + isMoving);
            switch (movement) {
                case DOWN:
                    nextY = YMAX;
                    break;
                case UP:
                    nextY = YMIN;
                    break;
                case LEFT:
                    nextX = XMIN;
                    break;
                case RIGHT:
                    nextX = XMAX;
                    break;

                default:
                    break;
            }
        }
    }

    public void move() {
            if (movement == GameLoopTask.Movement.LEFT) {
                Log.d(TAG, "moving: LEFT");
                currentX -= velocity;
                velocity += acceleration;
                if (currentX < nextX) {
                    stopMoving();
                    return;
                }
                this.setX(currentX);
                return;
            } else if (movement == GameLoopTask.Movement.RIGHT) {
                Log.d(TAG, "moving: RIGHT");
                currentX += velocity;
                velocity += acceleration;
                if (currentX > nextX) {
                    stopMoving();
                    return;
                }
                this.setX(currentX);
                return;
            }
            if (movement == GameLoopTask.Movement.DOWN) {
                Log.d(TAG, "moving: DOWN");
                currentY += velocity;
                velocity += acceleration;
                if (currentY > nextY) {
                    stopMoving();
                    return;
                }
                this.setY(currentY);
                return;
            } else if (movement == GameLoopTask.Movement.UP) {
                Log.d(TAG, "moving: UP");
                currentY -= velocity;
                velocity += acceleration;
                if (currentY < nextY) {
                    stopMoving();
                    return;
                }
                this.setY(currentY);
                return;
            }
    }

    public void stopMoving() {
        currentX = nextX;
        currentY = nextY;
        this.setY(currentY);
        this.setX(currentX);
        Log.d(TAG, "move: HERE" + currentX + " " + currentY);
        velocity = 0;
        isMoving = false;
        movement = GameLoopTask.Movement.NOMOVEMENT;
    }


    public boolean isMoving() {
        return isMoving;
    }
}
