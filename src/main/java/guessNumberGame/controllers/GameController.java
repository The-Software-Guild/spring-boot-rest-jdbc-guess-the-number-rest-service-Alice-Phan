package guessNumberGame.controllers;

import guessNumberGame.Service.GameService;
import guessNumberGame.data.GameDao;
import guessNumberGame.data.RoundDao;
import guessNumberGame.models.Game;
import java.util.List;

import guessNumberGame.models.Round;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class GameController {
    // create these fields to access gameDao and roundDao database
    private final GameDao gameDao;
    private final RoundDao roundDao;


    /*
    * Initialize the created variables above
    * Create the controller which takes in those 2 parameters
     */
    public GameController(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;

    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create() {
         //implement create gameService object and game object using gameService
        GameService gameService = new GameService();
        Game game = gameService.newGame();

        /*
        * add game object to database
        * and then return getGames, but getGame will hide the answer before returning it to the user
         */

        gameDao.add(game);
        return gameService.getGames(game);
        
    }

    /*
     * Create the object for the game based on id
     * Create the object for the round, initialize it using game object, getGuess, gameDao
     * At the end. add the round to roundDao
     */

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round guessNumber(@RequestBody Round body) {
        Game game = gameDao.findById(body.getGameId());
        GameService gameService = new GameService();
        Round round = gameService.guessNumber(game, body.getGuess(), gameDao);

        return roundDao.add(round);
    }

    /*
    * Use gameDao to get all the games
    * From gameDao we can implement the query to select and retrieve all from the game
     */
    @GetMapping("/game")
    public List<Game> all() {
      //implement
        List<Game> games = gameDao.getAll();
        GameService gameService = new GameService();
        gameService.getAllGames(games);
        return games;
    }


    /*
    * Create object for game and gameService
    * Retrieve the game based on a specific id
     */
    @GetMapping("game/{id}")
    public Game getGameById(@PathVariable int id) {
        //implement
        Game game = gameDao.findById(id);
        GameService gameService = new GameService();
        return gameService.getGames(game);
    }

    /*
    * Get rounds based on a specific id  from roundDao
     */
    @GetMapping("rounds/{gameId}")
    //implement
    public List<Round> getGameRounds(@PathVariable int gameId) {
        return roundDao.getAllOfGame(gameId);
    }

}