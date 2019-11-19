/*
 * Description: Campo de pesquisa do projeto
 * Project: virtual-web
 *
 * author: macelai
 * date: 28 de out de 2019
 * version $
 */

import * as React from 'react';
import { Filtro } from '../../common/models/model';

interface Props {
  filtro: Filtro;
  selecionarFiltro: (filtro: Filtro) => string;
  consulta: (filtro: Filtro) => void;
}

interface State {}

export class ArquivoFiltro extends React.Component<Props, State> {
  selecionarFiltro(filtro: Filtro): string {
    return this.props.selecionarFiltro(filtro);
  }

  consulta(filtro: Filtro) {
    this.props.consulta(filtro);
  }

  render() {
    const filtro = this.props.filtro;

    return (
      <button
        type="button"
        className={`item ${this.selecionarFiltro(filtro)}`}
        onClick={() => this.consulta(filtro)}
      >
        <i className={`ui icon file ${filtro.icone} outline`} />
        {filtro.label}
      </button>
    );
  }
}

export default ArquivoFiltro;
