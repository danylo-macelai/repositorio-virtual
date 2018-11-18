package br.com.master.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.master.domain.ConfiguracaoTO;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-master <br>

 * @author macelai
 * @date: 18 de nov de 2018
 */
public interface ConfiguracaoDAO extends JpaRepository<ConfiguracaoTO, Long> {


}
