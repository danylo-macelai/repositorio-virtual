# virtual-master

- O Master é um micros-serviço que interage diretamente com os clientes através da leitura, gravação e exclusão de arquivos. Os arquivos enviados serão divididos em blocos de tamanho fixo e armazenados no diretório temporário da aplicação, assim que alguma instância Slave estiver registrada no service discovery a tarefa de gravação de blocos será executada para que posteriormente seja realizada a replicação entre os nós Slave.

- Ao receber uma solicitação de leitura o Master recupera todos os blocos espalhados entre os diversos nós incluindo as réplicas que serão necessárias para a reconstrução do arquivo no diretório temporário da aplicação, após a entrega uma tarefa será responsável pela sua exclusão.

- Diariamente será executada uma tarefa que mantém uma estratégia para migração de blocos e balanceamento entre os nós Slaves.
