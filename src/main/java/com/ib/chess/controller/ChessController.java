package com.ib.chess.controller;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.impl.ChessGame;
import com.ib.chess.modules.ClickedCoin;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.PreviousMove;
import com.ib.chess.modules.Square;
import com.ib.chess.utils.ChessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static com.ib.chess.modules.Constance.*;
import static com.ib.chess.modules.Constance.Colours.BLACK;
import static com.ib.chess.modules.Constance.Colours.WHITE;
import static com.ib.chess.modules.Constance.Position.D1;
import static com.ib.chess.modules.Constance.Position.D8;

@RestController
public class ChessController {

    @Autowired
    DefaultChessBoard defaultChessBoard;

    @Autowired
    ChessGame chessGame;

    @Autowired
    ChessUtils chessUtils;

    public Square[][] chessboard;

    private PreviousMove previousMove;

    private ClickedCoin clickedCoin;

    private boolean ifClickedCoinIsMoved = false;

    private Position WHITE_KING_POSITION = D1;
    private Position BLACK_KING_POSITION = D8;
    private Position COIN_TO_BE_MOVED_KING_POSITION = WHITE_KING_POSITION;
    private Colours COIN_TO_BE_MOVED = WHITE;
    Map<Position,List<Position>> coinVsMove;

    @RequestMapping("/")
    public ModelAndView home(Model model)
    {
        chessboard = defaultChessBoard.getDefaultBoard();

        setAllToDefaultWhenReload();

        StringBuilder chessboardHtml = chessUtils.setCoinsInChessBoard(chessboard, Collections.emptySet(), true);

        model.addAttribute("chessboardHtml", chessboardHtml.toString());

        return new ModelAndView("chessBoard");
    }


    @RequestMapping("/getPossibleMoves")
    public ModelAndView getPossibleMoves(@RequestParam int x, @RequestParam int y, Model model)
    {
        ModelAndView chessBoard = chessUtils.isValidateCoin(x, y, model,previousMove,chessboard);
        if (chessBoard != null) return chessBoard;

        ModelAndView isOpponentMove = chessUtils.checkIsOpenentMoveAndReturnChessBoard(COIN_TO_BE_MOVED,previousMove,chessboard,model);
        if (isOpponentMove != null) return isOpponentMove;

        StringBuilder board = getChessBoardWithPossibleMoves(x, y);

        model.addAttribute("chessboardHtml", board.toString());
        return new ModelAndView("chessBoard");
    }


    @RequestMapping("/moveCoin")
    public ModelAndView moveCoin(@RequestParam int x, @RequestParam int y, Model model) {
        System.out.println(" inside MoveCoin");

        Square square = chessboard[x][y];
        Position movingPosition = square.getSquarePosition();

        ModelAndView chessBoard = chessUtils.validateIsValidMove(clickedCoin,ifClickedCoinIsMoved,chessboard,previousMove,model);

        if (chessBoard != null)
            return chessBoard;

        return moveCoinAndGetChessBoard(model,movingPosition);
    }

    @RequestMapping("/promotingPawn")
    public ModelAndView promotingPawn(@RequestParam int x, @RequestParam int y , @RequestParam String coinName, Model model)
    {
        // Logic to process the clicked coin and send it to the backend
        System.out.println("Clicked coin: " + coinName);
        System.out.println("x = " + x + " ::" + "y = " + y);

        chessboard = chessUtils.setPromotingPawn(x, y, coinName,chessboard);

        StringBuilder board = chessUtils.setCoinsInChessBoard(chessboard, clickedCoin.getPossiblePosition(), true);

        model.addAttribute("chessboardHtml", board.toString());

        return new ModelAndView("chessBoard");
    }

    private StringBuilder getChessBoardWithPossibleMoves(int x, int y)
    {
        Coin coin = chessboard[x][y].getCoin();

        Set<Position> possibleMoves = chessUtils.getValidPositions(coin,chessboard,previousMove);

        clickedCoin = new ClickedCoin(coin, possibleMoves);

        System.out.println("clickedCoin = " + clickedCoin);

        ifClickedCoinIsMoved = false;

        if (!coinVsMove.isEmpty())
            possibleMoves = chessUtils.filterPossibleMoves(possibleMoves,coinVsMove,coin.getCurrentPosition());

        else
            possibleMoves = new HashSet<>();

        clickedCoin.setPossiblePosition(possibleMoves);

        return chessUtils.setCoinsInChessBoard(chessboard, possibleMoves, false);
    }
    private ModelAndView moveCoinAndGetChessBoard(Model model, Position movingPosition)
    {
        Position currentPosition = clickedCoin.getClickedCoin().getCurrentPosition();

        System.out.println("currentPosition 1 = " + currentPosition);

        boolean[] coinisMoved = {false};
        Square[][] squares = chessGame.moveCoin(clickedCoin.getClickedCoin(), movingPosition, chessboard, clickedCoin.getPossiblePosition(), coinisMoved);

        ModelAndView chessBoard1 = chessUtils.validateIsPawnPromotion(model, movingPosition, squares,clickedCoin);

        if (chessBoard1 != null) return chessBoard1;

        StringBuilder board = chessUtils.setCoinsInChessBoard(squares, clickedCoin.getPossiblePosition(), true);

        changesIfCoinIsMoved(coinisMoved, currentPosition, movingPosition);

        return chessUtils.validateCheckMateOrNot(model, board,coinVsMove,chessboard,COIN_TO_BE_MOVED,COIN_TO_BE_MOVED_KING_POSITION);
    }

    private void changesIfCoinIsMoved(boolean[] coinisMoved, Position currentPosition, Position movingPosition)
    {
        if (coinisMoved[0])
        {
            previousMove = new PreviousMove(clickedCoin.getClickedCoin(), currentPosition, movingPosition);

            System.out.println("previousMove = " + previousMove);

            COIN_TO_BE_MOVED = COIN_TO_BE_MOVED == WHITE ? BLACK : WHITE;

            COIN_TO_BE_MOVED_KING_POSITION = COIN_TO_BE_MOVED == WHITE ? WHITE_KING_POSITION : BLACK_KING_POSITION;

            if (clickedCoin.getClickedCoin().getCoinName() == Coins.KING)
            {
                if (clickedCoin.getClickedCoin().coinColour == BLACK) {
                    BLACK_KING_POSITION = movingPosition;
                }
                else if (clickedCoin.getClickedCoin().coinColour == WHITE)
                    WHITE_KING_POSITION = movingPosition;
            }

            ifClickedCoinIsMoved = true;

            coinVsMove = chessUtils.getCoinVsMoves(chessboard,COIN_TO_BE_MOVED,COIN_TO_BE_MOVED_KING_POSITION);

            System.out.println("^^^^ coin vs move ^^^^^ = " + coinVsMove);
        }
    }


    private void setAllToDefaultWhenReload()
    {
        previousMove = null;
        clickedCoin = null;
        ifClickedCoinIsMoved = false;

        WHITE_KING_POSITION = D1;
        BLACK_KING_POSITION = D8;
        COIN_TO_BE_MOVED_KING_POSITION = WHITE_KING_POSITION;
        COIN_TO_BE_MOVED = WHITE;
        coinVsMove = null;
        coinVsMove = chessUtils.getCoinVsMoves(chessboard,COIN_TO_BE_MOVED,WHITE_KING_POSITION);

    }

}
