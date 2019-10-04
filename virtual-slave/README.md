# virtual-slave

- O Slave é um micros-serviço que oferece uma interface de serviços para interagir exclusivamente com Master realizando a leitura, gravação e exclusão de blocos no disco.

- Além de ser flexivelmente estendido, a cada inicialização ele se auto registra no service discovery permitindo assim que o Master sempre verifique a disponibilidade de suas instâncias.

- Periodicamente uma tarefa será executada afim de manter acuracidade dos blocos armazenados.
