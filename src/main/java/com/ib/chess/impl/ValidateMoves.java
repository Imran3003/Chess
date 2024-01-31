package com.ib.chess.impl;

import com.ib.chess.modules.Coin;

import java.util.*;

import static com.ib.chess.modules.Constance.*;

/**
 * ValidateMoves.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.impl
 * @created Jan 16, 2024
 */
public class ValidateMoves
{
    public static Set<Position> getPossibleMoves(Coin coin)
    {
        Position currentPosition = coin.getCurrentPosition();
        Position defaultPosition = coin.getDefaultPosition();
        Map<MovementDirection, Integer> moveDirVsSteps = coin.getMoveDirVsSteps();

        Set<Position> coinPossiblePositions = new HashSet<>();
        moveDirVsSteps.forEach((k, v) -> {
            System.out.println("k = " + k);
            System.out.println("v = " + v);
            List<Position> positions = getPossiblePositions(k, v, currentPosition, defaultPosition);
            coinPossiblePositions.addAll(positions);
        });
        System.out.println("coin possible positions = " + coinPossiblePositions);
        return coinPossiblePositions;
    }

    private static List<Position> getPossiblePositions(MovementDirection movementDirection, int steps, Position currenPosition, Position defaultPosition)
    {
        System.out.println("movementDirection = " + movementDirection);
        switch (movementDirection)
        {
            case LEFT:
                return leftMove(steps,currenPosition,defaultPosition);
            case RIGHT:
                return rightMove(steps,currenPosition,defaultPosition);
            case FORWARD:
                return forwardMove(steps,currenPosition,defaultPosition);
            case BACKWARD:
                return backwardMove(steps,currenPosition,defaultPosition);
            case PAWN_STARTING:
                return pawnStarting(currenPosition,defaultPosition);
            case  CROSS_RIGHT_FORWARD:
                return crossRightForwardMove(steps,currenPosition,defaultPosition);
            case CROSS_LEFT_FORWARD:
                return crossLeftForwardMove(steps,currenPosition,defaultPosition);
            case  CROSS_RIGHT_BACKWARD:
                return crossRightBackwardMove(steps,currenPosition,defaultPosition);
            case CROSS_LEFT_BACKWARD:
                return crossLeftBackwardMove(steps,currenPosition,defaultPosition);
            case KNIGHT_L_MOVE:
                return knightMove(currenPosition);
        }
        return Collections.emptyList();
    }


    private static List<Position> leftMove(int steps, Position currenPosition, Position defaultPosition)
    {
        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();

        int curPosy = currenPosition.getY();

        int defX = defaultPosition.getX();

        if (defX < 5)
        {
            for (int i = 0; i < steps; i++) {
                curPosy = curPosy+1;
                if (curPosy > 7)
                    break;
                positions.add(Position.setPos(x, curPosy));
            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosy = curPosy -1;
                if (curPosy < 0)
                    break;
                positions.add(Position.setPos(x, curPosy));
            }
        }

        return positions;
    }

    private static List<Position> rightMove(int steps, Position currenPosition, Position defaultPosition)
    {

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();

        int curPosy = currenPosition.getY();

        int defX = defaultPosition.getX();

        if (defX < 5)
        {
            for (int i = 0; i < steps; i++) {
                curPosy = curPosy-1;
                if (curPosy < 0)
                    break;
                positions.add(Position.setPos(x, curPosy));
            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosy = curPosy +1;
                if (curPosy > 7)
                    break;
                positions.add(Position.setPos(x, curPosy));
            }
        }

        return positions;

    }
    private static List<Position> pawnStarting(Position currenPosition, Position defaultPosition)
    {
        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;
        int defX = defaultPosition.getX();

        if (defaultPosition != currenPosition)
            return Collections.emptyList();

        if (defX < 5)
            curPosx = curPosx+2;
        else
            curPosx = curPosx - 2;
        if (curPosx == x)
            return Collections.emptyList();

        positions.add(Position.setPos(curPosx, y));
        return positions;
    }

