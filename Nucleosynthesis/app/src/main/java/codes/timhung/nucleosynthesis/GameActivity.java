package codes.timhung.nucleosynthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView testText;
    GridView boardGrid;
    ArrayAdapter<String> adapter;

    Board<Integer> board;
    Board<Integer> old;
    ArrayList<String> boardArray;

    /**
     * Combines two Integer tiles by adding their values
     * @param lhs Left hand side Tile
     * @param rhs Right hand side Tile
     * @return Combination of the two tiles
     */
    Tile<Integer> combine(Tile<Integer> lhs, Tile<Integer> rhs) {
        return new Tile<>(lhs.getVal() + rhs.getVal());
    }

    /**
     * Defines combination criteria for two Integer tiles
     * @param lhs Left hand side Tile
     * @param rhs Right hand side Tile
     * @return Whether they can be combined
     */
    boolean canCombine(Tile<Integer> lhs, Tile<Integer> rhs) {
        return lhs.equals(rhs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Grab views
        testText = (TextView) findViewById(R.id.testText);
        boardGrid = (GridView) findViewById(R.id.boardGrid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newGame();

        // Listen for swipe gestures
        boardGrid.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this) {
            @Override
            public void onSwipe(String flag) {
                old.setBoard(board);

                Log.d("ON_SWIPE", "Printing old and new boards. Should be the same!");
                Log.d("ON_SWIPE", "old: " + old);
                for(int y = 0; y < old.getWidth(); y++) {
                    Log.d("ON_SWIPE", "-row " + y + old.rowToStr(y));
                }
                Log.d("ON_SWIPE", "new: " + board);
                for(int y = 0; y < board.getWidth(); y++) {
                    Log.d("ON_SWIPE", "-row " + y + board.rowToStr(y));
                }

                testText.setText(flag);
                switch(flag) {
                    case Constants.UP:
                        Log.d("ON_SWIPE", "Sliding up");
                        board.slideUp();
                        break;
                    case Constants.DOWN:
                        Log.d("ON_SWIPE", "Sliding down");
                        board.slideDown();
                        break;
                    case Constants.LEFT:
                        Log.d("ON_SWIPE", "Sliding left");
                        board.slideLeft();
                        break;
                    case Constants.RIGHT:
                        Log.d("ON_SWIPE", "Sliding right");
                        board.slideRight();
                        break;
                    default:
                        break;
                }
                updateBoard();
            }
        });
    }

    public void newGame() {
        board = new Board<>(2, this::combine, this::canCombine);
        old = new Board<>(board);
        boardArray = board.toStrArrList();

        // Fill boardGrid with values of the board
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, boardArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Log.d("ADAPTER_GET_VIEW", "boardArr[" + position + "] = " + boardArray.get(position));
                int color = 0xFFCDC1B4;
                switch(boardArray.get(position)) {
                    case("2"): color = 0xFFEEE4D9; break;
                    case("4"): color = 0xFFEDE0C7; break;
                    case("8"): color = 0xFFF3B274; break;
                    case("16"): color = 0xFFF7955D; break;
                    case("32"): color = 0xFFF67C5F; break;
                    case("64"): color = 0xFFF95D32; break;
                    case("128"): color = 0xFFEDCF72; break;
                    case("256"): color = 0xFFEDCC61; break;
                    case("512"): color = 0xFFEDC850; break;
                    case("1024"): color = 0xFFEDC53F; break;
                    case("2048"): color = 0xFFECC12D; break;
                    case("4096"): color = 0xFFFF3D3C; break;
                    case("8192"): color = 0xFFFF1E1C; break;
                }
                view.setBackgroundColor(color);
                return view;
            }
        };
        boardGrid.setAdapter(adapter);
    }

    public void updateBoard() {
        Log.d("UPDATE_BOARD", "Printing both boards, should be diff if valid move!");
        Log.d("UPDATE_BOARD", "old: " + old);
        for(int y = 0; y < old.getWidth(); y++) {
            Log.d("UPDATE_BOARD", "-row " + y + old.rowToStr(y));
        }
        Log.d("UPDATE_BOARD", "new: " + board);
        for(int y = 0; y < board.getWidth(); y++) {
            Log.d("UPDATE_BOARD", "-row " + y + board.rowToStr(y));
        }
        //Log.d("UPDATE_BOARD", "are the boards equal? " + board.equals(old));
        if(!board.equals(old)) {
            Log.d("UPDATE_BOARD", "Spawning a new tile somewhere");
            board.spawn();
            boardArray = board.toStrArrList();
            adapter.clear();
            adapter.addAll(boardArray);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.new_game:
                newGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
