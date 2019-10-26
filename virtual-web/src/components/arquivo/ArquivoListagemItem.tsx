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

interface Props {
  readonly arquivo: Arquivo;
}

interface State {}

export class ArquivoListagemItem extends React.Component<Props, State> {
  render() {
    const arquivo = this.props.arquivo;
    const arquivoLink = `/arquivo/${arquivo.id}`;

    return (
      <div className="eight wide mobile four wide tablet four wide computer column">
        <a href="#" onClick={() => alert('Consulta não implementada!')}>
          <h4>
            <div>
              <img
                alt={arquivo.searchTab.tipo}
                className="ui centered image"
                src={require('../../assets/images/' +
                  arquivo.searchTab +
                  '.png')}
              />
            </div>
          </h4>
          <span className="nome">{arquivo.nome}</span>
          <span className="info">{arquivo.token.nome}</span>
          {tamanhoArquivo(arquivo.tamanho)}
        </a>
      </div>
    );
  }
}

export default ArquivoListagemItem;
