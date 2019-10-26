/*
 * Description: Campo de pesquisa do projeto
 * Project: virtual-web
 *
 * author: macelai
 * date: 28 de out de 2019
 * version $
 */

export interface Token {
  id: string;
  nome: string;
  email: string;
  perfilType: string;
}

export interface Filtro {
  tipo: string;
  icone: string;
  label: string;
}

export const Filtro = {
  ALL: { tipo: '', icone: '', label: 'Todos' },
  IMAGE: { tipo: 'IMAGE', icone: 'image', label: 'Imagem' },
  AUDIO: { tipo: 'AUDIO', icone: 'audio', label: 'Áudio' },
  VIDEO: { tipo: 'VIDEO', icone: 'video', label: 'Vídeo' },
};
