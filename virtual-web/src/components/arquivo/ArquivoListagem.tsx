/*
 * Description:
 * Project: virtual-web
 *
 * author: macelai
 * date: 8 de out de 2019
 * version $
 */

import * as React from 'react';
import './scss/ArquivoListagem.scss';

import { Arquivo } from '../../common/models/index';
import { ArquivoListagemItem, ArquivoFiltro } from './index';

interface Props {
  arquivos: Arquivo[];
}

interface State {}

class ArquivoListagem extends React.Component<Props, State> {
  arquivoListagemItems(arquivos: Arquivo[]) {
    return arquivos.map((arquivo: any) => (
      <ArquivoListagemItem key={arquivo.id} arquivo={arquivo} />
    ));
  }

  render() {
    return (
      <div>
        <div
          id="content"
          className="sixteen wide mobile thirteen wide tablet thirteen wide computer right floated column"
        >
          <div className="ui padded grid ">
            <div className="center aligned row">
              {this.arquivoListagemItems(this.props.arquivos)}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default ArquivoListagem;
