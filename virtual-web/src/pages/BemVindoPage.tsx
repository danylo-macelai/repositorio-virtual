/*
 * Description: Tela de bem vindo
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Mon Nov 04 2019 4:54:39 PM
 * version $
 */

import React from 'react';
import { VirtualWebState } from '../reducers';
import { connect } from 'react-redux';
import virtecaVerticalLogo from '../assets/images/virteca-vertical.png';

import './scss/BemVindo.scss';
import { Link } from 'react-router-dom';

const mapStateToPros = (state: VirtualWebState) => {
  return {
    user: state.user,
  };
};

type BemVindoProps = ReturnType<typeof mapStateToPros>;

interface BemVindoState {}

class BemVindoPage extends React.Component<BemVindoProps, BemVindoState> {
  render() {
    return (
      <div className="ui grid centered bem-vindo-page">
        <div className="ui center aligned basic segment">
          <div className="img">
            <img src={virtecaVerticalLogo} alt="Virteca" />
          </div>
          <div className="ui header">Olá {this.props.user.nome},</div>
          <div className="ui content">
            <p>Seja bem vindo ao VirTeca!</p>
          </div>
          <div className="buttons">
            <Link to="/" className="ui button primary" replace={true}>
              Página inicial
            </Link>
          </div>
        </div>
      </div>
    );
  }
}

export default connect(mapStateToPros)(BemVindoPage);
