package com.ib.chess.impl;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Constance.Coins;
import com.ib.chess.modules.Constance.Position;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

import java.util.Map;
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

    public Square[][] moveCoin(Coin coin, Position movingPosition, Square[][] currentBoard, Set<Position> possibleMoves, boolean[] coinIsMoved)
    {
        Position pawnSplPosition = checkIsPawsSplMove(coin, movingPosition, currentBoard);
        boolean isCastLine = checkIsCastLine(coin, movingPosition);

        System.out.println("isCastLine = " + isCastLine);

        if (isCastLine)
            doCastLing(coin, movingPosition, currentBoard);

        if (pawnSplPosition != null)
        {
            currentBoard[pawnSplPosition.getX()][pawnSplPosition.getY()].setCoin(null);
            currentBoard[pawnSplPosition.getX()][pawnSplPosition.getY()].setCoinIsPresent(false);
        }

        System.out.println("isPawsSplMove = " + pawnSplPosition);

//        System.out.println("currentBoard in move coin = " + Arrays.deepToString(currentBoard));
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
        coin.setMoveCount(coin.getMoveCount() + 1);
        movingSquare.setCoinIsPresent(true);

        movingSquare.setCoin(coin);

        System.out.println("movingSquare after move = " + movingSquare);

        currentBoard[movingX][movingY] = movingSquare;

        // currentSquare

        currentSquare.setCoin(null);
        currentSquare.setCoinIsPresent(false);
        currentBoard[x][y] = currentSquare;

//        System.out.println("#######coin = " + coin);
        System.out.println("currentSquare after move = " + currentSquare);


        coinIsMoved[0] = true;
        
        return currentBoard;
        
//        System.out.println("c = " + Arrays.deepToString(currentBoard));
    }

    private static void doCastLing(Coin coin, Position movingPosition, Square[][] currentBoard)
    {
        System.out.println("inside doCastLing = ");

        if (coin.getCurrentPosition().getY() + 2 == movingPosition.getY())
        {
            Square castLingSqure_rook = currentBoard[coin.getCurrentPosition().getX()][7];
            Coin castLingCoin_rook = currentBoard[coin.getCurrentPosition().getX()][7].getCoin();

            //move coin
            Square movingSqure = currentBoard[coin.getCurrentPosition().getX()][coin.getCurrentPosition().getY() + 1];
            castLingCoin_rook.setCurrentPosition(Position.setPos(coin.getCurrentPosition().getX(), coin.getCurrentPosition().getY() + 1));
            castLingCoin_rook.setMoveCount(castLingCoin_rook.getMoveCount() + 1);

            movingSqure.setCoin(castLingCoin_rook);
            movingSqure.setCoinIsPresent(true);

            currentBoard[movingSqure.getSquarePosition().getX()][movingSqure.getSquarePosition().getY()] = movingSqure;

            //oldRookPos
            castLingSqure_rook.setCoin(null);
            castLingSqure_rook.setCoinIsPresent(false);
            currentBoard[castLingSqure_rook.getSquarePosition().getX()][castLingSqure_rook.getSquarePosition().getY()] = castLingSqure_rook;
        } else {
            Square castLingSqure_rook = currentBoard[coin.getCurrentPosition().getX()][0];
            Coin castLingCoin_rook = currentBoard[coin.getCurrentPosition().getX()][0].getCoin();

            //move coin
            Square movingSqure = currentBoard[coin.getCurrentPosition().getX()][coin.getCurrentPosition().getY() - 1];
            castLingCoin_rook.setCurrentPosition(Position.setPos(coin.getCurrentPosition().getX(), coin.getCurrentPosition().getY() - 1));
            castLingCoin_rook.setMoveCount(castLingCoin_rook.getMoveCount() - 1);

            movingSqure.setCoin(castLingCoin_rook);
            movingSqure.setCoinIsPresent(true);

            currentBoard[movingSqure.getSquarePosition().getX()][movingSqure.getSquarePosition().getY()] = movingSqure;

            //oldRookPos
            castLingSqure_rook.setCoin(null);
            castLingSqure_rook.setCoinIsPresent(false);
            currentBoard[castLingSqure_rook.getSquarePosition().getX()][castLingSqure_rook.getSquarePosition().getY()] = castLingSqure_rook;
        }

    }

    private boolean checkIsCastLine(Coin coin, Position movingPosition) {
        return coin.getCoinName() == Coins.KING &&
                (coin.getCurrentPosition().getY() + 2 == movingPosition.getY() ||
                        coin.getCurrentPosition().getY() - 2 == movingPosition.getY());
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

    public Coin setSplCoin(Coins coins)
    {
        Map<Constance.MovementDirection, Integer> movementDirectionIntegerMap = DefaultChessBoard.setCoinMoves(coins);

        Coin coin = new Coin();
        coin.setMoveDirVsSteps(movementDirectionIntegerMap);
        coin.setCoinName(coins);
        return coin;
    }

}
