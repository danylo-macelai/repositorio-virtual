Workspace
=========================================

O ambiente de desenvolvimento será em `C:\workspaces\GITHUB\repositorio-virtual\virtual-mobile` abra a ide Android Studio apontando para este local.

---
##### ANROID STUDIO (Code Style)

As instruções a seguir descrevem como configurar o android studio para usar arquivos e convenções que obedecem aos padrões de codificação do Repositório Virtual.

>Todas as opções referentes ao `File > Settings > Editor > CodeStyle > Java` podem ser configuradas rapidamente importando apenas o arquivo [ [rv_preferences.epf](../docs/rv_preferences.epf) ].

As preferências de codestyle que serão importados são baseados na IDE *Eclipse*, e para importar para a IDE *Android Studio* será necessário a instalação do plugin *Eclipse Code Formatter*. Para instalar o plugin acesse `File > Settings`, no Menu de configuração vá ate `Plugins > Aba Marketplace`, no campo `Search` insira o nome do plugin, selecione e clique em `Install`. Após o procedimento reinicie o Android Studio.

Após o reiniciar a IDE acesse `File > Settings > Other Settings > Eclipse Code Formatter`, no meu ao lado selecione a opção `Use the Eclipse code formatter`

    +................................................................................................+
    |   Eclipse Code Formatter                                                                       |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   ( ) Disabled                                                                         |   |
    |   |   (.) Use the Eclipse code formatter                                                   |   |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

No menu `Supported file types` marque a opção `Java` marcada.

    +................................................................................................+
    |   Supported file types                                                                         |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Enable Java                                   [ ] Enable GWT                         |
    |   |   [ ] Enable Javascript                             [ ] Enable C/C++                       |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

No menu `Java formatter version` selecione a opção `Eclipse 2019-03` e a opção `Import ordering style for 4.5.1+`

    +................................................................................................+
    |   Java formatter version                                                                       |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   (.) Eclipse 4.4 Luna                              ( ) Import ordering style -4.5.0       |
    |   |   ( ) Eclipse 2019-03                               (.) Import ordering stylw 4.5.1+       |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

No próximo campo e configuração o `Eclipse Java Formatter config File` e onde vai adicionar o caminho onde o arquivo [ [rv_preferences.epf](../docs/rv_preferences.epf) ] está localizado. Após adicioná-lo clique no botão `Apply` seguido de `OK` e reinicie novamente o Android Studio.

Para configurar o *encoding* acesse `File > Settings`. No assistente de configurações, selecione `Editor > File Encodings`. Na seção denominada `Global Enconding` marque a opção `UTF-8`, já para a `Project Encoding`, marque `UTF-8` e por fim em `Properties File` marque também a opção `UTF-8` depois clique em `Apply`.

Vamos definir um conjunto de ações que serão executadas a cada salvamento do editor *Java*, para configurar primeiramente baixe o plugin
`Save Actions` acessando `File > Settings`, menu de configurações `Plugins`, procure pelo plugin clica em `Install` e reinicie a IDE.
Após esse procedimento vá novamente em `File > Settings`, no menu de configuração `Other Settings` selecione o `Save Actions`.

No menu `General` marque as opções `Activate save actions on save` e `No action if compile erros`.

    +................................................................................................+
    |   General                                                                                      |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Activate save actions on save                                                        |
    |   |   [ ] Activate save actions on shortcut                                                    |
    |   |   [ ] Activate save actions on batch                                                       |
    |   |   [x] No action if compile errors                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

No menu `Formatting actions` marque a opção `Reformat file`

    +................................................................................................+
    |   Formating actions                                                                            |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Optmize imports                                                                      |
    |   |   [x] Reformat file                                                                        |
    |   |   [ ] Reformat only changed code                                                           |
    |   |   [ ] Rearrange fields and methods                                                         |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

