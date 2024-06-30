package org.bumbum.ipltracker.controller;

import org.bumbum.ipltracker.model.Team;
import org.bumbum.ipltracker.repository.MatchRepository;
import org.bumbum.ipltracker.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {

    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository){
        this.teamRepository=teamRepository;
        this.matchRepository=matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName){
        Team team= this.teamRepository.findByTeamName(teamName);
        team.setMatches(this.matchRepository.getLatestMatches(teamName, 5));
        return team;
    }    
}
