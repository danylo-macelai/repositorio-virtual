/*
 * Description: Componente para renderizacao do template padrao e das rotas
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Fri Jul 26 2019 9:43:20 PM
 * version $
 */

import React from 'react';
import { HashRouter } from 'react-router-dom';

import './App.scss';

import Header from './components/Header';

class App extends React.Component {
  render() {
    return (
      <HashRouter basename="/">
        <Header />

        <div className="main-content">
          <div className="ui container">
            <div className="main-content-wrap">
              {
                // Components route here
              }
            </div>
          </div>
        </div>
      </HashRouter>
    );
  }
}

export default App;
