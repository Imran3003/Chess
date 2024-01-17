package com.ib.chess.controller;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChessController {

    @RequestMapping("/chess")
    public ModelAndView home(Model model) {
        Square[][] chessboard = DefaultChessBoard.getDefaultBoard();

        StringBuilder chessboardHtml = new StringBuilder();

        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length; j++)
            {
                Square square = chessboard[i][j];

                String squareClass = (i + j) % 2 == 0 ? "white" : "black";

                if (square.isCoinIsPresent)
                {
                    Coin coin = chessboard[i][j].getCoin();
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

        model.addAttribute("chessboardHtml", chessboardHtml.toString());
        return new ModelAndView("chessBoard");
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
