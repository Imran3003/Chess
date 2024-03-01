package com.ib.chess;

import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;

import java.util.HashMap;
import java.util.Map;

import static com.ib.chess.modules.Constance.MovementDirection.KNIGHT_L_MOVE;

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
        boolean a = false;
        System.out.println("a = " + a);
        getPossibleMv(a);
        System.out.println("a = " + a);
//        filter_moves();
    }

    private static void getPossibleMv(boolean a)
    {
        a = true;
//        Set<Constance.Position> possibleMoves = ValidateMoves.getPossibleMoves(extracted(),new Square[][]{});
//        System.out.println("possibleMoves = " + possibleMoves);
    }
    private static void validateMoves() {
    }

    private static void getDefaultBoard()
    {
//        Square[][] defaultBoard = DefaultChessBoard.getDefaultBoard();
//        System.out.println("defaultBoard = " + Arrays.deepToString(defaultBoard));
    }

    private static Coin extracted() {
        Coin coin = new Coin();

        coin.setCoinColour(Constance.Colours.BLACK);
        coin.setCoinName(Constance.Coins.KNIGHT);

        coin.setDefaultPosition(Constance.Position.setPos(0,6));
        coin.setCurrentPosition(Constance.Position.setPos(5,6));

        Map<Constance.MovementDirection,Integer> moveDirVsSteps = new HashMap<>();
        moveDirVsSteps.put(KNIGHT_L_MOVE,3);
        coin.setMoveDirVsSteps(moveDirVsSteps);

        return coin;

    }
}
