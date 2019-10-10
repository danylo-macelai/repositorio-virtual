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

import './MenuTop.scss';

class MenuTop extends React.Component {
  render() {
    return (
      <nav className="menu-top">
        <ul className="ui list celled horizontal link">
          <li className="item">
            <Link to="/sign-in">Login</Link>
          </li>
          <li className="item">
            <Link to="/sign-up">Cadastrar</Link>
          </li>
        </ul>
      </nav>
    );
  }
}

export default MenuTop;
