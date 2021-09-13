package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.GamePlayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeScoreDTO {

    public static Map<String, Object> scoreDTO(GamePlayer gp) {
        Map<String, Object> scoreDTO = new LinkedHashMap<>();
        if(gp.getScore().isPresent()) {
            scoreDTO.put("score", gp.getScore().get().getScore());
            scoreDTO.put("finishDate", gp.getScore().get().getFinishDate());
            scoreDTO.put("player", gp.getPlayer().getId());
        }
        else {
            scoreDTO.put("score", "match in progress");
            /*scoreDTO.put("finishDate", "match in progress");
            scoreDTO.put("player", gp.getPlayer().getId());*/
        }
        return scoreDTO;
    }

}

