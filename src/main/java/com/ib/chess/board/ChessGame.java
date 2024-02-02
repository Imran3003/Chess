package com.ib.chess.board;

import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

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
    private Square[][] currentBoard;

    public void initializeCurrentBoard()
    {
        currentBoard = DefaultChessBoard.getDefaultBoard();
    }

    public Square[][] getCurrentBoard()
    {
        return currentBoard;
    }

    public void moveCoin(Coin coin, Constance.Position movingPosition)
    {
        Constance.Position currentPosition = coin.getCurrentPosition();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        int movingX = movingPosition.getX();
        int movingY = movingPosition.getY();

        Square currentSquare = getSquare(x, y);
        Square movingSquare = getSquare(movingX, movingY);

        System.out.println("square = " + currentSquare);
        System.out.println("movingSquare = " + movingSquare);


//        moving square
        coin.setCurrentPosition(Constance.Position.setPos(movingX,movingY));
        movingSquare.setCoin(coin);
        movingSquare.setCoinIsPresent(true);

        System.out.println("movingSquare after move = " + movingSquare);

        currentBoard[movingX][movingY] = movingSquare;

        // currentSquare

        currentSquare.setCoin(null);
        currentSquare.setCoinIsPresent(false);
        currentBoard[x][y] = currentSquare;

//        System.out.println("#######coin = " + coin);
        System.out.println("currentSquare after move = " + currentSquare);

//        System.out.println("c = " + Arrays.deepToString(currentBoard));
    }

    public Square getSquare(int x, int y)
    {
         return currentBoard[x][y];
    }

}
