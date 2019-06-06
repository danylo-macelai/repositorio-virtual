Workspace
=========================================

Partindo o pressuposto que o ambiente de desenvolvimento será em `D:\workspaces\GITHUB\` abra a ide Eclipse apontando para este local.

---
##### ECLIPSE (Code Style)

As instruções a seguir descrevem como configurar o eclipse para usar arquivos e convenções que obedecem aos padrões de codificação do Repositório Virtual.

>Todas as opções referentes ao `Window > Preferences > Java > CodeStyle` podem ser configuradas rapidamente importando apenas o arquivo [ [rv_preferences.epf](../docs/rv_preferences.epf) ]. 

O arquivo de preferências pode ser importado para *Eclipse*, permitindo que você configure automaticamente o `Formatting`, `Import Organization`, `Clean Up` e `Code Templates`. Para importar preferências do sistema acesse `File > Import... `. No assistente de Importação, selecione `General > Prefereces` e clique em `Next >`. Clique em `Browse...` e localize o arquivo em seguida selecione as preferências que você quer importar na lista depois clique em `Finish`. 
        
    |   ...                                                                                          |
    |   From preference file: D:\...\rv_preferences.epf                              [ Browse... ]   |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Java Code Style Preferences                                                      |   |
    |   |   [x] Java Compiler  Prefereces                                                        |   |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+
    |                                               [  ...  ]   [  ...  ]   [ Finish ]   [  ...  ]   |
    +------------------------------------------------------------------------------------------------+
    |   ...                                                                                          |

Algumas configurações *NÃO* são cobertas pela importação e deverão ser realizadas manualmente. Para configurar o *encoding* e a *quebras de linha* acesse `Window > Preferences`. No assistente de preferencias, selecione `General > Workspace`. Na seção denominada `Text file encoding` marque a opção `Other` combinada com `UTF-8`, já para a `New text file line delimiter`, `Other` e `Unix`, depois clique em `Apply`.

    |                                              ...                                               |
    |                                                                                                |
    |  +-- Text file encoding ---------------------+ +- New text file line delimiter ------------+   |
    |  |  ( ) Default (cp1252                      | | ( ) Default (Windows)                     |   |
    |  |  (.) Other   [UTF-8]                      | | (.) Other   [Unix]                        |   |
    |  +-------------------------------------------+  +------------------------------------------+   |
    |                                                                                                |
    |                                                                        [  ...  ]   [ Apply ]   |
    +------------------------------------------------------------------------------------------------+
    |   ...                                                                                          |
    
Vamos precisar substituir as *tabs* por *spaces* dos arquivos de texto, para configurar acesse `Window > Preferences`. No assistente de preferencias, selecione `General > Editors > Text Editors` e marque a ckeckbox `Insert spaces for tabs`, depois clique em `Apply`.
    
    |                                              ...                                               |
    |  [x] Insert spaces for tabs                                                                    |
    |                                              ...                                               |
    |                                                                        [  ...  ]   [ Apply ]   |
    +------------------------------------------------------------------------------------------------+
    |   ...                                                                                          |
	
Para finalizar vamos definir um conjunto de ações que serão executadas a cada salvamento do editor *Java*, para configurar acesse `Window > Preferences`. No assistente de preferencias, selecione `Java > Editor > Save Actions`, para cada *tab* selecione as seguintes opções:

    |                                              ...                                               |
    |   [x] Perform the selected actions on save                                                     |
    |      [x] Format source code (Format all lines)                                                 |
    |         (.) Format all lines                                                                   |
    |         ( ) Format edited lines                                                                |
    |                                                                                                |
    |      [x] Organize Imports                                                                      |
    |                                                                                                |
    |      [x] Additional Actions                                                                    |
    |                                                                                                |
    |                                              ...                                               |
    |                                                                        [  ...  ]   [ Apply ]   |
    +------------------------------------------------------------------------------------------------+

Depois clique em `Configure...`

     _________________
    | Code Organizing \______________________________________________________________________________
    |                                                                                                |
    |   +-- Formatter ----------------------------------+ +--------------------------------------+   |
    |   |  [x] Remove Trailing whitespace               | |                                      |   |
    |   |     (.) All Lines ( ) Ignore empty lines      | |                                      |   |
    |   |  [x] Correct Indentation                      | |                                      |   |
    |   +-----------------------------------------------+ |                ...                   |   |
    |                                                     |                                      |   |
    |   +-- Members ------------------------------------+ |                                      |   |
    |   |  [ ] Sort members                             | |                                      |   |
    |   |  ...                                          | |                                      |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |                                              ...                                               |
    +------------------------------------------------------------------------------------------------+

                    ____________
     ______________| Code Style \____________________________________________________________________
    |                                                                                                |
    |   +-- Control statements -------------------------+ +--------------------------------------+   |
    |   |  [x] Use blocks in if/while/for/do statements | |                                      |   |
    |   |     (.) Always                                | |                                      |   |
    |   |     ...                                       | |                                      |   |
    |   +-----------------------------------------------+ |                                      |   |
    |                     ...                             |                ...                   |   |
    |   +-- Variable delarations------------------------+ |                                      |   |
    |   |  [x] Use final modifier where possible        | |                                      |   |
    |   |     [x] Private Fields [ ] Paramter [ ] Local.| |                                      |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |                                              ...                                               |
    +------------------------------------------------------------------------------------------------+

                                _________________
     __________________________| Member Accesses \___________________________________________________
    |                                              ...                                               |
    +------------------------------------------------------------------------------------------------+

                                                 ______________
     ___________________________________________| Missing Code \_____________________________________
    |                                                                                                |
    |   +-- Annotations --------------------------------+ +--------------------------------------+   |
    |   |  [x] Add missing Annotations                  | |                                      |   |
    |   |     [x] @Override                             | |                                      |   |
    |   |          [x] Implementations of interface me  | |                                      |   |
    |   |     [x] @Deprecated                           | |                                      |   |
    |   +-----------------------------------------------+ |                                      |   |
    |                                                     |                                      |   |
    |                                                     +--------------------------------------+   |
    |                                              ...                                               |
    +------------------------------------------------------------------------------------------------+

                                                            _________________
     ______________________________________________________| Unnecessary Code \______________________
    |                                                                                                |
    |   +-- Unused code --------------------------------+ +--------------------------------------+   |
    |   |  [x] Remove unused imports                    | |                                      |   |
    |   |  ...                                          | |                                      |   |
    |   +-----------------------------------------------+ |                                      |   |
    |                                                     |                ...                   |   |
    |   +-- Unnecessary code ---------------------------+ |                                      |   |
    |   |  [x] Remove unnecssary casts                  | |                                      |   |
    |   |  ...                                          | |                                      |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |                                              ...                                               |
    +------------------------------------------------------------------------------------------------+
    |                                                                       [   OK   ]   [  ...  ]   |
    +------------------------------------------------------------------------------------------------+

---
##### ECLIPSE (SSH)

O Eclipse possui um cliente de *SSH* para acessar o servidor remoto *Git*. O processo de configuração dos pares de chaves *SSH* é bastante simples, para configurar acesse `Window > Preferences`. No assistente de preferencias, selecione `General > Network Connections > SSH2`, preencha as *tabs* com as seguintes opções:

     __________
    | General  \_____________________________________________________________________________________
    |                                                                                                |
    |   SSH2 home....: C:\workspaces\TOOLS\.ssh                                                      |
    |   Private keys.: id_dsa,id_rsa                                                                 |
    |                                                                                                |
    |                                                                        [  ...  ]   [ Apply ]   |
    +------------------------------------------------------------------------------------------------+

                 ________________
     ___________| Key Management \___________________________________________________________________
    |                                                                                                |
    |   [        ...      ]   [ Generate RSA Key... ]   [        ...         ]                       |
    |                                                                                                |
    |   You can paste this public key into the remote authorized_key file:                           |
    |                                                                                                |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   ssh-rsa AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJ ... XXXXYYYZZZ== RSA-1024           |   |
    |   +----------------------------------------------------------------------------------------+   |
    |                                                                                                |
    |                                                                      [ Save Private Key... ]   |
    +------------------------------------------------------------------------------------------------+

---
##### GITHUB (SSH)
               
Acesse o menu `Settings` localizado no avatar, depois no menu lateral `Personal settings` selecione a opção `SSH and GPG keys` e clique no botão `New SSH key`

`Title:` pc_dev

`Key:` ssh-rsa AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJ ... XXXXYYYZZZ== RSA-1024

_Confirme a operação informando o password._

---
##### ECLIPSE (Clone)

EGit é a integração do *Git* para o IDE do *Eclipse*, ele já vem instalada, para configurar abra a perspectiva em `Window > Perspective > Open Perspective > Other..`. No assistente de perspectiva, selecione `Git` depois clique no botão `Open` para abrir a perspectiva.

     _________________
    | Git Repositories \_____________________________________________________________________________
    |                                                                                                |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   |    Select one of the following to add         | |                                      |   |
    |   |    a repository to this view                  | |                                      |   |
    |   |    ...                                        | |                                      |   |
    |   |    Clone a Git repository                     | +--------------------------------------+   |
    |   |    ...                                        | +--------------------------------------+   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   |                                               | |                                      |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    +------------------------------------------------------------------------------------------------+

Ao clicar em `Clone a Git repository` o assistente de clonagem será exibido, preencha com as seguintes informações, depois clique em `Next >`.

    |   +-- Location ----------------------------------------------------------------------------+   |
    |   |  URI: git@github.com:danylo-macelai/repositorio-virtual.git        [ Local File... ]   |   |
    |   |  Host: github.com                                                                      |   |
    |   |  Repository Path: danylo-macelai/repositorio-virtual.git                               |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |                                                                                                |
    |   +-- Connection --------------------------------------------------------------------------+   |
    |   |  Protocol: ssh                                                                         |   |
    |   |  ...                                                                                   |   |
    |   +-----------------------------------------------+ +--------------------------------------+   |
    |                                                                                                |
    |   +-- Authentication ----------------------------------------------------------------------+   |
    |   |  User: Git                                                                             |   |
    |   |  ....                                                                                  |   |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+
    |                                              [  ...  ]   [ Next > ]   [        ]   [  ...  ]   |
    +------------------------------------------------------------------------------------------------+

Na próxima tela marque checkbox e depois clique em `Next >`.

    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Master                                                                           |   |
    |   |   ...                                                                                  |   |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+
    |                                              [  ...  ]   [ Next > ]   [        ]   [  ...  ]   |
    +------------------------------------------------------------------------------------------------+

 Aqui devemos informar o local onde será armazenado o código fonte.

    |   +-- Destination -------------------------------------------------------------------------+   |
    |   |   Directory: C:\workspaces\GITHUB\repositorio-virtual                                  |   |
    |   |   ...                                                                                  |   |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+
    |                                              [  ...  ]   [        ]   [ Finish ]   [  ...  ]   |
    +------------------------------------------------------------------------------------------------+

---
##### ECLIPSE (Maven)

O *Eclipse* fornece um mecanismo para executar ferramentas que não fazem parte dele. Neste caso vamos executar comando do *Maven* através da configuração de ferramentas externas, para configurar acesse `Run > External Tools > External Tools Configurations`. No assistente de configuração, clique com botão direito em `Program` e selecione a opção `New`, preencha as *tab* `Main` com as seguintes informações.

    +------------------------------------------------------------------------------------------------+
    |   Name: eclipse_eclipse                                                                        |
    |    ______                                                                                      |
    |   | Main \_________________________________________________________________________________    |
    |   |                                                                                        |   |
    |   |   +-- Location --------------------------------------------------------------------+   |   |
    |   |   |  C:\workspaces\TOOLS\apache-maven-3.5.2\bin\mvn.cmd                            |   |   |
    |   |   |  ....                                                                          |   |   |
    |   |   +-----------------------------------------------+ +------------------------------+   |   |
    |   |                                                                                        |   |
    |   |   +-- Working Directory -----------------------------------------------------------+   |   |
    |   |   |  C:\workspaces\GITHUB\repositorio-virtual                                      |   |   |
    |   |   |  ...                                                                           |   |   |
    |   |   +-----------------------------------------------+ +------------------------------+   |   |
    |   |                                                                                        |   |
    |   |   +-- Arguments -------------------------------------------------------------------+   |   |
    |   |   |  clean eclipse:clean install eclipse:eclipse -DskipTests=true                  |   |   |
    |   |   |                                                                                |   |   |
    |   |   +--------------------------------------------------------------------------------+   |   |
    |   |   ...                                                                                  |   |
    |   +----------------------------------------------------------------------------------------+   |
    |   |                                                               [   ...  ]   [ Apply ]   |   |
    |   +----------------------------------------------------------------------------------------+   |
    |                                                                                                |    
    |                                                                   [   Run  ]   [  ...  ]       |
    +------------------------------------------------------------------------------------------------+

OBS: Repita este procedimento criando o um novo build com o nome `clean_install` e arguments `clean install -DskipTests=true`

---
##### ECLIPSE (Importando o Projeto)

Finalmente vamos importar o projeto, acesse `File > Import`. No assistente de importação, selecione a opção `Git > Projects from Git` e clique em `Next >`, na próxima tela selecione `Existing local repository` e clique em `Next >`, na próxima tela clique em `Next >` novamente, depois em `Wizard for Project import` selecione o *radio* `import existing Eclipse projects` e clique em `Next >`. Agora em `Projects` marque apenas `virtual-common`, `virtual-master` e `virtual-slave` e clique em `Finish`.

---
##### ECLIPSE (Inicializado o Projeto)

Copie os arquivos `../virtual-master/src/main/resources/config/config-master.properties` e `../virtual-slave/src/main/resources/config/config-slave.properties` para a pasta `D:\workspaces\GITHUB\`. Abra os aquivos e altere os parâmetros conforme a necessidade do seu ambiente.

Para iniciar o projeto clique com botão direito na classe `br.com.slave.Application.java`, no menu de contexto selecione `Debug As > Debug Configurations...`. No assistente de configuração do Debug, altere o `Name`, acesse a aba `(x)= Arguments` para informar os `VM arguments:`, em seguida clique em `Apply` depois em `Debug`.

    
    Name: Slave
                    ________________
     ______________| (x)= Arguments \________________________________________________________________
    |                                                                                                |
    |   +-----------------------------------------------------------------------------------------   | 
    |   | +-------------------------------------------------------------------------------------+ |  | 
    |   | |                                           ...                                       | |  |
    |   | +----------------------------------------------------------------------------------- -+ |  |
    |   |                                                                              [   ...  ] |  |
    |   | +-- VM arguments: --------------------------------------------------------------------+ |  |
    |   | |  -Dconfiguracoes.path=D:\workspaces\GITHUB\config-slave.properties                  | |  |
    |   | +-------------------------------------------------------------------------------------+ |  |
    |   |                                           ...                                           |  |
    |   |                                                                  [   ...  ]   [ Apply ] |  |
    |   +-----------------------------------------------------------------------------------------+  |
    |                                               ...                                              |
    |                                                                        [ Debug  ]   [  ...  ]  |
    +------------------------------------------------------------------------------------------------+    
    
OBS: Repita este procedimento com o name `Master` e Vm Arguments: `-Dconfiguracoes.path=D:\workspaces\GITHUB\config-master.properties` para a classe `br.com.master.Application.java`