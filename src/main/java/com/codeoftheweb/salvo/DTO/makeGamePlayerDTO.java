package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeGamePlayerDTO {

    public static Map<String, Object> GamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", makePlayerDTO.PlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }


}
