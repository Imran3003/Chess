package com.ib.chess.modules;

import java.util.List;
import java.util.Set;

public class ClickedCoin
{
    public Coin clickedCoin;

    public Set<Constance.Position> possiblePosition;

    public ClickedCoin(Coin clickedCoin, Set<Constance.Position> possiblePosition) {
        this.clickedCoin = clickedCoin;
        this.possiblePosition = possiblePosition;
    }

    public Coin getClickedCoin() {
        return clickedCoin;
    }

    public void setClickedCoin(Coin clickedCoin) {
        this.clickedCoin = clickedCoin;
    }

    public Set<Constance.Position> getPossiblePosition() {
        return possiblePosition;
    }

    public void setPossiblePosition(Set<Constance.Position> possiblePosition) {
        this.possiblePosition = possiblePosition;
    }


}
