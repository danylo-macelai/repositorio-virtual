/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 28 de jul de 2019
 * version $
 */

import {
  GraphQLObjectType,
  GraphQLString,
  GraphQLNonNull,
  GraphQLBoolean,
} from 'graphql';

import * as jwt from 'jsonwebtoken';

import { ENV } from '../../config/env.config';
import { UsuarioModel } from '../../models/UsuarioModel';
import { throwError } from '../../utils/utils';

const authTypes = {
  Auth: new GraphQLObjectType({
    name: 'Auth',
    fields: () => ({
      token: {
        type: new GraphQLNonNull(GraphQLString),
      },
    }),
  }),
};

const { Auth } = authTypes;

const authQueries = new GraphQLObjectType({
  name: 'authQueries',
  fields: {
    validarToken: {
      type: GraphQLBoolean,
      args: {
        token: {
          type: GraphQLString,
        },
      },
      resolve(parent: any, args, context) {
        try {
          jwt.verify(args.token, ENV.JWT_SECRET as string);
          return true;
        } catch (err) {
          return false;
        }
      },
    },
  },
});

const authMutations = new GraphQLObjectType({
  name: 'authMutations',
  fields: {
    criarToken: {
      type: Auth,
      args: {
        email: {
          type: new GraphQLNonNull(GraphQLString),
        },
        senha: {
          type: new GraphQLNonNull(GraphQLString),
        },
      },
      resolve: async (parent: any, { email, senha }) => {
        return UsuarioModel.findOne({
          where: { email },
          attributes: ['id', 'senha', 'ativo', 'bloqueado'],
        }).then((usuario: UsuarioModel) => {
          throwError(
            !usuario || !usuario.verificarSenha(usuario.get('senha'), senha),
            `Acesso negado: E-mail e/ou Senha incorretos.`
          );
          throwError(!usuario.ativo, `Acesso negado: Usuário desativado.`);
          throwError(usuario.bloqueado, `Acesso negado: Usuário bloqueado.`);
          const payload = { sub: usuario.get('id') };
          return {
            token: jwt.sign(payload, ENV.JWT_SECRET as string, {
              expiresIn: Number(ENV.JWT_EXPIRES_IN) * 60,
            }),
          };
        });
      },
    },
  },
});

export { authTypes, authQueries, authMutations };
