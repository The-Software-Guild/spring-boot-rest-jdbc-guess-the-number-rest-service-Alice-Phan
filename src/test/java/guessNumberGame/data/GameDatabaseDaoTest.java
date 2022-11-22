package guessNumberGame.data;

import guessNumberGame.data.GameDao;
import junit.framework.TestCase;
import guessNumberGame.Service.GameService;
import guessNumberGame.TestApplicationConfiguration;
import guessNumberGame.models.Game;
import guessNumberGame.models.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDatabaseDaoTest extends TestCase {
    @Autowired
    GameDao gameDao;

   @Autowired
   RoundDao roundDao;
   public GameDatabaseDaoTest()
 {}

    // We need to remove everything/ empty the test database before every single test
    // By emptying the database, we return it to an expected state:
    // if nothing is in the database, only what we add to it will be considered for the test.
    @Before
    public void setUp() {
        List<Round> rounds = roundDao.getAll();
        for(Round round : rounds) {
            roundDao.deleteById(round.getId());
        }

        List<Game> games = gameDao.getAll();
        for(Game game : games) {
            gameDao.deleteById(game.getGameId());
        }
    }


    /*
    * Start by creating our Game, filling in all the details so we can be sure our mapper is working correctly
    * Call add game method to put it into the database,
    * then retrieve the Game back out of the database by calling findById,
    * then use "assertEquals" to see that the game we created in this method is equal to
    * the one we pull from the database
     */
    @Test
    public void testAddGetGames() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game fromDao = gameDao.findById(game.getGameId());
        assertEquals(game.getGameId(), fromDao.getGameId());
    }

    /*
    * Start by creating a game with full details and adding it to the database.
    * Create a second room with full details and add it to the database as well.
    * Then make a call to "getAll" and save it into a List.
    * Start by asserting that we have received two games back
    * Then assert that each game is in the list we get back
     */
    @Test
    public void testGetAll() {
        //implement
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        Game game2 = gameService.newGame();

        gameDao.add(game);

//        GameService gameService2 = new GameService();
        gameDao.add(game2);

        List<Game> games = gameDao.getAll();

        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }


    @Test
    public void testUpdate() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);
        game.setIsFinished(false);
        gameDao.update(game);
//        Game updated = gameDao.findById(game.getGameId());
//        assertTrue(updated.getIsFinished());
        Game fromDao = gameDao.findById(game.getGameId());
        assertEquals(game, fromDao);

    }

    @Test
    public void testDeleteById() {
         //implement
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        Game game2 = gameService.newGame();

//        game.setGameId(game.getGameId());
//        game.setAnswer(game.getAnswer());
//        game.setIsFinished(game.getIsFinished());
        game = gameDao.add(game);
        game2 = gameDao.add(game2);

        gameDao.deleteById(game.getGameId());

        gameDao.findById(game.getGameId());
        List<Game> games = gameDao.getAll();
        assertEquals(1, games.size());
    }
}
