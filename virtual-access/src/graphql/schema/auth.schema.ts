/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 28 de jul de 2019
 * version $
 */

import { GraphQLObjectType, GraphQLString, GraphQLNonNull } from 'graphql';

import * as jwt from 'jsonwebtoken';

import { ENV } from '../../config/env.config';
import { UsuarioModel } from '../../models/UsuarioModel';
import { throwError, handleError } from '../../utils/utils';

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
      type: GraphQLString,
      args: {
        token: {
          type: GraphQLString,
        },
      },
      resolve(parent: any, args, context) {
        try {
          const dec: any = jwt.verify(args.token, ENV.JWT_SECRET as string);
          return dec.sub;
        } catch (err) {
          return null;
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
          attributes: [
            'id',
            'nome',
            'senha',
            'ativo',
            'bloqueado',
            'alterarSenha',
            'tentativasDeAutenticacao',
          ],
        })
          .then((usuario: UsuarioModel) => {
            throwError(
              !usuario,
              `Acesso negado: E-mail e/ou Senha incorretos.`
            );

            throwError(
              !usuario.get('ativo'),
              `Acesso negado: Usuário desativado.`
            );

            throwError(
              usuario.get('bloqueado'),
              `Acesso negado: Usuário bloqueado.`
            );

            let tentativas = usuario.get('tentativasDeAutenticacao');
            if (!usuario.verificarSenha(usuario.get('senha'), senha)) {
              UsuarioModel.update(
                {
                  tentativasDeAutenticacao: ++tentativas,
                  bloqueado: tentativas > 2 ? true : false,
                },
                {
                  where: { id: usuario.get('id') },
                }
              );

              throwError(
                true,
                tentativas == 3
                  ? `Usuário Bloqueado, entre em contato com o Administrador do Sistema.`
                  : `Senha inválida, você tem mais ${3 -
                      tentativas} tentativas antes de bloquear seu acesso.`
              );
            } else if (tentativas > 0) {
              UsuarioModel.update(
                {
                  tentativasDeAutenticacao: 0,
                },
                {
                  where: { id: usuario.get('id') },
                }
              );
            }

            const payload = { sub: usuario.get('id') };
            return {
              token: jwt.sign(payload, ENV.JWT_SECRET as string, {
                expiresIn: Number(ENV.JWT_EXPIRES_IN) * 60,
              }),
            };
          })
          .catch(handleError);
      },
    },
  },
});

export { authTypes, authQueries, authMutations };
