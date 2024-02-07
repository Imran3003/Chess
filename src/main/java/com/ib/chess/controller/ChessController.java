package com.ib.chess.controller;

import com.ib.chess.impl.ChessGame;
import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.impl.ValidateMoves;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static com.ib.chess.modules.Constance.*;
import static com.ib.chess.modules.Constance.MovementDirection.KNIGHT_L_MOVE;

@RestController
public class ChessController {

    @Autowired
    DefaultChessBoard defaultChessBoard;

    @Autowired
    ChessGame chessGame;

    @Autowired
    ValidateMoves validateMoves;

    public Square[][] chessboard;

    @RequestMapping("/getPossibleMoves")
    public ModelAndView getPossibleMoves(@RequestParam int x, @RequestParam int y, Model model) {
        // Add your logic to handle the API call using x and y parameters

        System.out.println("calling getPossibleMoves");
        if (!chessboard[x][y].isCoinIsPresent)
            return new ModelAndView("chessBoard");

        Coin coin = chessboard[x][y].getCoin();

        Set<Position> possibleMoves = validateMoves.getPossibleMoves(coin, chessboard);
        System.out.println("possibleMoves = " + possibleMoves);

//        Square[][] squares = chessGame.moveCoin(coin, Position.E2, chessboard, possibleMoves);

        StringBuilder board = setCoinsInChessBoard(chessboard,possibleMoves);

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
        // Add your API call logic here using x and y
    }
    @RequestMapping("/example")
    private ModelAndView example(Model model)
    {
        System.out.println("currentBoard = " + Arrays.deepToString(chessboard));

//        Coin coin = new Coin();
//
//        coin.setCoinColour(Colours.WHITE);
//        coin.setCoinName(Coins.KNIGHT);
//
//        coin.setDefaultPosition(Position.setPos(0,6));
//        coin.setCurrentPosition(Position.setPos(0,6));
//
//        Map<MovementDirection,Integer> moveDirVsSteps = new HashMap<>();
//        moveDirVsSteps.put(KNIGHT_L_MOVE,3);
//        coin.setMoveDirVsSteps(moveDirVsSteps);
//
//        Set<Position> possibleMoves = validateMoves.getPossibleMoves(coin, chessboard);
//        System.out.println("possibleMoves = " + possibleMoves);
//
//        Square[][] squares = chessGame.moveCoin(coin, Position.E2, chessboard, possibleMoves);
        Square[][] squares = chessboard;
        squares[2][1].coin = new Coin();
        squares[2][1].coin.setCoinName(Coins.PAWN);
        squares[2][1].isCoinIsPresent=true;

        StringBuilder board = setCoinsInChessBoard(squares,Collections.emptySet());

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
    }
    @RequestMapping("/")
    public ModelAndView home(Model model)
    {
        chessboard = defaultChessBoard.getDefaultBoard();

        StringBuilder chessboardHtml = setCoinsInChessBoard(chessboard, Collections.emptySet());

        model.addAttribute("chessboardHtml", chessboardHtml.toString());
        return new ModelAndView("chessBoard");
    }

    private StringBuilder setCoinsInChessBoard(Square[][] board, Set<Position> possibleMoves)
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

                String onclickAttribute = "";

                String squareId = "square-" + i + "-" + j; // Create a unique id for each square

                if (square.isCoinIsPresent) {
                    Coin coin = board[i][j].getCoin();
                    String pieceHtml = getPieceHtml(coin);

                    // Add onclick attribute for squares with coins
                    onclickAttribute = "onclick=\"handleSquareClick('" + i + "','" + j + "','" + coin.getCoinColour() + "','" + coin.getCoinName() +  "','" + squareId + "')\"";

                    if (!possibleMoves.contains(squarePosition))
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, coin.getCoinColour(), coin.getCoinName(), onclickAttribute, pieceHtml);
                    else
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\" style=\"%s\" data-coin-colour=\"%s\" data-coin-name=\"%s\" %s>%s</div>", squareId, squareClass, backgroundColor, coin.getCoinColour(), coin.getCoinName(), onclickAttribute, pieceHtml);

                    chessboardHtml.append(squareHtml);
                } else {
                    if (!possibleMoves.contains(squarePosition))
                        squareHtml = String.format("<div id=\"%s\" class=\"square %s\"></div>", squareId, squareClass);
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
