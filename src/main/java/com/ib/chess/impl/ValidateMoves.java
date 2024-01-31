package com.ib.chess.impl;

import com.ib.chess.modules.Coin;

import java.util.*;

import static com.ib.chess.modules.Constance.MovementDirection;
import static com.ib.chess.modules.Constance.MovementDirection.*;
import static com.ib.chess.modules.Constance.Position;

/**
 * ValidateMoves.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.impl
 * @created Jan 16, 2024
 */


public class ValidateMoves {

    public static Set<Position> getPossibleMoves(Coin coin) {
        Position currentPosition = coin.getCurrentPosition();
        Map<MovementDirection, Integer> moveDirVsSteps = coin.getMoveDirVsSteps();

        Set<Position> coinPossiblePositions = new HashSet<>();
        moveDirVsSteps.forEach((direction, steps) -> {
            List<Position> positions = getPossiblePositions(direction, steps, currentPosition);
            coinPossiblePositions.addAll(positions);
        });
        return coinPossiblePositions;
    }

    private static List<Position> getPossiblePositions(MovementDirection direction, int steps, Position currentPosition) {
        switch (direction) {
            case LEFT:
            case RIGHT:
            case FORWARD:
            case BACKWARD:
                return linearMove(direction, steps, currentPosition);
            case PAWN_STARTING:
                return pawnStarting(currentPosition);
            case CROSS_RIGHT_FORWARD:
            case CROSS_LEFT_FORWARD:
            case CROSS_RIGHT_BACKWARD:
            case CROSS_LEFT_BACKWARD:
                return diagonalMove(direction, steps, currentPosition);
            case KNIGHT_L_MOVE:
                return knightMove(currentPosition);
        }
        return Collections.emptyList();
    }

    private static List<Position> linearMove(MovementDirection direction, int steps, Position currentPosition) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();
        int stepModifierX = (direction == RIGHT) ? 1 : (direction == LEFT) ? -1 : 0;
        int stepModifierY = (direction == FORWARD) ? 1 : (direction == BACKWARD) ? -1 : 0;

        for (int i = 0; i < steps; i++) {
            x += stepModifierX;
            y += stepModifierY;

            if (x < 0 || x > 7 || y < 0 || y > 7) {
                break;
            }
            positions.add(Position.setPos(x, y));
        }

        return positions;
    }

    private static List<Position> pawnStarting(Position currentPosition) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        if (x == 1 || x == 6) {
            int forwardStep = (x == 1) ? 2 : -2;
            positions.add(Position.setPos(x + forwardStep, y));
        }

        return positions;
    }

    private static List<Position> diagonalMove(MovementDirection direction, int steps, Position currentPosition) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();
        int stepModifierX = (direction == CROSS_RIGHT_FORWARD || direction == CROSS_LEFT_BACKWARD) ? 1 :
                (direction == CROSS_LEFT_FORWARD || direction == CROSS_RIGHT_BACKWARD) ? -1 : 0;
        int stepModifierY = (direction == CROSS_RIGHT_FORWARD || direction == CROSS_LEFT_FORWARD) ? 1 :
                (direction == CROSS_RIGHT_BACKWARD || direction == CROSS_LEFT_BACKWARD) ? -1 : 0;

        for (int i = 0; i < steps; i++) {
            x += stepModifierX;
            y += stepModifierY;

            if (x < 0 || x > 7 || y < 0 || y > 7) {
                break;
            }
            positions.add(Position.setPos(x, y));
        }

        return positions;
    }

    private static List<Position> knightMove(Position currentPosition) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        int[][] knightMoves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        for (int[] move : knightMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            if (newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7) {
                positions.add(Position.setPos(newX, newY));
            }
        }

        return positions;
    }
}

