package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.Utils.Utils;
import com.codeoftheweb.salvo.controller.SalvoController;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.services.GameState;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class makeGameViewDTO {

    public static Map<String, Object> GameViewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gameState", Utils.getGameState(gamePlayer));
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(makeGamePlayerDTO::GamePlayerDTO).collect(Collectors.toSet()));
        return dto;
    }

}
