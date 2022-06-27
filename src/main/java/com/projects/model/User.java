package com.projects.model;

public class User {

    private int userId;
    private String name;
    private String passwordNotSecureAtAll;
    private int warId;
    private String wonWar;
    private String playedWar;
    private String fastestWarWin;
    private String slowestWarWin;
    private int goFishId;
    private String wonGoFish;
    private String playedGoFish;
    private String mostPointsGoFish;

    public User () {
    }

    public User (String name, String pwd) {
        this.name = name;
        this.passwordNotSecureAtAll = pwd;
    }

    // This is really only practical for the DAO testing
    public User(int userId, String name, String pwd, int warId, int goFishId) {
        this.userId = userId;
        this.name = name;
        this.passwordNotSecureAtAll = pwd;
        this.warId = warId;
        this.goFishId = goFishId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPasswordNotSecureAtAll() {
        return passwordNotSecureAtAll;
    }

    public void setPasswordNotSecureAtAll(String passwordNotSecureAtAll) {
        this.passwordNotSecureAtAll = passwordNotSecureAtAll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWarId() {
        return warId;
    }

    public void setWarId(int warId) {
        this.warId = warId;
    }

    public int getGoFishId() {
        return goFishId;
    }

    public void setGoFishId(int goFishId) {
        this.goFishId = goFishId;
    }

    public String getWonWar() {
        return wonWar;
    }

    public void setWonWar(String wonWar) {
        this.wonWar = wonWar;
    }

    public String getPlayedWar() {
        return playedWar;
    }

    public void setPlayedWar(String playedWar) {
        this.playedWar = playedWar;
    }

    public String getFastestWarWin() {
        return fastestWarWin;
    }

    public void setFastestWarWin(String fastestWarWin) {
        this.fastestWarWin = fastestWarWin;
    }

    public String getSlowestWarWin() {
        return slowestWarWin;
    }

    public void setSlowestWarWin(String slowestWarWin) {
        this.slowestWarWin = slowestWarWin;
    }

    public String getWonGoFish() {
        return wonGoFish;
    }

    public void setWonGoFish(String wonGoFish) {
        this.wonGoFish = wonGoFish;
    }

    public String getPlayedGoFish() {
        return playedGoFish;
    }

    public void setPlayedGoFish(String playedGoFish) {
        this.playedGoFish = playedGoFish;
    }

    public String getMostPointsGoFish() {
        return mostPointsGoFish;
    }

    public void setMostPointsGoFish(String mostPointsGoFish) {
        this.mostPointsGoFish = mostPointsGoFish;
    }

    public boolean validatePassword(String pwd) {
        return pwd.equals(this.passwordNotSecureAtAll);
    }
}
