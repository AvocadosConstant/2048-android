package codes.timhung.nucleosynthesis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
    ArrayList<String> boardContents;

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
        boardContents = board.toStrArrList();

        // Fill boardGrid with values of the board
        adapter = new ArrayAdapter<String>(this, R.layout.tile, boardContents) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                GradientDrawable tileBGShape = (GradientDrawable) view.getBackground();

                // TODO Programatically set tile color based on tile content.
                //int resourceId= Resources.getSystem().getIdentifier("tile2048", "colors", this.getContext().getPackageName());
                //tileBGShape.setColor(ContextCompat.getColor(this.getContext(), resourceId));

                switch(boardContents.get(position)) {
                    case "2" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile2)); break;
                    case "4" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile4)); break;
                    case "8" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile8)); break;
                    case "16" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile16)); break;
                    case "32" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile32)); break;
                    case "64" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile64)); break;
                    case "128" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile128)); break;
                    case "256" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile256)); break;
                    case "512" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile512)); break;
                    case "1024" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile1024)); break;
                    case "2048" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile2048)); break;
                    case "4096" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile4096)); break;
                    case "8192" : tileBGShape.setColor(ContextCompat.getColor(
                            this.getContext(), R.color.tile8192)); break;
                    default: tileBGShape.setColor(ContextCompat.getColor(this.getContext(), R.color.tileEmpty));
                }
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
            boardContents = board.toStrArrList();
            adapter.clear();
            adapter.addAll(boardContents);
            adapter.notifyDataSetChanged();
        }
    }

    public boolean newGameAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Starting new game");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newGame();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
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
                return newGameAlert();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
