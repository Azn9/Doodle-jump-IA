package dev.azn9.libgdxtest;

public class Tuple2<T, U> {

    private final T t;
    private final U u;

    public Tuple2(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getT() {
        return this.t;
    }

    public U getU() {
        return this.u;
    }

}
