package br.com.common.access.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 11 de ago de 2019
 * @version $
 */
@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "message",
        "locations",
        "path"
})
public class Error implements Serializable {

    @JsonProperty("message")
    private String message;

    @JsonProperty("locations")
    private List<Location> locations;

    @JsonProperty("path")
    private List<String> path;

    public Error() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Location> getLocations() {
        if (locations == null) {
            locations = new ArrayList<>();
        }
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<String> getPath() {
        if (path == null) {
            path = new ArrayList<>();
        }
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

}
