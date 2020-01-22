package br.com.master.wrappers;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 8 de out de 2019
 * @version $
 */
public enum SearchTab {

    AUDIO, IMAGE, VIDEO, OTHERS;

    // ----------------------------------------------------------------
    // STATIC METHODS + MEMBERS
    // ----------------------------------------------------------------

    static final Map<String, SearchTab> name_lookup = new HashMap<>();

    static {
        for (SearchTab s : EnumSet.allOf(SearchTab.class)) {
            SearchTab.name_lookup.put(s.name().toLowerCase(), s);
        }

        // Sound Files MIME Type
        SearchTab.name_lookup.put("audio/basic", SearchTab.AUDIO);
        SearchTab.name_lookup.put("audio/mid", SearchTab.AUDIO);
        SearchTab.name_lookup.put("audio/mpeg", SearchTab.AUDIO);
        SearchTab.name_lookup.put("audio/x-aiff", SearchTab.AUDIO);
        SearchTab.name_lookup.put("audio/x-mpegurl", SearchTab.AUDIO);
        SearchTab.name_lookup.put("audio/x-pn-realaudio", SearchTab.AUDIO);
        SearchTab.name_lookup.put("audio/x-wav", SearchTab.AUDIO);

        // Sound Files File Extension
        SearchTab.name_lookup.put("au", SearchTab.AUDIO);
        SearchTab.name_lookup.put("snd", SearchTab.AUDIO);
        SearchTab.name_lookup.put("mid", SearchTab.AUDIO);
        SearchTab.name_lookup.put("rmi", SearchTab.AUDIO);
        SearchTab.name_lookup.put("mp3", SearchTab.AUDIO);
        SearchTab.name_lookup.put("aif", SearchTab.AUDIO);
        SearchTab.name_lookup.put("aifc", SearchTab.AUDIO);
        SearchTab.name_lookup.put("aiff", SearchTab.AUDIO);
        SearchTab.name_lookup.put("m3u", SearchTab.AUDIO);
        SearchTab.name_lookup.put("ra", SearchTab.AUDIO);
        SearchTab.name_lookup.put("ram", SearchTab.AUDIO);
        SearchTab.name_lookup.put("wav", SearchTab.AUDIO);

        // Image Files MIME Type
        SearchTab.name_lookup.put("image/bmp", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/cis-cod", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/gif", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/ief", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/jpeg", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/png", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/pipeg", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/svg+xml", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/tiff", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-cmu-raster", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-cmx", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-icon", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-portable-anymap", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-portable-bitmap", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-portable-graymap", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-portable-pixmap", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-rgb", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-xbitmap", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-xpixmap", SearchTab.IMAGE);
        SearchTab.name_lookup.put("image/x-xwindowdump", SearchTab.IMAGE);

        // Image Files File Extension
        SearchTab.name_lookup.put("bmp", SearchTab.IMAGE);
        SearchTab.name_lookup.put("cod", SearchTab.IMAGE);
        SearchTab.name_lookup.put("gif", SearchTab.IMAGE);
        SearchTab.name_lookup.put("ief", SearchTab.IMAGE);
        SearchTab.name_lookup.put("jpe", SearchTab.IMAGE);
        SearchTab.name_lookup.put("jpeg", SearchTab.IMAGE);
        SearchTab.name_lookup.put("jpg", SearchTab.IMAGE);
        SearchTab.name_lookup.put("jfif", SearchTab.IMAGE);
        SearchTab.name_lookup.put("svg", SearchTab.IMAGE);
        SearchTab.name_lookup.put("tif", SearchTab.IMAGE);
        SearchTab.name_lookup.put("tiff", SearchTab.IMAGE);
        SearchTab.name_lookup.put("ras", SearchTab.IMAGE);
        SearchTab.name_lookup.put("cmx", SearchTab.IMAGE);
        SearchTab.name_lookup.put("ico", SearchTab.IMAGE);
        SearchTab.name_lookup.put("pnm", SearchTab.IMAGE);
        SearchTab.name_lookup.put("pbm", SearchTab.IMAGE);
        SearchTab.name_lookup.put("pgm", SearchTab.IMAGE);
        SearchTab.name_lookup.put("png", SearchTab.IMAGE);
        SearchTab.name_lookup.put("ppm", SearchTab.IMAGE);
        SearchTab.name_lookup.put("rgb", SearchTab.IMAGE);
        SearchTab.name_lookup.put("xbm", SearchTab.IMAGE);
        SearchTab.name_lookup.put("xpm", SearchTab.IMAGE);
        SearchTab.name_lookup.put("xwd", SearchTab.IMAGE);

        // Video Files MIME Type
        SearchTab.name_lookup.put("video/mpeg", SearchTab.VIDEO);
        SearchTab.name_lookup.put("video/mp4", SearchTab.VIDEO);
        SearchTab.name_lookup.put("video/quicktime", SearchTab.VIDEO);
        SearchTab.name_lookup.put("video/x-la-asf", SearchTab.VIDEO);
        SearchTab.name_lookup.put("video/x-ms-asf", SearchTab.VIDEO);
        SearchTab.name_lookup.put("video/x-msvideo", SearchTab.VIDEO);
        SearchTab.name_lookup.put("video/x-sgi-movie", SearchTab.VIDEO);

        // Video Files File Extension
        SearchTab.name_lookup.put("mp2", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mpa", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mpe", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mpeg", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mpg", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mpv2", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mp4", SearchTab.VIDEO);
        SearchTab.name_lookup.put("mov", SearchTab.VIDEO);
        SearchTab.name_lookup.put("qt", SearchTab.VIDEO);
        SearchTab.name_lookup.put("lsf", SearchTab.VIDEO);
        SearchTab.name_lookup.put("lsx", SearchTab.VIDEO);
        SearchTab.name_lookup.put("asf", SearchTab.VIDEO);
        SearchTab.name_lookup.put("asr", SearchTab.VIDEO);
        SearchTab.name_lookup.put("asx", SearchTab.VIDEO);
        SearchTab.name_lookup.put("avi", SearchTab.VIDEO);
        SearchTab.name_lookup.put("movie", SearchTab.VIDEO);
    }

    public static SearchTab ofNullable(String name) {
        SearchTab value = of(name);
        if (value == null) {
            value = SearchTab.OTHERS;
        }
        return value;
    }

    public static SearchTab of(String name) {
        if (StringUtils.isNotEmpty(name)) {
            return SearchTab.name_lookup.get(name.toLowerCase());
        }
        return null;
    }

}
