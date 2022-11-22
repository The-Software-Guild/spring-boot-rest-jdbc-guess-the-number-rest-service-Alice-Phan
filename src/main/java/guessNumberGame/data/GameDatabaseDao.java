package guessNumberGame.data;

import guessNumberGame.models.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;


/*
* @Repository makes our class an injectable dependency
* @Autowired on the constructor asks Spring DI for a JdbcTemplate. The Springboot-starter-jdbc package can create one with nothing but a bit of configuration
* "final" local variables prevent us from modifying variables that shouldn't
* be modified and allow the compiler to optimize our class
 */

@Repository
public class GameDatabaseDao implements GameDao {
     private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game add(Game game) {

        /* we dont need id here because the engine is going to support and give one automatically
        * because id is the primary key (auto increment)
         */
        final String sql = "INSERT INTO Game(answer, isFinished) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        // update jdbc
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, game.getAnswer());
            statement.setBoolean(2, game.getIsFinished());
            return statement;

        }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public List<Game> getAll() {
       //implement
        final String sql = "SELECT game_id, answer, isFinished FROM Game;";
        return jdbcTemplate.query(sql, new GameMapper());
    }


    @Override
    public Game findById(int game_id) {
       //implement
        try {
            final String sql = "SELECT * FROM Game WHERE game_id = ?;";
            return jdbcTemplate.queryForObject(sql, new GameMapper(), game_id);
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean update(Game game) {
        //implement
        final String sql = "UPDATE Game SET answer = ?, isFinished = ?"
                + " WHERE game_id = ?;";

        return jdbcTemplate.update(sql,
                game.getGameId(),
                game.getAnswer(),
                game.getIsFinished()) > 0;

    }

    @Override
    public boolean deleteById(int game_id) {
       //implement
        final String sql = " DELETE FROM Game"
                + " WHERE game_id = ?";
        return jdbcTemplate.update(sql, game_id) > 0;

    }



    /*
    * Use RowMapper to implement the methods for the game
    * ResultSet holds the data that comes back from a SELECT query
    * Step through the ResultSet one row at a time to process the data returned from the query
     */
    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("game_id"));
            game.setAnswer(rs.getString("answer"));
            game.setIsFinished(rs.getBoolean("isFinished"));
            return game;
        }
    }
}
