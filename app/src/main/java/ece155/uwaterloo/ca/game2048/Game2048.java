package ece155.uwaterloo.ca.game2048;

import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by havinleung on 2017-07-09.
 */

public class Game2048 {
    private RelativeLayout layout;
    private enum gameState{WIN, LOSE, PLAY}
    private TextView myTV;
    private static int currentHigh;
    private final static int targetHigh = 2048;
    private final static int sideLength = 4;
    private static gameState state = gameState.PLAY;
    private static boolean checkingMoves = false;
    private static Tile[][] TileArray;
    private static Random randomInt = new Random();


    public Game2048(TextView x, RelativeLayout y){
        myTV = x;
        layout = y;
        startGame();
    }

    private void startGame(){
        currentHigh = 0;
        state = gameState.PLAY;
        TileArray = new Tile[sideLength][sideLength];
        addRandomTile();
        addRandomTile();
    }
//    private void restartGame(){
//        //TODO: tile cleanup
//        startGame();
//    }


    private boolean move(int startTile, int incrementY, int incrementX){
        if(state != gameState.PLAY) return false;
        boolean moved = false;
        for(int i = 0; i < sideLength * sideLength; i++){
            int j = Math.abs(startTile - i);
            int row = j / sideLength;
            int col = j % sideLength;
            int nextRow = row + incrementY;
            int nextCol = col + incrementX;

            if(TileArray[row][col]==null) continue; //skip iteration because nothing to compare with

            while(nextCol >= 0 && nextCol <=3 && nextRow >= 0 && nextRow <= 3){//no out of bounds
                final Tile current = TileArray[row][col];
                Tile adjacent = TileArray[nextRow][nextCol];
                if(adjacent == null){ //adjacent can move to current position
                    if (checkingMoves) return true;

                    TileArray[nextRow][nextCol] = current;
                    TileArray[row][col] = null;
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
                    TileArray[nextRow][nextCol].doubleValue();
                    TileArray[row][col].moveTile(nextRow,nextCol);
                    final Handler handler = new Handler();

                    //finish animation before deleting tile
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current.deleteTile();
                        }
                    }, 60); // after 60 ms

                    TileArray[row][col] = null;
                    if(TileArray[nextRow][nextCol].value > currentHigh) currentHigh = TileArray[nextRow][nextCol].value;
                    moved = true;
                    break;
                }else{ //cannot be merged
                    break;
                }
            }
        }
        if(moved){
            if(currentHigh == targetHigh){
                state = gameState.WIN;
            }else{
                addRandomTile();
                if(!movesAvailable()){
                    state = gameState.LOSE;
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
        return move(sideLength * sideLength - 1, 1, 0);
    }
    public boolean moveLeft(){
        return move(0, 0, -1);
    }
    public boolean moveRight(){
        return move(sideLength * sideLength - 1, 0, 1);
    }
    private void addRandomTile(){
        int pos = randomInt.nextInt(sideLength * sideLength -1);
        int row = pos / sideLength;
        int col = pos % sideLength;
        //if we randomly selected a filled grid...
        while(TileArray[row][col]!=null){
            pos = (pos + 1) % (sideLength * sideLength);
            row = pos / sideLength;
            col = pos % sideLength;
        }
        int randomValue = (Math.random() < 0.9) ? 2 : 4; //90% chance of getting a 2
        TileArray[row][col] = new Tile(randomValue,row,col,layout);
        currentHigh = (randomValue > currentHigh) ? randomValue : currentHigh;
    }
    private boolean movesAvailable(){
        checkingMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingMoves = false;
        return hasMoves;
    }

    public void drawGraphics(){
            for (int r = 0; r < sideLength; r++) {
                for (int c = 0; c < sideLength; c++) {
                    if (TileArray[r][c] != null) {
                        TileArray[r][c].moveTile(r, c);
                    }
                }
            }
        if(state == gameState.LOSE){
            myTV.setText("LOSE");
            myTV.bringToFront();
        }else if(state == gameState.WIN){
            myTV.setText("WIN");
            myTV.bringToFront();
        }
    }
    private void resetMerged(){
        for(int i = 0; i< sideLength; i++){
            for(int j = 0; j< sideLength; j++){
                if(TileArray[i][j]!=null) TileArray[i][j].merged=false;
            }
        }
    }

}
