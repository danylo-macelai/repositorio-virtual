package br.com.master.persistence;

import br.com.master.configuration.MasterException;
import br.com.master.domain.ConfiguracaoTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
public interface ConfiguracaoDAO extends JpaRepository<ConfiguracaoTO, Long> {

    /**
     * O método buscar retorna a única instância da classe configuração.
     *
     * @return ConfiguracaoTO
     * @throws MasterException
     */
    @Query("SELECT C FROM br.com.master.domain.ConfiguracaoTO C")
    ConfiguracaoTO buscar() throws MasterException;

}
