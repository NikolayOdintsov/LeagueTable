package com.sport_company.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by nikolay.odintsov on 17/09/16.
 */
public class LeagueTableTest {

    @Test
    public void shouldStoreListOfLeaguesEntities() throws Exception {
        //given
        List<Match> matches = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            int teamNumber = 1;
            int homeScore = 2;
            int awayScore = 1;
            if (i > 2 && i <= 4) {
                teamNumber = 2;
                homeScore = 1;
                awayScore = 3;
            }
            Match match = new Match("homeTeam_" + teamNumber, "awayTeam_" + teamNumber, homeScore, awayScore);
            matches.add(match);
        }

        //when
        LeagueTable leagueTable = new LeagueTable(matches);
        List<LeagueTableEntry> result = leagueTable.getTableEntries();

        //then
        assertNotNull(leagueTable);
        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    public void shouldCreateLeagueTableEntriesByMatchAndProvideScoreCalculation() throws Exception {
        //given
        List<Match> matches = getPreparedMatchResults();

        //when
        LeagueTable leagueTable = new LeagueTable(matches);
        List<LeagueTableEntry> result = leagueTable.getTableEntries();

        //then
        assertNotNull(result);
        assertEquals(4, result.size());

        LeagueTableEntry homeTeam_2Result = getLeagueTableEntryForTeam(leagueTable, "awayTeam_2").get();

        assertEquals(3, homeTeam_2Result.getPlayed());
        assertEquals(2, homeTeam_2Result.getWon());
        assertEquals(1, homeTeam_2Result.getDrawn());
        assertEquals(0, homeTeam_2Result.getLost());
        assertEquals(6, homeTeam_2Result.getGoalsFor());
        assertEquals(2, homeTeam_2Result.getGoalsAgainst());
        assertEquals(4, homeTeam_2Result.getGoalDifference());
        //TODO: used stub because I have no idea how to compute points (what exact logic should be). Should be implemented after clarifying.
        assertEquals(0, homeTeam_2Result.getPoints());
    }

    @Test
    public void shouldSortLeagueTableEntries() {
        //given
        List<Match> matches = getPreparedMatchResults();

        //when
        LeagueTable leagueTable = new LeagueTable(matches);
        List<LeagueTableEntry> result = leagueTable.getTableEntries();

        //then
        assertNotNull(result);

        //printout result
        System.out.println("LeagueTable sorted result: ");
        result.stream().forEach(leagueTableEntry -> System.out.println(leagueTableEntry.toString()));

        assertEquals(4, result.size());

        //check max values
        assertEquals(getMaxPoints(result), result.get(0).getPoints());
        assertEquals(getMaxGoalDifference(result), result.get(0).getGoalDifference());
        assertEquals(getMaxGoalFor(result), result.get(0).getGoalsFor());

        //check min values
        assertEquals(getMinPoints(result), result.get(3).getPoints());
        assertEquals(getMinGoalDifference(result), result.get(3).getGoalDifference());
        assertEquals(getMinGoalFor(result), result.get(3).getGoalsFor());

        assertTrue(result.get(0).getPoints() >= result.get(3).getPoints());
        assertTrue(result.get(0).getGoalDifference() >= result.get(3).getGoalDifference());
        assertTrue(result.get(0).getGoalsFor() >= result.get(3).getGoalsFor());

        //we suppose we know team names. Check test data.
        assertEquals("awayTeam_2", result.get(0).getTeamName());
        assertEquals("homeTeam_2", result.get(3).getTeamName());
    }

    private List<Match> getPreparedMatchResults() {
        List<Match> matches = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int teamNumber = 1;
            int homeScore = 2;
            int awayScore = 1;
            if (i > 2 && i <= 4) {
                teamNumber = 2;
                homeScore = 1;
                awayScore = 3;
            }
            //drawn case
            if (i == 5) {
                teamNumber = 2;
                homeScore = 0;
                awayScore = 0;
            }

            Match match = new Match("homeTeam_" + teamNumber, "awayTeam_" + teamNumber, homeScore, awayScore);
            matches.add(match);
        }

        return matches;
    }

    private int getMaxPoints(List<LeagueTableEntry> leagueTableEntries) {
        IntSummaryStatistics stats = leagueTableEntries.stream()
                .collect(Collectors.summarizingInt(LeagueTableEntry::getPoints));
        return stats.getMax();
    }

    private int getMaxGoalDifference(List<LeagueTableEntry> leagueTableEntries) {
        IntSummaryStatistics stats = leagueTableEntries.stream()
                .collect(Collectors.summarizingInt(LeagueTableEntry::getGoalDifference));
        return stats.getMax();
    }

    private int getMaxGoalFor(List<LeagueTableEntry> leagueTableEntries) {
        IntSummaryStatistics stats = leagueTableEntries.stream()
                .collect(Collectors.summarizingInt(LeagueTableEntry::getGoalsFor));
        return stats.getMax();
    }

    private int getMinPoints(List<LeagueTableEntry> leagueTableEntries) {
        IntSummaryStatistics stats = leagueTableEntries.stream()
                .collect(Collectors.summarizingInt(LeagueTableEntry::getPoints));
        return stats.getMin();
    }

    private int getMinGoalDifference(List<LeagueTableEntry> leagueTableEntries) {
        IntSummaryStatistics stats = leagueTableEntries.stream()
                .collect(Collectors.summarizingInt(LeagueTableEntry::getGoalDifference));
        return stats.getMin();
    }

    private int getMinGoalFor(List<LeagueTableEntry> leagueTableEntries) {
        IntSummaryStatistics stats = leagueTableEntries.stream()
                .collect(Collectors.summarizingInt(LeagueTableEntry::getGoalsFor));
        return stats.getMin();
    }

    private Optional<LeagueTableEntry> getLeagueTableEntryForTeam(LeagueTable leagueTable, String teamName) {
        return leagueTable.getTableEntries().stream().filter(leagueTableEntry -> leagueTableEntry.getTeamName().equals(teamName)).findFirst();
    }
}