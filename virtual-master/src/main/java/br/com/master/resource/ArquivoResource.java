package br.com.master.resource;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.master.business.IArquivo;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@RestController()
public class ArquivoResource {

    @Autowired
    IArquivo arquivoBusiness;

    @GetMapping(value = "/arquivos/{nome}")
    public ResponseEntity<List<ArquivoTO>> consultaArquivo(@PathVariable String nome) {
        List<ArquivoTO> arquivos = arquivoBusiness.carregarPor(nome);
        if (arquivos == null) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }
        return ResponseEntity.ok(arquivos);
    }

    @GetMapping(value = "/arquivos/download/{id:\\d+}")
    public ResponseEntity<Resource> download(@PathVariable("id") long id) {
        ArquivoTO arquivo = arquivoBusiness.ache(id);
        if (arquivo == null) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }

        InputStreamResource stream = arquivoBusiness.download(arquivo);

        return ResponseEntity.ok()
                .contentLength(arquivo.getTamanho())
                .contentType(MediaType.parseMediaType(arquivo.getMimeType()))
                .body(stream);
    }

    @PostMapping("/arquivos/upload")
    public ResponseEntity<ArquivoTO> upload(@RequestParam("file") MultipartFile file) {
        ArquivoTO arquivo = arquivoBusiness.upload(file);
        return ResponseEntity.ok(arquivo);
    }

}
