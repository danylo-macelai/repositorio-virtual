/*
 * Description:
 * Project: virtual-web
 *
 * author: macelai
 * date: 8 de out de 2019
 * version $
 */

import * as React from 'react';

import { Arquivo } from '../../common/models/index';
import { ArquivoListagemItem } from './index';

import './scss/ArquivoListagem.scss';

interface Props {
  arquivos: Arquivo[];
  message: string;
}

interface State {}

class ArquivoListagem extends React.Component<Props, State> {
  arquivoListagemItems(arquivos: Arquivo[]) {
    return (
      <div className="ui three cards">
        {arquivos.map((arquivo: any) => (
          <ArquivoListagemItem key={arquivo.id} arquivo={arquivo} />
        ))}
      </div>
    );
  }

  renderMessage() {
    if (!this.props.message) {
      return <></>;
    }

    return (
      <div className="ui padded segment center aligned search-message">
        {this.props.message}
      </div>
    );
  }

  render() {
    return (
      <>
        {this.props.arquivos && this.props.arquivos.length > 0
          ? this.arquivoListagemItems(this.props.arquivos)
          : this.renderMessage()}
      </>
    );
  }
}

export default ArquivoListagem;
