package br.com.slave.resource;

import static br.com.slave.resource.VolumeResource.RESOURCE_ROOT_URL;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
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
                title = "Slave RESTful Web Services",
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
                @Tag(name="Volumes", description="<p>O serviço de volumes envolve a leitura, gravaço e exclusão dos blocos.</p>")
        }
        )
@Component
@Path(RESOURCE_ROOT_URL)
public class VolumeResource {

    public static final String RESOURCE_ROOT_URL   = "/volumes";

    @Autowired
    private IVolume business;

    @GET
    @Path("/status")
    @ApiOperation(
            value = "Verifica o status do sistema",
            nickname = "status",
            notes = "<p>O status indica a disponibilidade do sistema, caso esteja em funcionamento dentro de requisitos operacionais pré-estabelecidos será retornado o status <strong>200</strong>, caso contrário o <strong>503</strong>.</p>"
            )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<p>Os serviços estão disponíveis.</p>"),
            }
    )
    public Response status() {
        return Response.ok()
                .build();
    }

    @GET
    @Path("/healthCheck")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(
            value = "Examina o sistema",
            nickname = "healthCheck",
            notes = "<p>O healthCheck retorna a mensagem <strong>alive</strong> indicando que o sistema em funcionamento.</p>",
            produces = MediaType.TEXT_PLAIN
            )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<p>O sistema está em funcionamento.</p>")
            }
    )
    public Response healthCheck() {
        return Response.ok()
                .entity("alive")
                .build();
    }

    @OPTIONS
    @Path("/")
    @ApiOperation(
            value = "Consultar verbs HTTP suportados",
            nickname = "options",
            notes = "<p>O options é usado para consultar a lista de verbs <strong>HTTP</strong>, suportados no URI. Essa lista é enviada de volta em <strong>permitir</strong> cabeçalho de resposta.</p>"
            )
    public Response options() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "DELETE, GET, POST, PUT, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
                .build();
    }

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Consulta os metadados do volume",
            nickname = "consulta",
            notes = "<p>A consulta é usada para recuperar os <strong>metadados</strong> do volume. Se o volume existir os <strong>metadados</strong> serão enviados no corpo da mensagem de resposta no formato <strong>json</strong> caso contrário, o status <strong>404</strong> indicando que o volume não existe ou não foi localizado.</p>",
            produces = MediaType.APPLICATION_JSON
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "<p>Nenhum volume foi cadastrado!</p>")
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
    @ApiOperation(
            value = "Altera os metadados do volume",
            nickname = "alteracao",
            notes = "<p>A alteraço é usada para manter os <strong>metadados</strong> do único volume cadastrado. Se a operaço for realizada com sucesso, o status <strong>204</strong> será retornado caso contrário, a mensagem de erro.</p>")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "<p>A alteraço dos metadados foram bem-sucedidas.</p>"),
            @ApiResponse(code = 400, message = "<p>Regras de Negócio:</p> <ul> <li>A capacidade do volume é inferior ao tamanho,</li> <li>A localizaço do volume não existe ou é invalida,</li> <li>A capacidade do volume foi excedida.</li> <li>Atualmente o volume está indisponível. Contate o administrador do sistema.</li></ul>"),
            @ApiResponse(code = 403, message = "<p>Regras de Negócio:</p> <ul> <li>Não é permitido alterar a quantidade de arquivos no volume,</li><li>Não é permitido alterar o tamanho do volume.</li></ul>"),
            @ApiResponse(code = 404, message = "<p>Nenhum volume foi cadastrado!</p>")
            })
    public Response alteracao(
            @ApiParam(name = "patchs",
                    value = "<p>Lista de objetos que contém as informações necessárias para a alteraço das propriedades</p> <ul> <li><strong>OP:</strong> deverá ser informado o tipo de operaço realizada: <ul> <li><em>ADD</em> para incluir um valor</li> <li><em>REMOVE</em> para excluir o valor</li> <li><em>REPLACE</em> para alterar o valor</li> </ul> </li> <li><strong>PATH:</strong> nome da propriedade a ser alterada;</li> <li><strong>VALUE:</strong> o valor que será atribuído.</li> </ul>",
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
        try {
            JsonNode merge = JsonPatch.apply(patch, domain);
            volume = mapper.readerForUpdating(volume).readValue(merge);
        } catch (IOException e) {
            throw new SlaveException("slave.preencher.patchs.obj", e);
        }
        business.alterar(volume);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/gravacao")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Envia um bloco para o volume",
            nickname = "gravacao",
            notes = "<p>A gravaço é usada para fazer o upoload dos <strong>blocos</strong> no servidor, que será identificado de for única e salvo no volume. Se a operaço for realizada com sucesso, retorna os <strong>metadados</strong> de identificaço caso contrário, a mensagem de erro</p>",
            response = File.class,
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<p>Gravaço foi realizada com sucesso</p>"),
            @ApiResponse(code = 400, message = "<p>Regras de Negócio:</p> <ul> <li>A capacidade do volume foi excedida.</li> <li>Atualmente o volume está indisponível. Contate o administrador do sistema.</li> </ul>"),
            @ApiResponse(code = 409, message = "<p>O bloco (108cf...079ad), já existe no volume.</p>")
            }
    )
    public Response gravacao(@Context Request request) {
        File file = business.upload(request);
        return Response.status(Response.Status.OK).entity(file).build();
    }

    @GET
    @Path("/leitura/{uuid}")
    @ApiOperation(
            value = "Carrega o bloco do volume",
            nickname = "leitura",
            notes = "<p>A leitura é usada para fazer o download dos <strong>blocos</strong> no servidor, através do <strong>identificador</strong> único. Se a operaço for realizada com sucesso, retorna o <strong>binário</strong> no corpo da mensagem caso contrário, a mensagem de erro.</p>"
            )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<p>Leitura realizada com sucesso</p>"),
            @ApiResponse(code = 400, message = "<p>Regras de Negócio:</p> <ul> <li>O bloco (108cf...079ad), não existe no volume.</li> </ul>")
    }
            )
    public Response leitura(
            @ApiParam(
                    name = "uuid",
                    value = "<p>Identificador único do bloco</p>",
                    example = "0ac92987eeeb0d89c42a3ecf778356a9b52407a0b978b0fdbcf3b508ca2c9460",
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
            notes = "<p>A replicaço é usada para fazer uma <strong>cópia</strong> dos <strong>blocos</strong> nos outros servidores. O <strong>bloco</strong> será identificado através do <strong>uuid</strong> e replicado nos outros servidores que estão registrados no service discovery. Se a operaço for realizada com sucesso, retorna os <strong>metadados</strong> de identificaço caso contrário, a mensagem de erro</p>",
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "<p>Replicaço realizada com sucesso</p>"),
            @ApiResponse(code = 400, message = "<p>Regras de Negócio:</p> <ul> <li>O bloco (108cf...079ad), não existe no volume.</li> <li>Não existe serviço registrado no service discovery</li> </ul>")}
            )
    public Response replicacao(
            @ApiParam(
                    name = "uuid",
                    value = "<p>Identificador único do bloco</p>",
                    example = "0ac92987eeeb0d89c42a3ecf778356a9b52407a0b978b0fdbcf3b508ca2c9460",
                    required = true
                    )
            @PathParam("uuid") String uuid,
            @QueryParam("instance_id") String instanceId) throws Exception {
        File file = business.replicacao(uuid, instanceId);
        return Response.status(Response.Status.OK).entity(file).build();
    }

    @DELETE
    @Path("/exclusao/{uuid}")
    @ApiOperation(
            value = "Remove o arquivo do volume",
            nickname = "exclusao",
            notes = "<p>A exclusão é usada para remover o <strong>bloco</strong> do volume. O <strong>bloco</strong> será identificado através do <strong>uuid</strong> e excluído do volume. Se a operaço for realizada com sucesso, retorna o status <strong>204</strong> caso contrário, a mensagem de erro.</p>"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "<p>Exclusão foi bem-sucedidas</p></p>"),
            @ApiResponse(code = 404, message = "<p>Nenhum arquivo foi localizado!</p>")
            }
    )
    public Response exclusao(
            @ApiParam(
                    name = "uuid",
                    value = "<p>Identificador único do bloco</p>",
                    example = "0ac92987eeeb0d89c42a3ecf778356a9b52407a0b978b0fdbcf3b508ca2c9460",
                    required = true
            )
            @PathParam("uuid") String uuid) {

        business.excluir(uuid);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
