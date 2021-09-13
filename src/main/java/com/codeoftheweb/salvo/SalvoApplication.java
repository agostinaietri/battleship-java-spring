package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	@Bean
	public CommandLineRunner initData(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {

			Date d = new Date();

			Game game1 = new Game();
			Game game2 = new Game(Date.from(d.toInstant().plusSeconds(3600)));
			Game game3 = new Game(Date.from(d.toInstant().plusSeconds(7200)));


			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);

			Player player1 = new Player("stevewozniak@aol.com", passwordEncoder.encode("123"));
			Player player2 = new Player( "leonardeuler@gmail.com", passwordEncoder.encode("321"));
			Player player3 = new Player("konradzuse@aol.com", passwordEncoder.encode("999"));
			Player player4 = new Player("charlesbabbage@live.com", passwordEncoder.encode("667"));


			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			GamePlayer gamePlayer1 = new GamePlayer(game1, player1, Date.from(d.toInstant()));
			GamePlayer gamePlayer2 = new GamePlayer(game1, player2, Date.from(d.toInstant()));
			GamePlayer gamePlayer3 = new GamePlayer(game2, player2, Date.from(d.toInstant()));
			GamePlayer gamePlayer4 = new GamePlayer(game2, player3, Date.from(d.toInstant()));
			GamePlayer gamePlayer5 = new GamePlayer(game3, player3, Date.from(d.toInstant()));
			GamePlayer gamePlayer6 = new GamePlayer(game3, player1, Date.from(d.toInstant()));

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);


			List<String> locations1 = List.of("A2", "A3", "A4", "A5", "A6");
			List<String> locations2 = List.of("D3", "E3", "F3", "G3");
			List<String> locations3 = List.of("D5", "E5", "F5");
			List<String> locations4 = List.of("I6", "I7", "I8");
			List<String> locations5 = List.of("F9", "G9");
			List<String> locations6 = List.of("B9", "C9", "D9", "E9", "F9");
			List<String> locations7 = List.of("H4", "H5", "H6", "H7");
			List<String> locations8 = List.of("B2", "B3", "B4");
			List<String> locations9 = List.of("C6", "D6", "E6");
			List<String> locations10 = List.of("J1", "J2");
			List<String> locations11 = List.of("C9", "D9", "E9", "F9", "G9");
			List<String> locations12 = List.of("I4", "I5", "I6", "I7");
			List<String> locations13 = List.of("A2", "A3", "A4");
			List<String> locations14 = List.of("F6", "G6", "H6");
			List<String> locations15 = List.of("A9", "A10");

			Ship ship1 = new Ship("carrier", locations1, gamePlayer1);
			Ship ship2 = new Ship("battleship", locations2, gamePlayer1);
			Ship ship3 = new Ship("submarine", locations3, gamePlayer1);
			Ship ship4 = new Ship("destroyer", locations4, gamePlayer1);
			Ship ship5 = new Ship("patrolboat", locations5, gamePlayer1);

			Ship ship6 = new Ship("carrier", locations6, gamePlayer2);
			Ship ship7 = new Ship("battleship", locations7, gamePlayer2);
			Ship ship8 = new Ship("submarine", locations8, gamePlayer2);
			Ship ship9 = new Ship("destroyer", locations9, gamePlayer2);
			Ship ship10 = new Ship("patrolboat", locations10, gamePlayer2);

			Ship ship11 = new Ship("Carrier", locations11, gamePlayer3);
			Ship ship12 = new Ship("Battleship", locations12, gamePlayer3);
			Ship ship13 = new Ship("Submarine", locations13, gamePlayer3);
			Ship ship14 = new Ship("Destroyer", locations14, gamePlayer3);
			Ship ship15 = new Ship("Patrol Boat", locations15, gamePlayer3);

			Ship ship16 = new Ship("Carrier", locations1, gamePlayer4);
			Ship ship17 = new Ship("Battleship", locations2, gamePlayer4);
			Ship ship18 = new Ship("Submarine", locations3, gamePlayer4);
			Ship ship19 = new Ship("Destroyer", locations4, gamePlayer4);
			Ship ship20 = new Ship("Patrol Boat", locations5, gamePlayer4);

			Ship ship21 = new Ship("Carrier", locations6, gamePlayer5);
			Ship ship22 = new Ship("Battleship", locations7, gamePlayer5);
			Ship ship23 = new Ship("Submarine", locations8, gamePlayer5);
			Ship ship24 = new Ship("Destroyer", locations9, gamePlayer5);
			Ship ship25 = new Ship("Patrol Boat", locations10, gamePlayer5);

			Ship ship26 = new Ship("Carrier", locations11, gamePlayer6);
			Ship ship27 = new Ship("Battleship", locations12, gamePlayer6);
			Ship ship28 = new Ship("Submarine", locations13, gamePlayer6);
			Ship ship29 = new Ship("Destroyer", locations14, gamePlayer6);
			Ship ship30 = new Ship("Patrol Boat", locations15, gamePlayer6);

			List<String> locations16 = List.of("A1", "A10", "J1", "J10", "A2");
			List<String> locations17 = List.of("C3", "D9", "E9", "H8", "E7");
			List<String> locations18 = List.of("A2", "A3", "D2", "E10", "F2");
			List<String> locations19 = List.of("H7", "F5", "F3", "G3", "B9");
			List<String> locations20 = List.of("J1", "F5", "J3", "E9", "A8");
			List<String> locations21 = List.of("G7", "I2", "J2", "E2", "B10");
			List<String> locations22 = List.of("G3", "J2", "A4", "F1", "J2");
			List<String> locations23 = List.of("J1", "J10", "C5", "C7", "B2");
			List<String> locations24 = List.of("J6", "F5", "J3", "E9", "A9");
			List<String> locations25 = List.of("C1", "I3", "J8", "G2", "B10");
			List<String> locations26 = List.of("G3", "J7", "A4", "E1", "J2");
			List<String> locations27 = List.of("J7", "J9", "C5", "C7", "B2");


			Salvo salvo1 = new Salvo(gamePlayer1, 1, locations16);
			Salvo salvo2 = new Salvo(gamePlayer1, 2, locations17);
			Salvo salvo3 = new Salvo(gamePlayer2, 1, locations18);
			Salvo salvo4 = new Salvo(gamePlayer2, 2, locations19);
			Salvo salvo5 = new Salvo(gamePlayer3, 1, locations20);
			Salvo salvo6 = new Salvo(gamePlayer3, 2, locations21);
			Salvo salvo7 = new Salvo(gamePlayer4, 1, locations22);
			Salvo salvo8 = new Salvo(gamePlayer4, 2, locations23);
			Salvo salvo9 = new Salvo(gamePlayer5, 1, locations24);
			Salvo salvo10 = new Salvo(gamePlayer5, 2, locations25);
			Salvo salvo11 = new Salvo(gamePlayer6, 1, locations26);
			Salvo salvo12 = new Salvo(gamePlayer6, 2, locations27);

			Score score1 = new Score(d, player1, game1, 0.5);
			Score score2 = new Score(d, player2, game1, 1);
			Score score3 = new Score(d, player3, game2, 0);
			Score score4 = new Score(d, player4, game2, 0.5);

			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			shipRepository.save(ship11);
			shipRepository.save(ship12);
			shipRepository.save(ship13);
			shipRepository.save(ship14);
			shipRepository.save(ship15);
			shipRepository.save(ship16);
			shipRepository.save(ship17);
			shipRepository.save(ship18);
			shipRepository.save(ship19);
			shipRepository.save(ship20);
			shipRepository.save(ship21);
			shipRepository.save(ship22);
			shipRepository.save(ship23);
			shipRepository.save(ship24);
			shipRepository.save(ship25);
			shipRepository.save(ship26);
			shipRepository.save(ship27);
			shipRepository.save(ship28);
			shipRepository.save(ship29);
			shipRepository.save(ship30);

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);
			salvoRepository.save(salvo7);
			salvoRepository.save(salvo8);
			salvoRepository.save(salvo9);
			salvoRepository.save(salvo10);
			salvoRepository.save(salvo11);
			salvoRepository.save(salvo12);

		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userName-> {
			Player player = playerRepository.findByUserName(userName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + userName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/game_view/**").hasAuthority("USER")
				.antMatchers("/web/**", "/api/games", "/api/players").permitAll()

				.and()
				.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.csrf().disable();

		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.logout().logoutUrl("/api/logout");

		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}


}


