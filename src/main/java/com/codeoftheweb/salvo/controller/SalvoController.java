package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.Utils.Utils;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.services.GameState;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeoftheweb.salvo.Utils.Utils.isGuest;
import static com.codeoftheweb.salvo.Utils.Utils.makeMap;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    public Player getPlayers(Authentication auth) {
        return playerRepository.findByUserName(auth.getName());
    }


    @PostMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> placeSalvoes(@PathVariable long gamePlayerId, Authentication auth, @RequestBody Salvo salvo) {

        if (isGuest(auth)) {
            return new ResponseEntity<>(makeMap("error", "User not logged in"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayerRepository.findById(gamePlayerId).isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "There's no gameplayer with that ID"), HttpStatus.UNAUTHORIZED);
        }


        // tiros antes
        GamePlayer gamePlayerSelf = gamePlayerRepository.findById(gamePlayerId).get();

        if (gamePlayerSelf.getPlayer() != getPlayers(auth)) {
            return new ResponseEntity<>(makeMap("error", "You're not the gameplayer the ID is referencing"), HttpStatus.UNAUTHORIZED);
        }

        Optional<Game> game = gameRepository.findById(gamePlayerSelf.getGame().getId());
        List<GamePlayer> gamePlayerList = new ArrayList<>(game.get().getGamePlayers());

        GamePlayer gamePlayerOpponent;

        if (gamePlayerList.get(1).getId() == gamePlayerSelf.getId()) {
            gamePlayerOpponent = gamePlayerList.get(0);
        } else {
            gamePlayerOpponent = gamePlayerList.get(1);
        }


        Set<Salvo> salvoesSelf = gamePlayerSelf.getSalvoes();
        Set<Salvo> salvoesOpponent = gamePlayerOpponent.getSalvoes();
       /* si vos disparas antes del oponente son iguales
                la diferencia entre los 2 debe ser 1, o sea ya tiró
        */
        if (salvoesSelf.size() == salvoesOpponent.size()) {
            if (salvo.getSalvoLocations().size() > 0 && salvo.getSalvoLocations().size() <= 5) {
                salvoRepository.save(new Salvo(gamePlayerSelf, gamePlayerSelf.getSalvoes().size() + 1, salvo.getSalvoLocations()));
                return new ResponseEntity<>(makeMap("OK", "Salvo successfully added"), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(makeMap("error", "placeholder"), HttpStatus.FORBIDDEN);
            }
        }

        if (salvoesOpponent.size() - salvoesSelf.size() == 1) {
            if (salvo.getSalvoLocations().size() > 0 && salvo.getSalvoLocations().size() <= 5) {
                if (Utils.getGameState(gamePlayerSelf) == GameState.PLAY) {

                    salvoRepository.save(new Salvo(gamePlayerSelf, gamePlayerSelf.getSalvoes().size() + 1, salvo.getSalvoLocations()));


                    return new ResponseEntity<>(makeMap("OK", "Salvo successfully added"), HttpStatus.CREATED);
                }

                return new ResponseEntity<>(makeMap("OK", "Salvo successfully added"), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(makeMap("error", "You can only place 5 shots"), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "You can't place any more salvoes"), HttpStatus.FORBIDDEN);
        }
    }



    public GameRepository getGameRepository() {
        return gameRepository;
    }
}
