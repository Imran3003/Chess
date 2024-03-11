package com.ib.chess.utils;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.modules.*;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static com.ib.chess.modules.Constance.*;
import static com.ib.chess.modules.Constance.Coins.KING;
import static com.ib.chess.modules.Constance.Coins.ROOK;
import static com.ib.chess.modules.Constance.Colours.BLACK;
import static com.ib.chess.modules.Constance.Colours.WHITE;
import static com.ib.chess.modules.Constance.MovementDirection.*;

/**
 * ValidateMoves.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.impl
 * @created Jan 16, 2024
 */

@Component
public class ChessUtils {

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

    public List<Position> getPawnSplPositions(PreviousMove previousMove, Coin coin)
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

    public List<Position> getCastLingPositions(Coin coin, Square[][] chessboard)
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
            validateNoCoinsBetweenRookAndKing(coin.getCurrentPosition(), chessboard, "LEFT",castLingPosition);


        if (rightRook.getCoinName() == ROOK && rightRook.getDefaultPosition() == rightRook.getCurrentPosition() && rightRook.getMoveCount() == 0)
            validateNoCoinsBetweenRookAndKing(coin.getCurrentPosition(), chessboard, "RIGHT", castLingPosition);

        return castLingPosition;
    }

    private void validateNoCoinsBetweenRookAndKing(Position kingPosition, Square[][] chessboard, String direction, List<Position> castLingPositions)
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
    public boolean validateCheckForKing(Square[][] chessboard, Colours coinColour, Position kingPosition)
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

                List<Position> positions = validateCheckWithBlockingCoin(possibleMoves, chessboard, coinColour, kingPosition, square.getCoin());

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
            List<Position> positions = coinVsMove.getOrDefault(currentPosition, Collections.emptyList());

            if (positions.contains(possibleMove))
                    filteredPosition.add(possibleMove);

        }

        return filteredPosition;
    }

    private List<Position> validateCheckWithBlockingCoin(Set<Position> possibleMoves, Square[][] chessboard, Colours coinColour, Position kingPosition, Coin coin)
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

            boolean ch = validateCheckForKing(chessboard, coinColour, kingNewPosition);

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

    public StringBuilder setCoinsInChessBoard(Square[][] board, Set<Position> possibleMoves, boolean isCoinIsMoved)
    {
        System.out.println("Inside SetCoinsInCHessBoard possiblePosision = " + possibleMoves);

        StringBuilder chessboardHtml = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Square square = board[i][j];

                Position squarePosition = square.getSquarePosition();

                String squareClass = (i + j) % 2 == 0 ? "white" : "black";

                String backgroundColor = "background-color: rgba(15, 236, 15, 0.47);";// Set the background color to green

                String squareHtml;

                String squareId = "square-" + i + "-" + j; // Create a unique id for each square

                String onclickAttribute = "onclick=\"handleSquareClick('" + i + "','" + j + "','" + squareId + "')\"";
                String onclickAttributeForCoin = "onclick=\"handleCoinClick('" + i + "','" + j + "','" + squareId + "')\"";
//                String onclickAttributeForEmptySquare = "onclick=\"emptySquare('" + i + "','" + j + "','" + squareId + "')\"";


                if (square.isCoinIsPresent) {
                    Coin coin = board[i][j].getCoin();
                    String pieceHtml = getPieceHtml(coin);

                    if (!possibleMoves.contains(squarePosition))
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, coin.getCoinColour(), coin.getCoinName(), onclickAttributeForCoin, pieceHtml);
                    else if (isCoinIsMoved)
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, coin.getCoinColour(), coin.getCoinName(), onclickAttributeForCoin, pieceHtml);
                    else
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" style=\"%s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, backgroundColor, coin.getCoinColour(), coin.getCoinName(), onclickAttribute, pieceHtml);

                    chessboardHtml.append(squareHtml);
                } else {
                    if (!possibleMoves.contains(squarePosition))
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" ></div>", squareId, squareClass);
                    else if (isCoinIsMoved)
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" %s></div>", squareId, squareClass, onclickAttribute);
                    else
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" style=\"%s\" %s></div>", squareId, squareClass, backgroundColor, onclickAttribute);

                    chessboardHtml.append(squareHtml);
                }
            }
        }
        return chessboardHtml;
    }

    private String getPieceHtml(Coin coin) {
        String pieceSymbol = "";

        switch (coin.getCoinName()) {
            case KING:
                pieceSymbol = (coin.getCoinColour() == WHITE) ? "&#9812;" : "&#9818;";
                break;
            case QUEEN:
                pieceSymbol = (coin.getCoinColour() == WHITE) ? "&#9813;" : "&#9819;";
                break;
            case ROOK:
                pieceSymbol = (coin.getCoinColour() == WHITE) ? "&#9814;" : "&#9820;";
                break;
            case BISHOP:
                pieceSymbol = (coin.getCoinColour() == WHITE) ? "&#9815;" : "&#9821;";
                break;
            case KNIGHT:
                pieceSymbol = (coin.getCoinColour() == WHITE) ? "&#9816;" : "&#9822;";
                break;
            case PAWN:
                pieceSymbol = (coin.getCoinColour() == WHITE) ? "&#9817;" : "&#9823;";
                break;
        }

        return pieceSymbol;
    }

    public String createPromotionPopUp(Position position, Colours colour)
    {
        System.out.println(" Inside createPromotionPopUp for position = " + position + "&& colour = " + colour);

        int i = position.getX();
        int j = position.getY();

        String queenPieceSymbol  = "&#9813;";
        String rookPieceSymbol   = "&#9814;";
        String knightPieceSymbol = "&#9816;";
        String bishopPieceSymbol = "&#9815;";
        String listItemStyle = "list-style-type: none; cursor: pointer; border: 1px solid black; margin-top: -1px; background-color: #3b6205; padding: 12px; border-radius: 10px;";
        String hoverEffect = "transition: background-color 0.3s ease;";

        if (colour == BLACK) {
            queenPieceSymbol = "&#9819;";
            rookPieceSymbol  = "&#9820;";
            knightPieceSymbol = "&#9822;";
            bishopPieceSymbol = "&#9821;";
        }

        return "<div>\n" +
                "<ul style=\"padding: 0;\" id=\"promotionList\">" +
                "    <li id=\"QUEEN\"  style=\"" + listItemStyle + hoverEffect + "\" onclick=\"pawnPromotion('" + i + "','" + j + "','QUEEN')\">" + queenPieceSymbol +"</li>\n" +
                "    <li id=\"ROOK\"  style=\"" + listItemStyle + hoverEffect + "\" onclick=\"pawnPromotion ('" + i + "','" + j + "','ROOK')\">" + rookPieceSymbol +"</li>\n" +
                "    <li id=\"KNIGHT\"  style=\"" + listItemStyle + hoverEffect + "\" onclick=\"pawnPromotion('"+ i + "','" + j + "','KNIGHT')\">" + knightPieceSymbol + "</li>\n" +
                "    <li id=\"BISHOP\" style=\"" + listItemStyle + hoverEffect + "\" onclick=\"pawnPromotion('" + i + "','" + j + "','BISHOP')\">" + bishopPieceSymbol + "</li>\n" +
                "</ul>"+
                "</div>\n";
    }

    public String createMatePopUP(String message)
    {

        return "<div>\n" +
                "<p>" + message + "</p> \n" +
                "</div> \n";
    }

    public boolean validateCheckMate(Map<Position, List<Position>> coinVsMove)
    {
        System.out.println(" Inside checkMateOrNot *******" );
        for (Map.Entry<Position, List<Position>> entry : coinVsMove.entrySet()) {
            Position k = entry.getKey();
            List<Position> v = entry.getValue();
            System.out.println("v = " + v);
            if (!v.isEmpty())
                return false;
        }
        return true;
    }

    public Square[][] setPromotingPawn(int x, int y, String coinName, Square[][] chessboard)
    {
        Square promotionSq = chessboard[x][y];
        Coin promotionSqCoin = promotionSq.getCoin();

        Coin coin = setSplCoin(Coins.valueOf(coinName));
        coin.setMoveCount(0);
        coin.setCoinColour(promotionSqCoin.getCoinColour());
        coin.setCurrentPosition(promotionSqCoin.getCurrentPosition());
        coin.setDefaultPosition(promotionSqCoin.getDefaultPosition());

        promotionSq.setCoin(coin);

        return chessboard;
    }

    public Coin setSplCoin(Coins coins)
    {
        Map<Constance.MovementDirection, Integer> movementDirectionIntegerMap = DefaultChessBoard.setCoinMoves(coins);

        Coin coin = new Coin();
        coin.setMoveDirVsSteps(movementDirectionIntegerMap);
        coin.setCoinName(coins);
        return coin;
    }

    public Set<Position> getValidPositions(Coin coin, Square[][] chessboard, PreviousMove previousMove)
    {
        System.out.println("coin = " + coin);
        Set<Position> possibleMoves = getPossibleMoves(coin, chessboard, false);
        System.out.println("possibleMoves = " + possibleMoves);

        if (coin.getCoinName() == Coins.PAWN && previousMove != null && previousMove.getCoin().getCoinName() == Coins.PAWN)
        {
            List<Position> positions = getPawnSplPositions(previousMove, coin);
            possibleMoves.addAll(positions);
            System.out.println("Pawn spl positions = " + positions);
        }

        if (coin.getCoinName() == Coins.KING) {
            List<Position> castLingMoves = getCastLingPositions(coin, chessboard);
            possibleMoves.addAll(castLingMoves);
            System.out.println("castLingMoves = " + castLingMoves);
        }

        return possibleMoves;
    }

    public ModelAndView checkIsOpenentMoveAndReturnChessBoard(Colours coinToBeMoved, PreviousMove previousMove, Square[][] chessboard, Model model)
    {
        System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
        System.out.println("previousMove = " + previousMove);
        System.out.println("clickedCoin = " + coinToBeMoved);

        if (previousMove != null && coinToBeMoved.equals(previousMove.getCoin().getCoinColour())) {
            System.out.println("Opponent Move");
            StringBuilder chessBoard = setCoinsInChessBoard(chessboard, Collections.emptySet(), true);
            model.addAttribute("chessboardHtml", chessBoard.toString());
            return new ModelAndView("chessBoard");
        }
        return null;
    }

    public ModelAndView isValidateCoin(int x, int y, Model model, PreviousMove previousMove, Square[][] chessboard)
    {
        if (previousMove == null && chessboard[x][y].isCoinIsPresent && chessboard[x][y].getCoin().getCoinColour() != WHITE) {
            System.out.println("First Move white");
            StringBuilder chessBoard = setCoinsInChessBoard(chessboard, Collections.emptySet(), false);
            model.addAttribute("chessboardHtml", chessBoard.toString());
            return new ModelAndView("chessBoard");
        }
        // Add your logic to handle the API call using x and y parameters

        System.out.println("calling getPossibleMoves");
        if (!chessboard[x][y].isCoinIsPresent) {
            StringBuilder chessBoard = setCoinsInChessBoard(chessboard, Collections.emptySet(), false);
            model.addAttribute("chessboardHtml", chessBoard.toString());
            return new ModelAndView("chessBoard");
        }
        return null;
    }

    public ModelAndView validateIsValidMove(ClickedCoin clickedCoin, boolean ifClickedCoinIsMoved, Square[][] chessboard, PreviousMove previousMove, Model model) {
        if (clickedCoin == null || ifClickedCoinIsMoved) {
            StringBuilder board = setCoinsInChessBoard(chessboard, Collections.emptySet(), true);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }

        if (previousMove != null && clickedCoin.getClickedCoin().getCoinColour().equals(previousMove.getCoin().getCoinColour())) {
            System.out.println("Opponent Move");
            StringBuilder board = setCoinsInChessBoard(chessboard, Collections.emptySet(), true);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }
        return null;
    }

    public ModelAndView validateIsPawnPromotion(Model model, Position movingPosition, Square[][] squares, ClickedCoin clickedCoin)
    {
        if (clickedCoin.getClickedCoin().coinName == Coins.PAWN && (movingPosition.getX() == 0 || movingPosition.getX() == 7))
        {
            System.out.println("calling promoting pawn = ");

            String promotionPopUp = createPromotionPopUp(movingPosition, WHITE);

            model.addAttribute("popup",promotionPopUp);
            StringBuilder board = setCoinsInChessBoard(squares, clickedCoin.getPossiblePosition(), true);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");

        }
        return null;
    }

    public ModelAndView validateCheckMateOrNot(Model model, StringBuilder board, Map<Position, List<Position>> coinVsMove, Square[][] chessboard, Colours COIN_TO_BE_MOVED, Position COIN_TO_BE_MOVED_KING_POSITION)
    {
        boolean checkMate = validateCheckMate(coinVsMove);

        System.out.println("checkMate = " + checkMate);

        if (checkMate)
        {
            System.out.println("insideCheckMate " );

            String checkMateMessage;

            if (validateCheckForKing(chessboard,COIN_TO_BE_MOVED,COIN_TO_BE_MOVED_KING_POSITION))
                checkMateMessage = createMatePopUP("CheckMate");
            else
                checkMateMessage = createMatePopUP("StaleMate");

            model.addAttribute("matemessage",checkMateMessage);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");

        }

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
    }

}

