package com.ib.chess.controller;

import com.ib.chess.modules.Constance;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    private Constance.Coins selectedSpecialCoin;

    @GetMapping(value = "/promotingAPawn", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Constance.Coins> promotingAPawn() {
        List<Constance.Coins> splCoins = new ArrayList<>();
        splCoins.add(Constance.Coins.QUEEN);
        splCoins.add(Constance.Coins.ROOK);
        splCoins.add(Constance.Coins.BISHOP);
        splCoins.add(Constance.Coins.KNIGHT);
        return splCoins;
    }

//    @PostMapping("/selectSpecialCoin")
//    @ResponseBody
//    public void selectSpecialCoin(@RequestBody Constance.Coins selectedCoin) {
//        selectedSpecialCoin = selectedCoin;
//    }

    public Constance.Coins getSelectedSpecialCoin() {
        return selectedSpecialCoin;
    }
}
