package com.ib.chess;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.impl.ValidateMoves;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;

import java.util.*;

import static com.ib.chess.modules.Constance.MovementDirection.*;
import static com.ib.chess.modules.Constance.MovementDirection.CROSS_LEFT_FORWARD;

/**
 * TestMain.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess
 * @created Jan 16, 2024
 */
public class TestMain
{
    public static void main(String[] args)
    {

//        extracted();
//        getDefaultBoard();
        getPossibleMv();
    }

    private static void getPossibleMv()
    {
        Set<Constance.Position> possibleMoves = ValidateMoves.getPossibleMoves(extracted());
        System.out.println("possibleMoves = " + possibleMoves);
    }
    private static void validateMoves() {
    }

    private static void getDefaultBoard()
    {
        Square[][] defaultBoard = DefaultChessBoard.getDefaultBoard();
        System.out.println("defaultBoard = " + Arrays.deepToString(defaultBoard));
    }

    private static Coin extracted() {
        Coin coin = new Coin();

        coin.setCoinColour(Constance.Colours.BLACK);
        coin.setCoinName(Constance.Coins.KNIGHT);

        coin.setDefaultPosition(Constance.Position.setPos(0,5));
        coin.setCurrentPosition(Constance.Position.setPos(1,6));

        Map<Constance.MovementDirection,Integer> moveDirVsSteps = new HashMap<>();
        moveDirVsSteps.put(CROSS_LEFT_FORWARD,7);
        moveDirVsSteps.put(CROSS_LEFT_BACKWARD,7);
        moveDirVsSteps.put(CROSS_RIGHT_BACKWARD,7);
        moveDirVsSteps.put(CROSS_RIGHT_FORWARD,7);
        coin.setMoveDirVsSteps(moveDirVsSteps);

        return coin;

    }
}