No menu `Java inspection and quick fix` e marque as opções `Add final modifier to field`, `Add missing @Override annotations`, `Add blocks to if/while/for statements` 

    +................................................................................................+
    |   Java inspection and quick fix                                                                |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Add final modifier to field                                                          |
    |   |   ....                                                                                     |
    |   |   [x] Add missing @Override annotations                                                    |
    |   |   ....                                                                                     |
    |   |   [x] Add blocks to if/while/for statements                                                |     
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

Faça a configuração do FILE HEADER para ser incluido nas classes.
Menu Settings > Editor > File and code templates
Aba `Includes` > Clique em `File Header`
No espaço ao lado inclua no arquivo: 
>/**Description: FIXME: Document this type<br>
*Project: ${PROJECT_NAME}<br>
*@author ${USER}<br>
*@date: ${DATE}<br>
*@version $$<br>
*/

E por fim defina o keymap da IDE acessando o `File > Settings`, clique no menu `Keymap` marque a opção `Eclipse`

---
##### ANDROID STUDIO (GIT/SSH)

> Para seguir este passo obrigatoriamente deve-se ter feito o passo ( *Eclipse SSH* e *GITHUB SSH* ) que se encontra no documento [ [Documentação Eclipse](../docs/eclipse.md) ].

Acesse o menu de configurações em `File > Settings`, vá ate o menu `Version Control > GitHub`. Clique no icone de `+` e adicione sua conta do GitHub e depois clique em `Apply`.

No menu `Version Control > Git` na opção `SSH Executable` coloca em `Native`

Agora vá em tools em `File > Settings`, no menu `Tools > Terminal`. Mude o terminal padrão para o terminal no próprio GIT na opção
`Shell Path` coloque o caminho do Git Cmd que no SO Windows geralmente fica no caminho `C:\Program Files\Git\git-cmd.exe` caso seu sistema seja em 64bits se for em x86 provavelmente fica no caminho `C:\Program Files (x86)\Git\git-cmd.exe`, depois insira um nome em `Tab name` clica em `Apply` no final.

Por fim acesse a pasta `.ssh` que o Git cria depois que é instalado no seu sistema, no caso do Windows fica em `C:\users\%User%\.ssh`, copie o conteúdo de `c:\Workspace\TOOLS\.ssh` e insira nesta pasta.

---
##### ANDROID STUDIO (SDK)

>O projeto virtual mobile roda como base a API 23 do Android a versão 6.0 Marshmallow e recursos da API 28 Android Pie.

Acesse `File > Settings` e em seguida o menu `Appearance & Behavior > System Settings > ANDROID SDK`.
No menu ao lado selecione o SDK `Android 6.0 (Marshmalow)` e `Android 9.0 (Pie)`.

    +................................................................................................+
    |   SDK Platforms                                                                                |
    |   ...                                                                                          |
    |   +----------------------------------------------------------------------------------------+   |
    |   |   [x] Android 9.0 (Pie)                                                                    |
    |   |   ....                                                                                     |
    |   |   [x] Android 7.0 (Nougat)                                                            |
    |   +----------------------------------------------------------------------------------------+   |
    |   ...                                                                                          |
    +------------------------------------------------------------------------------------------------+

---
##### ANDROID STUDIO (Inicializando o Projeto)
>Abra o projeto no android studio apontando para o caminho: `C:\workspaces\GITHUB\repositorio-virtual\virtual-mobile`

>Para inicializar o projeto Virtual-Mobile existe duas opções:

>AVD (Android Virtual Device Manager)
    
Para está opção será necessário criar um dispositivo virtual para executar a aplicação, acesse o menu `Tools > AVD Manager`.
Na tela de configuração do AVD Manager clique em `+ Create Virtual Device ` e clique em `Next`. Selecione o dispositivo que deseja executar e clique em `Next`, depois selecione uma imagem do sistema que seja *API 24 +*, depois clique em `Next` e `Finish`.

Feito isso clique no menu `Run > Run 'app'`.

>Usando um dispositivo Android Físico (Depuração USB)
Habilite a depuração USB e o modo desenvolvedor do seu dispositivo *Android 6.0 (API 23)+*.

Por fim vá no menu `Run > Run 'app'`.