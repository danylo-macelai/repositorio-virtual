/*
 * Description: Utils
 * Project: virtual-web
 *
 * author: macelai
 * date: Mon Out 10 2019 10:41:16 PM
 * version $
 */

export function mensagem(erro: any): string {
  let message = '';
  if (erro.response) {
    message = erro.response.data;
  } else if (erro.request) {
    message = erro.request;
  } else {
    message = erro.message;
  }
  return message;
}

export const tratarExcecao = (erro: any) => {
  let message: string = mensagem(erro);
  console.log(message);
  return Promise.reject(new Error(message));
};

export function tamanhoArquivo(x: number) {
  var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
  if (x === 0) return '0 Byte';
  var i = Math.floor(Math.log(x) / Math.log(1024));
  return Math.round(x / Math.pow(1024, i)) + ' ' + sizes[i];
}
