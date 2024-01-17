package com.ib.chess.modules;

import static com.ib.chess.modules.Constance.*;

/**
 * Square.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.modules
 * @created Jan 16, 2024
 */
public class Square
{
    public Coin coin;

    public boolean isCoinIsPresent;

    public Square() {
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public boolean isCoinIsPresent() {
        return isCoinIsPresent;
    }

    public void setCoinIsPresent(boolean coinIsPresent) {
        isCoinIsPresent = coinIsPresent;
    }

    public Square(Coin coin, boolean isCoinIsPresent) {
        this.coin = coin;
        this.isCoinIsPresent = isCoinIsPresent;
    }

    @Override
    public String toString() {
        return "Square{" +
                "coin=" + coin +
                ", isCoinIsPresent=" + isCoinIsPresent +
                '}';
    }
}
