/*
 * Description: Pagina de Login
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 7:14:01 PM
 * version $
 */

import React from 'react';
import { withAlert, AlertManager } from 'react-alert';
import { connect } from 'react-redux';

import UserAction from '../actions/users';
import { VirtualWebState } from '../reducers';
import { Redirect } from 'react-router';
import { login } from '../services/servicosApi';

const mapStateToProps = (state: VirtualWebState) => {
  return {
    user: state.user,
  };
};

const mapDispatchToProps = {
  setUserByToken: UserAction.setUserByToken,
};

interface LoginProps {
  alert: AlertManager;
}

type AllLoginProps = LoginProps &
  ReturnType<typeof mapStateToProps> &
  typeof mapDispatchToProps;

interface LoginState {
  email: string;
  senha: string;
  errors: any;
  invalids: any;
}

class LoginPage extends React.Component<AllLoginProps, LoginState> {
  constructor(props: AllLoginProps) {
    super(props);

    this.state = {
      email: '',
      senha: '',
      errors: {
        email: false,
        senha: false,
      },
      invalids: {
        email: false,
      },
    };
  }

  handleEmail(e: any) {
    const value = e.currentTarget.value.trim();
    this.setState({
      email: value,
      errors: {
        ...this.state.errors,
        email: !value,
      },
    });
  }

  handleSenha(e: any) {
    const value = e.currentTarget.value.trim();
    this.setState({
      senha: value,
      errors: {
        ...this.state.errors,
        senha: !value,
      },
    });
  }

  async login(e: any) {
    e.preventDefault();
    let hasEmailError = true;
    if (this.state.email.trim()) {
      hasEmailError = false;
    }
    let hasSenhaError = true;
    if (this.state.senha.trim()) {
      hasSenhaError = false;
    }
    this.setState({
      errors: {
        email: hasEmailError,
        senha: hasSenhaError,
      },
    });

    if (hasEmailError || hasSenhaError) {
      return;
    }

    try {
      const token = await login(this.state.email, this.state.senha);
      this.props.setUserByToken(token);
    } catch (e) {
      this.props.alert.error(e);
    }
  }

  render() {
    if (this.props.user.token) {
      return <Redirect to="/" />;
    }

    return (
      <div className="ui grid centered">
        <div className="column six wide">
          <h2>Login</h2>
          <form className="ui form">
            <div className={`field ${this.state.errors.email ? 'error' : ''}`}>
              <label>E-mail</label>
              <input
                type="text"
                name="email"
                placeholder="E-mail"
                onChange={e => this.handleEmail(e)}
                value={this.state.email}
                required={true}
                autoFocus
              />
            </div>
            <div className={`field ${this.state.errors.senha ? 'error' : ''}`}>
              <label>Senha</label>
              <input
                type="password"
                name="senha"
                placeholder="Senha"
                onChange={e => this.handleSenha(e)}
                value={this.state.senha}
                required={true}
              />
            </div>
            <button
              className="ui button"
              type="submit"
              onClick={e => this.login(e)}
            >
              Entrar
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default withAlert()(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(LoginPage)
);
