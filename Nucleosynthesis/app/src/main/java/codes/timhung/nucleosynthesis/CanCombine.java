package codes.timhung.nucleosynthesis;

/**
 * Created by Tim Hung on 2/3/2017.
 */

public interface CanCombine<T> {
    boolean apply(Tile<T> lhs, Tile<T> rhs);
}
