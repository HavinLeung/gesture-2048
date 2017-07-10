package lab2_203_12.uwaterloo.ca.lab2;

import android.app.Activity;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by havinleung on 2017-07-09.
 */

public class Game2048 {
    //variables
    private Activity mainactivity;
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


    public Game2048(Activity mainactivity, TextView x, RelativeLayout y){
        this.mainactivity = mainactivity;
        myTV = x;
        layout = y;
        startGame();
    }

    void startGame(){
        currentHigh = 0;
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
                final Tile current = tiles[row][col];
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

                    if(adjacent.merged) break;
                    //can be merged
                    tiles[nextRow][nextCol].doubleValue();
                    tiles[row][col].moveTile(nextRow,nextCol);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current.deleteTile();
                        }
                    }, 60); // after 60 ms
                    tiles[row][col] = null;
                    if(tiles[nextRow][nextCol].value > currentHigh) currentHigh = tiles[nextRow][nextCol].value;
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
        resetMerged();
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
        tiles[row][col] = new Tile(randomValue,row,col,layout);
        currentHigh = (randomValue > currentHigh) ? randomValue : currentHigh;
    }
    private boolean movesAvailable(){
        checkingMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingMoves = false;
        return hasMoves;
    }

    public void drawGraphics(){
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] != null) {
                        tiles[r][c].moveTile(r, c);
                    }
                }
            }
        if(state == gameState.Lose){
            myTV.setText("LOSE");
            myTV.bringToFront();
        }else if(state == gameState.Win){
            myTV.setText("WIN");
            myTV.bringToFront();
        }
    }
    private void resetMerged(){
        for(int i = 0; i<side ;i++){
            for(int j = 0; j<side; j++){
                if(tiles[i][j]!=null)tiles[i][j].merged=false;
            }
        }
    }

}
