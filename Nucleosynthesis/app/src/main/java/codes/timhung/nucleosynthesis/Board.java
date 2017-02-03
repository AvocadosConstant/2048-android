package codes.timhung.nucleosynthesis;

import android.util.Log;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * Created by Tim Hung on 2/1/2017.
 */

public class Board<T> {
    public static final int DEFAULT_DIM = 4;

    private ArrayList<ArrayList<Tile<T>>> board;
    private int width;
    private int height;
    private T spawnValue;
    private Combine<T> combine;

    public Board(T spawnValue, Combine<T> combine) {
        this.board = new ArrayList<>();
        for(int y = 0; y < DEFAULT_DIM; y++) {
            ArrayList<Tile<T>> col = new ArrayList<>();
            for(int x = 0; x < DEFAULT_DIM; x++) {
                col.add(new Tile<T>(null));
            }
            this.board.add(col);
        }
        this.width = 4;
        this.height = 4;
        this.spawnValue = spawnValue;
        this.combine = combine;
        this.init();
    }

    public Board(int width, int height, T spawnValue, Combine<T> combine) {
        this.board = new ArrayList<>();
        for(int x = 0; x < height; x++) {
            ArrayList<Tile<T>> col = new ArrayList<>();
            for(int y = 0; y < width; y++) {
                col.add(new Tile<>(null));
            }
            //Log.d("BOARD_CTOR", "added col: " + col);
            this.board.add(col);
        }
        this.width = width;
        this.height = height;
        this.spawnValue = spawnValue;
        this.combine = combine;
        this.init();
    }

    public Board(final Board<T> that) {
        Log.d("BOARD_CTOR", "constructing a new board from a given one!");
        //this.setBoard(that);
        this.width = that.getWidth();
        this.height = that.getHeight();
        this.setBoard(that);
        this.spawnValue = that.getSpawnValue();
        this.combine = that.getCombine();
    }

    public void setBoard(final Board<T> that) {
        this.board = new ArrayList<>();
        for(int x = 0; x < this.getHeight(); x++) {
            ArrayList<Tile<T>> col = new ArrayList<>();
            for(int y = 0; y < this.getWidth(); y++) {
                Tile<T> copiedTile = new Tile<>(that.get(x, y).getVal());
                copiedTile.setEmpty(that.get(x, y).isEmpty());
                col.add(copiedTile);
            }
            this.board.add(col);
        }
        Log.d("SET_BOARD", "set board to:\n");
        for(int y = 0; y < this.getWidth(); y++) {
            Log.d("SET_BOARD", "row " + y + this.rowToStr(y));
        }
    }

    public Tile<T> get(int x, int y) {return this.getBoard().get(x).get(y);}

    public ArrayList<ArrayList<Tile<T>>> getBoard() {return this.board;}

    public int getWidth() {return this.width;}

    public int getHeight() {return this.height;}

    public T getSpawnValue() {return this.spawnValue;}

    public void setWidth(int width) {this.width = width;}

    public void setHeight(int height) {this.height = height;}

    public Combine<T> getCombine() {return this.combine;}

    public boolean equals(Board<T> that) {
        for(int y = 0; y < width; y++) {
            for(int x = 0; x < height; x++) {
                Log.d("BOARD_EQUALS", "(" + x + ", " + y + ") Comparing " + this.get(x,y).toString() + " and "
                        + that.get(x, y).toString() + " | are they equal? " + this.get(x, y).equals(that.get(x, y)));
                if(!this.get(x, y).equals(that.get(x, y))) return false;
            }
        } return true;
    }

    public void init() {
        Random rng = new Random();
        for(int i = 0; i < 2; i++) {
            int x = rng.nextInt(4);
            int y = rng.nextInt(4);
            if(this.get(x, y).getVal() == null) {
                this.get(x, y).setVal(this.spawnValue);
                this.get(x, y).setEmpty(false);
            }
            else i--;
        }
    }

    public void spawn() {
        Random rng = new Random();
        boolean spawned = false;
        while(!spawned) {
            int x = rng.nextInt(4);
            int y = rng.nextInt(4);
            if(board.get(x).get(y).isEmpty()) {
                this.get(x, y).setVal(this.spawnValue);
                this.get(x, y).setEmpty(false);
                spawned = true;
            }
        }
    }

