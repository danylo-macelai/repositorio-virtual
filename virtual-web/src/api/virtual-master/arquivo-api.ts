/*
 * Description: API para arquivos
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Mon Aug 05 2019 12:58:16 PM
 * version $
 */

import { virtualMasterConfig } from './virtual-master-api-config';

class ArquivoApi {
  static async gravar(file: File) {
    var formData = new FormData();
    formData.append('file', file);
    const response: any = await virtualMasterConfig.post('arquivos', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response;
  }

  static async consulta(nome: string, searchTab: string) {
    const response: any = await virtualMasterConfig.get('arquivos', {
      params: { nome: nome, search_tab: searchTab },
    });
    return response.data;
  }

  static async leitura(id: number) {
    return null;
  }

  static async gravacao(file: File, authorization: string, token: string) {
    return null;
  }

  static async exclusao(id: number, authorization: string, token: string) {
    return null;
  }
}

export default ArquivoApi;
