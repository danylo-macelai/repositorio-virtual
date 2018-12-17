package br.com.master.resource;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.master.business.IArquivo;
import br.com.master.business.IBloco;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.domain.BlocoTO;

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

    @Autowired
    IBloco   blocoBusiness;

    @GetMapping(value = "/arquivos/{nome}")
    public ResponseEntity<ArquivoTO> blocos(@PathVariable String nome) {
        ArquivoTO arquivo = arquivoBusiness.carregarPor(nome);
        if (arquivo == null) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }
        List<BlocoTO> blocos = blocoBusiness.carregarTodosPor(arquivo);
        if (blocos.isEmpty()) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }
        arquivo.setBlocos(new HashSet<>(blocos));
        return ResponseEntity.ok(arquivo);
    }

    @PostMapping("/arquivos/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        arquivoBusiness.upload(file);
        return ResponseEntity.ok("ok");
    }

}
