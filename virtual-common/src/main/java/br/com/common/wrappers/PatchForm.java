package br.com.common.wrappers;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 3 de nov de 2018
 */
public class PatchForm {

    // ADD, REMOVE, REPLACE, MOVE, COPY, TEST
    private String op;
    private String path;
    private String value;

    public PatchForm() {
        super();
    }

    public PatchForm(String op, String path, String value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
