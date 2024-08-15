import {React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { MatchDetailCard } from '../components/MatchDetailCard';
import "./MatchPage.scss";
import { YearSelector } from '../components/YearSelector';

export const MatchPage = () => {

  const [matches, setMatches] = useState([]);
  const {teamName, year} = useParams();
  useEffect(
    () => {
      const fetchMatches = async () => {
        const response = await fetch(`/team/${teamName}/matches?year=${year}`);
        const data = await response.json();
        setMatches(data);
      };
      fetchMatches();
    }, [teamName,year]
  );

  if(!matches)
      return <h1>No Match Found :-( </h1>
  return (
    <div className="MatchPage">
      <div className='year-selector'>
      <h3>Select Year</h3>
      <YearSelector teamName={teamName}/>
      </div>
        <div>
        <h1 className='match-heading'>{teamName} matches for {year}</h1>
        {
        matches.map(match => <MatchDetailCard key={match.id} teamName={teamName} match={match}/>)
        }
        </div>
    </div>
  );
}