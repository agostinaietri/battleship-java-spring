package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.DTO.makeGameDTO;
import com.codeoftheweb.salvo.DTO.makePlayerDTO;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.codeoftheweb.salvo.Utils.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeoftheweb.salvo.Utils.Utils.isGuest;
import static com.codeoftheweb.salvo.Utils.Utils.makeMap;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private GameRepository gameRepository;

    public static boolean isGuest(Authentication auth) {
        return auth == null || auth instanceof AnonymousAuthenticationToken;
    }

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @GetMapping("/games")
    public ResponseEntity<Map<String, Object>> getCurrentGameInfo(Authentication authentication) {
        List<Game> gameList = gameRepository.findAll();
        Map<String, Object> gameInfoDTO = new LinkedHashMap<String, Object>();

        if (isGuest(authentication)) {
            gameInfoDTO.put("player", "Guest");
        } else {
            gameInfoDTO.put("player", makePlayerDTO.PlayerDTO(playerRepository.findByUserName(authentication.getName())));
        }
        gameInfoDTO.put("games", gameList.stream().map(makeGameDTO::GameDTO).collect(Collectors.toList()));
        return new ResponseEntity<>(gameInfoDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> joinGame(Authentication auth) {
        Player player = playerRepository.findByUserName(auth.getName());
        if (isGuest(auth)) {
            return new ResponseEntity<>(makeMap("error", "user not logged in"), HttpStatus.UNAUTHORIZED);
        }
        Date joinDate = new Date();
        Game game = gameRepository.save(new Game(joinDate));
        GamePlayer gp = gamePlayerRepository.save(new GamePlayer(game, player, Date.from(joinDate.toInstant())));
        return new ResponseEntity<>(makeMap("gpid", gp.getId()), HttpStatus.CREATED);
    }


    @PostMapping(path = "/game/{gameId}/players")
    public ResponseEntity<Object> joinGame(@PathVariable long gameId, Authentication auth) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (!isGuest(auth)) {
            if (!game.isEmpty()) {
                if (game.get().getGamePlayers().size() < 2) {
                    GamePlayer gamePlayer = game.get().getGamePlayers().stream().findFirst().get();
                    Player player = playerRepository.findByUserName(auth.getName());
                    if (gamePlayer.getPlayer() != player) {
                        Date creationDate = new Date();
                        GamePlayer gp = new GamePlayer(game.get(), player, creationDate);
                        gamePlayerRepository.save(gp);
                        return new ResponseEntity<>((makeMap("gpid", gp.getId())), HttpStatus.CREATED);
                    }
                    return new ResponseEntity<>(makeMap("error", "cannot join the same game twice"), HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(makeMap("error", "game already full"), HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(makeMap("error", "game doesn't exist"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(makeMap("error", "user isn't logged in"), HttpStatus.UNAUTHORIZED);
    }


}
