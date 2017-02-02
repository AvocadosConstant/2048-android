package codes.timhung.nucleosynthesis;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Tim Hung on 2/1/2017.
 */

public class Board {

    private int[][] board;

    public Board() {
        this.board = new int[4][4];
        this.init();
    }

    public Board(Board that) {
        this.board = new int[that.getBoard().length][that.getBoard()[0].length];
        this.setBoard(that);
    }

    public void setBoard(Board that) {
        for(int i = 0; i < that.getBoard()[0].length; i++) {
            this.board[i] = that.getBoard()[i].clone();
        }
    }

    public int[][] getBoard() {
        return this.board;
    }

    public boolean equals(Board that) {
        return Arrays.deepEquals(this.getBoard(), that.getBoard());
    }

    public void init() {
        Random rng = new Random();
        for(int i = 0; i < 2; i++) {
            int x = rng.nextInt(4);
            int y = rng.nextInt(4);
            if(board[x][y] == 0) board[x][y] = 2;
            else i--;
        }
    }

    public void spawn() {
        Random rng = new Random();
        boolean spawned = false;
        while(!spawned) {
            int x = rng.nextInt(4);
            int y = rng.nextInt(4);
            if(board[x][y] == 0) {
                board[x][y] = (rng.nextInt(2) + 1) * 2;
                spawned = true;
            }
        }
    }

    public static void slideCol(int[] col) {
        for(int i = 1; i < col.length; i++) {
            if(col[i] != 0) {
                int targetIndex = 0;
                for(int j = i - 1; j >= 0; j--) {
                    if(col[j] != 0) {
                        targetIndex = j;
                        j = -1;
                    }
                }
                if(col[targetIndex] == col[i]) {
                    col[targetIndex] = col[i] * -2;
                    col[i] = 0;
                } else if(col[targetIndex] == 0) {
                    col[targetIndex] = col[i];
                    col[i] = 0;
                } else if(targetIndex < i && col[targetIndex + 1] == 0) {
                    col[targetIndex + 1] = col[i];
                    col[i] = 0;
                }
            }
        }
        for(int i = 0; i < col.length; i++) {
            if(col[i] < 0) {
                col[i] *= -1;
            }
        }
    }

    public void transpose() {
        for(int i = 0; i < board.length; i++) {
            for(int j = i; j < board[0].length; j++) {
                int tmp = board[i][j];
                board[i][j] = board[j][i];
                board[j][i] = tmp;
            }
        }
    }

    private void reverseRows() {
        for(int i = 0, j = board.length - 1; i < j; i++, j--) {
            int[] tmp = board[i];
            board[i] = board[j];
            board[j] = tmp;
        }
    }

    public void rotateCW() {
        this.transpose();
        this.reverseRows();
    }

    public void rotateCCW() {
        this.reverseRows();
        this.transpose();
    }

    public void slideUp() {
        for(int[] col : board) {
            slideCol(col);
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
}
