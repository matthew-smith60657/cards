package com.projects.dao;

import com.projects.model.User;

public interface WarDAO {

    // create new record, return war_id
    public int createWar();
    // get existing record
    public User getWar(User user);
    // update existing record
    public void updateWar(User user);

}
