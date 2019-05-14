const usuarioTypes = `

    type Usuario {
        id: ID!
        nome: String!
        email: String!
    }
`;

const usuarioQueries = `
    carregarTodos: [ Usuario! ]!
`;

export {
    usuarioTypes,
    usuarioQueries
}