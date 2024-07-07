import {React } from 'react';
import { Link } from 'react-router-dom';
import './MatchDetailCard.scss';

export const MatchDetailCard = ({teamName, match}) => {
  const otherTeam = match.team1 === teamName ? match.team2 : match.team1;
  const isWinner = teamName === match.winner;
  if (!match) 
    return null;
  return (
    <div className={isWinner?'MatchDetailCard won-card':'MatchDetailCard lost-card'}>
      <div>
      <span className='vs'>vs</span>
      <h2><Link to={`/teams/${otherTeam}`}>{otherTeam}</Link></h2>
      <h3 className='match-info'>on {match.date} at {match.venue}</h3>
      <h3 className='match-result'>Result : {match.winner} won by {match.resultMargin} {match.result}</h3>
      </div>
      <div className='additional-info'>
      <h2>Toss</h2>
      <p>{match.tossWinner} won the Toss and chose to {match.tossDecision}</p>
      <h2>Man Of The Match</h2>
      <p>{match.manOfMatch}</p>
      </div>
    </div>
  );
}