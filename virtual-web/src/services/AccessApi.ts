/*
 * Description: API para comunicacao com o virtual-access
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Oct 30 2019 7:26:50 PM
 * version $
 */

import axios from 'axios';

const virtualAccessConfig = axios.create({
  baseURL: 'http://localhost:3000/access',
});

const DEFAULT_MESSAGE: string =
  'Ocorreu um problema. Tente novamente mais tarde.';

class AccessApi {
  /**
   * Valida o email e a senha do usuario para a criacao do token
   *
   * @param email
   * @param senha
   */
  static async login(email: string, senha: string) {
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
  }

  /**
   * Pega o usuario pelo token
   *
   * @param token
   */
  static async getUserByToken(token: string) {
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
  }

  /**
   * Valida a existencia do token
   *
   * @param token
   */
  static async validaToken(token: string) {
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
  }
}

export default AccessApi;