    /**
     * Handles the sliding of a column of Tiles
     * @param col Column to be slid
     */
    public ArrayList<Tile<T>> slideCol(ArrayList<Tile<T>> col) {
        Log.d("SLIDE", "sliding column: " + col.toString());
        for(int i = 1; i < col.size(); i++) {
            // If current tile isn't empty
            if(!col.get(i).isEmpty()) {
                // Search for the first non-empty tile before it (target)
                int target = 0;
                for(int j = i - 1; j >= 0; j--) {
                    if(!col.get(j).isEmpty()) {
                        // Found a non-empty tile before current tile
                        target = j;
                        // Exit this inner for loop
                        j = -1;
                    }
                }
                Log.d("SLIDE", "i = " + i + " | target: " + target);
                Log.d("SLIDE", "col[i] = " + col.get(i) + " | col[target]: " + col.get(target));
                //Log.d("SLIDE", "are they equal? " + col.get(i).equals(col.get(target)));
                //Log.d("SLIDE", "is target empty? " + col.get(target).isEmpty());
                if(!col.get(target).wasMoved() && !col.get(i).wasMoved()
                        && col.get(i).equals(col.get(target))
                        && !col.get(target).isEmpty()) {
                    // Current and target were not moved, and they have the "same" value
                    // Set target as the combination of the two
                    col.get(target).setVal(combine.apply(col.get(target), col.get(i)).getVal());
                    col.get(target).setMoved(true);
                    col.get(target).setEmpty(false);
                    col.get(i).setEmpty(true);
                    Log.d("SLIDE", "combined tiles, now is: " + col.toString());
                } else if(col.get(target).isEmpty()) {
                    Log.d("SLIDE", "target is empty!");
                    // Target is empty, move current to target
                    col.get(target).setVal(col.get(i).getVal());
                    col.get(target).setEmpty(false);
                    col.get(i).setEmpty(true);
                    Log.d("SLIDE", "slid into empty space, now is: " + col.toString());
                } else if(target < i && col.get(target + 1).isEmpty()) {
                    // Tiles are different, so move current Tile to empty space right of target
                    Log.d("SLIDE", "tiles are different, sliding into next empty, now is: " + col.toString());
                    col.get(target + 1).setVal(col.get(i).getVal());
                    col.get(target + 1).setEmpty(false);
                    col.get(i).setEmpty(true);
                }
            }
        }
        for(int i = 0; i < col.size(); i++) {
            col.get(i).setMoved(false);
        }
        return col;
    }

    private void transpose() {
        Log.d("TRANSPOSE", "before: " + board);
        for(int y = 0; y < this.getWidth(); y++) {
            Log.d("TRANSPOSE", "-row " + y + this.rowToStr(y));
        }
        ArrayList<ArrayList<Tile<T>>> transposed = new ArrayList<>();

        // For each row in the board
        for(int y = 0; y < this.getWidth(); y++) {
            // We will construct the corresponding column in the transpose
            ArrayList<Tile<T>> transCol = new ArrayList<>();
            for(int x = 0; x < this.getHeight(); x++) {
                // Fill transCol with values of the original row
                transCol.add(this.get(x, y));
            }
            transposed.add(transCol);
        }
        this.board = transposed;
        int tmp = this.getWidth();
        this.setWidth(this.getHeight());
        this.setHeight(tmp);
        Log.d("TRANSPOSE", "after: " + board);
        for(int y = 0; y < this.getWidth(); y++) {
            Log.d("TRANSPOSE", "-row " + y + this.rowToStr(y));
        }
    }

    private void reverseRows() {
        Log.d("REV_ROWS", "before: " + board);
        for(int y = 0; y < this.getWidth(); y++) {
            Log.d("REV_ROWS", "-row " + y + this.rowToStr(y));
        }
        for(int i = 0, j = this.getWidth() - 1; i < j; i++, j--) {
            ArrayList<Tile<T>> tmp = this.getBoard().get(i);
            this.getBoard().set(i, this.getBoard().get(j));
            this.getBoard().set(j, tmp);
        }
        Log.d("REV_ROWS", "after: " + board);
        for(int y = 0; y < this.getWidth(); y++) {
            Log.d("REV_ROWS", "-row " + y + this.rowToStr(y));
        }
    }

    private void rotateCW() {
        this.transpose();
        this.reverseRows();
    }

    private void rotateCCW() {
        this.reverseRows();
        this.transpose();
    }

    public void slideUp() {
        for(int x = 0; x < this.getWidth(); x++) {
            Log.d("SLIDE_UP", "sliding col " + x + ": " + this.getBoard().get(x));
            slideCol(this.getBoard().get(x));
            Log.d("SLIDE_UP", "board column: " + this.getBoard().get(x));
        }
    }

    public void slideLeft() {
        this.rotateCW();
        this.slideUp();
        this.rotateCCW();
    }

    public void slideRight() {
        this.rotateCCW();
        this.slideUp();
        this.rotateCW();
    }

    public void slideDown() {
        this.rotateCW();
        this.rotateCW();
        this.slideUp();
        this.rotateCW();
        this.rotateCW();
    }

    public String[] toStrArr() {
        int size = this.getWidth() * this.getHeight();
        String[] boardArray = new String[size];
        for(int i = 0; i < size; i++) {
            boardArray[i] = this.get(i % 4, i / 4).toString();
        }
        return boardArray;
    }

    public String rowToStr(int index) {
        String row = "|";
        for(int x = 0; x < this.getHeight(); x++) {
            row += "\t" + this.get(x, index).toString();
        } return row + "\t|";
    }
}
