Como Contribuir
=========================================
Vamos utilizar a abordagem de Branch para as contribuições. Será necessário acessar a perspectiva do Git em `Window > Perspective > Open Perspective > Other..`. No assistente de perspectiva, selecione `Git` depois clique no botão `Open` para abrir a perspectiva.

     _________________
    | Git Repositories \_____________________________________________________________________________
    |                                                                                                |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |   |  > [] repositorio-virtual [master] - C:\wo... | |                                      |   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   |                                               | +--------------------------------------+   |
    |   |                                               | +--------------------------------------+   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    +------------------------------------------------------------------------------------------------+

Clique no botão direito em `> [] repositorio-virtual [master] - C:\wo...`, no menu de contexto `Switch To > New Branch...`. No assistente de criação de branch, informe o `Branch name`, lembre-se que o nome deverá representar o objetivo desta contribuição depois clique em `Finish`.

     ________________________________________________________________________________________________
    | Create Branch                                                                                  |
    |                                                                                                |
    |      ...                                                                                       |
    |      Branch name: 123_correcao_na_integracao                                                   |
    |      ...                                                                                       |
    |      [x] Check out new branch                                                                  |
    +------------------------------------------------------------------------------------------------+
    |                                                                   [ Finish ]   [  ...  ]       |
    +------------------------------------------------------------------------------------------------+

Envie sua branch para o repositório na nuvem clicando em `> [] repositorio-virtual [master] - C:\wo...`, no menu de contexto `Push Branch ‘123_correcao_na_integracao’...`. No assistente de push branch, nao informe nada apenas clique em `Next` em seguida no `Finish`.

1. Implemente / corrija o código conforme necessário;
2. Comente as suas contribuições;
3. Siga o estilo de código do projeto;
4. Adicione / altere a documentação conforme necessário;
5. Commit o código com uma mensagem sucinta.

> Antes de abrir o pull request, caso você tenha mais de um commit realize `squash` para manter apenas um commit com a mensagem que corresponde o trabalho realizado, em seguida faça um `rebase` para manter o seu commit no topo da pilha do histórico.

Agora acesse o `github` e abra um `New pull request` e aguarde que suas contribuições sejam revisadas e aprovadas pelos outros contribuintes para serem mesclada. 
