# Repositório Virtual

>*O sistema de arquivos Master fornece uma interface serviços para comunicação com os usuários permitindo a leitura, gravação e exclusão dos arquivos que estão divididos em blocos de dados distribuídos entre os vários nós de armazenamento Slave. O Master deverá armazenar apenas as informações do proprietário, nome do arquivo e as localizações dos blocos, com estas informações será possível reconstruir o arquivo.*

# Requisitos Funcionais

* Executar uma instância Master e várias instâncias do Slave na plataforma de microsservos;

* O Master fornece interface REST para interagir com os usuários através do upload, download e exclusão de arquivos;

* O Slave não interage diretamente com os usuários;

* O Master divide o arquivo enviado pelo usuário em blocos de armazenamento de tamanho fixo, que é distribuído em os nós Slave. Cada bloco contém várias cópias. O tamanho do bloco e o número de cópias podem ser configurados através dos parâmetros do sistema;

* O serviço Slave pode ser flexivelmente estendido e, a cada vez que um serviço Slave é iniciado, ele pode ser descoberto e incorporado ao sistema Master;

* O Master é responsável por verificar o status de integridade de cada Slave. Quando o Slave está offline,o Master cópia (migra) automaticamente o bloco de dados originalmente salvo pelo serviço off-line;

* O Master deve implementar certas estratégias no processo de gerenciamento de armazenamento e migração de blocos de dados para manter o balanceamento de carga de cada Slave.
