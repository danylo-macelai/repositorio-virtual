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
import InfiniteScroll from 'react-infinite-scroll-component';
import { Filtro, Arquivo } from '../common/models';
import { consultaArquivo } from '../services/servicosApi';
import { mensagem } from '../common/utils';
import ArquivoListagem from '../components/arquivo/ArquivoListagem';
import ArquivoFiltro from '../components/arquivo/ArquivoFiltro';

interface Props {
  alert: AlertManager;
}

interface State {
  filtro: Filtro;
  texto: string;
  arquivos: Arquivo[];
  message: string;
  page: number;
  totalPages: number;
}

class ArquivoPage extends React.Component<Props, State> {
  componentWillMount() {
    this.setState({
      filtro: Filtro.ALL,
      texto: '',
      arquivos: [],
      message: '',
      page: 0,
      totalPages: 0,
    });
  }

  componentDidMount() {
    this.consultaInicial(Filtro.ALL);
  }

  enter(event: any) {
    switch (event.keyCode) {
      case 13:
        this.consultaInicial(this.state.filtro);
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

  consulta(filtro: Filtro, page: number) {
    this.setState({
      page,
    });

    const { texto } = this.state;
    consultaArquivo(texto, filtro.tipo, page)
      .then((result: any) => {
        this.setState({
          arquivos: [...this.state.arquivos, ...result.content],
          message: '',
          totalPages: result.totalPages,
        });
      })
      .catch(erro => {
        let message: string = mensagem(erro);

        this.setState({
          arquivos: [],
          message,
          totalPages: 0,
        });
      });
  }

  consultaInicial(filtro: Filtro) {
    this.setState({
      filtro,
      arquivos: [],
      totalPages: 0,
    });
    this.consulta(filtro, 0);
  }

  consultaInfinita(filtro: Filtro) {
    this.consulta(filtro, this.state.page + 1);
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
        consulta={filtro => this.consultaInicial(filtro)}
      />
    );
  }

  arquivoBusca(filtro: Filtro) {
    return (
      <div className="ui icon input fluid large">
        <i
          className="search icon"
          onClick={() => this.consultaInicial(filtro)}
        ></i>
        <input
          type="text"
          placeholder="Digite o que deseja pesquisar em nosso site."
          onKeyDown={e => this.enter(e)}
          onChange={e => this.digitando(e)}
        />
      </div>
    );
  }

  render() {
    const { arquivos, filtro, message } = this.state;
    return (
      <div className="search ui vertical segment">
        {this.arquivoFiltro()}
        {this.arquivoBusca(filtro)}
        <InfiniteScroll
          dataLength={arquivos.length}
          next={() => this.consultaInfinita(filtro)}
          hasMore={this.state.totalPages > this.state.page + 1}
          loader={<div className="loader">Carregando...</div>}
        >
          <ArquivoListagem arquivos={arquivos} message={message} />
        </InfiniteScroll>
      </div>
    );
  }
}

export default withAlert()(ArquivoPage);
