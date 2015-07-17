package net.digital_alexandria.sshmm.structs;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Triple<T, U, V>
{
	private final T t;
	private final U u;
	private final V v;

	public Triple(T t, U u, V v)
	{
		this.t = t;
		this.u = u;
		this.v = v;
	}

	public T getFirst()
	{
		return t;
	}

	public U getSecond()
	{
		return u;
	}

	public V getThird()
	{
		return v;
	}
}
