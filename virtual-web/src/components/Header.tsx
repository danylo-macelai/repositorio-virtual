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

import './Header.scss';
import { Link } from 'react-router-dom';

class Header extends React.Component {
  render() {
    return (
      <header className="main-header">
        <div className="ui container">
          <div className="main-header-wrap">
            <div className="logo">
              <Link to="/">
                <img src="https://via.placeholder.com/150x38?text=+" alt=" " />
              </Link>
            </div>

            <Menu />
          </div>
        </div>
      </header>
    );
  }
}

export default Header;
