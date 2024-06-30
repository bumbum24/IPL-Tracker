package org.bumbum.ipltracker.repository;

import org.bumbum.ipltracker.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<Team,String>{
    Team findByTeamName(String teamName);
}
