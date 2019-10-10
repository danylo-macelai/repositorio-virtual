/*
 * Description: Campo de pesquisa do projeto
 * Project: virtual-web
 *
 * author: macelai
 * date: 8 de out de 2019
 * version $
 */

import React from 'react';
import { withAlert, AlertManager } from 'react-alert';
import './Search.scss';

import ArquivoApi from '../../api/virtual-master/arquivo-api';
import { tratarExcecao, tamanhoArquivo } from '../../utils/utils';

interface SearchProps {
  alert: AlertManager;
}

interface SearchState {
  filtro: Filtro;
  texto: string;
  lista: any[];
}

interface Filtro {
  tipo: string;
  icone: string;
  label: string;
}

const FiltroImpl = {
  ALL: { tipo: '', icone: '', label: 'Todos' },
  IMAGE: { tipo: 'IMAGE', icone: 'image', label: 'Imagem' },
  AUDIO: { tipo: 'AUDIO', icone: 'audio', label: 'Áudio' },
  VIDEO: { tipo: 'VIDEO', icone: 'video', label: 'Vídeo' },
};

class Search extends React.Component<SearchProps, SearchState> {
  componentWillMount() {
    this.setState({
      filtro: FiltroImpl.ALL,
      texto: '',
      lista: [],
    });
  }

  enter(event: any) {
    switch (event.keyCode) {
      case 13:
        this.gerarListagem(this.state.filtro);
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

  async gerarListagem(filtro: Filtro) {
    const { texto } = this.state;
    let lista = [];
    try {
      lista = await ArquivoApi.consulta(texto, filtro.tipo);
    } catch (error) {
      tratarExcecao(error, this.props.alert);
    } finally {
      this.setState({
        lista,
        filtro,
      });
    }
  }

  FiltroItemElement(filtro: Filtro) {
    return (
      <button
        type="button"
        className={`item ${this.selecionarFiltro(filtro)}`}
        onClick={() => this.gerarListagem(filtro)}
      >
        <i className={`ui icon file ${filtro.icone} outline`} />
        {filtro.label}
      </button>
    );
  }

  ListaItemElement(params: any) {
    return params.lista.map((item: any) => (
      <div className="eight wide mobile four wide tablet four wide computer column">
        <a href="#" onClick={() => alert('Consulta não implementada!')}>
          <h4>
            <div>
              <img
                alt={item.searchTab}
                className="ui centered image"
                src={require('../../assets/images/' + item.searchTab + '.png')}
              />
            </div>
          </h4>
          <span className="nome">{item.nome}</span>
          <span className="info">{item.token.nome}</span>
          {tamanhoArquivo(item.tamanho)}
        </a>
      </div>
    ));
  }

  render() {
    const { lista, filtro } = this.state;

    return (
      <div className="search ui vertical segment">
        <div className="ui secondary pointing menu">
          {this.FiltroItemElement(FiltroImpl.ALL)}
          {this.FiltroItemElement(FiltroImpl.AUDIO)}
          {this.FiltroItemElement(FiltroImpl.IMAGE)}
          {this.FiltroItemElement(FiltroImpl.VIDEO)}
        </div>
        <div className="ui icon input fluid large">
          <i
            className="search icon"
            onClick={() => this.gerarListagem(filtro)}
          ></i>
          <input
            type="text"
            placeholder="Digite o que deseja pesquisar em nosso site."
            onKeyDown={e => this.enter(e)}
            onChange={e => this.digitando(e)}
          />
        </div>
        <div>
          <div
            id="content"
            className="sixteen wide mobile thirteen wide tablet thirteen wide computer right floated column"
          >
            <div className="ui padded grid ">
              <div className="center aligned row">
                <this.ListaItemElement lista={lista} />
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default withAlert()(Search);
