package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.codeoftheweb.salvo.Utils.Utils.isGuest;
import static com.codeoftheweb.salvo.Utils.Utils.makeMap;


@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private PlayerRepository playerRepository;


    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable long gamePlayerId, @RequestBody List<Ship> ships, Authentication auth) {
        if (gamePlayerRepository.findById(gamePlayerId).isPresent()) {
            GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
            if (!isGuest(auth)) {
                if (gamePlayer.getPlayer() == playerRepository.findByUserName(auth.getName())) {
                    if (gamePlayer.getShips().size() == 0) {
                        if (ships.size() == 5) {
                            ships.forEach(ship -> shipRepository.save(new Ship(ship.getType(), ship.getShipLocations(), gamePlayer)));
                            return new ResponseEntity<>(makeMap("OK", "Ships successfully added"), HttpStatus.CREATED);

                        } else if (ships.size() < 5) {
                            return new ResponseEntity<>(makeMap("error", "Please place all the 5 ships"), HttpStatus.FORBIDDEN);
                        }
                        return new ResponseEntity<>(makeMap("error", "Cannot place more than 5 ships"), HttpStatus.FORBIDDEN);
                    }
                    return new ResponseEntity<>(makeMap("error", "You've already placed your 5 ships"), HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(makeMap("error", "You're not the gameplayer your ID is referencing"), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(makeMap("error", "Only logged in users can place ships"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(makeMap("error", "Cannot find match"), HttpStatus.UNAUTHORIZED);
    }
}
