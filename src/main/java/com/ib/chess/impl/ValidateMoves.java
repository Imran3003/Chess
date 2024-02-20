package com.ib.chess.impl;

import com.ib.chess.modules.Coin;
import com.ib.chess.modules.PreviousMove;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ib.chess.modules.Constance.Coins.KING;
import static com.ib.chess.modules.Constance.*;
import static com.ib.chess.modules.Constance.Coins.PAWN;
import static com.ib.chess.modules.Constance.MovementDirection.*;

/**
 * ValidateMoves.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.impl
 * @created Jan 16, 2024
 */

@Component
public class ValidateMoves {

    public  Set<Position> getPossibleMoves(Coin coin, Square[][] board, boolean isKingPositionIsReq)
    {
        System.out.println("getPossibleMoves() ");
        Position currentPosition = coin.getCurrentPosition();
        Map<MovementDirection, Integer> moveDirVsSteps = coin.getMoveDirVsSteps();

        Set<Position> coinPossiblePositions = new HashSet<>();
        moveDirVsSteps.forEach((direction, steps) -> {
            List<Position> positions = getPossiblePositions(direction, steps, currentPosition,board,coin.getCoinColour(),isKingPositionIsReq);
            coinPossiblePositions.addAll(positions);
        });


        return  checkKingIsSafeAndFilterMoves(board,coinPossiblePositions);

    }

    private static List<Position> getPossiblePositions(MovementDirection direction, int steps, Position currentPosition, Square[][] board, Colours coinColour, boolean isKingPositionIsReq) {
        switch (direction) {
            case LEFT:
            case RIGHT:
            case FORWARD:
            case BACKWARD:
                return linearMove(direction, steps, currentPosition,board,coinColour,isKingPositionIsReq);
            case PAWN_MOVE:
                return pawnMove(currentPosition,board,isKingPositionIsReq);
            case CROSS_RIGHT_FORWARD:
            case CROSS_LEFT_FORWARD:
            case CROSS_RIGHT_BACKWARD:
            case CROSS_LEFT_BACKWARD:
                return diagonalMove(direction, steps, currentPosition,board,coinColour,isKingPositionIsReq);
            case KNIGHT_L_MOVE:
                return knightMove(currentPosition,board,coinColour,isKingPositionIsReq);
        }
        return Collections.emptyList();
    }

    private static List<Position> linearMove(MovementDirection direction, int steps, Position currentPosition, Square[][] board, Colours coinColour, boolean isKingPositionIsReq) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();
        int stepModifierX = (direction == RIGHT) ? 1 : (direction == LEFT) ? -1 : 0;
        int stepModifierY = (direction == FORWARD) ? 1 : (direction == BACKWARD) ? -1 : 0;


        for (int i = 0; i < steps; i++) {
            x += stepModifierX;
            y += stepModifierY;

            if (x < 0 || x > 7 || y < 0 || y > 7) {
                break;
            }

            Square square = board[x][y];

            if (square.isCoinIsPresent) {
                if (square.getCoin().coinColour == coinColour || (square.getCoin().getCoinName().equals(KING) && !isKingPositionIsReq))
                    break;
                else {
                    positions.add(Position.setPos(x, y));
                    break;
                }
            }
            positions.add(Position.setPos(x, y));
        }

