package com.sport_company.model;

/**
 * Created by nikolay.odintsov on 17/09/16.
 * <p>
 * Class is a model for team match score.
 * It also provides score calculation operation according to home/away team.
 */
public class TeamMatchScore {
    private Match match;
    private boolean isHome;
    private int won;
    private int drawn;
    private int lost;
    private int goalDifference;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    public TeamMatchScore(String teamName, Match match) {
        this.match = match;

        boolean isHome = false;
        if (match.getHomeTeam().equals(teamName)) {
            //compute as home team
            isHome = true;
        }
        this.isHome = isHome;
    }

    public int getWon() {
        return won;
    }

    public int getDrawn() {
        return drawn;
    }

    public int getLost() {
        return lost;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getPoints() {
        return points;
    }

    /**
     * Performs team score calculation for both home/away teams.
     *
     * @return match score for specific team.
     */
    public TeamMatchScore calculate() {
        if (match.getAwayScore() > match.getHomeScore()) {
            lost = isHome ? 1 : 0;
            won = isHome ? 0 : 1;
        } else if (match.getAwayScore() < match.getHomeScore()) {
            won = isHome ? 1 : 0;
            lost = isHome ? 0 : 1;
        } else {
            drawn = 1;
        }

        goalsFor = isHome ? match.getHomeScore() : match.getAwayScore();
        goalsAgainst = isHome ? match.getAwayScore() : match.getHomeScore();

        goalDifference = goalsFor - goalsAgainst;

        //TODO: clarify point scoring algorithm. Used zero value temporary.
        points = 0;

        return this;
    }
}
