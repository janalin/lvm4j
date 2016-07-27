package net.digital_alexandria.lvm4j.structs;

/**
 * Class that holds a triple of values.
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Triple<T, U, V> extends Pair<T, U>
{
    private final V _V;

    public Triple(T t, U u, V v)
    {
        super(t, u);
        this._V = v;
    }

    /**
     * Getter for the third value.
     *
     * @return returns the third value
     */
    public V getThird()
    {
        return _V;
    }
}
