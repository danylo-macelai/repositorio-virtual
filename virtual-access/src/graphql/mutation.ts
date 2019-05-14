import { usuarioMutations } from './resources/usuario/usuario.schema';

const Mutation = `
    type Mutation {
        ${usuarioMutations}
    }
`;

export {
    Mutation
}