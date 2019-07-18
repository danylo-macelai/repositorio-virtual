# Como Contribuir

Vamos utilizar a abordagem de Branch para as contribuições. Será necessário acessar via **Dos**/**Git Bash** o diretório do repositório `D:\workspaces\GITHUB\repositorio-virtual`

1 - Digite o comando `status` se resultado for **On branch master** siga para o passo `3`.

    D:\workspaces\GITHUB\repositorio-virtual>git status
    ...

2 – Será necessário fazer um `switch` para a branch **_master_**, para através do comando `checkout`.

    D:\workspaces\GITHUB\repositorio-virtual>git checkout master
    ...

3 – Faça um `fetch` para baixar as atualizações do repositório remoto.

    D:\workspaces\GITHUB\repositorio-virtual>git fetch origin
    ...

4 – Reset o seu ambiente local de acordo com o remoto

    D:\workspaces\GITHUB\repositorio-virtual>git reset --hard origin/master
    ...

5 – Crie a sua `branch`, lembre-se que o nome deverá representar o objetivo desta contribuição.

    Exemplo:
    004_ajustar_documentacao
      |    |
      |    +------> Use o título da issues
      +------> Use o número da issues

    D:\workspaces\GITHUB\repositorio-virtual>git checkout -b 004_ajustar_documentacao
    ...

6 – Envie sua `branch` para o repositório na nuvem através do comando.

    D:\workspaces\GITHUB\repositorio-virtual>git push origin 004_ajustar_documentacao
    ...

1. Implemente / corrija o código conforme necessário;
2. Comente as suas contribuições;
3. Siga o estilo de código do projeto;
4. Adicione / altere a documentação conforme necessário;
5. Commit o código com uma mensagem sucinta.

> Antes de abrir o pull request, caso você tenha mais de um commit realize `squash` para manter apenas um commit com a mensagem que corresponde o trabalho realizado, em seguida faça um `rebase` para manter o seu commit no topo da pilha do histórico.

Agora acesse o `github` e abra um `New pull request` e aguarde que suas contribuições sejam revisadas e aprovadas pelos outros contribuintes para serem mesclada.
