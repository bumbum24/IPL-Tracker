import {React } from 'react';
import { Link } from 'react-router-dom';
import './MatchSmallCard.scss';

export const MatchSmallCard = ({teamName, match}) => {
  if (!match) 
    return null;
  const otherTeam = match.team1 === teamName ? match.team2 : match.team1;
  const isWinner = teamName === match.winner;
  return (
    <div className={isWinner?'MatchSmallCard won-card':'MatchSmallCard lost-card'}>
      <span><h4> vs </h4></span>
      <h4><Link to={`/teams/${otherTeam}`}> {otherTeam} </Link></h4>
      <p className='match-result'>Result :  {match.winner} won by {match.resultMargin} {match.result}</p>
    </div>
  );
}