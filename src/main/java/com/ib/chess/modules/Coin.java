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
    public boolean isJump;
    public boolean isMovingBackward;

    public boolean isMovingBackward() {
        return isMovingBackward;
    }

    public void setMovingBackward(boolean movingBackward) {
        isMovingBackward = movingBackward;
    }

    public Coin() {
    }

    public Coin(Coins coinName, Colours coinColour, Position defaultPosition,Position currentPosition, Map<MovementDirection, Integer> moveDirVsSteps, boolean isJump, boolean isMovingBackward) {
        this.coinName = coinName;
        this.coinColour = coinColour;
        this.defaultPosition = defaultPosition;
        this.currentPosition = currentPosition;
        this.moveDirVsSteps = moveDirVsSteps;
        this.isJump = isJump;
        this.isMovingBackward = isMovingBackward;
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

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "coinName=" + coinName +
                ", coinColour=" + coinColour +
                ", position=" + defaultPosition +
                ", moveDirVsSteps=" + moveDirVsSteps +
                ", isJump=" + isJump +
                ", isMovingBackward=" + isMovingBackward +
                '}';
    }
}
