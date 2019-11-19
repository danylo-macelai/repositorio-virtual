/*
 * Description: Campo de pesquisa do projeto
 * Project: virtual-web
 *
 * author: macelai
 * date: 28 de out de 2019
 * version $
 */

import * as React from 'react';

import { Arquivo } from '../../common/models/index';
import { tamanhoArquivo } from '../../common/utils';
import { visualizarArquivoURL } from '../../services/servicosApi';

interface Props {
  readonly arquivo: Arquivo;
}

interface State {}

export class ArquivoListagemItem extends React.Component<Props, State> {
  render() {
    const arquivo = this.props.arquivo;
    const arquivoLink = visualizarArquivoURL(arquivo.id);

    return (
      <div className="card" title={arquivo.nome}>
        <div className="image">
          <img
            alt={arquivo.searchTab.tipo}
            className="ui centered image"
            src={require('../../assets/images/' + arquivo.searchTab + '.png')}
          />
        </div>
        <div className="content">
          <span className="header center aligned">{arquivo.nome}</span>
          <div className="meta center aligned">
            <div>{arquivo.token.nome}</div>
            <div>{tamanhoArquivo(arquivo.tamanho)}</div>
          </div>
        </div>
        <div className="extra content center aligned">
          <a href={arquivoLink} target="blank">
            Obter
          </a>
        </div>
      </div>
    );
  }
}

export default ArquivoListagemItem;
