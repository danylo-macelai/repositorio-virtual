package br.com.slave.resource;

import static br.com.slave.resource.VolumeResource.RESOURCE_ROOT_URL;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.wso2.msf4j.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;

import br.com.common.wrappers.File;
import br.com.common.wrappers.PatchForm;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de out de 2018
 */
@Api(tags = {"Volumes"})
@SwaggerDefinition(
        info = @Info(
                title = "Volumes RESTful Web Services",
                version = "1.0",
                description = "Manual de Integração",
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(
                        name = "Danylo Macelai",
                        email = "danylomacelai@gmail.com",
                        url = "http://danylomacelai.com")
                ),
        tags = {
                @Tag(name="Volumes", description="O serviço de volumes envolve a leitura, gravação e exclusão de blocos.")
        }
        )
@Component
@Path(RESOURCE_ROOT_URL)
public class VolumeResource {

    public static final String RESOURCE_ROOT_URL   = "/volumes";

    @Autowired
    private IVolume business;

    @GET
    @Path("/healthCheck")
    public Response healthCheck() {
        return Response.status(200)
                .entity("alive")
                .build();
    }

    @GET
    @Path("/status")
    @ApiOperation(
            value = "Verifica o status da aplicação",
            nickname = "status",
            notes = "Retorna o status 200 para indicando que as solicitações HTTP serão bem-sucedidas",
            response = Response.class
    )
    public Response status() {
        return Response.status(200)
                .build();
    }

    @OPTIONS
    @Path("/")
    @ApiOperation(
            value = "Consultar as opções de requisição",
            nickname = "options",
            notes = "Retorna as operações de requisições permitidas para o volume",
            response = Response.class
            )
    public Response options() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
                .build();
    }

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Consulta os metadados do volume",
            nickname = "consulta",
            notes = "Retorna os metadados do volume",
            response = Response.class,
            produces = MediaType.APPLICATION_JSON
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum volume foi cadastrado!")
            }
    )
    public Response consulta() {
        VolumeTO volume = business.buscar();
        if (volume == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(volume).build();
    }

    @PUT
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Altera os metadados do volume",
            nickname = "edicao",
            notes = "Retorna o status que indica a situação da solicitações HTTP",
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "A alteração dos metadados foram bem-sucedidas"),
            @ApiResponse(code = 404, message = "Nenhum volume foi cadastrado!")
            })
    public Response edicao(
            @ApiParam(name = "patchs",
                    value = "Lista de objetos que contém as informações necessárias para a alteração das propriedades, em OP deverá ser informado o tipo de operação realizada (ADD, REMOVE, REPLACE, MOVE, COPY, TEST), no PATH nome da propriedade a ser alterada, em VALUE o valor que será atribuído",
                    required = true
            )
            PatchForm[] patchs) {
        VolumeTO volume = business.buscar();
        if (volume == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patch = mapper.valueToTree(patchs);
        JsonNode domain = mapper.valueToTree(volume);
        JsonNode merge = JsonPatch.apply(patch, domain);
        try {
            volume = mapper.readerForUpdating(volume).readValue(merge);
        } catch (IOException e) {
            throw new SlaveException("volume.preencher.erro");
        }
        business.alterar(volume);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/gravacao")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Envia um bloco para o servidor remoto",
            nickname = "gravacao",
            notes = "Retorna os metadados que identifica o bloco no servidor Slave",
            response = File.class,
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gravação foi realizada com sucesso"),
            @ApiResponse(code = 409, message = "O identificador gerado já existe")})
    public Response gravacao(@Context Request request) {
        File file = business.upload(request);
        return Response.status(Response.Status.OK).entity(file).build();
    }

    @GET
    @Path("/leitura/{uuid}")
    @ApiOperation(
            value = "Transfere o bloco para o computador local",
            nickname = "leitura",
            notes = "Retorna uma cópia do bloco que está no servidor Slave para um computador local",
            response = Resource.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Leitura realizada com sucesso"),
            @ApiResponse(code = 404, message = "Nenhum arquivo foi localizado!")
            }
    )
    public Response leitura(
            @ApiParam(
                    name = "uuid",
                    value = "Identificador único do bloco",
                    required = true
            )
            @PathParam("uuid") String uuid) {
        StreamingOutput stream = business.download(uuid);
        return Response.status(Response.Status.OK).entity(stream).build();
    }

    @POST
    @Path("/replicacao/{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Gera uma cópia do bloco",
            nickname = "replicacao",
            notes = "Gera uma cópia do bloco em outro servidor Slave e retorna os metadados que identifica a sua localização",
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Replicação realizada com sucesso", response = String.class)}
    )
    public Response replicacao(@PathParam("uuid") String uuid) throws Exception {
        File file = business.replicacao(uuid);
        return Response.status(Response.Status.OK).entity(file).build();
    }

}
