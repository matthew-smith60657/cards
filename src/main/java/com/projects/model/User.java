package com.projects.model;

public class User {

    private int userId;
    private String name;
    private String passwordNotSecureAtAll;
    private int warId;
    private int wonWar;
    private int playedWar;
    private int fastestWarWin;
    private int slowestWarWin;
    private int goFishId;
    private int wonGoFish;
    private int playedGoFish;
    private int mostPointsGoFish;

    public User () {
    }

    public User (String name, String pwd) {
        this.name = name;
        this.passwordNotSecureAtAll = pwd;
    }

    // This is really only practical for the User DAO testing
    public User(int userId, String name, String pwd, int warId, int goFishId) {
        this.userId = userId;
        this.name = name;
        this.passwordNotSecureAtAll = pwd;
        this.warId = warId;
        this.goFishId = goFishId;
    }
    // This is really only practical for the War DAO testing
    public User(int userId, String name, String pwd, int warId, int goFishId, int playedWar, int wonWar, int fastestWarWin, int slowestWarWin) {
        this.userId = userId;
        this.name = name;
        this.passwordNotSecureAtAll = pwd;
        this.warId = warId;
        this.goFishId = goFishId;
        this.playedWar = playedWar;
        this.wonWar = wonWar;
        this.fastestWarWin = fastestWarWin;
        this.slowestWarWin = slowestWarWin;
    }
    // War User Constructor -- I wonder if I should have made child class for WarUser instead of this
    public User(int warId, int playedWar, int wonWar, int fastestWarWin, int slowestWarWin) {
        this.warId = warId;
        this.playedWar = playedWar;
        this.wonWar = wonWar;
        this.fastestWarWin = fastestWarWin;
        this.slowestWarWin = slowestWarWin;
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

    public int getWonWar() {
        return wonWar;
    }

    public void increaseWonWar() {
        this.wonWar++;
    }

    public int getPlayedWar() {
        return playedWar;
    }

    public void increasePlayedWar() {
        this.playedWar++;
    }

    public int getFastestWarWin() {
        return fastestWarWin;
    }

    public void setIfFastestWarWin(int fastestWarWin) {
        if(this.fastestWarWin > fastestWarWin || this.fastestWarWin == 0) {
            if(this.fastestWarWin != 0) {
                System.out.println("You set a record for your fastest win!");
                System.out.println("You've improved from " + this.fastestWarWin + " to " + fastestWarWin + ". Way to kick some butt!");
            };
            this.fastestWarWin = fastestWarWin;
        }
    }

    public int getSlowestWarWin() {
        return slowestWarWin;
    }

    public void setIfSlowestWarWin(int slowestWarWin) {
        if(this.slowestWarWin < slowestWarWin || slowestWarWin == 0) {
            if(this.slowestWarWin != 0) {
                System.out.println("You set a record for your slowest win!");
                System.out.println("You've 'improved' from " + this.slowestWarWin + " to " + slowestWarWin + ". Way to stick it out!");
            };
            this.slowestWarWin = slowestWarWin;
        }
    }

    public int getWonGoFish() {
        return wonGoFish;
    }

    public void incrementWonGoFish() {
        this.wonGoFish++;
    }

    public int getPlayedGoFish() {
        return playedGoFish;
    }

    public void incrementPlayedGoFish() {
        this.playedGoFish++;
    }

    public int getMostPointsGoFish() {
        return mostPointsGoFish;
    }

    public boolean setIfMostPointsGoFish(int mostPointsGoFish) {
        if(this.mostPointsGoFish < mostPointsGoFish) {
            this.mostPointsGoFish = mostPointsGoFish;
            return true;
        }
        return false;
    }
    // Copy User record from DAO into logged in user instance
    public void copyWarRecordsIntoUser (User warUser) {
        if(this.warId == 0) {
            // First time War user
            this.warId = warUser.getWarId();
        }
        if (this.warId == warUser.getWarId()) {
            this.playedWar = warUser.getPlayedWar();
            this.wonWar = warUser.getWonWar();
            this.slowestWarWin = warUser.getSlowestWarWin();
            this.fastestWarWin = warUser.getFastestWarWin();
        } // else they match and carry on with the import
        else {
            System.out.println("Matt messed up and these tables don't match...");
        }
    }
    public void warCompleted(boolean victory, int length) {
        this.increasePlayedWar();
        if(victory) {
            this.increaseWonWar();
            this.setIfFastestWarWin(length);
            this.setIfSlowestWarWin(length);
        }
    }

    public boolean validatePassword(String pwd) {
        return pwd.equals(this.passwordNotSecureAtAll);
    }
}
