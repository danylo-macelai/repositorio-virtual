package br.com.common.business;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.dao.DataAccessException;

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
     * @throws DataAccessException
     */
    List<D> carregarTodos() throws DataAccessException;

    /**
     * O método {@link IBusiness#ache(long)} lê o registro da tabela com base na chave primária.
     *
     * @apiNote: Se a chave estiver contida no contexto de persistência, ela será retornada caso contrário null
     *
     * @param id
     * @return D
     * @throws DataAccessException
     */
    D ache(final long id) throws DataAccessException;

    /**
     * O método {@link IBusiness#carregar(long)} lê o registro da tabela com base na chave primária.
     *
     * @apiNote: Se a chave estiver contida no contexto de persistência, ele será retornada caso contrário uma {@link NoResultException}
     *
     * @param id
     * @return D
     * @throws DataAccessException
     */
    D carregar(final long id) throws DataAccessException;

    /**
     * O método {@link IBusiness#incluir(Domain)} executa a instrução insert para criar o registro informado.
     *
     * @param dominio
     * @throws DataAccessException
     */
    void incluir(final D dominio) throws DataAccessException;

    /**
     * O método {@link IBusiness#alterar(Domain)} executa uma instrução update com base nas informações do registro.
     *
     * @param dominio
     * @throws DataAccessException
     */
    void alterar(final D dominio) throws DataAccessException;

    /**
     * O método {@link IBusiness#excluir(Domain)} exclui apenas o registro informado.
     *
     * @param dominio void
     * @throws DataAccessException
     */
    void excluir(final D dominio) throws DataAccessException;

    /**
     * O método {@link IBusiness#carregarTodos()} exclui todos os registros que correspondem à especificação.
     *
     * @return List<D>
     * @throws DataAccessException
     */
    void excluirTodos() throws DataAccessException;

    /**
     * O método count retorna a quantidade de registros cadastrados
     *
     * @return long
     * @throws DataAccessException
     */
    long count() throws DataAccessException;

}
