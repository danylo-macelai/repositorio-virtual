package br.com.master.business;

import java.util.List;

import org.springframework.dao.DataAccessException;

import br.com.common.business.IBusiness;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
public interface IBloco extends IBusiness<BlocoTO> {

    List<BlocoTO> carregarTodosPor(ArquivoTO arquivo) throws DataAccessException;

}
