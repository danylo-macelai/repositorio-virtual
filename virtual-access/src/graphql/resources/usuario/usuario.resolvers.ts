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

        carregarTodos: () => {
            return listUsuario;
        }

    }

};