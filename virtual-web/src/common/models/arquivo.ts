/*
 * Description: Campo de pesquisa do projeto
 * Project: virtual-web
 *
 * author: macelai
 * date: 28 de out de 2019
 * version $
 */

import { Filtro, Token } from './model';

export class Arquivo {
  public id!: number;
  public tamanho!: number;
  public pecas!: number;
  public nome!: string;
  public mimeType!: string;
  public searchTab!: Filtro;
  public token!: Token;
}
