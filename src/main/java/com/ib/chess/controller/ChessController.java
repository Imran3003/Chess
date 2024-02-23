package com.ib.chess.controller;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.impl.ChessGame;
import com.ib.chess.impl.ValidateMoves;
import com.ib.chess.modules.ClickedCoin;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.PreviousMove;
import com.ib.chess.modules.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.ib.chess.modules.Constance.*;

@RestController
public class ChessController {

    @Autowired
    DefaultChessBoard defaultChessBoard;

    @Autowired
    ChessGame chessGame;

    @Autowired
    ValidateMoves validateMoves;

    public Square[][] chessboard;

    private PreviousMove previousMove;

    private ClickedCoin clickedCoin;

    private boolean ifClickedCoinIsMoved = false;

    @RequestMapping("/")
    public ModelAndView home(Model model)
    {
        chessboard = defaultChessBoard.getDefaultBoard();

        previousMove = null;
        clickedCoin = null;
        ifClickedCoinIsMoved = false;

        StringBuilder chessboardHtml = setCoinsInChessBoard(chessboard, Collections.emptySet(),true);

        model.addAttribute("chessboardHtml", chessboardHtml.toString());
        return new ModelAndView("chessBoard");
    }

    @RequestMapping("/getPossibleMoves")
    public ModelAndView getPossibleMoves(@RequestParam int x, @RequestParam int y, Model model)
    {

        if (previousMove == null && chessboard[x][y].isCoinIsPresent &&  chessboard[x][y].getCoin().getCoinColour() != Colours.WHITE)
        {
            System.out.println("First Move white");
            StringBuilder board = setCoinsInChessBoard(chessboard, Collections.emptySet(),false);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }
        // Add your logic to handle the API call using x and y parameters

        System.out.println("calling getPossibleMoves");
        if (!chessboard[x][y].isCoinIsPresent)
        {
            StringBuilder board = setCoinsInChessBoard(chessboard, Collections.emptySet(),false);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }

        Coin coin = chessboard[x][y].getCoin();

        System.out.println("coin = " + coin);
        Set<Position> possibleMoves = validateMoves.getPossibleMoves(coin, chessboard,false);
        System.out.println("possibleMoves = " + possibleMoves);

        if(coin.getCoinName() == Coins.PAWN && previousMove != null && previousMove.getCoin().getCoinName() == Coins.PAWN)
        {
            List<Position> positions = validateMoves.pawnSplMove(previousMove, coin);
            possibleMoves.addAll(positions);
            System.out.println("Pawn spl positions = " + positions);
        }

        if (coin.getCoinName() == Coins.KING)
        {
            List<Position> castLingMoves = validateMoves.castLing(coin, chessboard);
            possibleMoves.addAll(castLingMoves);
            System.out.println("castLingMoves = " + castLingMoves);
        }

        System.out.println("possibleMovesAfterAdd = " + possibleMoves);
//        Square[][] squares = chessGame.moveCoin(coin, Position.E2, chessboard, possibleMoves);

        StringBuilder board = setCoinsInChessBoard(chessboard,possibleMoves,false);

        clickedCoin = new ClickedCoin(coin,possibleMoves);

        System.out.println("clickedCoin 1 = " + clickedCoin);

        ifClickedCoinIsMoved=false;


        if (previousMove != null && clickedCoin.getClickedCoin().getCoinColour().equals(previousMove.getCoin().getCoinColour()))
        {
            System.out.println( "Opponent Move");
            board = setCoinsInChessBoard(chessboard, Collections.emptySet(),true);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
        // Add your API call logic here using x and y
    }

    @RequestMapping("/moveCoin")
    public ModelAndView moveCoin(@RequestParam int x, @RequestParam int y, Model model)
    {
        System.out.println(" inside MoveCoin");

        Square square = chessboard[x][y];
        Position movingPosition = square.getSquarePosition();
        if (clickedCoin == null || ifClickedCoinIsMoved)
        {
            StringBuilder board = setCoinsInChessBoard(chessboard, Collections.emptySet(),true);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }

        if (previousMove != null && clickedCoin.getClickedCoin().getCoinColour().equals(previousMove.getCoin().getCoinColour()))
        {
            System.out.println( "Opponent Move");
            StringBuilder board = setCoinsInChessBoard(chessboard, Collections.emptySet(),true);
            model.addAttribute("chessboardHtml", board.toString());
            return new ModelAndView("chessBoard");
        }

        Position currentPosition = clickedCoin.getClickedCoin().getCurrentPosition();

        System.out.println("currentPosition 1 = " + currentPosition);

        boolean[] coinisMoved ={false};
        Square[][] squares = chessGame.moveCoin(clickedCoin.getClickedCoin(), movingPosition, chessboard, clickedCoin.getPossiblePosition(),coinisMoved);

        StringBuilder board = setCoinsInChessBoard(squares,clickedCoin.getPossiblePosition(),true);

        if (coinisMoved[0])
        {
            previousMove = new PreviousMove(clickedCoin.getClickedCoin(), currentPosition, movingPosition);

            System.out.println("previousMove = " + previousMove);

            ifClickedCoinIsMoved = true;
        }

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
    }
    private StringBuilder setCoinsInChessBoard(Square[][] board, Set<Position> possibleMoves,boolean isCoinIsMoved)
    {
        StringBuilder chessboardHtml = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++)
            {
                Square square = board[i][j];

                Position squarePosition = square.getSquarePosition();

                String squareClass = (i + j) % 2 == 0 ? "white" : "black";

                String backgroundColor = "background-color: rgba(15, 236, 15, 0.47);";// Set the background color to green

                String squareHtml;

                String squareId = "square-" + i + "-" + j; // Create a unique id for each square

                String onclickAttribute = "onclick=\"handleSquareClick('" + i + "','" + j + "','" + squareId + "')\"";
                String onclickAttributeForCoin = "onclick=\"handleCoinClick('" + i + "','" + j + "','" + squareId + "')\"";
                String onclickAttributeForEmptySquare = "onclick=\"emptySquare('" + i + "','" + j + "','" + squareId + "')\"";



                if (square.isCoinIsPresent) {
                    Coin coin = board[i][j].getCoin();
                    String pieceHtml = getPieceHtml(coin);

                    if (!possibleMoves.contains(squarePosition))
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, coin.getCoinColour(), coin.getCoinName(), onclickAttributeForCoin, pieceHtml);
                    else
                        if (isCoinIsMoved)
                            squareHtml = String.format("<div id=\"%s\" class=\"square %s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, coin.getCoinColour(), coin.getCoinName(), onclickAttributeForCoin, pieceHtml);
                        else
                            squareHtml = String.format("<div id=\"%s\" class=\"square %s\" style=\"%s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, backgroundColor, coin.getCoinColour(), coin.getCoinName(), onclickAttribute, pieceHtml);

                    chessboardHtml.append(squareHtml);
                } else
                {
                    if (!possibleMoves.contains(squarePosition))
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" %s></div>", squareId, squareClass,onclickAttributeForEmptySquare);
                    else
                        if (isCoinIsMoved)
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
                pieceSymbol = (coin.getCoinColour() == Colours.WHITE) ? "&#9812;" : "&#9818;";
                break;
            case QUEEN:
                pieceSymbol = (coin.getCoinColour() == Colours.WHITE) ? "&#9813;" : "&#9819;";
                break;
            case ROOK:
                pieceSymbol = (coin.getCoinColour() == Colours.WHITE) ? "&#9814;" : "&#9820;";
                break;
            case BISHOP:
                pieceSymbol = (coin.getCoinColour() == Colours.WHITE) ? "&#9815;" : "&#9821;";
                break;
            case KNIGHT:
                pieceSymbol = (coin.getCoinColour() == Colours.WHITE) ? "&#9816;" : "&#9822;";
                break;
            case PAWN:
                pieceSymbol = (coin.getCoinColour() == Colours.WHITE) ? "&#9817;" : "&#9823;";
                break;
        }

        return pieceSymbol;
    }

}
