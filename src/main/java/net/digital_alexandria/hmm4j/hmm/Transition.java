package net.digital_alexandria.hmm4j.hmm;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public class Transition extends HMMEdge
{
    private double _transitionProbability;

    Transition(State source, State sink, double weight)
    {
        super(source, sink);
        this._transitionProbability = weight;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(source()).append(" -(").append
            (_transitionProbability).append(")-> ").append
            (sink()).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Transition)
        {
            Transition t = (Transition) o;
            return t.sink().equals(sink())
                   && t.source().equals(source());
        }
        return false;
    }

    @Override
    public void increment()
    {
        this._transitionProbability++;
    }

    public double transitionProbability()
    {
        return _transitionProbability;
    }

    public void transitionProbability(double d)
    {
        this._transitionProbability = d;
    }
}
