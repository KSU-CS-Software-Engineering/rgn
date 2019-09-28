package com.CIS642.Rural.GroceryImplementation.utils;

/**
 *
 */
public class Tuple2<A, B> {

    public final A _1;
    public final B _2;

    public Tuple2(A _1, B _2) {
        this._1 = _1;
        this._2 = _2;
    }

    @Override
    public int hashCode() {
        int code = 29;
        if (_1 != null) code += 37 * _1.hashCode();
        if (_2 != null) code += 53 * _2.hashCode();
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tuple2 && hashCode() == obj.hashCode() &&
                ((_1 == null && ((Tuple2) obj)._1 == null) || (_1 != null && _1.equals(((Tuple2) obj)._1))) &&
                ((_2 == null && ((Tuple2) obj)._2 == null) || (_2 != null && _2.equals(((Tuple2) obj)._2)));
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", _1, _2);
    }
}
