package br.com.master.business;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import br.com.common.business.IBusiness;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
public interface IArquivo extends IBusiness<ArquivoTO> {

    /**
     * Consulta o arquivo por nome
     *
     * @param nome - Nome do arquivo
     *
     * @return List<ArquivoTO>
     * @throws MasterException
     */
    List<ArquivoTO> carregarPor(String nome) throws MasterException;

    /**
     * Realiza o upload e grava o arquivo enviado
     *
     * @param file
     */
    ArquivoTO gravar(MultipartFile file) throws MasterException;

    /**
     * Realiza o download do arquivo enviado
     *
     * @param arquivo
     * @return InputStreamResource
     * @throws MasterException
     */
    InputStreamResource ler(ArquivoTO arquivo) throws MasterException;

}
