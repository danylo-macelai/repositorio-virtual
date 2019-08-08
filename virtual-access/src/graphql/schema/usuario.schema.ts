/**
 * Description:</b> FIXME: Document this type <br>
 * Project:</b> virtual-access <br>
 *
 * author: macelai
 * date: 22 de jul de 2019
 * version $
 */

import {
  GraphQLObjectType,
  GraphQLString,
  GraphQLID,
  GraphQLList,
  GraphQLNonNull,
  GraphQLInputObjectType,
  GraphQLBoolean,
  GraphQLInt,
  GraphQLEnumType,
} from 'graphql';
import { sequelize } from '../../config/sequelize';
import { Transaction } from 'sequelize';
import isEmail from 'validator/lib/isEmail';

import { handleError, throwError } from '../../utils/utils';
import { UsuarioModel } from '../../models/UsuarioModel';
import PerfilType from '../../enums/PerfilType';
import {
  isAutenticado,
  isAdminstrador,
  isUsuarioOuAdminstrador,
} from '../../utils/utils';

const perfilTypes = new GraphQLEnumType({
  name: 'perfilEnum',
  values: {
    Adminstrador: {
      value: PerfilType.Adminstrador,
    },
    Padrao: {
      value: PerfilType.Padrao,
    },
  },
});

const usuarioTypes = {
  Usuario: new GraphQLObjectType({
    name: 'Usuario',
    fields: () => ({
      id: {
        type: new GraphQLNonNull(GraphQLID),
      },
      nome: {
        type: new GraphQLNonNull(GraphQLString),
      },
      email: {
        type: new GraphQLNonNull(GraphQLString),
      },
      perfilType: {
        type: new GraphQLNonNull(perfilTypes),
      },
      ativo: {
        type: new GraphQLNonNull(GraphQLBoolean),
      },
      bloqueado: {
        type: new GraphQLNonNull(GraphQLBoolean),
      },
      alterarSenha: {
        type: new GraphQLNonNull(GraphQLBoolean),
      },
      tentativasDeAutenticacao: {
        type: new GraphQLNonNull(GraphQLInt),
      },
      ultimaTrocaSenha: {
        type: new GraphQLNonNull(GraphQLString),
      },
      criacao: {
        type: new GraphQLNonNull(GraphQLString),
      },
      alteracao: {
        type: new GraphQLNonNull(GraphQLString),
      },
    }),
  }),

  UsuarioInclusao: new GraphQLInputObjectType({
    name: 'UsuarioInclusao',
    fields: () => ({
      nome: {
        type: new GraphQLNonNull(GraphQLString),
      },
      email: {
        type: new GraphQLNonNull(GraphQLString),
      },
      senha: {
        type: new GraphQLNonNull(GraphQLString),
      },
    }),
  }),

  UsuarioAlteracao: new GraphQLInputObjectType({
    name: 'UsuarioAlteracao',
    fields: () => ({
      id: {
        type: new GraphQLNonNull(GraphQLID),
      },
      nome: {
        type: GraphQLString,
      },
      email: {
        type: GraphQLString,
      },
      // Ações que deverão ser realizadas pelo administrador
      perfilType: {
        type: perfilTypes,
      },
      ativo: {
        type: GraphQLBoolean,
      },
    }),
  }),
};

const { Usuario, UsuarioInclusao, UsuarioAlteracao } = usuarioTypes;

const usuarioQueries = new GraphQLObjectType({
  name: 'usuarioQueries',
  fields: {
    consulta: {
      type: Usuario,
      args: {
        id: {
          type: GraphQLID,
        },
      },
      resolve(parent: any, args, context) {
        return UsuarioModel.findByPk(args.id)
          .then((usuario: UsuarioModel) => {
            throwError(
              !usuario,
              `Não foi possível carregar o usuário ${
                args.id
              }. Ele não foi encontrado no sistema.`
            );

            throwError(
              !isUsuarioOuAdminstrador(usuario, context.auth),
              `Você não possui permissão para a consulta. Por favor, entrar em contato com o Administrador do Sistema.`
            );

            return usuario;
          })
          .catch(handleError);
      },
    },

    listagem: {
      type: new GraphQLList(Usuario),
      resolve(parent: any, args, context) {
        throwError(
          !isAdminstrador(context.auth),
          `Você não possui permissão para a listagem. Por favor, entrar em contato com o Administrador do Sistema.`
        );

        return UsuarioModel.findAll().catch(handleError);
      },
    },

    quantidade: {
      type: GraphQLInt,
      resolve(parent: any, context) {
        throwError(
          !isAdminstrador(context.auth),
          `Você não possui permissão para a quantidade. Por favor, entrar em contato com o Administrador do Sistema.`
        );
        return UsuarioModel.count().catch(handleError);
      },
    },
  },
});

