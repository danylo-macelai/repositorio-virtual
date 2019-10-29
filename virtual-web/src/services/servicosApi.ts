/*
 * Description:
 * Project: virtual-web
 *
 * author: macelai
 * date: 29 de out de 2019
 * version $
 */

import axios from 'axios';
import * as Constants from '../common/constants';
import { tratarExcecao } from '../common/utils';
import { Arquivo } from '../common/models';

export const consultaArquivo = (
  nome: string,
  filtro: string
): Promise<Arquivo[]> => {
  return axios
    .get(Constants.ARQUIVO_RESOURCE, {
      params: { nome: nome, search_tab: filtro },
    })
    .then(response => Promise.resolve(response.data))
    .catch(tratarExcecao);
};

export const leituraArquivo = (id: number): Promise<Arquivo> => {
  return axios
    .get(Constants.ARQUIVO_RESOURCE, {
      baseURL: `${id}`,
    })
    .then(response => Promise.resolve(response.data))
    .catch(tratarExcecao);
};

export const gravacaoArquivo = (file: File) => {};

export const exclusaoArquivo = (id: number) => {};

export const consultaConfiguracao = (id: number) => {};

export const alteracaoConfiguracao = (id: number) => {};

/*static async gravar(file: File) {
  var formData = new FormData();
  formData.append('file', file);
  const response: any = await virtualMasterConfig.post('arquivos', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return response;
}*/

/*
  static async login(email: string, senha: string) {
    const mutation = `mutation {
      criarToken(email: "${email}", senha: "${senha}") {
        token,
      }
    }`;

    const response: any = await virtualAccessConfig.post('', {
      query: mutation,
    });

    if (response.data.data.criarToken) {
      return response.data.data.criarToken.token;
    }

    return '';
  }

*/
