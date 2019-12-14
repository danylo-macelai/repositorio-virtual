/*
 * Description: Componente para renderizacao do template padrao e das rotas
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Fri Jul 26 2019 9:43:20 PM
 * version $
 */

import * as React from 'react';
import { HashRouter, Route } from 'react-router-dom';
import { transitions, positions, Provider as AlertProvider } from 'react-alert';
import { connect } from 'react-redux';

import { AlertTemplate, Header } from './components/layout/index';
import {
  ArquivoPage,
  LoginPage,
  CadastrarPage,
  BemVindoPage,
  ArquivoUploadPage,
  MeusArquivosPage,
  SobrePage,
} from './pages/index';

import './App.scss';
import { VirtualWebState } from './reducers';
import UserAction from './actions/users';
import virtecaVerticalLogo from './assets/images/virteca-vertical.png';

const options: any = {
  position: positions.TOP_RIGHT,
  timeout: 5000,
  offset: '12px',
  transition: transitions.FADE,
};

const mapStateToProps = (state: VirtualWebState) => ({
  user: state.user,
});

const mapDispatchToProps = {
  setUserFromStorage: UserAction.setUserFromStorage,
};

type AppProps = ReturnType<typeof mapStateToProps> & typeof mapDispatchToProps;

interface AppState {
  isUserLoaded: Boolean;
}

class App extends React.Component<AppProps, AppState> {
  constructor(props: AppProps) {
    super(props);

    this.props.setUserFromStorage();
    this.state = {
      isUserLoaded: false,
    };
  }

  componentWillReceiveProps(nextProps: AppProps, prevProps: AppProps) {
    if (!prevProps.user) {
      setTimeout(() => {
        this.setState({
          isUserLoaded: true,
        });
      }, 1000);
    }
  }

  render() {
    if (!this.state.isUserLoaded) {
      return (
        <div className="app-loading">
          <div className="ui active loader">
            <img src={virtecaVerticalLogo} alt="Virteca" />
          </div>
        </div>
      );
    }

    return (
      <HashRouter basename="/">
        <AlertProvider template={AlertTemplate} {...options}>
          <Header />
          <div className="main-content">
            <div className="ui container">
              <div className="main-content-wrap">
                <Route path="/" exact={true} component={ArquivoPage} />
                <Route path="/arquivo" exact={true} component={ArquivoPage} />
                <Route path="/login" exact={true} component={LoginPage} />
                <Route
                  path="/cadastrar"
                  exact={true}
                  component={CadastrarPage}
                />
                <Route
                  path="/bem-vindo"
                  exact={true}
                  component={BemVindoPage}
                />
                <Route
                  path="/arquivo-upload"
                  exact={true}
                  component={ArquivoUploadPage}
                />
                <Route
                  path="/meus-arquivos"
                  exact={true}
                  component={MeusArquivosPage}
                />
                <Route path="/sobre" exact={true} component={SobrePage} />
              </div>
            </div>
          </div>
        </AlertProvider>
      </HashRouter>
    );
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
