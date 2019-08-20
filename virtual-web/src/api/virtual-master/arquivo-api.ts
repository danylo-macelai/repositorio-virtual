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
}

export default ArquivoApi;
