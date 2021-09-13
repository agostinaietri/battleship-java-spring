package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.makeGameViewDTO;
import com.codeoftheweb.salvo.DTO.makeSalvoDTO;
import com.codeoftheweb.salvo.DTO.makeShipDTO;
import com.codeoftheweb.salvo.Utils.Utils;
import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.*;
import com.codeoftheweb.salvo.services.GameState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeoftheweb.salvo.Utils.Utils.isGuest;
import static com.codeoftheweb.salvo.Utils.Utils.makeMap;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class GameViewController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> checkUser(@PathVariable long gamePlayerId, Authentication auth) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        Game game = gamePlayer.getGame();
        Player playerAuth = playerRepository.findByUserName(auth.getName());


        if (Utils.getGameState(gamePlayer) == GameState.WON) {
            scoreRepository.save(new Score(new Date(), gamePlayer.getPlayer(), gamePlayer.getGame(), 1.0));
            scoreRepository.save(new Score(new Date(), gamePlayer.getOpponent().get().getPlayer(), gamePlayer.getOpponent().get().getGame(), 0.0));
        }

        if (Utils.getGameState(gamePlayer) == GameState.LOST) {
            scoreRepository.save(new Score(new Date(), gamePlayer.getPlayer(), gamePlayer.getGame(), 0.0));
            scoreRepository.save(new Score(new Date(), gamePlayer.getOpponent().get().getPlayer(), gamePlayer.getOpponent().get().getGame(), 1.0));
        }

        if (Utils.getGameState(gamePlayer) == GameState.TIE) {
            scoreRepository.save(new Score(new Date(), gamePlayer.getPlayer(), gamePlayer.getGame(), 0.5));
            scoreRepository.save(new Score(new Date(), gamePlayer.getOpponent().get().getPlayer(), gamePlayer.getOpponent().get().getGame(), 0.5));
        }

        if (gamePlayer.getPlayer().getId() == playerAuth.getId()) {
            Map<String, Object> dto = makeGameViewDTO.GameViewDTO(gamePlayer);
            dto.put("ships", gamePlayer.getShips().stream().map(makeShipDTO::ShipDTO).collect(Collectors.toList()));
            dto.put("salvoes", game.getGamePlayers().stream().flatMap(salvo -> salvo.getSalvoes().stream().map(makeSalvoDTO::SalvoDTO)).collect(Collectors.toList()));
            dto.put("hits", makeHitsInfoDTO(gamePlayer));
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }

        return new ResponseEntity<>(makeMap("error", "you're not allowed to see this information"), HttpStatus.UNAUTHORIZED);
    }


    public static Map<String, Object> HitsDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurn());
        dto.put("hitLocations", Utils.getHits(salvo));
        dto.put("damages", DamagesDTO(salvo));
        // obtengo el tamaño de las locations del salvo que se tiró, si no se encuentran coincidencias se le resta uno al missed
        dto.put("missed", salvo.getSalvoLocations().size() - Utils.getHits(salvo).size());
        return dto;
    }


    public static Map<String, Object> makeHitsInfoDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (gamePlayer.getOpponent().isPresent()) {
            dto.put("self", gamePlayer.getOpponent().get().getSalvoes().stream().map(salvo -> HitsDTO(salvo)).collect(Collectors.toList()));
        } else {
            dto.put("self", new ArrayList<>());
        }
        dto.put("opponent", gamePlayer.getSalvoes().stream().map(salvo -> HitsDTO(salvo)).collect(Collectors.toList()));

        return dto;
    }

    public static List<String> findShipLocations(GamePlayer self, String type) {
        Ship response;
        response = self.getShips().stream().filter(ship -> ship.getType().equals(type)).findFirst().orElse(null);
        if (response == null) {
            return new ArrayList<>();
        } else {
            return response.getShipLocations();
        }
    }

    public static Map<String, Object> DamagesDTO(Salvo salvo) {

        Map<String, Object> dto = new LinkedHashMap<>();

        GamePlayer gamePlayerOpponent = salvo.getGamePlayer().getOpponent().get();

        List<String> carrierLocations = findShipLocations(gamePlayerOpponent, "carrier"); //salvo.getGamePlayer().getShips().stream().filter(s -> s.getType().equals("carrier")).flatMap(s -> s.getShipLocations().stream()).collect(Collectors.toList());
        List<String> battleshipLocations = findShipLocations(gamePlayerOpponent, "battleship"); //salvo.getGamePlayer().getShips().stream().filter(s -> s.getType().equals("battleship")).flatMap(s -> s.getShipLocations().stream()).collect(Collectors.toList());
        List<String> submarineLocations = findShipLocations(gamePlayerOpponent, "submarine"); //salvo.getGamePlayer().getShips().stream().filter(s -> s.getType().equals("submarine")).flatMap(s -> s.getShipLocations().stream()).collect(Collectors.toList());
        List<String> destroyerLocations = findShipLocations(gamePlayerOpponent, "destroyer"); //salvo.getGamePlayer().getShips().stream().filter(s -> s.getType().equals("destroyer")).flatMap(s -> s.getShipLocations().stream()).collect(Collectors.toList());
        List<String> patrolBoatLocations = findShipLocations(gamePlayerOpponent, "patrolboat"); //salvo.getGamePlayer().getShips().stream().filter(s -> s.getType().equals("patrolboat")).flatMap(s -> s.getShipLocations().stream()).collect(Collectors.toList());

        Map<String, Object> hitsPerTurn = new LinkedHashMap<>();
        Map<String, Object> damagesPerTurn = new LinkedHashMap<>();

        List<String> allSalvoesList = salvo.getGamePlayer().getSalvoes().stream().filter(s -> s.getTurn() <= salvo.getTurn()).flatMap(s -> s.getSalvoLocations().stream()).collect(Collectors.toList());

        dto.put("carrierHits", carrierLocations.stream().filter(l -> salvo.getSalvoLocations().contains(l)).count());
        dto.put("battleshipHits", battleshipLocations.stream().filter(l -> salvo.getSalvoLocations().contains(l)).count());
        dto.put("submarineHits", submarineLocations.stream().filter(l -> salvo.getSalvoLocations().contains(l)).count());
        dto.put("destroyerHits", destroyerLocations.stream().filter(l -> salvo.getSalvoLocations().contains(l)).count());
        dto.put("patrolboatHits", patrolBoatLocations.stream().filter(l -> salvo.getSalvoLocations().contains(l)).count());

        dto.put("carrier", carrierLocations.stream().filter(l -> allSalvoesList.contains(l)).count());
        dto.put("battleship", battleshipLocations.stream().filter(l -> allSalvoesList.contains(l)).count());
        dto.put("submarine", submarineLocations.stream().filter(l -> allSalvoesList.contains(l)).count());
        dto.put("destroyer", destroyerLocations.stream().filter(l -> allSalvoesList.contains(l)).count());
        dto.put("patrolboat", patrolBoatLocations.stream().filter(l -> allSalvoesList.contains(l)).count());

        return dto;
    }

}
