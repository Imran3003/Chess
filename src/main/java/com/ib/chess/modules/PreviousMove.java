package com.ib.chess.modules;

public class PreviousMove
{
    public Coin coin;

    public Constance.Position from;

    public Constance.Position to;


    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public Constance.Position getFrom() {
        return from;
    }

    public void setFrom(Constance.Position from) {
        this.from = from;
    }

    public Constance.Position getTo() {
        return to;
    }

    public void setTo(Constance.Position to) {
        this.to = to;
    }

    public PreviousMove(Coin coin, Constance.Position from, Constance.Position to) {
        this.coin = coin;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "PreviousMove{" +
                "coin=" + coin +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
