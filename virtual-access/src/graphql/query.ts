import { usuarioQueries } from './resources/usuario/usuario.schema';

const Query = `
    type Query {
        ${usuarioQueries}
    }
`;

export {
    Query
}