/*
 * Description: Pagina de Sobre
 * Project: virtual-web
 *
 * author: Renato Araujo Jesus
 * date: Sat Dec 14 2019 11:59:06 AM
 * version $
 */

import React from 'react';

class SobrePage extends React.Component {
  render() {
    return (
      <div className="ui grid centered">
        <div className="column eight wide">
          <h2>Sobre</h2>
          <p>
            O VirTeca é um serviço de hospedagem de arquivos baseado em nuvem,
            que permite aos usuários armazenar ou consultar livros, filmes,
            softwares, músicas e outros arquivos no formato digital, por meio de
            dispositivos clientes Web ou Mobile.
          </p>
          <h3>Autores</h3>
          <ul className="ui middle aligned divided horizontal list">
            <li className="item">
              <img
                alt="Brenicio"
                className="ui avatar image"
                src="https://avatars0.githubusercontent.com/u/6737144?s=460&v=4"
              />
              <div className="content">
                <a
                  className="header"
                  href="https://github.com/brenicio/"
                  target="blank"
                >
                  Brenicio
                </a>
              </div>
            </li>
            <li className="item">
              <img
                alt="Danylo"
                className="ui avatar image"
                src="https://avatars2.githubusercontent.com/u/8239569?s=460&v=4"
              />
              <div className="content">
                <a
                  className="header"
                  href="https://github.com/danylo-macelai/"
                  target="blank"
                >
                  Danylo
                </a>
              </div>
            </li>
            <li className="item">
              <img
                alt="Renato"
                className="ui avatar image"
                src="https://avatars2.githubusercontent.com/u/1007389?s=460&v=4"
              />
              <div className="content">
                <a
                  className="header"
                  href="https://github.com/orenatoaraujo/"
                  target="blank"
                >
                  Renato
                </a>
              </div>
            </li>
          </ul>
        </div>
      </div>
    );
  }
}

export default SobrePage;
