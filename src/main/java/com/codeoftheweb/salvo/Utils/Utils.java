package com.codeoftheweb.salvo.Utils;

import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Salvo;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.services.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Utils {

    @Autowired
    private PlayerRepository playerRepository;

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static boolean isGuest(Authentication auth) {
        return auth == null || auth instanceof AnonymousAuthenticationToken;
    }

    public Player getPlayers(Authentication auth) {
        return playerRepository.findByUserName(auth.getName());
    }

    /*

    traeme todas las locations del oponente y mis ship locations,
    si la location del ship y la del salvo del oponente no coinciden, ponelas en el list de hits

     */

    public static List<String> getHits(Salvo salvo) {
        List<String> hitLocations = new ArrayList<>();
        List<String> gpSelfSalvoLocations = salvo.getSalvoLocations(); //this.getGamePlayer().getSalvoes().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(toList());
        List<String> ShipLocations = salvo.getGamePlayer().getOpponent().get().getShips().stream().flatMap(location -> location.getShipLocations().stream()).collect(toList());
        hitLocations = gpSelfSalvoLocations.stream().filter(location -> ShipLocations.contains(location)).collect(toList());

        return hitLocations;
    }

    public static boolean hasSunk(GamePlayer gpSelf, GamePlayer gpOpp) {
        if (gpSelf.getSalvoes().isEmpty()) {
            return false;
        }

        List<String> allOppShipLocations = gpOpp.getShips().stream().flatMap(s -> s.getShipLocations().stream()).collect(Collectors.toList());
        List<String> allSelfSalvoLocations = gpSelf.getSalvoes().stream().flatMap(s -> s.getSalvoLocations().stream()).collect(Collectors.toList());

        return (allSelfSalvoLocations.containsAll(allOppShipLocations));
    }

    public static GameState getGameState(GamePlayer gamePlayer) {

        if (gamePlayer.getShips().size() == 0) {
            return GameState.PLACESHIPS;
        }
        if (gamePlayer.getOpponent().isEmpty()) {
            return GameState.WAITINGFOROPP;
        }

        if (gamePlayer.getOpponent().isPresent()) {
            GamePlayer gpOpp = gamePlayer.getOpponent().get();

            if (gpOpp.getOpponent().isPresent()) {
                if (gamePlayer.getSalvoes().size() == gpOpp.getSalvoes().size() && hasSunk(gamePlayer, gpOpp) && !hasSunk(gpOpp, gamePlayer)) {
                    return GameState.WON;
                }
                if (gamePlayer.getSalvoes().size() == gpOpp.getSalvoes().size() && hasSunk(gpOpp, gamePlayer) && hasSunk(gamePlayer, gpOpp)) {
                    return GameState.TIE;
                }
                if (gamePlayer.getSalvoes().size() == gpOpp.getSalvoes().size() && hasSunk(gpOpp, gamePlayer) && !hasSunk(gamePlayer, gpOpp)) {
                    return GameState.LOST;
                }
                if (gamePlayer.getSalvoes().size() == gamePlayer.getOpponent().get().getSalvoes().size()) {
                    return GameState.PLAY;
                }
                if (gamePlayer.getSalvoes().size() > gpOpp.getSalvoes().size()) {
                    return GameState.WAIT;
                }
                if (gamePlayer.getSalvoes().size() < gpOpp.getSalvoes().size()) {
                    return GameState.PLAY;
                }

            }
        }
        return GameState.UNDEFINED;
    }

}
