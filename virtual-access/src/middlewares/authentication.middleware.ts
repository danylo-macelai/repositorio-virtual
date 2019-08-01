/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 27 de jul de 2019
 * version $
 */

import * as jwt from 'jsonwebtoken';
import { RequestHandler, Request, Response, NextFunction } from 'express';

import { ENV } from '../config/env.config';

import { UsuarioModel } from '../models/UsuarioModel';
import PerfilType from '../enums/PerfilType';
import { handleError } from './../utils/utils';

export const Authentication = (): RequestHandler => {
  return (req: Request, res: Response, next: NextFunction): void => {
    let token: string = '';
    if (req.headers.authorization) {
      token = req.headers.authorization;
    } else if (req.query && req.query.token) {
      token = req.query.token;
    }

    if (!token) {
      return next();
    }

    jwt.verify(token, ENV.JWT_SECRET as string, (err, decoded: any) => {
      if (err) {
        return next();
      }

      UsuarioModel.findByPk(decoded.sub, {
        attributes: [
          'id',
          'nome',
          'email',
          'ativo',
          'bloqueado',
          'alterarSenha',
          'perfilType',
        ],
      })
        .then((usuario: UsuarioModel) => {
          if (usuario) {
            req.auth = {
              id: usuario.get('id'),
              nome: usuario.get('nome'),
              email: usuario.get('email'),
              ativo: usuario.get('ativo'),
              bloqueado: usuario.get('bloqueado'),
              alterarSenha: usuario.get('alterarSenha'),
              perfilType: usuario.get('perfilType') as PerfilType,
            };
          }
          return next();
        })
        .catch(handleError);
    });
  };
};
