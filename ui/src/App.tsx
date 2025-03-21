import React from 'react';
import './App.css';
import Movies from "./components/Movies";
import Actors from "./components/Actors";

function App() {
  return (
    <div className="App">
        <Movies/>
        <Actors/>
    </div>
  );
}

export default App;
