/*
 * Description: Componente de Header do projeto
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Fri Jul 26 2019 9:42:22 PM
 * version $
 */

import React from 'react';

import Menu from './Menu';
import MenuTop from './MenuTop';
import { Link } from 'react-router-dom';

import './scss/Header.scss';

import logo from '../../assets/images/virteca-horizontal.png';

class Header extends React.Component {
  render() {
    return (
      <header className="main-header">
        <div className="ui container">
          <div className="main-header-wrap">
            <div className="logo">
              <Link to="/">
                <img src={logo} alt=" " />
              </Link>
            </div>

            <Menu />

            <MenuTop />
          </div>
        </div>
      </header>
    );
  }
}

export default Header;
