<h1 align="center">
  <img src="docs/virteca-ver@3x.png" height="230">
</h1>

O VirTeca é um serviço de hospedagem de arquivos baseado em nuvem, que permite os usuários a armazenar ou consultar livros, filmes, softwares, músicas e outros arquivos no formato digital, através de dispositivos clientes Web ou Mobile. Os usuários também poderão se registrar através destas interfaces.

O método de armazenamento permite que um arquivo seja dividido em blocos usando uma convenção própria que inviabilizara a sua reconstrução por outros agentes leigos. Após a divisão, estes blocos são enviados a um ou mais servidores para a gravação em disco. Além disso, será mantida uma política de redundância para garantir disponibilidade dos dados em caso de falha em alguma instância de servidor.

Quando o dispositivo cliente solicitar o download do arquivo, o servidor recupera os blocos espalhados nas instâncias dos outros servidores para reconstruir arquivo que será devolvido.

Para armazenar arquivos o usuário deverá estar registrado e autenticado na aplicação.

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
                ???
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

Neste contexto surgiu o VirTeca como projeto, que pretendesse consolidar ao máximo a utilização destas práticas e recursos tecnológicos não só realizado a persistência de dados, más também a manipulação de binários e a troca de informações entre sistemas. Para isso dividimos nos módulos virtual-common, virtual-master, virtual-slave, virtual-access, virtual-web e virtual-mobile responsáveis por tarefas específicas.

# Funcionalidade

<img src="docs/rv_mod.png" align="right"  height="450">

O VirTeca é sistema de gestão distribuída de arquivos que oferece um serviço para a hospedagem de livros, filmes, softwares, músicas e outros arquivos no formato digital. Qualquer pessoa com uma conta poderá interagir com leitura, gravação e exclusão dos arquivos que estão divididos em blocos de dados distribuídos entre os vários nós de armazenamento.

_O arquivo remessa.txt está divido em dois blocos ac92.rvf e b508.rvf. Cada bloco foi armazenado em locais diferentes o ac92.rvf está no Slave 1 e sua cópia no Slave 4, já o bloco b508.rvf no Slave 2 e 3._

Quando o cliente solicitar a leitura do arquivo o Master deverá recuperar os blocos nas instâncias dos Slaves e reconstruir arquivo que será devolvido ao cliente.

O sistema está dividido nos módulos virtual-common, virtual-master, virtual-slave, virtual-access, virtual-web e virtual-mobile responsáveis por tarefas específicas permitindo assim a utilização de diversas linguagens e frameworks.

# Crítica

A velocidade lenta da conexão de banda larga pode dificultar a transmissão de arquivos grandes, como filmes para dispositivos móveis e outros dispositivos remotos.

Somente o servidor Máster detêm o conhecimento lógico para reconstruir o arquivo.

Enquanto os blocos estão armazenados no Slave, esse conteúdo ainda está acessível aos usuários do VirTeca.
