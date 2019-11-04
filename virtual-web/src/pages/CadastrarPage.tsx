/*
 * Description: Tela de cadastro de usuario
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Mon Nov 04 2019 4:54:39 PM
 * version $
 */

import React from 'react';
import { connect } from 'react-redux';
import { withAlert, AlertManager } from 'react-alert';
import UserAction from '../actions/users';
import { VirtualWebState } from '../reducers';
import { Redirect } from 'react-router';
import { login, cadastrar } from '../services/servicosApi';

const mapStateToProps = (state: VirtualWebState) => {
  return {};
};

const mapDispatchToProps = {
  setUserByToken: UserAction.setUserByToken,
};

interface CadastrarProps {
  alert: AlertManager;
}

type AllCadastrarProps = CadastrarProps & typeof mapDispatchToProps;

interface CadastrarState {
  nome: string;
  email: string;
  senha: string;
  errors: any;
  created: boolean;
}

class CadastrarPage extends React.Component<AllCadastrarProps, CadastrarState> {
  constructor(props: AllCadastrarProps) {
    super(props);

    this.state = {
      nome: '',
      email: '',
      senha: '',
      errors: {
        nome: false,
        email: false,
        senha: false,
      },
      created: false,
    };
  }

  handleNome(e: any) {
    const value = e.currentTarget.value;

    this.setState({
      nome: value,
      errors: {
        ...this.state.errors,
        nome: !value,
      },
    });
  }

  handleEmail(e: any) {
    const value = e.currentTarget.value;

    this.setState({
      email: value,
      errors: {
        ...this.state.errors,
        email: !value,
      },
    });
  }

  handleSenha(e: any) {
    const value = e.currentTarget.value;

    this.setState({
      senha: value,
      errors: {
        ...this.state.errors,
        senha: !value,
      },
    });
  }

  async cadastrar(e: any) {
    e.preventDefault();

    const { nome, email, senha } = this.state;

    let hasNomeError = false;
    if (!nome.trim()) {
      hasNomeError = true;
    }

    let hasEmailError = false;
    if (!email.trim()) {
      hasEmailError = true;
    }

    let hasSenhaError = false;
    if (!senha.trim()) {
      hasSenhaError = true;
    }

    this.setState({
      errors: {
        nome: hasNomeError,
        email: hasEmailError,
        senha: hasSenhaError,
      },
    });

    if (hasNomeError || hasEmailError || hasSenhaError) {
      return;
    }

    const usuario = {
      id: '',
      nome,
      email,
      senha,
      token: '',
      perfilType: '',
    };
    try {
      await cadastrar(usuario);
      console.log('Testeste');
      const token = await login(email, senha);
      console.log('TOKEN ====>' + token);
      this.props.setUserByToken(token);
      this.setState({
        nome: '',
        email: '',
        senha: '',
        errors: {
          nome: false,
          email: false,
          senha: false,
        },
        created: true,
      });
    } catch (e) {
      this.props.alert.error(e);
    }
  }

  render() {
    if (this.state.created) {
      return <Redirect to="/bem-vindo" />;
    }

    return (
      <div className="ui grid centered">
        <div className="column six wide">
          <h2>Cadastro</h2>
          <form className="ui form">
            <div className={`field ${this.state.errors.nome ? 'error' : ''}`}>
              <label>Nome</label>
              <input
                type="text"
                name="nome"
                value={this.state.nome}
                onChange={e => this.handleNome(e)}
              />
            </div>
            <div className={`field ${this.state.errors.email ? 'error' : ''}`}>
              <label>E-mail</label>
              <input
                type="text"
                name="email"
                value={this.state.email}
                onChange={e => this.handleEmail(e)}
              />
            </div>
            <div className={`field ${this.state.errors.senha ? 'error' : ''}`}>
              <label>Senha</label>
              <input
                type="password"
                name="senha"
                value={this.state.senha}
                onChange={e => this.handleSenha(e)}
              />
            </div>

            <button
              type="submit"
              className="ui submit button"
              onClick={e => this.cadastrar(e)}
            >
              Cadastrar
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
  )(CadastrarPage)
);
