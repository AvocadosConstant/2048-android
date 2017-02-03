package codes.timhung.nucleosynthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView testText;
    GridView boardGrid;
    ArrayAdapter<String> adapter;

    Board board;
    Board old;
    String[] boardArray;

    /**
     * Combines two Integer tiles by adding their values
     * @param lhs Left hand side Tile
     * @param rhs Right hand side Tile
     * @return Combination of the two tiles
     */
    Tile<Integer> combine(Tile<Integer> lhs, Tile<Integer> rhs) {
        return new Tile<>(lhs.getVal() + rhs.getVal());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Handle toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Handle testing text view
        testText = (TextView) findViewById(R.id.testText);

        // Handle game board
        board = new Board<>(2, this::combine);
        old = new Board<>(board);
        boardArray = board.toStrArr();

        // Fill boardGrid with values of the board
        boardGrid = (GridView) findViewById(R.id.boardGrid);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, boardArray);
        boardGrid.setAdapter(adapter);

        // Listen for swipe gestures
        boardGrid.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this) {
            @Override
            public void onSwipe(String flag) {
                old = new Board<>(board);
                testText.setText(flag);
                switch(flag) {
                    case Constants.UP:
                        board.slideUp();
                        break;
                    case Constants.DOWN:
                        board.slideDown();
                        break;
                    case Constants.LEFT:
                        board.slideLeft();
                        break;
                    case Constants.RIGHT:
                        board.slideRight();
                        break;
                    default:
                        break;
                }
                updateBoard();
            }
        });
    }

    public void updateBoard() {
        Log.d("UPDATEBOARD", "old: " + old);
        Log.d("UPDATEBOARD", "new: " + board);
        if(!board.equals(old)) {
            board.spawn();
            boardArray = board.toStrArr();
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, boardArray);
            boardGrid.invalidateViews();
            boardGrid.setAdapter(adapter);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
