package com.ib.chess.controller;

import com.ib.chess.board.DefaultChessBoard;
import com.ib.chess.modules.Coin;
import com.ib.chess.modules.Constance;
import com.ib.chess.modules.Square;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller.java
 *
 * @author Mohamed Subaideen Imran A (mohamedsubaideenimran@nmsworks.co.in)
 * @module com.ib.chess.controller
 * @created Jan 16, 2024
 */
@RestController
public class Controller
{
    @RequestMapping("/")
    public ModelAndView home(Model model)
    {
        Square[][] defaultBoard = DefaultChessBoard.getDefaultBoard();

        StringBuilder divHtml = new StringBuilder();

        Constance.Colours coinColour = defaultBoard[0][0].getCoin().getCoinColour();
        Constance.Coins coinName = defaultBoard[0][0].getCoin().getCoinName();

        divHtml.append("<div class=\"coins\" data-value = ").append(coinColour).append("th:style=\"${buttonStyle} style=\"background-color: yellow;\" value = ").append(coinName).append(" \" >").append("</div>\n");
        model.addAttribute("buttonHtml", divHtml.toString());
        return new ModelAndView("index.html");
    }

}
