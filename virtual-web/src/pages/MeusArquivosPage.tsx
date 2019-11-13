/*
 * Description: Pagina para apresentacao dos arquivos do usuario
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Mon Nov 11 2019 11:19:43 PM
 * version $
 */

import React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';

import {
  arquivosUsuario,
  leituraArquivo,
  exclusaoArquivo,
  visualizarArquivoURL,
} from '../services/servicosApi';
import { VirtualWebState } from '../reducers';

import Pagination from '../components/layout/Pagination';
import { tamanhoArquivo } from '../common/utils';

const mapStateToProps = (state: VirtualWebState) => ({
  user: state.user,
});

type MeusArquivosProps = ReturnType<typeof mapStateToProps>;

interface MeusArquivosState {
  arquivos: [];
  page: number;
  totalPages: number;
  showEmpty: boolean;
}

class MeusArquivosPage extends React.Component<
  MeusArquivosProps,
  MeusArquivosState
> {
  constructor(props: MeusArquivosProps) {
    super(props);

    this.state = {
      arquivos: [],
      page: 0,
      totalPages: 0,
      showEmpty: false,
    };
  }

  componentDidMount() {
    this.findArquivos(0);
  }

  async findArquivos(page: number) {
    if (!this.props.user || !this.props.user.token) {
      return;
    }

    this.setState({ page });

    const response = await arquivosUsuario(this.props.user.token, page);

    this.setState({
      arquivos: response.content,
      totalPages: response.totalPages,
      showEmpty: !response.content.length,
    });
  }

  viewFile(arquivo: any) {
    window.open(visualizarArquivoURL(arquivo.id));
  }

  removerFile(arquivo: any) {
    if (window.confirm('Tem certeza que deseja remover?')) {
      exclusaoArquivo(arquivo.id, this.props.user.token).then(data => {
        this.findArquivos(0);
      });
    }
  }

  mimeType(mimeType: string) {
    if (!mimeType) {
      return '';
    }

    return mimeType.split(/\//)[1];
  }

  renderEmptyItem() {
    if (this.state.showEmpty) {
      return (
        <li>
          <div className="ui padded segment center aligned">
            <p>Nenhum arquivo encontrato!</p>
          </div>
        </li>
      );
    }
  }

  render() {
    if (!this.props.user || !this.props.user.token) {
      return <Redirect to="/login" />;
    }

    return (
      <div className="my-files-container ui grid centered">
        <div className="column ten wide">
          <h2>Meus Arquivos</h2>
          <ul className="ui middle aligned divided list">
            {this.state.arquivos.map((arquivo: any, i: number) => (
              <li className="item" key={arquivo.id}>
                <div className="right floated">
                  <button
                    type="button"
                    className="ui button mini primary"
                    onClick={() => this.viewFile(arquivo)}
                  >
                    Obter
                  </button>
                  <button
                    type="button"
                    className="ui button mini red"
                    onClick={() => this.removerFile(arquivo)}
                  >
                    Remover
                  </button>
                </div>
                <i
                  className={`large middle aligned icon file outline ${this.mimeType(
                    arquivo.mimeType
                  )}`}
                ></i>
                <div className="content">
                  <span className="header">{arquivo.nome}</span>
                  <div className="description">
                    {tamanhoArquivo(arquivo.tamanho)}
                  </div>
                </div>
              </li>
            ))}
            {this.renderEmptyItem()}
          </ul>
          <div className="ui hidden divider"></div>
          <Pagination
            onSelectPage={(page: number) => this.findArquivos(page)}
            page={this.state.page}
            totalPages={this.state.totalPages}
          ></Pagination>
        </div>
      </div>
    );
  }
}

export default connect(mapStateToProps)(MeusArquivosPage);
