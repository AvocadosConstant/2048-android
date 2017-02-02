package codes.timhung.nucleosynthesis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    Toolbar toolbar;
    GridView boardGrid;
    ArrayAdapter<String> adapter;
    TextView testText;
    Board board;
    Board old;
    String[] boardArray = new String[] {
            "0", "0", "0", "0",
            "0", "0", "0", "0",
            "0", "0", "0", "0",
            "0", "0", "0", "0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        board = new Board();
        old = new Board(board);
        boardArray = board.toStrArr();

        boardGrid = (GridView) findViewById(R.id.boardGrid);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, boardArray);
        boardGrid.setAdapter(adapter);

        testText = (TextView) findViewById(R.id.testText);

        boardGrid.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this) {
            public void onSwipeTop() {
                old = new Board(board);
                testText.setText("top");
                board.slideUp();
                updateBoard();
            }
            public void onSwipeRight() {
                old = new Board(board);
                testText.setText("right");
                board.slideRight();
                updateBoard();
            }
            public void onSwipeLeft() {
                old = new Board(board);
                testText.setText("left");
                board.slideLeft();
                updateBoard();
            }
            public void onSwipeBottom() {
                old = new Board(board);
                testText.setText("down");
                board.slideDown();
                updateBoard();
            }
        });
    }

    public void updateBoard() {
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
