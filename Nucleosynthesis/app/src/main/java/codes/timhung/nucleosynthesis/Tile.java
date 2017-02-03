package codes.timhung.nucleosynthesis;

import java.util.function.Function;

/**
 * Created by Tim Hung on 2/2/2017.
 */

public class Tile<T> {
    private T value;
    private boolean empty;
    private boolean moved;

    public Tile(T value) {
        this.value = value;
        this.empty = true;
        this.moved = false;
    }

    public T getVal() {return value;}

    public void setVal(T value) {this.value = value;}

    public boolean isEmpty() {return this.empty;}

    public void setEmpty(boolean empty) {this.empty = empty;}

    public boolean wasMoved() {return this.moved;}

    public void setMoved(boolean moved) {this.moved = moved;}

    public boolean equals(Tile<T> that) {
        return this.getVal() != null && this.getVal().equals(that.getVal());
    }

    public String toString() {
        return (this.getVal() == null || this.isEmpty()) ? "" : this.getVal().toString();
    }
}
