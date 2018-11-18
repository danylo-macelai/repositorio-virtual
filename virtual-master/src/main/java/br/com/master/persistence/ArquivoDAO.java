package br.com.master.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-master <br>

 * @author macelai
 * @date: 18 de nov de 2018
 */
public interface ArquivoDAO extends JpaRepository<ArquivoTO, Long> {

    /**
     * Consulta o arquivo por nome
     *
     * @param nome - Nome do arquivo
     * @return ArquivoTO
     * @throws MasterException
     */
    ArquivoTO findByNome(String nome);

}
