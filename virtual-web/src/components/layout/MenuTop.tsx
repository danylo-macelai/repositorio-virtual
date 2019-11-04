/*
 * Description: Componente de menu do projeto
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Sat Sep 07 2019 11:30:50 AM
 * version $
 */

import React from 'react';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';

import { VirtualWebState } from '../../reducers';

import './scss/MenuTop.scss';
import UserAction from '../../actions/users';

const mapStateToProps = (state: VirtualWebState) => ({
  user: state.user,
});

const mapDispatchToProps = {
  clearUser: UserAction.clearUser,
};

type MenuTopProps = ReturnType<typeof mapStateToProps> &
  typeof mapDispatchToProps;

interface MenuTopState {}

class MenuTop extends React.Component<MenuTopProps, MenuTopState> {
  isUserLogged() {
    return !!this.props.user.token;
  }

  logout() {
    this.props.clearUser();
  }

  loggedOptions() {
    const { nome, email } = this.props.user;

    return (
      <li className="item ui dropdown pointing top right icon simple logged">
        {nome} <i className="chevron down icon small"></i>
        <div className="ui vertical menu">
          <div className="name">{nome}</div>
          <div className="email">{email}</div>

          <div className="ui divider"></div>

          <button
            type="button"
            className="ui button basic fluid small"
            onClick={() => this.logout()}
          >
            Sair
          </button>
        </div>
      </li>
    );
  }

  unloggedOptions() {
    return (
      <>
        <li className="item">
          <Link to="/login">Login</Link>
        </li>
        <li className="item">
          <Link to="/sign-up">Cadastrar</Link>
        </li>
      </>
    );
  }

  render() {
    return (
      <nav className="menu-top">
        <ul
          className={`ui list horizontal link ${
            !this.isUserLogged() ? 'celled' : ''
          }`}
        >
          {this.isUserLogged() ? this.loggedOptions() : this.unloggedOptions()}
        </ul>
      </nav>
    );
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MenuTop);
