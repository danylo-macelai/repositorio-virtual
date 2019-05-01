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
    <a href="#virtual-access">Virtual Access</a>
  </strong>
</p>

<img src="docs/rv_mod.png" align="right"  height="450">

 O Repositório é um sistema de gerenciamento de arquivos que fornece uma interface de serviços para comunicação com os clientes permitindo a leitura, gravação e exclusão dos arquivos   que estão divididos em blocos de dados distribuídos entre os vários nós de armazenamento.

_O arquivo remessa.txt está divido em dois blocos ac92.rvf e b508.rvf. Cada bloco foi armazenado em locais diferentes o ac92.rvf está no Slave 1 e sua cópia no Slave 4, já o bloco b508.rvf no Slave 2 e 3._

Quando o cliente solicitar a leitura do arquivo o Master deverá recuperar os blocos nas instâncias dos Slaves e reconstruir arquivo que será devolvido ao cliente.

 
## Virtual Common

* O Common permite que os recursos sejam compartilhados com os demais projetos, fornecendo uma base para eles que combinam as soluções hibernate e spring.

## Virtual Master

* O Master é um micros-serviço que interage diretamente com os clientes através da leitura, gravação e exclusão de arquivos. Os arquivos enviados serão divididos em blocos de tamanho fixo e armazenados no diretório temporário da aplicação, assim que alguma instância Slave estiver registrada no service discovery a tarefa de gravação de blocos será executada para que posteriormente seja realizada a replicação entre os nós Slave.

* Ao receber uma solicitação de leitura o Master recupera todos os blocos espalhados entre os diversos nós incluindo as réplicas que serão necessárias para a reconstrução do arquivo no diretório temporário da aplicação, após a entrega uma tarefa será responsável pela sua exclusão.

* Diariamente será executada uma tarefa que mantem uma estratégia para migração de blocos e balanceamento entre os nós Slaves.

## Virtual Slave

* O Slave é um micros-serviço oferecendo uma interface de serviços para interagir exclusivamente com Master realizando a leitura, gravação e exclusão de blocos no disco. 

* Além de ser flexivelmente estendido, a cada inicialização ele se auto registra no service discovery permitindo assim que o Master sempre verifique a disponibilidade de suas instâncias. 

* Periodicamente uma tarefa será executada afim de manter acuracidade dos blocos armazenados.

## Virtual Access

## Desenvolvimento

As configurações de Code Style são obrigatórias. Todo o código do Repositório Virtual deverá estar de acordo com este formato de estilo. 

Antes de enviar qualquer código para o repositório, o committer DEVE ter certeza
que o código alterado adere ao formato canônico do Repositório Virtual. Assim, ao passar `Source > Cleanup` ou (`Alt + S + U`) no repositório, se todo o código estiver em conformidade resultará em nenhuma alteração.

Você precisará verificar se suas configurações [(veja aqui)](../master/docs/eclipse.md#configurar-o-ambiente-do-eclipse) estão definidas corretamente.
