package br.com.slave.resource;

import static br.com.slave.resource.SwaggerUIResource.RESOURCE_ROOT_URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.stereotype.Component;
import org.wso2.msf4j.Request;

import br.com.common.utils.Utils;
import br.com.slave.configuration.SlaveException;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de out de 2018
 */
@Component
@Path(RESOURCE_ROOT_URL)
public class SwaggerUIResource {

    public static final String RESOURCE_ROOT_URL = "/swagger-ui.html";

    @GET
    @Path("/")
    @Produces({ MediaType.TEXT_HTML })
    public String documentacao(@Context Request request) {

        String url = String.format("%s://%s", request.getProperties().get("PROTOCOL"), request.getProperties().get("listener.interface.id"));
        String json = url + "/swagger?path=" + VolumeResource.RESOURCE_ROOT_URL;

        StringBuilder html = new StringBuilder();
        html.append(" <!DOCTYPE html>                                                                                                           ");
        html.append(" <html lang=\"en\">                                                                                                        ");
        html.append("    <head>                                                                                                                 ");
        html.append("       <meta charset=\"UTF-8\">                                                                                            ");
        html.append("       <title>Swagger UI</title>                                                                                           ");
        html.append("       <link rel=\"stylesheet\" type=\"text/css\" href=\"").append(url).append(RESOURCE_ROOT_URL)
                .append("/resources/swagger-ui.css\" >                    ");
        html.append("       <link rel=\"icon\" type=\"image/png\" href=\"").append(url).append(RESOURCE_ROOT_URL)
                .append("/resources/favicon-32x32.png\" sizes=\"32x32\" />     ");
        html.append("       <link rel=\"icon\" type=\"image/png\" href=\"").append(url).append(RESOURCE_ROOT_URL)
                .append("/resources/favicon-16x16.png\" sizes=\"16x16\" />     ");
        html.append("       <style>                                                                                                             ");
        html.append("          html {                                                                                                           ");
        html.append("              box-sizing: border-box;                                                                                      ");
        html.append("              overflow: -moz-scrollbars-vertical;                                                                          ");
        html.append("              overflow-y: scroll;                                                                                          ");
        html.append("          }                                                                                                                ");
        html.append("          *, *:before, *:after {                                                                                           ");
        html.append("              box-sizing: inherit;                                                                                         ");
        html.append("          }                                                                                                                ");
        html.append("          body {                                                                                                           ");
        html.append("              margin:0;                                                                                                    ");
        html.append("              background: #fafafa;                                                                                         ");
        html.append("          }                                                                                                              ");
        html.append("       </style>                                                                                                            ");
        html.append("    </head>                                                                                                                ");
        html.append("    <body>                                                                                                                 ");
        html.append("       <div id=\"swagger-ui\"></div>                                                                                       ");
        html.append("       <script src=\"").append(url).append(RESOURCE_ROOT_URL).append("/resources/swagger-ui-bundle.js\"> </script>         ");
        html.append("       <script src=\"").append(url).append(RESOURCE_ROOT_URL).append("/resources/swagger-ui-standalone-preset.js\"> </script> ");
        html.append("       <script>                                                                                                            ");
        html.append("          window.onload = function() {                                                                                     ");
        html.append("              const ui = SwaggerUIBundle({                                                                               \n");
        html.append("              url: \"").append(json).append("\",                                                                          \n");
        html.append("              enableCORS: false,                                                                                         \n");
        html.append("              dom_id: '#swagger-ui',                                                                                     \n");
        html.append("              deepLinking: true,                                                                                         \n");
        html.append("              presets: [                                                                                                 \n");
        html.append("                SwaggerUIBundle.presets.apis,                                                                            \n");
        html.append("                SwaggerUIStandalonePreset                                                                                \n");
        html.append("              ],                                                                                                         \n");
        html.append("              plugins: [                                                                                                 \n");
        html.append("                SwaggerUIBundle.plugins.DownloadUrl                                                                      \n");
        html.append("              ],                                                                                                         \n");
        html.append("              layout: \"StandaloneLayout\"                                                                               \n");
        html.append("            })                                                                                                           \n");
        html.append("            window.ui = ui                                                                                               \n");
        html.append("          }                                                                                                              \n");
        html.append("       </script>                                                                                                           ");
        html.append("    </body>                                                                                                                ");
        html.append(" </html>                                                                                                                   ");
        return html.toString();
    }

    @GET
    @Path("/resources/{nome}")
    public Response resources(@PathParam("nome") String nome) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            StreamingOutput stream = Utils.fileLer(classLoader.getResource("swagger_ui/" + nome).openStream());
            return Response.status(Response.Status.OK).entity(stream).build();
        } catch (Exception e) {
            throw new SlaveException(e.getMessage(), e);
        }
    }

}
