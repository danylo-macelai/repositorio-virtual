import { virtualAccessConfig } from './virtual-access-api-config';

/*
 * Description: API para comunicacao com o virtual-access
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Sat Sep 21 2019 4:23:35 PM
 * version $
 */

class AccessApi {
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
}

export default AccessApi;
