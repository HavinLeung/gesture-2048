package lab2_203_12.uwaterloo.ca.lab2;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

/**
 * Created by havinleung on 2017-07-09.
 */

public class Game2048 {
    //variables
    private RelativeLayout layout;
    protected enum gameState{Win,Lose,Play}
    private TextView myTV;
    static int currentHigh;
    final static int targetHigh = 256;
    final static int side = 4;
    static gameState state = gameState.Play;
    static boolean checkingMoves = false;
    private static Tile[][] tiles;
    private static Random rand = new Random();


    public Game2048(TextView x, RelativeLayout y){
        myTV = x;
        layout = y;
        startGame();
    }

    void startGame(){
        state = gameState.Play;
        tiles = new Tile[side][side];
        addRandomTile();
        addRandomTile();
    }

    private boolean move(int startTile, int incrementY, int incrementX){
        if(state != gameState.Play) return false;
        boolean moved = false;
        for(int i = 0; i < side*side ; i++){
            int j = Math.abs(startTile - i);
            int row = j / side;
            int col = j % side;
            int nextRow = row + incrementY;
            int nextCol = col + incrementX;
            if(tiles[row][col]==null) continue; //skip iteration because nothing to compare with
            while(nextCol >= 0 && nextCol <=3 && nextRow >= 0 && nextRow <= 3){//no out of bounds
                Tile current = tiles[row][col];
                Tile adjacent = tiles[nextRow][nextCol];
                if(adjacent == null){ //adjacent can move to current position
                    if (checkingMoves) return true;

                    tiles[nextRow][nextCol] = current;
                    tiles[row][col] = null;
                    //shift comparison to next two blocks
                    row = nextRow;
                    col = nextCol;
                    nextRow += incrementY;
                    nextCol += incrementX;
                    moved = true;
                }else if(current.value == adjacent.value){
                    if (checkingMoves) return true;

                    //can be merged
                    tiles[nextRow][nextCol].doubleValue();
                    tiles[row][col].deleteTile();
                    tiles[row][col] = null;
                    if(current.value > currentHigh) currentHigh = current.value;
                    moved = true;
                    break;
                }else{
                    break;
                }
            }
        }
        if(moved){
            if(currentHigh == targetHigh){
                state = gameState.Win;
            }else{
                addRandomTile();
                if(!movesAvailable()){
                    state = gameState.Lose;
                }
            }
        }
        return moved;
    }
    public boolean moveUp(){
        return move(0, -1, 0);
    }
    public boolean moveDown(){
        return move(side*side - 1, 1, 0);
    }
    public boolean moveLeft(){
        return move(0, 0, -1);
    }
    public boolean moveRight(){
        return move(side*side - 1, 0, 1);
    }
    private void addRandomTile(){
        int pos = rand.nextInt(side*side-1);
        int row = pos / side;
        int col = pos % side;
        //if we randomly selected a filled grid...
        while(tiles[row][col]!=null){
            pos = (pos + 1) % (side*side);
            row = pos / side;
            col = pos % side;
        }
        int randomValue = (Math.random() < 0.9) ? 2 : 4; //90% chance of getting a 2
        tiles[row][col] = new Tile(randomValue,row,col);
        currentHigh = (randomValue > currentHigh) ? randomValue : currentHigh;
    }
    private boolean movesAvailable(){
        checkingMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingMoves = false;
        return hasMoves;
    }

    public void drawGraphics(){
        if (state == gameState.Play) {
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] != null) {
                        tiles[r][c].moveTile(r, c);
                    }
                }
            }
        }else if(state == gameState.Lose){
            myTV.setText("LOSE");
            myTV.bringToFront();
        }else{
            myTV.setText("WIN");
            myTV.bringToFront();
        }
    }

    private class Tile extends android.support.v7.widget.AppCompatImageView{
        private final int CORNER = 9;
        private final float IMAGE_SCALE = 0.75f;
        private final float TV_OFFSET_Y = 70;
        private final float TV_OFFSET_X = 125;
        private final int animateTime = 30;
        private int value;
        private int currentX;
        private int currentY;
        private TextView TV;
        @TargetApi(12)
        public Tile(int x, int r, int c){
            super(layout.getContext());
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
        private void moveTile(int r, int c){
            this.animate().translationX(c*360).setDuration(animateTime);
            this.animate().translationY(r*360).setDuration(animateTime);
            TV.animate().translationX(c*360+TV_OFFSET_X).setDuration(animateTime);
            TV.animate().translationY(r*360+TV_OFFSET_Y).setDuration(animateTime);
        }
        private void doubleValue(){
            value = value*2;
            TV.setText(String.format("%d",value));
        }
        private void deleteTile(){
            layout.removeView(TV);
            layout.removeView(this);
        }

    }
}
