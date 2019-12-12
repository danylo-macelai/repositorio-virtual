<h1 align="center">
  <img src="docs/virteca-ver@3x.png" height="230">
</h1>

O VirTeca é um serviço de hospedagem de arquivos baseado em nuvem, que permite os usuários a armazenar ou consultar livros, filmes, softwares, músicas e outros arquivos no formato digital, através de dispositivos clientes Web ou Mobile. Os usuários também poderão se registrar através destas interfaces.

O método de gravação permite que um arquivo seja dividido em blocos usando uma convenção própria que inviabilizara a sua reconstrução por outros agentes leigos. Após a divisão, estes blocos são enviados a um ou mais servidores para a gravação em disco. Além disso, será mantida uma política de redundância para garantir disponibilidade dos dados em caso de falha em alguma instância de servidor.

Quando o dispositivo cliente solicitar a leitura do arquivo, o servidor recupera os blocos espalhados nas instâncias dos outros servidores para reconstruir arquivo que será devolvido.

Para armazenar os arquivos o usuário deverá estar registrado e autenticado na aplicação.

<table border="0" >
    <tbody>
        <tr>
            <td colspan="2" align="center">
                <a href="docs/File:virteca-hor@3x.png">
                <img alt="VirTeca Logo.png" src="docs/virteca-hor@3x.png" width="220" height="73">
                </a>
            </td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Categoria</th>
            <td align="left" valign="middle" width="650px">Serviço de hospedagem de arquivos</td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Cadastro</th>
            <td align="left" valign="middle" width="650px">Opcional</td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Início</th>
            <td align="left" valign="middle" width="650px">Outubro 2018</td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Colaboradores</th>
            <td align="left" valign="middle" width="650px">
                <a href="https://github.com/brenicio"><b>Brenicio</b></a>, <a href="https://github.com/danylo-macelai"><b>Danylo</b></a> e <a href="https://github.com/orenatoaraujo"><b>Renato</b></a>
            </td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Recursos</th>
            <td align="left" valign="middle" width="650px">
                Spring-Framework Boot Cloud, Hibernate, Eureka, Swagger, Msf4j, NodeJs, Grapqhl, Jwt, Sequelize, React, Android e etc...
            </td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Contribuições</th>
            <td align="left" valign="middle" width="650px">
                Veja <a href="../master/docs/CONTRIBUTING.md#como-contribuir"><b>aqui</b></a> os termos para as contribuições.
            </td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Configuração</th>
            <td align="left" valign="middle" width="650px">
                Veja <a href="../master/docs/ide.md#configuração-do-ambiente"><b>aqui</b></a> as configurações para desenvolvimento.
            </td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Licença</th>
            <td align="left" valign="middle" width="650px">
                ?????
            </td>
        </tr>
        <tr>
            <th scope="row" align="right" width="260px">Website</th>
            <td align="left" valign="middle" width="650px">virteca.com.br (Offline)</td>
        </tr>
    </tbody>

</table>

# História