const usuarioMutations = new GraphQLObjectType({
  name: 'usuarioMutations',
  fields: {
    inclusao: {
      type: Usuario,
      args: {
        input: {
          type: new GraphQLNonNull(UsuarioInclusao),
        },
      },
      resolve: async (parent: any, { input }) => {
        return sequelize
          .transaction((t: Transaction) => {
            throwError(
              input.email && !isEmail(input.email),
              `Endereço ${input.email} de e-mail é inválido.`
            );
            return UsuarioModel.create(input, { transaction: t });
          })
          .catch(handleError);
      },
    },

    alteracao: {
      type: GraphQLInt,
      args: {
        input: {
          type: new GraphQLNonNull(UsuarioAlteracao),
        },
      },
      resolve: async (parent: any, { input }, context) => {
        return sequelize
          .transaction((t: Transaction) => {
            return UsuarioModel.findByPk(input.id).then(
              (usuario: UsuarioModel) => {
                throwError(
                  !usuario,
                  `Não foi possível carregar o usuário ${
                    input.id
                  }. Ele não foi encontrado no sistema.`
                );

                throwError(
                  !isUsuarioOuAdminstrador(usuario, context.auth),
                  `Você não possui permissão para a alteração. Por favor, entrar em contato com o Administrador do Sistema.`
                );

                throwError(
                  input.perfilType === 'ADMINSTRADOR' &&
                    !isAdminstrador(context.auth),
                  `Você não tem permissão para alterar o perfil. Por favor, entrar em contato com o Administrador do Sistema.`
                );

                throwError(
                  input.ativo !== undefined && !isAdminstrador(context.auth),
                  `Você não tem permissão para ativar ou inativar usuários. Por favor, entrar em contato com o Administrador do Sistema.`
                );

                return UsuarioModel.update(input, {
                  where: { id: usuario.id },
                  transaction: t,
                }).then(rowsUpdated => rowsUpdated);
              }
            );
          })
          .catch(handleError);
      },
    },

    exclusao: {
      type: GraphQLBoolean,
      args: {
        id: {
          type: new GraphQLNonNull(GraphQLID),
        },
      },
      resolve: async (parent: any, args, context) => {
        return sequelize
          .transaction((t: Transaction) => {
            return UsuarioModel.findByPk(args.id).then(
              (usuario: UsuarioModel) => {
                throwError(
                  !usuario,
                  `Não foi possível carregar o usuário ${
                    args.id
                  }. Ele não foi encontrado no sistema.`
                );

                throwError(
                  !isAdminstrador(context.auth),
                  `Você não possui permissão para a exclusão. Por favor, entrar em contato com o Administrador do Sistema.`
                );

                return usuario
                  .destroy({ transaction: t })
                  .then(result => result);
              }
            );
          })
          .catch(handleError);
      },
    },

    desbloqueio: {
      type: GraphQLInt,
      args: {
        id: {
          type: new GraphQLNonNull(GraphQLID),
        },
      },
      resolve: async (parent: any, args, context) => {
        return sequelize
          .transaction((t: Transaction) => {
            throwError(
              !isAdminstrador(context.auth),
              `Você não tem permissão para desbloquear usuários. Por favor, entrar em contato com o Administrador do Sistema.`
            );

            return UsuarioModel.findByPk(args.id).then(
              (usuario: UsuarioModel) => {
                throwError(
                  !usuario,
                  `Não foi possível carregar o usuário ${
                    args.id
                  }. Ele não foi encontrado no sistema.`
                );

                throwError(
                  !usuario.get('bloqueado'),
                  `O usuário ${usuario.get('nome')}, não está bloqueado.`
                );

                return UsuarioModel.update(
                  {
                    bloqueado: false,
                    tentativasDeAutenticacao: 0,
                    alterarSenha: true,
                  },
                  {
                    where: { id: usuario.get('id') },
                    transaction: t,
                  }
                ).then(rowsUpdated => rowsUpdated);
              }
            );
          })
          .catch(handleError);
      },
    },

    alteracaoSenha: {
      type: GraphQLInt,
      args: {
        password: {
          type: new GraphQLNonNull(GraphQLString),
        },
      },
      resolve: async (parent: any, args, context) => {
        return sequelize
          .transaction((t: Transaction) => {
            isAutenticado(context.auth);

            return UsuarioModel.findByPk(context.auth.id).then(
              (usuario: UsuarioModel) => {
                throwError(
                  !usuario,
                  `Não foi possível carregar o usuário ${
                    args.id
                  }. Ele não foi encontrado no sistema.`
                );
                return usuario
                  .update(
                    {
                      senha: args.password,
                      alterarSenha: false,
                      ultimaTrocaSenha: Date.now(),
                    },
                    { transaction: t }
                  )
                  .then((up: UsuarioModel) => !!up);
              }
            );
          })
          .catch(handleError);
      },
    },
  },
});

export { usuarioTypes, usuarioQueries, usuarioMutations };
