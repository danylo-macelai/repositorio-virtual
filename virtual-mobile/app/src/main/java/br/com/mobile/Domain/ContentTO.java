package br.com.mobile.Domain;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 02/12/2019 > _@version $$ >
 */
public class ContentTO implements Serializable {
    @SerializedName("content")
    private List<ArquivoTO> ListArchives;

    public List<ArquivoTO> getListArchives() {
        return ListArchives;
    }

    public void setListArchives(List<ArquivoTO> listArchives) {
        ListArchives = listArchives;
    }
}
