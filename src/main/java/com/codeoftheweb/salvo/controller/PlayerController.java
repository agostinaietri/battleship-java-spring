package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.Utils.Utils;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.CONFLICT);
        } else {
            playerRepository.save(new Player(email, passwordEncoder.encode(password)));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

}
