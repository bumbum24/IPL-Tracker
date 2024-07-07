package org.bumbum.ipltracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.bumbum.ipltracker.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MatchRepository extends CrudRepository<Match,Long>{

    List<Match> getByTeam1OrTeam2OrderByDateDesc(String team1,String team2, Pageable pageable);

    default List<Match> getLatestMatches(String teamName, int count){
        return getByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0,count));
    }

    @Query("SELECT m FROM Match m WHERE (m.team1=:teamName OR m.team2=:teamName) AND m.date BETWEEN :startDate AND :endDate ORDER BY m.date DESC")
    List<Match> getMatchesByTeamBetweenDates(
    @Param("teamName") String teamName,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate);
}
