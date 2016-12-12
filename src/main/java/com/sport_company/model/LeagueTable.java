package com.sport_company.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nikolay.odintsov on 17/09/16.
 * <p>
 * Class is a model for league table.
 */
public class LeagueTable {

    private final List<LeagueTableEntry> leagueTableEntries;

    /**
     * Creates a league table from the supplied list of match results.
     * Provides calculation for {@link LeagueTableEntry} based on {@link Match} result.
     *
     * @param matches is data for league table entry calculation
     */
    public LeagueTable(final List<Match> matches) {
        this.leagueTableEntries = calculateLeagueTable(matches);
    }

    /**
     * Performs ordering list of {@link LeagueTableEntry} by points, goalDifference, goalsFor, teamName.
     *
     * @return sorted {@link LeagueTableEntry} list
     */
    public List<LeagueTableEntry> getTableEntries() {
        return leagueTableEntries.stream().sorted(Comparator.comparing(LeagueTableEntry::getPoints).reversed()
                .thenComparing(LeagueTableEntry::getGoalDifference).reversed()
                .thenComparing(LeagueTableEntry::getGoalsFor)
                .thenComparing(LeagueTableEntry::getTeamName)
        ).collect(Collectors.toList());
    }

    /**
     * Performs calculation of {@link LeagueTableEntry} based on {@link Match}.
     * <p>
     * 1. Retrieves all {@link Match} scores for each team.
     * 2. Converts a {@link Match} result of a team to team league plays list {@link LeagueTableEntry}.
     * 2. Reduces team plays list {@link LeagueTableEntry} to a single entry and calculates its totals.
     *
     * @param matches matches list
     * @return list of league table entry
     */
    private List<LeagueTableEntry> calculateLeagueTable(List<Match> matches) {

        Map<String, List<Match>> teamsMatches = getMatchesListPerTeam(matches);

        List<LeagueTableEntry> leagueTableEntryList = new ArrayList<>();

        //loop team matches and compute LeagueTableEntry per team.
        teamsMatches.entrySet().stream().forEach(team -> {

            String teamName = team.getKey();

            List<LeagueTableEntry> leaguePlays = new ArrayList<>();
            team.getValue().stream().forEach(match -> {
                TeamMatchScore teamMatchScore = new TeamMatchScore(teamName, match).calculate();
                LeagueTableEntry leagueTableEntry = new LeagueTableEntry(teamName, 1,
                        teamMatchScore.getWon(), teamMatchScore.getDrawn(), teamMatchScore.getLost(),
                        teamMatchScore.getGoalsFor(), teamMatchScore.getGoalsAgainst(), teamMatchScore.getGoalDifference(),
                        teamMatchScore.getPoints());
                leaguePlays.add(leagueTableEntry);
            });

            LeagueTableEntry result =
                    leaguePlays
                            .stream()
                            .reduce(new LeagueTableEntry("", 0, 0, 0, 0, 0, 0, 0, 0), (p1, p2) -> {
                                int played = p1.getPlayed() + p2.getPlayed();
                                int won = p1.getWon() + p2.getWon();
                                int drawn = p1.getDrawn() + p2.getDrawn();
                                int lost = p1.getLost() + p2.getLost();
                                int goalsFor = p1.getGoalsFor() + p2.getGoalsFor();
                                int goalsAgainst = p1.getGoalsAgainst() + p2.getGoalsAgainst();
                                int goalDifference = p1.getGoalDifference() + p2.getGoalDifference();
                                int points = p1.getPoints() + p2.getPoints();

                                return new LeagueTableEntry(teamName, played, won, drawn, lost, goalsFor, goalsAgainst, goalDifference, points);
                            });

            System.out.println(String.format("Team %s has league scores %s : ", teamName, result.toString()));
            leagueTableEntryList.add(result);

        });
        return leagueTableEntryList;
    }

    private Map<String, List<Match>> getMatchesListPerTeam(List<Match> matches) {
        Map<String, List<Match>> teamsMatches = new HashMap<>();
        teamsMatches.putAll(matches.stream().collect(Collectors.groupingBy(Match::getHomeTeam)));
        teamsMatches.putAll(matches.stream().collect(Collectors.groupingBy(Match::getAwayTeam)));
        return teamsMatches;
    }

}
