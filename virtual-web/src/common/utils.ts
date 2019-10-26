/*
 * Description: Utils
 * Project: virtual-web
 *
 * author: macelai
 * date: Mon Out 10 2019 10:41:16 PM
 * version $
 */

import { AlertManager } from 'react-alert';

export const tratarExcecao = (error: any, manager: AlertManager) => {
  let message = '';
  if (error.response) {
    message = error.response.data;
  } else if (error.request) {
    message = error.request;
  } else {
    message = error.message;
  }
  manager.error(message, {
    timeout: 2000,
  });
};

export function tamanhoArquivo(x: number) {
  var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  if (x === 0) return '0 Byte';
  var i = Math.floor(Math.log(x) / Math.log(1024));
  return Math.round(x / Math.pow(1024, i)) + ' ' + sizes[i];
}