    private static List<Position> backwardMove(int steps, Position currenPosition, Position defaultPosition)
    {
        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;

        int defX = defaultPosition.getX();

        if (defX < 5)
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx-1;
                if (curPosx < 0)
                    break;
                positions.add(Position.setPos(curPosx, y));
            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx +1;
                if (curPosx > 7)
                    break;
                positions.add(Position.setPos(curPosx, y));
            }
        }

        return positions;
    }

    private static List<Position> forwardMove(int steps, Position currenPosition, Position defaultPosition)
    {

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;
        int defX = defaultPosition.getX();

        if (defX < 5)
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx+1;
                if (curPosx > 7)
                    break;
                positions.add(Position.setPos(curPosx, y));
            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx -1;
                if (curPosx < 0)
                    break;
                positions.add(Position.setPos(curPosx, y));
            }
        }

        return positions;
    }

    private static List<Position> crossLeftForwardMove(int steps, Position currenPosition, Position defaultPosition)
    {
        System.out.println("==== CrossLeftForwardMove ====");

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;
        int curPosy = y;
        int defX = defaultPosition.getX();
        if (defX < 2)
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx+1;
                curPosy = curPosy+1;
                if (curPosx > 7 || curPosy > 7)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));
            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx -1;
                curPosy = curPosy -1;
                if (curPosx < 0 || curPosy < 0)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));
            }
        }

        return positions;
    }
    private static List<Position> crossLeftBackwardMove(int steps, Position currenPosition, Position defaultPosition) {

        System.out.println("==== CrossLeftBackwardMove ====");

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;
        int curPosy = y;
        int defX = defaultPosition.getX();

        if (defX < 2)
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx-1;
                curPosy = curPosy+1;
                if (curPosx < 0 || curPosy > 7)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));

            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx +1;
                curPosy = curPosy -1;
                if (curPosx > 7 || curPosy < 0)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));

            }
        }

        return positions;
    }

    private static List<Position> crossRightForwardMove(int steps, Position currenPosition, Position defaultPosition)
    {
        System.out.println("==== CrossRightForwardMove ====");

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;
        int curPosy = y;
        int defX = defaultPosition.getX();

        if (defX < 2)
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx+1;
                curPosy = curPosy-1;
                if (curPosy < 0 || curPosx > 7)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));

            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx -1;
                curPosy = curPosy +1;
                if (curPosx < 0 || curPosy > 7)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));

            }
        }

        return positions;

    }

    private static List<Position> crossRightBackwardMove(int steps, Position currenPosition, Position defaultPosition) {

        System.out.println("==== CrossLeftForwardMove ====");

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();

        int curPosx = x;
        int curPosy = y;
        int defX = defaultPosition.getX();

        if (defX < 2)
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx-1;
                curPosy = curPosy-1;
                if (curPosx < 0 || curPosy < 0)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));
            }
        }
        else
        {
            for (int i = 0; i < steps; i++) {
                curPosx = curPosx +1;
                curPosy = curPosy +1;
                if (curPosx > 7 || curPosy > 7)
                    break;
                positions.add(Position.setPos(curPosx,curPosy));

            }
        }

        return positions;

    }

    private static List<Position> knightMove(Position currenPosition) {

        List<Position> positions = new ArrayList<>();

        int x = currenPosition.getX();
        int y = currenPosition.getY();


        if (x < 6 && y < 7 )
            positions.add(Position.setPos(x + 2, y + 1));
        if (x < 6 && y > 0)
            positions.add(Position.setPos(x + 2, y - 1));
        if (x > 1 &&  y > 0)
            positions.add(Position.setPos(x - 2, y - 1));
        if (x > 1 && y < 7)
            positions.add(Position.setPos(x - 2, y + 1));
        if (x < 7 && y < 6)
            positions.add(Position.setPos(x + 1, y + 2));
        if (x < 7 && y > 1)
            positions.add(Position.setPos(x + 1, y - 2));
        if (x > 0 && y < 6 && y > 1)
            positions.add(Position.setPos(x - 1, y + 2));
        if (x > 0 && y > 1)
            positions.add(Position.setPos(x - 1, y - 2));

        return positions;
    }


}
