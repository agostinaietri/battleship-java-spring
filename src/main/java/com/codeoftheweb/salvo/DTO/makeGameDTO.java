package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Game;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class makeGameDTO {

    public static Map<String, Object> GameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(makeGamePlayerDTO::GamePlayerDTO).collect(Collectors.toList()));
        dto.put("scores", game.getGamePlayers().stream().map(makeScoreDTO::scoreDTO).collect(Collectors.toList()));
        return dto;
    }

}
