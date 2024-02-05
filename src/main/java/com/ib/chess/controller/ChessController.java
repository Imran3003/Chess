package com.ib.chess.controller;

import com.ib.chess.impl.ChessGame;
import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.impl.ValidateMoves;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @RequestMapping("/example")
    private ModelAndView example(Model model)
    {
        System.out.println("currentBoard = " + Arrays.deepToString(chessboard));

        Coin coin = new Coin();

        coin.setCoinColour(Constance.Colours.WHITE);
        coin.setCoinName(Constance.Coins.KNIGHT);

        coin.setDefaultPosition(Constance.Position.setPos(0,6));
        coin.setCurrentPosition(Constance.Position.setPos(0,6));

        Map<Constance.MovementDirection,Integer> moveDirVsSteps = new HashMap<>();
        moveDirVsSteps.put(KNIGHT_L_MOVE,3);
        coin.setMoveDirVsSteps(moveDirVsSteps);

        Set<Constance.Position> possibleMoves = validateMoves.getPossibleMoves(coin, chessboard);
        System.out.println("possibleMoves = " + possibleMoves);

        Square[][] squares = chessGame.moveCoin(coin, Constance.Position.E2, chessboard);

        StringBuilder board = setCoinsInChessBoard(squares);

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
    }
    @RequestMapping("/")
    public ModelAndView home(Model model)
    {
        chessboard = defaultChessBoard.getDefaultBoard();

        StringBuilder chessboardHtml = setCoinsInChessBoard(chessboard);

        model.addAttribute("chessboardHtml", chessboardHtml.toString());
        return new ModelAndView("chessBoard");
    }

    private StringBuilder setCoinsInChessBoard(Square[][] board)
    {
        StringBuilder chessboardHtml = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++)
            {
                Square square = board[i][j];

                String squareClass = (i + j) % 2 == 0 ? "white" : "black";

                if (square.isCoinIsPresent)
                {
                    Coin coin = board[i][j].getCoin();
                    String pieceHtml =  getPieceHtml(coin);
                    String squareHtml = String.format("<div class=\"square %s\" data-coin-colour=\"%s\" data-coin-name=\"%s\">%s</div>", squareClass,coin.getCoinColour() , coin.getCoinName(), pieceHtml);
                    chessboardHtml.append(squareHtml);
                }
                else
                {
                    String squareHtml = String.format("<div class=\"square %s\"></div>", squareClass);
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
                pieceSymbol = (coin.getCoinColour() == Constance.Colours.WHITE) ? "&#9812;" : "&#9818;";
                break;
            case QUEEN:
                pieceSymbol = (coin.getCoinColour() == Constance.Colours.WHITE) ? "&#9813;" : "&#9819;";
                break;
            case ROOK:
                pieceSymbol = (coin.getCoinColour() == Constance.Colours.WHITE) ? "&#9814;" : "&#9820;";
                break;
            case BISHOP:
                pieceSymbol = (coin.getCoinColour() == Constance.Colours.WHITE) ? "&#9815;" : "&#9821;";
                break;
            case KNIGHT:
                pieceSymbol = (coin.getCoinColour() == Constance.Colours.WHITE) ? "&#9816;" : "&#9822;";
                break;
            case PAWN:
                pieceSymbol = (coin.getCoinColour() == Constance.Colours.WHITE) ? "&#9817;" : "&#9823;";
                break;
        }

        return pieceSymbol;
    }

}
