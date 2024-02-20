package com.ib.chess.impl;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Constance.Coins;
import com.ib.chess.modules.Constance.Position;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

import java.util.*;

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

    public Square[][] moveCoin(Coin coin, Position movingPosition, Square[][] currentBoard, Set<Position> possibleMoves)
    {
        Position position = checkIsPawsSplMove(coin, movingPosition, currentBoard);

        if (position != null)
        {
            currentBoard[position.getX()][position.getY()].setCoin(null);
            currentBoard[position.getX()][position.getY()].setCoinIsPresent(false);
        }

        System.out.println("isPawsSplMove = " + position);

        System.out.println("currentBoard in move coin = " + Arrays.deepToString(currentBoard));
        Position currentPosition = coin.getCurrentPosition();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        int movingX = movingPosition.getX();
        int movingY = movingPosition.getY();
        
        Square currentSquare = getSquare(currentBoard,x, y);
        Square movingSquare = getSquare(currentBoard,movingX, movingY);

        if (!possibleMoves.contains(movingPosition))
            return currentBoard;
        
//       moving square
        coin.setCurrentPosition(Position.setPos(movingX,movingY));
        movingSquare.setCoinIsPresent(true);

        if (coin.coinName == Coins.PAWN && movingSquare.getSquarePosition().getX() == 0 || movingSquare.getSquarePosition().getX() == 7)
        {
            System.out.println("is pawn ready to Queen");
            Coin selectedCoin = choose_special_coin();
            selectedCoin.setCoinName(selectedCoin.getCoinName());
            selectedCoin.setMoveDirVsSteps(selectedCoin.getMoveDirVsSteps());
            selectedCoin.setCoinColour(coin.getCoinColour());
            selectedCoin.setCurrentPosition(coin.getCurrentPosition());
            selectedCoin.setDefaultPosition(coin.getDefaultPosition());
            coin = selectedCoin;
        }

        System.out.println("coin after queen = " + coin);
        movingSquare.setCoin(coin);

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

    private Position checkIsPawsSplMove(Coin coin, Position movingPosition, Square[][] currentBoard)
    {
        if (coin.getCoinName() != Coins.PAWN || currentBoard[movingPosition.getX()][movingPosition.getY()].isCoinIsPresent)
           return null;

        int y = coin.getCurrentPosition().getY();

        int y1 = movingPosition.getY();

        if (y-1 != y1 && y+1 != y1)
            return null;

        return Position.setPos(coin.getCurrentPosition().getX(), y1);


    }

    public Square getSquare(Square[][] currentBoard,int x, int y)
    {
         return currentBoard[x][y];
    }


    public Coin choose_special_coin()
    {
        List<Coins> splCoins = new ArrayList<>();
        splCoins.add(Coins.QUEEN);
        splCoins.add(Coins.ROOK);
        splCoins.add(Coins.BISHOP);
        splCoins.add(Coins.KNIGHT);

        return setSplCoin(Coins.QUEEN);
    }

    public Coin setSplCoin(Coins coins)
    {
        Map<Constance.MovementDirection, Integer> movementDirectionIntegerMap = DefaultChessBoard.setCoinMoves(coins);

        Coin coin = new Coin();
        coin.setMoveDirVsSteps(movementDirectionIntegerMap);
        coin.setCoinName(coins);
        return coin;
    }

}
