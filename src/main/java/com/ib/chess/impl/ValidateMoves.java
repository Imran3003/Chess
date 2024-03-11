package com.ib.chess.impl;

import com.ib.chess.modules.Coin;
import com.ib.chess.modules.PreviousMove;
import com.ib.chess.modules.Square;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ib.chess.modules.Constance.Coins.KING;
import static com.ib.chess.modules.Constance.Coins.ROOK;
import static com.ib.chess.modules.Constance.*;
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
        System.out.println("getPossibleMoves() ::  " + coin.getCoinColour() + "_" +coin.getCoinName());
        Position currentPosition = coin.getCurrentPosition();
        Map<MovementDirection, Integer> moveDirVsSteps = coin.getMoveDirVsSteps();

        Set<Position> coinPossiblePositions = new HashSet<>();
        moveDirVsSteps.forEach((direction, steps) -> {
            List<Position> positions = getPossiblePositions(direction, steps, currentPosition,board,coin.getCoinColour(),isKingPositionIsReq);
            coinPossiblePositions.addAll(positions);
        });

        return  coinPossiblePositions;

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

        if (previousMove.getTo().getX() != coin.getCurrentPosition().getX() || coin.getCurrentPosition().getX() != 3 && coin.getCurrentPosition().getX() != 4 || coin.getCurrentPosition() != coin.getDefaultPosition())
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
    }

    public List<Position> castLing(Coin coin, Square[][] chessboard)
    {

        List<Position> castLingPosition = new ArrayList<>();

        System.out.println("inside castling " );

        System.out.println("coin = " + coin);

        if (coin.getCurrentPosition() != coin.getDefaultPosition() || coin.getMoveCount() != 0)
            return  castLingPosition;

        Map<String,Position> castLingRookMap = new HashMap<>();

        castLingRookMap.put("TOP_LEFT_ROOK",Position.setPos(0,0));
        castLingRookMap.put("TOP_RIGHT_ROOK",Position.setPos(0,7));
        castLingRookMap.put("BOTTOM_LEFT_ROOK",Position.setPos(7,0));
        castLingRookMap.put("BOTTOM_RIGHT_ROOK",Position.setPos(7,7));

        System.out.println("&*********************$");
        System.out.println("castLingRookMap = " + castLingRookMap);
        Coin leftRook;
        Coin rightRook;

        if (coin.getDefaultPosition().getX() < 2)
        {
            System.out.println("coin.getDefaultPos < 2");
            Position topLeftRookPos = castLingRookMap.get("TOP_LEFT_ROOK");
            leftRook = chessboard[topLeftRookPos.getX()][topLeftRookPos.getY()].getCoin();
            System.out.println("leftRook = " + leftRook);

            Position topRightRookPos = castLingRookMap.get("TOP_RIGHT_ROOK");
            rightRook = chessboard[topRightRookPos.getX()][topRightRookPos.getY()].getCoin();
            System.out.println("rightRook = " + rightRook);
        }
        else
        {
            System.out.println("coin.getDefaultPos > 2");
            Position bottomLeftRookPos = castLingRookMap.get("BOTTOM_LEFT_ROOK");
            leftRook = chessboard[bottomLeftRookPos.getX()][bottomLeftRookPos.getY()].getCoin();

            Position bottomRightRookPos = castLingRookMap.get("BOTTOM_RIGHT_ROOK");
            rightRook = chessboard[bottomRightRookPos.getX()][bottomRightRookPos.getY()].getCoin();
        }


        System.out.println("rightRook = " + rightRook);
        System.out.println("leftRook = " + leftRook);

        if (leftRook.getCoinName() == ROOK && leftRook.getDefaultPosition() == leftRook.getCurrentPosition() && leftRook.getMoveCount() == 0)
            anyCoinsBtwnAndAddPositions(coin.getCurrentPosition(), chessboard, "LEFT",castLingPosition);


        if (rightRook.getCoinName() == ROOK && rightRook.getDefaultPosition() == rightRook.getCurrentPosition() && rightRook.getMoveCount() == 0)
            anyCoinsBtwnAndAddPositions(coin.getCurrentPosition(), chessboard, "RIGHT", castLingPosition);

        return castLingPosition;
    }

    private void anyCoinsBtwnAndAddPositions(Position kingPosition, Square[][] chessboard, String direction, List<Position> castLingPositions)
    {
        System.out.println("inside anyCoinsBtwnAndAddPositions direction = " + direction);

        Square coin1;
        Square coin2;
        Square coin3;

        if (direction.equals("RIGHT"))
        {
            if (kingPosition.getX() < 2)
            {
                System.out.println("kingPosition.getX() < 2");

                coin1 = chessboard[kingPosition.getX()][kingPosition.getY()+1];
                coin2 = chessboard[kingPosition.getX()][kingPosition.getY()+2];
                coin3 = chessboard[kingPosition.getX()][kingPosition.getY()+3];
            }
            else
            {
                System.out.println("kingPosition.getX() > 2");
                coin1 = chessboard[kingPosition.getX()][kingPosition.getY()-1];
                coin2 = chessboard[kingPosition.getX()][kingPosition.getY()-2];
                coin3 = chessboard[kingPosition.getX()][kingPosition.getY()-3];
            }
        }
        else
        {
            System.out.println("Else");
            if (kingPosition.getX() < 2)
            {
                System.out.println("kingPosition.getX() < 2");
                coin1 = chessboard[kingPosition.getX()][kingPosition.getY()-1];
                coin2 = chessboard[kingPosition.getX()][kingPosition.getY()-2];
                coin3 = chessboard[kingPosition.getX()][kingPosition.getY()-3];
            }
            else
            {
                System.out.println("kingPosition.getX() > 2");

                coin1 = chessboard[kingPosition.getX()][kingPosition.getY()+1];
                coin2 = chessboard[kingPosition.getX()][kingPosition.getY()+2];
                coin3 = chessboard[kingPosition.getX()][kingPosition.getY()+3];

            }
        }

        System.out.println("coin1 = " + coin1);
        System.out.println("coin2 = " + coin2);
        System.out.println("coin2 = " + coin3);

        if (!coin1.isCoinIsPresent && !coin2.isCoinIsPresent) {
            int coin3Y = coin3.getSquarePosition().getY();
            if (coin3Y == 0 || coin3Y == 7 || !coin3.isCoinIsPresent) {
                castLingPositions.add(Position.setPos(kingPosition.getX(), coin2.getSquarePosition().getY()));
            }
        }

        System.out.println("castLingPositions = " + castLingPositions);
    }
    public boolean checkAnyCheck(Square[][] chessboard, Colours coinColour, Position kingPosition)
    {
        System.out.println("Inside checkAnyCheck");

        System.out.println("KingPosition :::::: " + kingPosition);

        for (Square[] squares : chessboard)
        {
            for (Square square : squares)
            {
                if (!square.isCoinIsPresent || square.getCoin() == null)
                {
                    System.out.println("square in checkAnyCheck coin not present ");

                    continue;
                }

                System.out.println(" coin is present :: " + square );

                if (square.getCoin().getCoinColour() == coinColour)
                {
                    System.out.println("square in checkAnyCheck coin colour same ");

                    continue;
                }

                Set<Position> possibleMoves = getPossibleMoves(square.getCoin(), chessboard, true);

                if (possibleMoves.contains(kingPosition))
                    return true;

                }

        }
        return false;
        /*
        * get own coin colour
        *
        * iterate chess board and get each sqare and check if coin is present and if coin is present then the coin colour = opp coin colour
        * get Opponent coins All possible Moves and call getPossibleMovesMethod , tag isKingPos is Req = true
        *
        * if(possible moves.contains kings position then it is check.
        * */
    }

    public Map<Position, List<Position>> getCoinVsMoves(Square[][] chessboard, Colours coinColour, Position kingPosition)
    {
        System.out.println("Inside  CoinVsMoves" );

        Map<Position,List<Position>> coinVsMove = new HashMap<>();
        // white
        for (Square[] squares : chessboard)
        {
            for (Square square : squares)
            {
                if (!square.isCoinIsPresent || square.getCoin().getCoinColour() != coinColour)
                    continue;

                Set<Position> possibleMoves = getPossibleMoves(square.getCoin(), chessboard, false);

                List<Position> positions = checkAnyCheckWithBlockingPosition(possibleMoves, chessboard, coinColour, kingPosition, square.getCoin());

                coinVsMove.put(square.getSquarePosition(), positions);

            }
        }

        System.out.println("coinVsMove = " + coinVsMove);
        return coinVsMove;
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
    public Set<Position> filterPossibleMoves(Set<Position> possibleMoves, Map<Position, List<Position>> coinVsMove, Position currentPosition)
    {
        System.out.println("inside filterPossibleMoves  ");

        System.out.println("possibleMoves = " + possibleMoves + " ::: " + "coinVsMove = " + coinVsMove + " :::: " + "current Position = " + currentPosition );

        Set<Position> filteredPosition = new HashSet<>();

        for (Position possibleMove : possibleMoves)
        {
            List<Position> positions = coinVsMove.get(currentPosition);

            if (positions.contains(possibleMove))
                    filteredPosition.add(possibleMove);

        }

        return filteredPosition;
    }

    private List<Position> checkAnyCheckWithBlockingPosition(Set<Position> possibleMoves, Square[][] chessboard, Colours coinColour, Position kingPosition, Coin coin)
    {
        List<Position> filterPositions = new ArrayList<>();

        for (Position possibleMove : possibleMoves)
        {
            Position kingNewPosition = kingPosition;

            Square possibleSq = chessboard[possibleMove.getX()][possibleMove.getY()];

            if (coin.getCoinName() == KING)
                kingNewPosition = possibleSq.getSquarePosition();


            boolean coinStatusForPossibleSq = possibleSq.isCoinIsPresent;
            Coin coinForPossibleSq = possibleSq.getCoin();

            Position oldCoinPosition = coin.getCurrentPosition();
            Square oldCoinSq = chessboard[oldCoinPosition.getX()][oldCoinPosition.getY()];
            boolean oldSqCoinStatus = chessboard[oldCoinPosition.getX()][oldCoinPosition.getY()].isCoinIsPresent;

            possibleSq.isCoinIsPresent = true;
            possibleSq.setCoin(coin);

            oldCoinSq.setCoin(null);
            oldCoinSq.setCoinIsPresent(false);

            boolean ch = checkAnyCheck(chessboard, coinColour, kingNewPosition);

            possibleSq.isCoinIsPresent = coinStatusForPossibleSq;
            possibleSq.setCoin(coinForPossibleSq);
            oldCoinSq.setCoin(coin);
            oldCoinSq.setCoinIsPresent(oldSqCoinStatus);

            if (ch)
                continue;

            filterPositions.add(possibleMove);

        }

        return filterPositions;
    }
}

