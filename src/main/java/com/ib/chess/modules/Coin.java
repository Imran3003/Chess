package com.ib.chess.modules;

import java.util.Map;

import static com.ib.chess.modules.Constance.*;

/**
 * CoinsAndMoves.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.modules
 * @created Jan 11, 2024
 */
public class Coin
{
    public Coins coinName;
    public Colours coinColour;
    public Position defaultPosition;
    public Position currentPosition;
    public Map<MovementDirection,Integer> moveDirVsSteps;
    public int moveCount;
    public Coin() {
    }

    public Coin(Coins coinName, Colours coinColour, Position defaultPosition,Position currentPosition, Map<MovementDirection, Integer> moveDirVsSteps,int moveCount) {
        this.coinName = coinName;
        this.coinColour = coinColour;
        this.defaultPosition = defaultPosition;
        this.currentPosition = currentPosition;
        this.moveDirVsSteps = moveDirVsSteps;
        this.moveCount = moveCount;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Coins getCoinName() {
        return coinName;
    }

    public void setCoinName(Coins coinName) {
        this.coinName = coinName;
    }

    public Colours getCoinColour() {
        return coinColour;
    }

    public void setCoinColour(Colours coinColour) {
        this.coinColour = coinColour;
    }

    public Position getDefaultPosition() {
        return defaultPosition;
    }

    public void setDefaultPosition(Position defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    public Map<MovementDirection, Integer> getMoveDirVsSteps() {
        return moveDirVsSteps;
    }

    public void setMoveDirVsSteps(Map<MovementDirection, Integer> moveDirVsSteps) {
        this.moveDirVsSteps = moveDirVsSteps;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "coinName=" + coinName +
                ", coinColour=" + coinColour +
                ", defaultPosition=" + defaultPosition +
                ", currentPosition=" + currentPosition +
                ", moveDirVsSteps=" + moveDirVsSteps +
                ", moveCount=" + moveCount +
                '}';
    }
}
