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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@RestController()
@Api(value= "Arquivos")
public class ArquivoResource {

    @Autowired
    IArquivo arquivoBusiness;

    @GetMapping(value = "/arquivos/{nome}")
    @ApiOperation(
            value = "Consulta os arquivos",
            nickname = "consultaArquivo",
            notes = "Retorna uma lista de ArquivoTO",
            response = ArquivoTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum arquivo foi localizado com o nome informado")})
    public ResponseEntity<List<ArquivoTO>> consultaArquivo(
            @ApiParam("Nome do arquivo a ser obtido. Não pode estar vazio.")
            @PathVariable String nome) {
        List<ArquivoTO> arquivos = arquivoBusiness.carregarPor(nome);
        if (arquivos == null) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }
        return ResponseEntity.ok(arquivos);
    }


    @GetMapping(value = "/arquivos/download/{id:\\d+}")
    @ApiOperation(
            value = "Download de arquivo",
            nickname = "download",
            notes = "Retorna o binário do arquivo")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum arquivo foi localizado com o id informado")})
    public ResponseEntity<Resource> download(
            @ApiParam("Id do arquivo a ser obtido. Não pode estar vazio.")
            @PathVariable("id") long id) {
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
    @ApiOperation(
            value = "Upload de arquivos",
            nickname = "upload",
            notes = "Retorna o ArquivoTO",
            response = ArquivoTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArquivoTO> upload(@RequestParam("file") MultipartFile file) {
        ArquivoTO arquivo = arquivoBusiness.upload(file);
        return ResponseEntity.ok(arquivo);
    }

}
