package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private long id;
    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() { }

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        joinDate = new Date();
    }

    public GamePlayer(Game game, Player player, Date joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = new Date();
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public Optional<Score> getScore(){
        return player.getScore(game);
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public Player getPlayer() {
        return player;
    }

    public Player setPlayer(Player player) {
        return player;
    }

    public void addShip(Ship ship) {
        this.ships.add(ship);
    }

    public Game getGame() {
        return game;
    }

    public long getId() {
        return id;
    }

    public void setShip(Set<Ship> ship) {
        this.ships = ship;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Optional<GamePlayer> getOpponent() {
        return this.getGame().getGamePlayers().stream().filter(gp -> (gp != this)).findAny();
    }

}
