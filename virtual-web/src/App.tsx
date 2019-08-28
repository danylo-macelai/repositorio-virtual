/*
 * Description: Componente para renderizacao do template padrao e das rotas
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Fri Jul 26 2019 9:43:20 PM
 * version $
 */

import React from 'react';
import { HashRouter, Route } from 'react-router-dom';
import { transitions, positions, Provider as AlertProvider } from 'react-alert';

import './App.scss';

import AlertTemplate from './components/AlertTemplate';
import Header from './components/Header';
import Arquivo from './pages/Arquivo';

const options: any = {
  position: positions.TOP_RIGHT,
  timeout: 5000,
  offset: '12px',
  transition: transitions.FADE,
};

class App extends React.Component {
  render() {
    return (
      <HashRouter basename="/">
        <AlertProvider template={AlertTemplate} {...options}>
          <Header />

          <div className="main-content">
            <div className="ui container">
              <div className="main-content-wrap">
                <Route path="/arquivo" component={Arquivo} />
              </div>
            </div>
          </div>
        </AlertProvider>
      </HashRouter>
    );
  }
}

export default App;
