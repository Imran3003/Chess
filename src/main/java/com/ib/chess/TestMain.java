package com.ib.chess;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;

import java.util.Arrays;

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
        getDefaultBoard();
    }

    private static void validateMoves() {
    }

    private static void getDefaultBoard()
    {
        Square[][] defaultBoard = DefaultChessBoard.getDefaultBoard();
        System.out.println("defaultBoard = " + Arrays.deepToString(defaultBoard));
    }

    private static void extracted() {
        Coin coin = new Coin();

        coin.setCoinColour(Constance.Colours.BLACK);
        coin.setCoinName(Constance.Coins.ROOK);

        coin.setDefaultPosition(Constance.Position.setPos(0,0));
        Constance.Position pos = Constance.Position.getPos(1, 0);
        System.out.println("pos = " + pos);


        Constance.Position pos1 = Constance.Position.getPos("A8");
        System.out.println("pos1 = " + pos1);


        System.out.println("coin = " + coin);
    }
}
