package com.codeoftheweb.salvo.DTO;

import com.codeoftheweb.salvo.model.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class makeShipDTO {

    public static Map<String, Object> ShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getShipLocations());
        return dto;
    }

}
