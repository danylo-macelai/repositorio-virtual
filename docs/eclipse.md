Configurar o ambiente do Eclipse
=========================================

As instruções a seguir descrevem como configurar o eclipse para usar arquivos e convenções que obedecem aos padrões de codificação do Repositório Virtual.

Todas as opções referentes ao `Window > Preferences > Java > CodeStyle` podem ser configuradas rapidamente importando apenas o arquivo [ [rv_preferences.epf](../docs/rv_preferences.epf) ]. As configurações de `Formatting`, `Import Organization`, `Clean Up` e `Code Templates` serão cobertas por este arquivo.

A importação deverá ser realizada em `File > Import... > General > Prefereces` clicando no botão `Next >` para selecionar em [`Browse...`] o arquivo _rv_preferences.epf_ e as opções abaixo e logo em seguida clique em `Finish`.

    [x] Java Compiler  Prefereces
    [x] Java Code Style  Prefereces

Algumas configurações NÃO são cobertas pela importação e deverão ser ralizadas manualmente.

A configuração do encoding e quebras de linha em `Window > Preferences > General > Workspace`

      +- Text file encoding-----+-- New text file line delimiter -+
      |  ...                    |   ....                          |
      |  [.] Other [UTF-8]      |   [.] Other [Unix]              |
      +-------------------------+---------------------------------+

Substituindo tabs por spaces em arquivos de texto genéricos Navegue até `Window > Preferences > General > Editors > Text Editors` e marque a *ckeckbox* 
    
    [x] Insert spaces for tabs	
	

`Save Actions` (NÃO coberto pela importação) `Window->Preferences->Java->Editor->Save Actions`. Selecione as seguintes opções:  

    [x] Perform the selected actions on save
      [x] Format source code (Format all lines)
      [x] Organize Imports
      [x] Additional Actions

      Configure...
        | Code Organizing |
            [x] Remove Trailing whitespace (All Lines)
            [x] Correct Indentation
            ...

        | Code Style |
            [x] Use blocks in if/while/for/do statements (Always)
            ...
            [x] Use final modifier where possible
              [x] Private Fields [ ] Paramter [ ] Local Variables
            ...

        | Member Accesses |    
          
        | Missing Code |
          (Selecione Todos)

        | Unnecessary Code |
          [x] Remove unused imports
          ...
          [x] Remove unnecssary casts