package br.com.common.business;

import java.util.List;

import javax.persistence.NoResultException;

import br.com.common.domain.Domain;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
public interface IBusiness<D extends Domain> {

    /**
     * O método {@link IBusiness#carregarTodos()} retornará todos os registros que correspondem à especificação.
     *
     * @return List<D>
     */
    List<D> carregarTodos();

    /**
     * O método {@link IBusiness#ache(long)} lê o registro da tabela com base na chave primária.
     *
     * @apiNote: Se a chave estiver contida no contexto de persistência, ela será retornada caso contrário null
     *
     * @param id
     * @return D
     */
    D ache(final long id);

    /**
     * O método {@link IBusiness#carregar(long)} lê o registro da tabela com base na chave primária.
     *
     * @apiNote: Se a chave estiver contida no contexto de persistência, ele será retornada caso contrário uma {@link NoResultException}
     *
     * @param id
     * @return D
     */
    D carregar(final long id);

    /**
     * O método {@link IBusiness#incluir(Domain)} executa a instrução insert para criar o registro informado.
     *
     * @param dominio
     */
    void incluir(final D dominio);

    /**
     * O método {@link IBusiness#alterar(Domain)} executa uma instrução update com base nas informações do registro.
     *
     * @param dominio
     */
    void alterar(final D dominio);

    /**
     * O método {@link IBusiness#excluir(Domain)} exclui apenas o registro informado.
     *
     * @param dominio void
     */
    void excluir(final D dominio);

    /**
     * O método {@link IBusiness#carregarTodos()} exclui todos os registros que correspondem à especificação.
     *
     * @return List<D>
     */
    void excluirTodos();

}
