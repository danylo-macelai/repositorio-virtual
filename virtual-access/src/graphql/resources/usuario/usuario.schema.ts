const usuarioTypes = `

    type Usuario {
        id: ID!
        nome: String!
        email: String!
    }

    input UsuarioInclusao {
        nome: String!
        email: String!
    }
`;

const usuarioQueries = `
    carregarTodos: [ Usuario! ]!
`;

const usuarioMutations = `
    incluir(input: UsuarioInclusao!): Usuario
`;

export {
    usuarioTypes,
    usuarioQueries,
    usuarioMutations
}