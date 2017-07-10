package lab2_203_12.uwaterloo.ca.lab2;

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
    private final float TV_OFFSET_X = 125;
    private final int animateTime = 20;
    protected int value;
    private int currentX;
    private int currentY;
    private TextView TV;
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
        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.setPivotX(this.getWidth() / 2);
        this.setPivotY(this.getHeight() / 2);
        this.setX(currentX);
        this.setY(currentY);
        layout.addView(this);
        //child textview
        TV = new TextView(layout.getContext());
        TV.setGravity(Gravity.CENTER);
        TV.setX(currentX + TV_OFFSET_X);
        TV.setY(currentY + TV_OFFSET_Y);
        TV.setTextSize(40);
        TV.setText(String.format("%d",value));
        layout.addView(TV);
        TV.bringToFront();
    }
    @TargetApi(12)
    public void moveTile(int r, int c){
        this.animate().translationX(c*360).setDuration(animateTime);
        this.animate().translationY(r*360).setDuration(animateTime);
        TV.animate().translationX(c*360+TV_OFFSET_X).setDuration(animateTime);
        TV.animate().translationY(r*360+TV_OFFSET_Y).setDuration(animateTime);
    }
    void doubleValue(){
        value = value*2;
        TV.setText(String.format("%d",value));
        merged=true;
    }
    public void deleteTile(){
        layout.removeView(TV);
        layout.removeView(this);
    }
}
