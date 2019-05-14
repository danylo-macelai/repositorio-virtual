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

    input UsuarioAlteracao {
        id: ID!
        nome: String!
        email: String!
    }    
`;

const usuarioQueries = `    
    consulta(id: ID!): Usuario!
    listagem: [ Usuario! ]!
    quantidade: Int!
`;
    
const usuarioMutations = `
    inclusao(input: UsuarioInclusao!): Usuario
    alteracao(input: UsuarioAlteracao!): Usuario
    exclusao(id: ID!): Boolean
    exclusaoTodos: Boolean
`;

export {
    usuarioTypes,
    usuarioQueries,
    usuarioMutations
}