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
      bloqueado: {
        type: new GraphQLNonNull(GraphQLBoolean),
      },
      ativo: {
        type: new GraphQLNonNull(GraphQLBoolean),
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
      perfilType: {
        type: new GraphQLNonNull(perfilTypes),
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
      perfilType: {
        type: perfilTypes,
      },
      bloqueado: {
        type: GraphQLBoolean,
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
      resolve(parent: any, input) {
        return UsuarioModel.findByPk(input.id)
          .then((usuario: UsuarioModel) => {
            throwError(
              !usuario,
              `Usuário com o id ${input.id} não foi localizado!`
            );
            return usuario;
          })
          .catch(handleError);
      },
    },

    listagem: {
      type: new GraphQLList(Usuario),
      resolve(parent: any) {
        return UsuarioModel.findAll().catch(handleError);
      },
    },

    quantidade: {
      type: GraphQLInt,
      resolve(parent: any) {
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
              `E-mail ${input.email} não está em formato válido!`
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
      resolve: async (parent: any, { input }) => {
        return sequelize
          .transaction((t: Transaction) => {
            return UsuarioModel.findByPk(input.id).then(
              (usuario: UsuarioModel) => {
                throwError(
                  !usuario,
                  `Usuário com o id ${input.id} não foi localizado!`
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
      resolve: async (parent: any, args) => {
        return sequelize
          .transaction((t: Transaction) => {
            return UsuarioModel.findByPk(args.id).then(
              (usuario: UsuarioModel) => {
                throwError(
                  !usuario,
                  `Usuário com o id ${args.id} não foi localizado!`
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
  },
});

export { usuarioTypes, usuarioQueries, usuarioMutations };
