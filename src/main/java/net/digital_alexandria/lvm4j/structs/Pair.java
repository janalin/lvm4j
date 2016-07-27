package net.digital_alexandria.lvm4j.structs;

/**
 * Class that holds a pair of two values.
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Pair<T, U>
{
    private final T _T;
    private final U _U;

    public Pair(T t, U u)
    {
        this._T = t;
        this._U = u;
    }

    /**
     * Getter for the first value.
     *
     * @return returns the first value
     */
    public T getFirst()
    {
        return _T;
    }

    /**
     * Getter for the second value.
     *
     * @return returns the second value
     */
    public U getSecond()
    {
        return _U;
    }

}
