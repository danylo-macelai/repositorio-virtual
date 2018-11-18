package br.com.master.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
public interface BlocoDAO extends JpaRepository<BlocoTO, Long> {

    List<BlocoTO> findByArquivoOrderByNumeroAsc(ArquivoTO arquivo);

}
