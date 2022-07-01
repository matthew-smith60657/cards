package com.projects.dao;

import com.projects.model.User;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDAO {

    private final JdbcTemplate jdbcTemplate;
    private final int ERROR_CODE = -1;

    public JdbcUserDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public User getUser(int user_id) {
        User user;

        String sql = "SELECT user_id, name, " +
                        " password_not_secure_at_all, war_id, go_fish_id " +
                        "FROM user_table " +
                        "WHERE user_id = ? " +
                        "LIMIT 1;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user_id);

        if(rowSet.next()) {
            user = mapUserRowToUser(rowSet);
        } else {
            return null;
        }

        return user;
    }

    private User mapUserRowToUser(SqlRowSet rowSet) {
        User user = new User();

        user.setUserId(rowSet.getInt("user_id"));
        user.setName(rowSet.getString("name"));
        user.setPasswordNotSecureAtAll(rowSet.getString("password_not_secure_at_all"));
        user.setWarId(rowSet.getInt("war_id"));
        user.setGoFishId(rowSet.getInt("go_fish_id"));

        return user;
    }

    @Override
    public User getUserByNameAndPassword(String name, String pwd) {
        String sql = "SELECT user_id, name, " +
                "password_not_secure_at_all, war_id, go_fish_id " +
                "FROM user_table " +
                "WHERE name = ? LIMIT 1;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, name);
        // No User record with that name found, so return null
        if (!rowSet.next()) {
            return null;
        }
        // User found, password matches so return User
        else if (pwd.equals(rowSet.getString("password_not_secure_at_all"))) {
            return mapUserRowToUser(rowSet);
        }
        // User found, but password doesn't match
        else {
            // Return a non-null User with id = -1 for error handling on wrong password
            User dummy = new User();
            dummy.setUserId(ERROR_CODE);
            return dummy;
        }
    }

    @Override
    public List<User> getAllUserNames() {
        List<User> userList = new ArrayList<>();

        String sql = "SELECT user_id, name, " +
                        "password_not_secure_at_all, war_id, go_fish_id " +
                        "FROM user_table;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while(rowSet.next()) {
            userList.add(mapUserRowToUser(rowSet));
        }

        return userList;
    }

    @Override
    public int createUser(String name, String pwd) {
        User user = getUserByNameAndPassword(name, pwd);
        // If User by that name exists, return error code -1
        if(user == null) {
            String sql = "INSERT INTO user_table (name, password_not_secure_at_all) " +
                    "VALUES (?, ?) RETURNING user_id;";
            int newId = jdbcTemplate.queryForObject(sql, Integer.class, name, pwd);
            // Get record just inserted for validation
            user = getUser(newId);

            return user.getUserId();
        } else {
            // Returned user wasn't null, so that username already exists
            System.out.println("A user by that name already exists");
            return ERROR_CODE;
        }
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE user_table " +
                        "SET name = ?, " +
                        "password_not_secure_at_all = ?, " +
                        "WHERE user_id = ? ;";

        jdbcTemplate.update(sql, user.getName(), user.getPasswordNotSecureAtAll(), user.getUserId());

        sql = "SELECT war_id, go_fish_id FROM user_table WHERE user_id = ? ;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user.getWarId());

        if(rowSet.next()) {
            System.out.println("Stored War ID: " + rowSet.getInt("war_id"));
            System.out.println("Stored Go Fish ID: " + rowSet.getInt("go_fish_id"));
        }
    }

    public void updateUserWarID(User user) {
        String sql = "UPDATE user_table " +
                        "SET war_id = ? " +
                        "WHERE user_id = ? ;";
        jdbcTemplate.update(sql, user.getWarId(), user.getUserId());
    }

    public void updateUserGoFishID(User user) {
        String sql = "UPDATE user_table " +
                        "SET go_fish_id = ? " +
                        "WHERE user_id = ? ;";
        jdbcTemplate.update(sql, user.getGoFishId(), user.getUserId());
    }

    @Override
    public int deleteUser(User user) {
        String sql = "DELETE FROM user_table " +
                        "WHERE user_id = ? RETURNING user_id;";
        int deletedId = jdbcTemplate.queryForObject(sql, Integer.class, user.getUserId());
        return deletedId;
    }
}
