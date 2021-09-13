package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Salvo;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeSalvoDTO {

    public static Map<String, Object> SalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getSalvoLocations());
        return dto;
    }

}
