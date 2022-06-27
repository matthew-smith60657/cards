package com.projects.dao;

import com.projects.model.User;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.util.List;


public interface UserDAO {

    // Get User by Id
    public User getUser(int user_id);
    // Get User by Name
    public User getUserByNameAndPassword(String name, String pwd);
    // Get All Users
    public List<User> getAllUserNames();
    // Create New User (return id)
    public int createUser(String name, String pwd);
    // Update Existing User
    public void updateUser(User user);
    // Delete User (return id)
    public int deleteUser(User user);

}
