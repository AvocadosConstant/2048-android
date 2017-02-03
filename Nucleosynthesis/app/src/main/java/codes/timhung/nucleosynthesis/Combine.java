package codes.timhung.nucleosynthesis;

/**
 * Created by Tim Hung on 2/3/2017.
 */

public interface Combine<T> {
    Tile<T> apply(Tile<T> lhs, Tile<T> rhs);
}
