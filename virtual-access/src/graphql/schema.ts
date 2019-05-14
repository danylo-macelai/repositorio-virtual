import { makeExecutableSchema } from 'graphql-tools';
import { merge } from 'lodash';

import { Query } from './query';
import { Mutation } from './mutation';

import { usuarioTypes } from './resources/usuario/usuario.schema';

import { usuarioResolvers } from './resources/usuario/usuario.resolvers';

const resolvers = merge(
    usuarioResolvers
);

const SchemaDefinition = `
    type Schema {
        query: Query
        mutation: Mutation
    }
`;

export default makeExecutableSchema({
    typeDefs: [
        SchemaDefinition,
        Query,
        Mutation,
        usuarioTypes
    ],
    resolvers
});