No decorrer da especialização em [Desenvolvimento Web e Mobile - Full Stack](http://inf.ufg.br/espweb-mob) da [Universidade Federal de Goiás](https://www.ufg.br/) foi discutido alguns recursos tecnológicos conforme [ementa](https://docs.google.com/document/d/1QoNsiIL_b1FXZBbqHYIVFwHHTRKQwP1WCW3r_jXUSAw/edit).

Neste contexto surgiu o VirTeca como projeto, que pretendesse consolidar ao máximo a utilização destas práticas e recursos tecnológicos não só realizado a persistência de dados, más também a manipulação de binários e a troca de informações entre sistemas. Para isso dividimos nos módulos [virtual-common](/virtual-common/README.md), [virtual-master](/virtual-master/README.md), [virtual-slave](/virtual-slave/README.md), [virtual-access](/virtual-access/README.md), [virtual-web](/virtual-web/README.md) e [virtual-mobile](/virtual-mobile/README.md) responsáveis por tarefas específicas.

# Funcionalidade

Os módulos **Web** e **Mobile** referem-se ao lado do cliente, serão as ferramentas utilizadas que os usuários utilizarão para acessarem as funcionalidades dos demais módulos que possuem responsabilidades, papeis e ambientes diferentes.

O **Master** atendera as solicitações de gravação, leitura e as configurações relacionadas aos arquivos, já o **Access** todas operações que envolverá os usuários e o controle de acesso a aplicação.

O **Eureka** oferece uma estrutura de dados que permite cada servidor **Slave** se ligar a uma _instanceId_, sempre que o **Master** informar uma _instanceId_ ele devolverá a sua respectiva _homePageUrl_.

O **Slave** quando inicializado se auto registrara no **Eureka** através da sua _instanceId_ para atender as solicitações do **Master** realizado a gravação, leitura, replicação ou até mesmo a exclusão dos blocos em disco.

<div align="center">
    <img src="docs/modulo.gif">
</div>

Para a gravação dos arquivos o usuário deve se autenticar no **Access** para receber um token que precisará ser enviando junto com arquivo ao **Master**. Mas antes de começar o processamento este token deverá ser validado no **Access**. O processamento realizara a divisão do arquivo em blocos de tamanho fixo conforme as configurações do **Master** e identificam-no de forma única para o armazenamento no diretório temporário do **Master**.

Uma tarefa será executada periodicamente para enviar estes blocos do diretório temporário do **Master** para os servidores **Slave** que estão registrados no **Eureka**, mantendo assim a _instanceId_ do servidor que o bloco foi enviado.

Após a gravação uma outra tarefa periodicamente será executada, desta vez para replicar os blocos gravados de acordo com a quantidade de réplicas definidas nas configurações do **Master**. Para replicar será necessário recuperar uma _instanceId_ no **Eureka** onde o tal bloco não existe e solicitar ao **Slave** detentor que envie uma cópia para a nova _instanceId_.

Uma vez realizado a replicação uma outra tarefa periodicamente será executada para liberar espaço do diretório temporário do **Master** removendo os arquivos que já foram totalmente processados.

<div align="center">
    <img src="docs/upload.gif">
</div>

> _O arquivo Demonstrativo Financeiro.pdf está divido em três blocos **1Z4A5Q.rvf**, **2X6S7W.rvf** e **3C8D9E.rvf**. Cada bloco foi armazenado em locais diferentes o **1Z4A5Q.rvf** está gravado no **localhost:APP-SLAVE:8080** e replicado no **localhost:APP-SLAVE:8083**, já o bloco **2X6S7W.rvf** no **localhost:APP-SLAVE:8081** e **localhost:APP-SLAVE:8080** e por último o **3C8D9E.rvf** no **localhost:APP-SLAVE:8082** e **localhost:APP-SLAVE:8081**._

Para leitura de arquivos o usuário não precisará estar autenticado, basta informar o arquivo que servidor **Master** buscará todas as _instanceId_ independentemente de serem ou não réplicas dos blocos.

O **Master** deverá recupera a _homePageUrl_ da _instanceId_ que está ativa no **Eureka** para realizar o download do bloco para o seu diretório temporário, repetindo assim esta atividade até obter todos os blocos que serão necessários para reconstrução do arquivo.

A reconstrução do arquivo respeitara a mesma ordem em que os blocos foram divididos, colocando assim cada bloco em seu devido lugar até gerar o arquivo final que será devolvido ao dispositivo cliente.

Uma vez finalizado este processo a mesma tarefa de limpeza citada no armazenamento ficará responsável de remover estes arquivos do diretório temporário.

<div align="center">
    <img src="docs/download.gif">
</div>

Assim como o envio a edição ou até mesmo a exclusão só será permitida pelo o autor que foi previamente cadastrado no access. O Administrador do sistema poderá gerenciar todo o acervo bem como os usuários ou até mesmo as configurações do master e/ou slave.

Além das tarefas de gravação, replicação e limpeza do diretório outras também deverão ser executadas para manter as instancias slave ativas, migração dos blocos e as informações dos volumes.

# Crítica

A velocidade lenta da conexão de banda larga pode dificultar a transmissão de arquivos grandes, como filmes para dispositivos móveis e outros dispositivos remotos.

Somente o servidor Máster detêm o conhecimento lógico para reconstruir o arquivo.

Enquanto os blocos estão armazenados no Slave, esse conteúdo ainda está acessível aos usuários do VirTeca.
