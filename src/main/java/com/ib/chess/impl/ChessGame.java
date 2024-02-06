package com.ib.chess.impl;

import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

/**
 * ChessGame.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.board
 * @created Feb 01, 2024
 */

@Component
public class ChessGame
{
    public Square[][] moveCoin(Coin coin, Constance.Position movingPosition, Square[][] currentBoard, Set<Constance.Position> possibleMoves)
    {
        System.out.println("currentBoard in move coin = " + Arrays.deepToString(currentBoard));
        Constance.Position currentPosition = coin.getCurrentPosition();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        int movingX = movingPosition.getX();
        int movingY = movingPosition.getY();

        Square currentSquare = getSquare(currentBoard,x, y);
        Square movingSquare = getSquare(currentBoard,movingX, movingY);

        System.out.println("current square = " + currentSquare);
        System.out.println("movingSquare = " + movingSquare);


//        moving square
        if (!possibleMoves.contains(movingPosition))
            return currentBoard;

        System.out.println("movingSquare after move = " + movingSquare);

        currentBoard[movingX][movingY] = movingSquare;

        // currentSquare

        currentSquare.setCoin(null);
        currentSquare.setCoinIsPresent(false);
        currentBoard[x][y] = currentSquare;

//        System.out.println("#######coin = " + coin);
        System.out.println("currentSquare after move = " + currentSquare);

        return currentBoard;
//        System.out.println("c = " + Arrays.deepToString(currentBoard));
    }

    public Square getSquare(Square[][] currentBoard,int x, int y)
    {
         return currentBoard[x][y];
    }

}
