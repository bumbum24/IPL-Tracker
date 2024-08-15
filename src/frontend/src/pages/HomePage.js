import {React, useEffect, useState } from 'react';
import { TeamTile } from '../components/TeamTile';
import  './HomePage.scss';

export const HomePage = () => {

  const [team, setTeams] = useState([]);
  useEffect(
    () => {
      const fetchTeams = async () => {
        const response = await fetch(`/teams`);
        const data = await response.json();
        setTeams(data);
      };
      fetchTeams();
    }, []
  );

  return (
    <div className="HomePage">
      <div className='header-section'>
        <h1 className='app-name'> Welcome to BumBum's IPL Tracker </h1>
      </div>
        <div className="team-grid">
            {team.map(team => <TeamTile key={team.teamName} teamName={team.teamName}/>)}
        </div>
      
    </div>
  );
}