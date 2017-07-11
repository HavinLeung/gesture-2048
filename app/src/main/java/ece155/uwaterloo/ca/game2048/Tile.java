package ece155.uwaterloo.ca.game2048;

import android.annotation.TargetApi;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by havinleung on 2017-07-09.
 */

class Tile extends android.support.v7.widget.AppCompatImageView implements TileTemplate{
    private final int CORNER = 9;
    private final float IMAGE_SCALE = 0.75f;
    private final float TV_OFFSET_Y = 70;
    private final float TV_OFFSET_X = 85;
    private final int animateTime = 40;
    protected int value;
    private int currentX;
    private int currentY;
    private TextView valueTV;
    protected boolean merged;
    private RelativeLayout layout;
    @TargetApi(12)
    public Tile(int x, int r, int c, RelativeLayout z){
        super(z.getContext());
        layout = z;
        merged = false;
        value = x;
        this.setImageResource(R.drawable.gameblock);
        currentX = CORNER+360*c;
        currentY = CORNER+360*r;
        this.setScaleX(0.1f);
        this.setScaleY(0.1f);
        this.setPivotX(this.getWidth() / 2);
        this.setPivotY(this.getHeight() / 2);
        this.setX(currentX);
        this.setY(currentY);
        layout.addView(this);
        //animation when created
        this.animate().scaleX(IMAGE_SCALE);
        this.animate().scaleY(IMAGE_SCALE);
        //child textview
        valueTV = new TextView(layout.getContext());
        valueTV.setGravity(Gravity.CENTER);
        valueTV.setX(currentX + TV_OFFSET_X);
        valueTV.setY(currentY + TV_OFFSET_Y);
        valueTV.setTextSize(40);
        valueTV.setText(String.format("%d",value));
        layout.addView(valueTV);
        valueTV.bringToFront();
    }
    @TargetApi(12)
    public void moveTile(int r, int c){
        if(currentX == CORNER+360*c && currentY == CORNER+360*r) return; //no movement
        currentX = CORNER+360*c;
        currentY = CORNER+360*r;
        this.animate().translationX(currentX).setDuration(animateTime);
        this.animate().translationY(currentY).setDuration(animateTime);
        valueTV.animate().translationX(currentX + TV_OFFSET_X).setDuration(animateTime);
        valueTV.animate().translationY(currentY + TV_OFFSET_Y).setDuration(animateTime);
    }
    void doubleValue(){
        value = value*2;
        valueTV.setText(String.format("%d",value));
        merged = true;
    }
    public void deleteTile(){
        layout.removeView(valueTV);
        layout.removeView(this);
    }
}
