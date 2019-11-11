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
import User from '../common/models/user';

const DEFAULT_MESSAGE: string =
  'Ops! Ocorreu um problema. Tente novamente mais tarde.';

const virtualAccessConfig = axios.create({
  baseURL: Constants.ACCESS_RESOURCE,
});

const virtualMasterConfig = axios.create({
  baseURL: Constants.ARQUIVO_RESOURCE,
});

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

export const gravacaoArquivo = async (file: File, token: string) => {
  var formData = new FormData();
  formData.append('file', file);

  const response: any = await virtualMasterConfig.post('', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      Authorization: token,
    },
  });

  return response;
};

export const exclusaoArquivo = (id: number) => {};

export const consultaConfiguracao = (id: number) => {};

export const alteracaoConfiguracao = (id: number) => {};

/**
 * Access methods
 */

/**
 * Valida o email e a senha do usuario para a criacao do token
 *
 * @param email
 * @param senha
 */
export const login = async (email: string, senha: string) => {
  // Query para o Access
  const mutation = `mutation {
    criarToken(email: "${email}", senha: "${senha}") {
      token,
    }
  }`;

  let response: any = {};

  try {
    response = await virtualAccessConfig.post('', {
      query: mutation,
    });
  } catch (e) {
    throw DEFAULT_MESSAGE;
  }

  // Valida se ocorreu algum erro
  if (response.data.errors && response.data.errors.length) {
    throw response.data.errors[0].message;
  }

  // Returna o token
  if (response.data.data.criarToken) {
    return response.data.data.criarToken.token;
  }

  // Caso nao seja retornado nenhum valor eh enviado uma mensagem padrao
  throw DEFAULT_MESSAGE;
};

/**
 * Pega o usuario pelo token
 *
 * @param token
 */
export const getUserByToken = async (token: string) => {
  // Query para o Access
  const query = `query {
    validarToken(token: "${token}") {
      id,
      nome,
      email,
      perfilType
    }
  }`;

  let response: any = {};

  try {
    response = await virtualAccessConfig.post('', {
      query,
    });
  } catch (e) {
    return {};
  }

  // Valida se existe um token valido e se retorna o usuario
  if (response.data.data.validarToken) {
    const user: any = response.data.data.validarToken;
    return {
      id: user.id,
      nome: user.nome,
      email: user.email,
      perfilType: user.perfilType,
      token,
    };
  }

  return {};
};

/**
 * Valida a existencia do token
 *
 * @param token
 */
export const validaToken = async (token: string) => {
  // Query para o Access
  const query = `query {
    validarToken(token: "${token}") {
      id
    }
  }`;

  let response: any = {};

  try {
    response = await virtualAccessConfig.post('', {
      query,
    });
  } catch (e) {
    return false;
  }

  return !!response.data.data.validarToken;
};

export const cadastrar = async (usuario: User) => {
  const { nome, email, senha } = usuario;

  const mutation = `
    mutation {
      inclusao(input: {nome: "${nome}", email: "${email}", senha: "${senha}"}) {
        id,
      }
    }`;

  let response: any = {};

  try {
    response = await virtualAccessConfig.post('', {
      query: mutation,
    });
  } catch (e) {
    throw DEFAULT_MESSAGE;
  }

  // Valida se ocorreu algum erro
  if (response.data.errors && response.data.errors.length) {
    throw response.data.errors[0].message;
  }

  // Returna o token
  if (response.data.data.inclusao) {
    return response.data.data.inclusao.id;
  }

  // Caso nao seja retornado nenhum valor eh enviado uma mensagem padrao
  throw DEFAULT_MESSAGE;
};

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
