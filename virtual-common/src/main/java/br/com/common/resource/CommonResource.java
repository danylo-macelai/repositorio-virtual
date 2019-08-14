package br.com.common.resource;

import br.com.common.access.ValidarTokenAccess;
import br.com.common.access.property.ValidarToken;
import br.com.common.utils.Utils;
import br.com.common.wrappers.CommonException;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Response;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
public abstract class CommonResource {

    final String       ACCESS_SERVICE_URL_DEFAULT = "access.serviceUrl.default";
    final String       PERFIL_TYPE_ADMINSTRADOR;
    final ObjectMapper mapper;
    final String       serviceUrl;

    @Autowired
    public CommonResource(Environment env) {
        PERFIL_TYPE_ADMINSTRADOR = "Adminstrador";
        mapper = new ObjectMapper().configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        serviceUrl = env.getProperty(ACCESS_SERVICE_URL_DEFAULT);
    }

    /**
     * Carrega as informações do {@link ValidarToken} com base na authorization ou token.
     *
     * @param authorization
     * @param token
     * @return ValidarToken
     * @throws CommonException
     */
    protected final ValidarToken validarTokenAccess(String authorization, String token) throws CommonException {

        if (authorization != null && token != null) {
            throw new CommonException("common.resource.validar.token");
        }

        if (authorization == null && token == null) {
            throw new CommonException("common.resource.validar.token.null");
        }

        token = Objects.toString(authorization, token);

        StringBuilder query = new StringBuilder();

        query.append(serviceUrl);

        query.append("?query=");

        query.append(" query($token:String) { ");
        query.append("     validarToken(token: $token) { ");
        query.append("       id ");
        query.append("       nome ");
        query.append("       email ");
        query.append("       perfilType ");
        query.append("     } ");
        query.append(" } ");
        query.append(" &variables={ ");
        query.append("   \"token\":\"").append(token).append("\" ");
        query.append(" } ");

        try (Response response = Utils.httpGet(query.toString())) {
            final ValidarTokenAccess access = mapper.readValue(response.body().string(), ValidarTokenAccess.class);

            if (!access.getErrors().isEmpty()) {
                StringBuilder erros = new StringBuilder();
                access.getErrors().forEach(e -> {
                    erros.append(e.getMessage());
                });
                throw new CommonException(erros.toString());
            }
            return access.getData().getValidarToken();

        } catch (Exception e) {
            throw new CommonException(e.getMessage(), e);
        }
    }

    /**
     *
     * Verifica se a authorization ou token informado pertence a um administrador do sistema
     *
     * @param authorization
     * @param token
     * @throws CommonException
     */
    protected final void restritoAdminstrador(String authorization, String token) throws CommonException {
        restritoAdminstrador(validarTokenAccess(authorization, token));
    }

    /**
     * Verifica se o usuário informado e um administrador do sistema
     *
     * @apiNote: Caso o usuário informado seja inválido ou não for um administrador uma CommonException será lancada.
     *
     * @param usuario
     * @throws CommonException
     */
    protected final void restritoAdminstrador(ValidarToken usuario) throws CommonException {
        if (usuario == null) {
            throw new CommonException("common.resource.usuario.null");
        }
        if (!PERFIL_TYPE_ADMINSTRADOR.equals(usuario.getPerfilType())) {
            throw new CommonException("common.resource.usuario.administrador");
        }
    }

}
