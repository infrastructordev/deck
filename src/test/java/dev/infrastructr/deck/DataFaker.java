package dev.infrastructr.deck;

import com.github.javafaker.Faker;

public final class DataFaker {

    private static final Faker INSTANCE = new Faker();

    private DataFaker(){
        throw new AssertionError();
    }

    public static Faker getInstance(){
        return INSTANCE;
    }
}
