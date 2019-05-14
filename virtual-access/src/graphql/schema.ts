import { makeExecutableSchema } from 'graphql-tools';
import { merge } from 'lodash';

import { Query } from './query';

import { usuarioTypes } from './resources/usuario/usuario.schema';

import { usuarioResolvers } from './resources/usuario/usuario.resolvers';

const resolvers = merge(
    usuarioResolvers
);

const SchemaDefinition = `
    type Schema {
        query: Query
    }
`;

export default makeExecutableSchema({
    typeDefs: [
        SchemaDefinition,
        Query,
        usuarioTypes
    ],
    resolvers
});