        return positions;
    }

    private static List<Position> pawnMove(Position currentPosition, Square[][] board, boolean isKingPositionIsReq) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();
        Position defaultPosition = board[x][y].getCoin().defaultPosition;
        int defX = defaultPosition.getX();
        int forwardStep = (defX < 2) ? 2 : -2;

        // Starting move

        if (defX < 2)
        {
            if ((x == 1) && !((board[x + forwardStep][y].isCoinIsPresent) || (board[x + forwardStep / 2][y].isCoinIsPresent)))
            {
                positions.add(Position.setPos(x + forwardStep, y));
            }
        }
        else
        {
            if ((x == 6 && !((board[x + forwardStep][y].isCoinIsPresent) || (board[x + forwardStep / 2][y].isCoinIsPresent))))
            {
                positions.add(Position.setPos(x + forwardStep, y));
            }
        }
        // Forward move
        int step = (defX < 2) ? 1 : -1;
        int newX = x + step;

        if (isValidMove(newX, y) && !board[newX][y].isCoinIsPresent)
        {
            positions.add(Position.setPos(newX, y));
        }

        // Diagonal moves
        int diagonalStep = (defX < 2) ? 1 : -1;
        for (int diagonalMove : new int[]{1, -1}) {
            int newXDiagonal = x + step;
            int newYDiagonal = y + diagonalStep * diagonalMove;
            if (isValidMove(newXDiagonal, newYDiagonal) && hasOpponent(board, newXDiagonal, newYDiagonal,isKingPositionIsReq) && !board[newXDiagonal][newYDiagonal].coin.getCoinColour().equals(board[x][y].coin.getCoinColour())) {
                positions.add(Position.setPos(newXDiagonal, newYDiagonal));
            }
        }


        return positions;
    }

    public List<Position> pawnSplMove(PreviousMove previousMove, Coin coin)
    {
        System.out.println("inside pawn spl move");
        List<Position> positions = new ArrayList<>();

        if (previousMove.getTo().getX() != coin.getCurrentPosition().getX() || coin.getCurrentPosition().getX() != 3 && coin.getCurrentPosition().getX() != 4)
            return positions;

        Position previousMoveTo = previousMove.getTo();
        int prevX = previousMoveTo.getX();
        int prevY = previousMoveTo.getY();

        System.out.println("prevY = " + prevY);

        Position currentPosition = coin.getCurrentPosition();
        int clickedX = currentPosition.getX();
        int clickedY = currentPosition.getY();

        System.out.println("clickedY = " + clickedY);

        if (clickedY-1 != prevY && clickedY +1 != prevY)
            return positions;

        if (clickedY-1 == prevY)
        {
            if(coin.getDefaultPosition().getX() < 2)
            {
                positions.add(Position.setPos(clickedX +1 , clickedY - 1));
            }
            else
            {
                positions.add(Position.setPos(clickedX - 1 , clickedY - 1));

            }
        }
        else
        {
            if(coin.getDefaultPosition().getX() < 2)
            {
                positions.add(Position.setPos(clickedX +1 , clickedY + 1));
            }
            else
            {
                positions.add(Position.setPos(clickedX - 1 , clickedY + 1));

            }
        }

        return positions;
        /*
        * previos move == pawn
        * clicked coin.position.y-1 || y+1 == previousMove.position --> eligible
        *
        * if(y-1)
        *
        *    if(x < 2)
        *      position.add(currenPos.x+1,y-1)
        *
        *     else
        *       position.add(currenPos.x-1,y-1)
        * else
        *    if(x < 2)
        *      position.add(currenPos.x+1,y+1)
        *
        *     else
        *       position.add(currenPos.x-1,y+1)
        *
        * && null this opponentPawn and move own pawn to the above mentioned place.


        * */
    }

    private void crosslineMove()
    {
        /*
        * if(clicked  coin is king)
        * leftCrossLine
        *    king's moveCount == 0 && leftRook's moveCount == 0 && between king and leftRook coins are null* --> eligible */
    }
    private void checkAnyCheck()
    {
        /*
        * get own coin colour
        *
        * iterate chess board and get each sqare and check if coin is present and if coin is present then the coin colour = opp coin colour
        * get Opponent coins All possible Moves and call getPossibleMovesMethod , tag isKingPos is Req = true
        *
        * if(possible moves.contains kings position then it is check.
        * */
    }

    private static boolean isValidMove(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    private static boolean hasOpponent(Square[][] board, int x, int y,boolean isKingPosIsReq) {
        if (isKingPosIsReq)
            return isValidMove(x, y) && board[x][y].isCoinIsPresent;
        return isValidMove(x, y) && board[x][y].isCoinIsPresent && !board[x][y].coin.coinName.equals(KING);
    }

    private static List<Position> diagonalMove(MovementDirection direction, int steps, Position currentPosition, Square[][] board, Colours coinColour, boolean isKingPositionIsReq) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();
        int stepModifierX = (direction == CROSS_RIGHT_FORWARD || direction == CROSS_LEFT_BACKWARD) ? 1 :
                (direction == CROSS_LEFT_FORWARD || direction == CROSS_RIGHT_BACKWARD) ? -1 : 0;
        int stepModifierY = (direction == CROSS_RIGHT_FORWARD || direction == CROSS_LEFT_FORWARD) ? 1 :
                (direction == CROSS_RIGHT_BACKWARD || direction == CROSS_LEFT_BACKWARD) ? -1 : 0;

        for (int i = 0; i < steps; i++) {
            x += stepModifierX;
            y += stepModifierY;

            if (x < 0 || x > 7 || y < 0 || y > 7) {
                break;
            }
            Square square = board[x][y];

            if (square.isCoinIsPresent)
                if (square.getCoin().coinColour == coinColour || (square.getCoin().getCoinName().equals(KING) && !isKingPositionIsReq))
                    break;
                else
                {
                    positions.add(Position.setPos(x, y));
                    break;
                }

            positions.add(Position.setPos(x, y));
        }

        return positions;
    }

    private static List<Position> knightMove(Position currentPosition, Square[][] board, Colours coinColour, boolean isKingPositionIsReq) {
        List<Position> positions = new ArrayList<>();

        int x = currentPosition.getX();
        int y = currentPosition.getY();

        int[][] knightMoves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        for (int[] move : knightMoves) {
            int newX = x + move[0];
            int newY = y + move[1];


            if (newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7)
            {
                Square square = board[newX][newY];

                if (square.isCoinIsPresent)
                    if (square.getCoin().coinColour == coinColour || (square.getCoin().getCoinName().equals(KING) && !isKingPositionIsReq))
                        continue;
                positions.add(Position.setPos(newX, newY));
            }
        }

        return positions;
    }

    private Set<Position> checkKingIsSafeAndFilterMoves(Square[][] board, Set<Position> coinPossiblePositions)
    {
        return coinPossiblePositions;
    }

    public void cross_line_move(Map<Coins, Map<Position, Integer>> monitoringMoves, Square[][] chessboard, Coin clickedCoin)
    {
        Integer pos = monitoringMoves.getOrDefault(KING, Collections.emptyMap()).getOrDefault(clickedCoin.getDefaultPosition(),null);


    }
}

