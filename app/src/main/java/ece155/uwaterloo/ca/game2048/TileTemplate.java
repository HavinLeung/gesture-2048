package ece155.uwaterloo.ca.game2048;

import android.annotation.TargetApi;

/**
 * Created by havinleung on 2017-07-09.
 */

public interface TileTemplate {
    int CORNER = 9;
    float IMAGE_SCALE = 0.75f;
    float TV_OFFSET_Y = 70;
    float TV_OFFSET_X = 125;
    int animateTime = 20;
    @TargetApi(12)
    void moveTile(int r, int c);
    void deleteTile();
}
