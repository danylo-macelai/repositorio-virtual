<h1 align="center">
  <img src="docs/rv_logo.png" width="400">
</h1>

<h3 align="center">
    Gestão Distribuída de Arquivos
</h3>

<p align="center">
  <strong>
    <a href="#virtual-common">Virtual Common</a> •
    <a href="#virtual-master">Virtual Master</a> •
    <a href="#virtual-slave">Virtual Slave</a> •
    <a href="#virtual-access">Virtual Access</a> •
    <a href="#virtual-web">Virtual Web</a> •
    <a href="#virtual-mobile">Virtual Mobile</a>
  </strong>
</p>

<img src="docs/rv_mod.png" align="right"  height="450">

O Repositório Virtual é sistema de gestão distribuída de arquivos que oferece um serviço para a hospedagem de livros, filmes, softwares, músicas e outros arquivos no formato digital. Qualquer pessoa com uma conta poderá interagir com leitura, gravação e exclusão dos arquivos que estão divididos em blocos de dados distribuídos entre os vários nós de armazenamento.

_O arquivo remessa.txt está divido em dois blocos ac92.rvf e b508.rvf. Cada bloco foi armazenado em locais diferentes o ac92.rvf está no Slave 1 e sua cópia no Slave 4, já o bloco b508.rvf no Slave 2 e 3._

Quando o cliente solicitar a leitura do arquivo o Master deverá recuperar os blocos nas instâncias dos Slaves e reconstruir arquivo que será devolvido ao cliente.

O sistema está dividido nos módulos virtual-common, virtual-master, virtual-slave, virtual-access, virtual-web e virtual-mobile responsáveis por tarefas específicas permitindo assim a utilização de diversas linguagens e frameworks.
 
## Virtual Common

* O Common permite que os recursos sejam compartilhados com os demais projetos, fornecendo uma base para eles que combinam as soluções hibernate e spring.

## Virtual Master

* O Master é um micros-serviço que interage diretamente com os clientes através da leitura, gravação e exclusão de arquivos. Os arquivos enviados serão divididos em blocos de tamanho fixo e armazenados no diretório temporário da aplicação, assim que alguma instância Slave estiver registrada no service discovery a tarefa de gravação de blocos será executada para que posteriormente seja realizada a replicação entre os nós Slave.

* Ao receber uma solicitação de leitura o Master recupera todos os blocos espalhados entre os diversos nós incluindo as réplicas que serão necessárias para a reconstrução do arquivo no diretório temporário da aplicação, após a entrega uma tarefa será responsável pela sua exclusão.

* Diariamente será executada uma tarefa que mantém uma estratégia para migração de blocos e balanceamento entre os nós Slaves.

## Virtual Slave

* O Slave é um micros-serviço que oferece uma interface de serviços para interagir exclusivamente com Master realizando a leitura, gravação e exclusão de blocos no disco. 

* Além de ser flexivelmente estendido, a cada inicialização ele se auto registra no service discovery permitindo assim que o Master sempre verifique a disponibilidade de suas instâncias. 

* Periodicamente uma tarefa será executada afim de manter acuracidade dos blocos armazenados.

## Virtual Access

...

## Virtual Web

...

## Virtual Mobile

...

## Contribuições

A contribuição é uma ótima maneira de aprender e dividir conhecimentos de novas tecnologias e seus ecossistemas. Se você deseja contribuir para o projeto e torná-lo melhor, sua ajuda é muito bem-vinda. Mas será necessário seguir alguns procedimentos. 

Para o desenvolvimento as configurações de Code Style são obrigatórias. Todo o código do Repositório Virtual deverá estar de acordo com este formato de estilo. 

Antes de enviar qualquer código para o repositório, o committer DEVE ter certeza que o código alterado adere ao formato canônico do Repositório Virtual. Assim, ao passar `Source > Cleanup` no repositório, se todo o código estiver em conformidade resultará em nenhuma alteração.

Você precisará verificar se as [(configurações)](../master/docs/ide.md#configuração-do-ambiente) das IDE's estão definidas corretamente. Além das configurações [(veja aqui)](../master/docs/CONTRIBUTING.md#como-contribuir) as recomendações para as contribuições.

## Colaboradores

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
| [<img src="https://avatars0.githubusercontent.com/u/6737144?s=460&v=4" width="100px;"/><br /><sub><b>brenicio</b></sub>](https://github.com/brenicio)<br /> | [<img src="https://avatars2.githubusercontent.com/u/8239569?s=460&v=4" width="100px;"/><br /><sub><b>danylo-macelai</b></sub>](https://github.com/danylo-macelai)<br /> | [<img src="https://avatars3.githubusercontent.com/u/1007389?s=400&v=4" width="100px;"/><br /><sub><b>Renato Araujo</b></sub>](https://github.com/orenatoaraujo)<br /> | 
| :---------------------------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
<!-- ALL-CONTRIBUTORS-LIST:END -->





