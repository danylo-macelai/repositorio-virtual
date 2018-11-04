package br.com.common.persistence;

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
public interface IPersistence<D extends Domain> {

    /**
     * Remove a entidade do contexto de persistência, fazendo com que as suas alterações não sejam sincronizadas com o banco de dados.
     *
     * @param dominio
     * @throws DataAccessException
     */
    void detach(D dominio) throws DataAccessException;

    /**
     * Sincronize o contexto de persistência com o banco de dados forçando a execução dos Sqls.
     *
     * @throws DataAccessException
     */
    void flush() throws DataAccessException;

    /**
     * Limpa o contexto de persistência, fazendo com que as alterações realizads nas entidades que ainda não foram persistidas em banco de dados sejam
     * descatadas.
     *
     * @throws DataAccessException
     */
    void clear() throws DataAccessException;

    /**
     * Verifique se a instância da entidade está presente no contexto de persistência.
     *
     * @param dominio
     * @throws DataAccessException
     */
    boolean contains(D dominio) throws DataAccessException;

    /**
     * O método {@link IPersistence#carregarTodos()} retornará todos os registros que correspondem à especificação.
     *
     * @return List<D>
     * @throws DataAccessException
     */
    List<D> carregarTodos() throws DataAccessException;

    /**
     * O método {@link IPersistence#ache(long)} lê o registro da tabela com base na chave primária.
     *
     * @apiNote: Se a chave estiver contida no contexto de persistência, ela será retornada caso contrário null
     *
     * @param id
     * @return D
     * @throws DataAccessException
     */
    D ache(final long id) throws DataAccessException;

    /**
     * O método {@link IPersistence#carregar(long)} lê o registro da tabela com base na chave primária.
     *
     * @apiNote: Se a chave estiver contida no contexto de persistência, ele será retornada caso contrário uma {@link NoResultException}
     *
     * @param id
     * @return D
     * @throws DataAccessException
     */
    D carregar(final long id) throws DataAccessException;

    /**
     * O método {@link IPersistence#incluir(Domain)} executa a instrução insert para criar o registro informado.
     *
     * @param dominio
     * @throws DataAccessException
     */
    void incluir(final D dominio) throws DataAccessException;

    /**
     * O método {@link IPersistence#alterar(Domain)} executa uma instrução update com base nas informações do registro.
     *
     * @param dominio
     * @throws DataAccessException
     */
    void alterar(final D dominio) throws DataAccessException;

    /**
     * O método {@link IPersistence#excluir(Domain)} exclui apenas o registro informado.
     *
     * @param dominio void
     * @throws DataAccessException
     */
    void excluir(final D dominio) throws DataAccessException;

    /**
     * O método {@link IPersistence#carregarTodos()} exclui todos os registros que correspondem à especificação.
     *
     * @return List<D>
     * @throws DataAccessException
     */
    void excluirTodos() throws DataAccessException;

}
