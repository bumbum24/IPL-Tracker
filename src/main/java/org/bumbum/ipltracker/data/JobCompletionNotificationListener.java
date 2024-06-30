package org.bumbum.ipltracker.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bumbum.ipltracker.model.Match;
import org.bumbum.ipltracker.model.Team;
import org.hibernate.sql.Insert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    // @PersistenceContext
    // private final EntityManager entityManager;

    // @Autowired
    // public JobCompletionNotificationListener(EntityManager entityManager) {
    // this.entityManager = entityManager;
    // }

    private final JdbcTemplate jdbcTemplate;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamData = new HashMap<>();

            jdbcTemplate.query("select m.team1,count(*) from Match m group by m.team1",
                    new RowMapper<Map<String, Integer>>() {
                        @Override
                        public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Map<String, Integer> result = new HashMap<>();
                            result.put(rs.getString(1), rs.getInt(2));
                            Team team = new Team(rs.getString(1), rs.getInt(2));
                            teamData.put(team.getTeamName(), team);
                            return result;
                        }
                    });

            jdbcTemplate.query("select m.team2,count(*) from Match m group by m.team2",
                    new RowMapper<Map<String, Integer>>() {
                        @Override
                        public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Map<String, Integer> result = new HashMap<>();
                            result.put(rs.getString(1), rs.getInt(2));
                            Team team = teamData.get(rs.getString(1));
                            if (team != null)
                                team.setTotalMatches(team.getTotalMatches() + (long) rs.getLong(2));
                            return result;
                        }
                    });

            jdbcTemplate.query("select m.winner,count(*) from Match m group by m.winner",
                    new RowMapper<Map<String, Integer>>() {
                        @Override
                        public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Map<String, Integer> result = new HashMap<>();
                            result.put(rs.getString(1), rs.getInt(2));
                            Team team = teamData.get(rs.getString(1));
                            if (team != null)
                                team.setTotalWins(team.getTotalWins() + (long) rs.getLong(2));
                            return result;
                        }
                    });
            
            // entityManager.createQuery("select m.team1,count(*) from Match m group by m.team1", Object[].class)
            // .getResultList()
            // .stream()
            // .map(tm -> new Team((String)tm[0],(long)tm[1]))
            // .forEach(team->teamData.put(team.getTeamName(),team));

            // entityManager.createQuery("select m.team2,count(*) from Match m group by m.team2", Object[].class)
            // .getResultList()
            // .stream()
            // .forEach(tm -> {
            // Team team = teamData.get((String) tm[0]);
            // if (team != null)
            // team.setTotalMatches(team.getTotalMatches() + (long) tm[1]);
            // });

            // entityManager.createQuery("select m.winner,count(*) from Match m group by m.winner", Object[].class)
            // .getResultList()
            // .stream()
            // .forEach(tm -> {
            // Team team = teamData.get((String) tm[0]);
            // if (team != null)
            // team.setTotalWins((long) tm[1]);
            // });

            // teamData.values().forEach(team -> entityManager.persist(team));

            teamData.values().forEach(team -> System.out
                    .println("[ " + team.getTeamName() + " ] => Total Matches : " + team.getTotalMatches()
                            + " | Wins : " + team.getTotalWins()));

            String teamSQL = "INSERT INTO Team (TEAM_NAME,TOTAL_MATCHES,TOTAL_WINS) VALUES (?,?,?)";
            teamData.values().forEach(team -> jdbcTemplate.update(teamSQL, team.getTeamName(), team.getTotalMatches(),
                     team.getTotalWins()));

        }
    }
}
