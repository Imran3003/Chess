package com.ib.chess;

import com.ib.chess.board.ChessGame;
import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.impl.ValidateMoves;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.ib.chess.impl.ValidateMoves.filter_moves;
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

        ChessGame chessGame = new ChessGame();
        chessGame.initializeCurrentBoard();

        chessGame.moveCoin(extracted(), Constance.Position.setPos(0,0));

        filter_moves();
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

        coin.setDefaultPosition(Constance.Position.setPos(0,6));
        coin.setCurrentPosition(Constance.Position.setPos(5,6));

        Map<Constance.MovementDirection,Integer> moveDirVsSteps = new HashMap<>();
        moveDirVsSteps.put(KNIGHT_L_MOVE,3);
        coin.setMoveDirVsSteps(moveDirVsSteps);

        return coin;

    }
}
