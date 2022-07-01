package com.projects.dao;

import com.projects.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcWarDao implements WarDAO {

    JdbcTemplate jdbcTemplate;

    public JdbcWarDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public int createWar() {
        int newId;
        String sql = "INSERT INTO war(played_war, won_war) " +
                        "VALUES (0, 0) RETURNING war_id;";

        newId = jdbcTemplate.queryForObject(sql, int.class);
        // TODO: 6/29/2022 don't forget to have UserDAO update User record with newly assigned war_id
        return newId;
    }

    @Override
    public User getWar(User user) {
        String sql = "SELECT war_id, played_war, won_war, shortest_win, longest_win " +
                        "FROM war " +
                        "WHERE war_id = ? ;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user.getWarId());
        if(rowSet.next()) {
            return mapWarToExistingUser(user, rowSet);
        } else {
            return null;
        }
    }

    @Override
    public void updateWar(User user) {
        String sql = "UPDATE war SET played_war = ? ," +
                        "won_war = ? , " +
                        "shortest_win = ? , " +
                        "longest_win = ? " +
                        "WHERE war_id = ? ;";
        // TODO: 6/29/2022 Coalesce shortest/fastest & longest/slowest... 
        jdbcTemplate.update(sql, user.getPlayedWar(),
                                 user.getWonWar(),
                                 user.getFastestWarWin(),
                                 user.getSlowestWarWin(),
                                 user.getWarId());
        // TODO: 6/29/2022 Need to validate the war table update went ok?

        sql = "SELECT played_war, won_war, shortest_win, longest_win FROM war WHERE war_id = ? ;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user.getWarId());

        if(rowSet.next()) {
            System.out.println("Stored played_war: " + rowSet.getInt("played_war"));
            System.out.println("Stored won_war: " + rowSet.getInt("won_war"));
            System.out.println("Stored shortest_win: " + rowSet.getInt("shortest_win"));
            System.out.println("Stored longest_win: " + rowSet.getInt("longest_win"));
        }
    }

    private User mapWarToExistingUser(User user, SqlRowSet rowSet) {

        User warUser = new User(rowSet.getInt("war_id"),
                                rowSet.getInt("played_war"),
                                rowSet.getInt("won_war"),
                                rowSet.getInt("shortest_win"),
                                rowSet.getInt("longest_win"));
        user.copyWarRecordsIntoUser(warUser);
        return user;
    }
}