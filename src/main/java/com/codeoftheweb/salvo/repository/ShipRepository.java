package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Player> findById(long playerId);
}
