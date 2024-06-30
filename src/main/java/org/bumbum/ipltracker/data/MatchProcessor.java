package org.bumbum.ipltracker.data;

import java.time.LocalDate;

import org.bumbum.ipltracker.model.Match;
import org.springframework.batch.item.ItemProcessor;

public class MatchProcessor implements ItemProcessor<MatchData, Match> {

  private static final String BAT = "bat";

  @Override
  public Match process(final MatchData matchData) {
    Match match = new Match();
    match.setId(Long.parseLong(matchData.getId()));
    match.setSeason(matchData.getSeason());
    match.setCity(matchData.getCity());
    match.setDate(LocalDate.parse(matchData.getDate()));
    match.setManOfMatch(matchData.getPlayer_of_match());
    match.setVenue(matchData.getVenue());

    String firstInningsTeam, secondInningsTeam;
    if (BAT.equals(matchData.getToss_decision())) {
      firstInningsTeam = matchData.getToss_winner();
      secondInningsTeam = matchData.getToss_winner().equals(matchData.getTeam1()) ? matchData.getTeam2()
          : matchData.getTeam1();
    } else {
      secondInningsTeam = matchData.getToss_winner();
      firstInningsTeam = matchData.getToss_winner().equals(matchData.getTeam1()) ? matchData.getTeam2()
          : matchData.getTeam1();

    }
    match.setTeam1(firstInningsTeam);
    match.setTeam2(secondInningsTeam);
    match.setTossWinner(matchData.getToss_winner());
    match.setTossDecision(matchData.getToss_decision());
    match.setResult(matchData.getResult());
    match.setResultMargin(matchData.getResult_margin());
    match.setWinner(matchData.getWinner());
    match.setUmpire1(matchData.getUmpire1());
    match.setUmpire2(matchData.getUmpire2());

    return match;
  }
}
