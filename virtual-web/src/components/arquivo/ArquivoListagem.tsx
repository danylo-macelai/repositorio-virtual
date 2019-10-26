/*
 * Description:
 * Project: virtual-web
 *
 * author: macelai
 * date: 8 de out de 2019
 * version $
 */

import * as React from 'react';
import { withAlert, AlertManager } from 'react-alert';
import './scss/ArquivoListagem.scss';

import { mensagem } from '../../common/utils';
import { Arquivo, Filtro } from '../../common/models/index';
import { ArquivoListagemItem, ArquivoFiltro } from './index';
import { consultaArquivos } from '../../services/servicosApi';

interface Props {
  alert: AlertManager;
}

interface State {
  filtro: Filtro;
  texto: string;
  arquivos: Arquivo[];
}

class ArquivoListagem extends React.Component<Props, State> {
  componentWillMount() {
    this.setState({
      filtro: Filtro.ALL,
      texto: '',
      arquivos: [],
    });
  }

  enter(event: any) {
    switch (event.keyCode) {
      case 13:
        this.consulta(this.state.filtro);
    }
  }

  digitando(event: any) {
    const { target } = event;
    this.setState({
      texto: target.value,
    });
  }

  selecionarFiltro(filtro: Filtro): string {
    return this.state.filtro === filtro ? 'active' : '';
  }

  consulta(filtro: Filtro) {
    const { texto } = this.state;
    consultaArquivos(texto, filtro.tipo)
      .then((arquivos: Arquivo[]) => {
        this.setState({
          arquivos,
          filtro,
        });
      })
      .catch(erro => {
        let message: string = mensagem(erro);
        this.props.alert.error(message, {
          timeout: 2000,
        });
      });
  }

  arquivoFiltro() {
    return (
      <div className="ui secondary pointing menu">
        {this.arquivoFiltroItem(Filtro.ALL)}
        {this.arquivoFiltroItem(Filtro.AUDIO)}
        {this.arquivoFiltroItem(Filtro.IMAGE)}
        {this.arquivoFiltroItem(Filtro.VIDEO)}
      </div>
    );
  }

  arquivoFiltroItem(filtro: Filtro) {
    return (
      <ArquivoFiltro
        filtro={filtro}
        selecionarFiltro={filtro => this.selecionarFiltro(filtro)}
        consulta={filtro => this.consulta(filtro)}
      />
    );
  }

  arquivoBusca(filtro: Filtro) {
    return (
      <div className="ui icon input fluid large">
        <i className="search icon" onClick={() => this.consulta(filtro)}></i>
        <input
          type="text"
          placeholder="Digite o que deseja pesquisar em nosso site."
          onKeyDown={e => this.enter(e)}
          onChange={e => this.digitando(e)}
        />
      </div>
    );
  }

  arquivoListagem(arquivos: Arquivo[]) {
    return (
      <div>
        <div
          id="content"
          className="sixteen wide mobile thirteen wide tablet thirteen wide computer right floated column"
        >
          <div className="ui padded grid ">
            <div className="center aligned row">
              {this.arquivoListagemItems(arquivos)}
            </div>
          </div>
        </div>
      </div>
    );
  }

  arquivoListagemItems(arquivos: Arquivo[]) {
    return arquivos.map((arquivo: any) => (
      <ArquivoListagemItem key={arquivo.id} arquivo={arquivo} />
    ));
  }

  render() {
    const { arquivos, filtro } = this.state;
    return (
      <div className="search ui vertical segment">
        {this.arquivoFiltro()}
        {this.arquivoBusca(filtro)}
        {this.arquivoListagem(arquivos)}
      </div>
    );
  }
}

export default withAlert()(ArquivoListagem);
