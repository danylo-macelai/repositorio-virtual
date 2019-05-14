const listUsuario: any[] = [
    {
        id: 1,
        nome: 'Danylo Macelai',
        email: 'danylomacelai@gmail.com'
    },
    {
        id: 2,
        nome: 'Yarn',
        email: 'yarn@mail.com'
    },
];

export const usuarioResolvers = {
    Query: {
        consulta: (parent, {id}) => {
            id = parseInt(id);
            return listUsuario.find( u => {
                return u.id === id;
            });
        },
        
        listagem: () => {
            return listUsuario;
        },

        quantidade: () => {
            return listUsuario.length;
        }
    },

    Mutation: {
        inclusao: (parent, {input}) => {
            const u = Object.assign({id: listUsuario.length + 1}, input)
            listUsuario.push(u);
            return u;
        },

        alteracao: (parent, {input}) => {
            const id = parseInt(input.id);
            for( var i = 0; i < listUsuario.length; i++){ 
                if (listUsuario[i].id === id) {
                    listUsuario[i].nome = input.nome;
                    listUsuario[i].email = input.email;
                    break;
                }
            }
            return input;
        },

        exclusao: (parent, {id}) => {
            id = parseInt(id);
            const tm = listUsuario.length;
            for( var i = 0; i < listUsuario.length; i++){
                if ( listUsuario[i].id === id) {
                    listUsuario.splice(i, 1);
                }
            }
            return tm > listUsuario.length;
        },

        exclusaoTodos: () => {
            while(listUsuario.length > 0) {
                listUsuario.pop();
            }
            return listUsuario.length === 0;
        }
    }
};