package br.com.master.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.master.configuration.MasterException;
import br.com.master.domain.ConfiguracaoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
public interface ConfiguracaoDAO extends JpaRepository<ConfiguracaoTO, Long> {

    /**
     *  O método buscar retorna a única instância da classe configuracao.
     *
     * @return ConfiguracaoTO
     * @throws MasterException
     */
    @Query("SELECT C FROM br.com.master.domain.ConfiguracaoTO C")
    ConfiguracaoTO buscar();

}
