package com.ib.chess.board;

import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.ib.chess.modules.Constance.*;
import static com.ib.chess.modules.Constance.Coins.*;
import static com.ib.chess.modules.Constance.Colours.BLACK;
import static com.ib.chess.modules.Constance.Colours.WHITE;
import static com.ib.chess.modules.Constance.MovementDirection.*;
import static com.ib.chess.modules.Constance.Position.*;

/**
 * DefaultChessBoard.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.board
 * @created Jan 16, 2024
 */

@Component
public class DefaultChessBoard {
    public  Square[][] getDefaultBoard()
    {
        Square[][] squares = new Square[8][8];

        setWhiteAndBlackCoins(squares);

        setEmptySpaces(squares);

        return squares;
    }

    private static void setEmptySpaces(Square[][] squares)
    {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares.length; j++) {
                if(squares[i][j] == null)
                {
                    squares[i][j] = new Square(null,false,Position.setPos(i,j));
                }
            }
        }
    }

    private static void setWhiteAndBlackCoins(Square[][] board)
    {
        setDefaultCoins(board, A1, B1, C1, D1, E1, F1, G1, H1, WHITE, A2, B2, C2, D2, E2, F2, G2, H2);
        setDefaultCoins(board, A8, B8, C8, D8, E8, F8, G8, H8, BLACK, A7, B7, C7, D7, E7, F7, G7, H7);
    }

    private static void setDefaultCoins(Square[][] board, Position position1, Position position2, Position position3, Position position4, Position position5, Position position6, Position position7, Position position8, Colours colour, Position position9, Position position10, Position position11, Position position12, Position position13, Position position14, Position position15, Position position16)
    {
        Map<Position, Coins> coinsVsPos = new HashMap<>();
        coinsVsPos.put(position1,ROOK);
        coinsVsPos.put(position2,KNIGHT);
        coinsVsPos.put(position3,BISHOP);
        coinsVsPos.put(position4,KING);
        coinsVsPos.put(position5,QUEEN);
        coinsVsPos.put(position6,BISHOP);
        coinsVsPos.put(position7,KNIGHT);
        coinsVsPos.put(position8,ROOK);

        setMainCoins(board,coinsVsPos,colour);
        setPawns(board, colour, position9, position10, position11, position12, position13, position14, position15, position16);
    }

    private static void setMainCoins(Square[][] board, Map<Position,Coins> coinsPositionMap, Colours colour) {

        coinsPositionMap.forEach((pos,coinName)->
                setCoins(board, colour, pos,coinName));
    }

    private static void setCoins(Square[][] board, Colours colour, Position position, Coins coinName)
    {
            int x = position.getX();
            int y = position.getY();
            board[x][y] = new Square(new Coin(coinName,colour,position,position,setCoinMoves(coinName)),true,Position.setPos(x,y));

    }

    private static void setPawns(Square[][] squares, Colours colour, Position... positions) {
        for (Position position : positions) {
            int x = position.getX();
            int y = position.getY();
            squares[x][y] = new Square(new Coin(PAWN,colour,position,position,setCoinMoves(PAWN)),true,Position.setPos(x,y));
        }
    }
    public static Map<MovementDirection, Integer> setCoinMoves(Coins coin)
    {
        Map<MovementDirection,Integer> moveDirVsSteps = new HashMap<>();

        switch (coin)
        {
            case KING:
            {
                moveDirVsSteps.put(LEFT,1);
                moveDirVsSteps.put(RIGHT,1);
                moveDirVsSteps.put(FORWARD,1);
                moveDirVsSteps.put(BACKWARD,1);
                moveDirVsSteps.put(CROSS_LEFT_BACKWARD,1);
                moveDirVsSteps.put(CROSS_RIGHT_BACKWARD,1);
                moveDirVsSteps.put(CROSS_RIGHT_FORWARD,1);
                moveDirVsSteps.put(CROSS_LEFT_FORWARD,1);
                moveDirVsSteps.put(KING_CROSS_LINE,2);
                return moveDirVsSteps;
            }

            case ROOK:
            {
                moveDirVsSteps.put(LEFT,7);
                moveDirVsSteps.put(RIGHT,7);
                moveDirVsSteps.put(FORWARD,7);
                moveDirVsSteps.put(BACKWARD,7);
                return moveDirVsSteps;

            }

            case KNIGHT:
            {
                moveDirVsSteps.put(KNIGHT_L_MOVE,3);
                return moveDirVsSteps;
            }
            case QUEEN:
            {
                moveDirVsSteps.put(LEFT,7);
                moveDirVsSteps.put(RIGHT,7);
                moveDirVsSteps.put(FORWARD,7);
                moveDirVsSteps.put(BACKWARD,7);
                moveDirVsSteps.put(CROSS_LEFT_BACKWARD,7);
                moveDirVsSteps.put(CROSS_RIGHT_BACKWARD,7);
                moveDirVsSteps.put(CROSS_RIGHT_FORWARD,7);
                moveDirVsSteps.put(CROSS_LEFT_FORWARD,7);
                return moveDirVsSteps;
            }
            case BISHOP:
            {
                moveDirVsSteps.put(CROSS_LEFT_FORWARD,7);
                moveDirVsSteps.put(CROSS_LEFT_BACKWARD,7);
                moveDirVsSteps.put(CROSS_RIGHT_BACKWARD,7);
                moveDirVsSteps.put(CROSS_RIGHT_FORWARD,7);
                return moveDirVsSteps;
            }

            case PAWN:
            {
                moveDirVsSteps.put(PAWN_MOVE,2);
                return moveDirVsSteps;
            }
        }
        return new HashMap<>();
    }
}
