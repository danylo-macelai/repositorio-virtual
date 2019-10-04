# virtual-access

- O Access é um micros-serviço que oferece proteção contra a leitura, gravação e exclusão de arquivos na troca de informações entre os micros-serviços.

- Todo o usuário deverá criar um conta no Access com as informações pessoais, para manter a conta segura.

- Será utilizado um mecanismo de token para permitir a autorização entre os micros-serviços. Quando algum micro-serviço receber solicitaçao de acessso, ele deverá consultar o Access para garantir que o token está assinado.

- Uma vez que o token está assinado o Access não deverá chamar o banco de dados para recuperar as informações armazenadas.

- Se um micro-servico enviar um token ao Access e for rejeitado a operação do micro-serviço deverá ser abortada.

## Configuração

Crie uma cópia do arquivo `../virtual-access/config-access.env` no mesmo local com o nome `.env`. Abra os aquivos e altere os parâmetros conforme a necessidade do seu ambiente.

## Scripts

Comandos que podem ser executados dentro do projeto:

### `npm run gulp` e depois `npm run dev`

Para executar o projeto no modo de desenvolvimento.<br>
Vá até a URL [http://localhost:3000/access](http://localhost:3000/access) para ver o projeto no navegador.

A página é atualizada automaticamente ao fazer alterações no projeto.
