package eu.europeana.edm.fulltext.text;


/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public class FullTextResource implements TextReference {
    private String _url = null;
    private String _str = null;
    private String _lang = null;
    private String _rights = null;
    private String _recordURI = null;

    public FullTextResource() {
        this(null, null, null, null, null);
    }

    public FullTextResource(String url, String str, String lang
            , String rights, String recordURI) {
        _url = url;
        _lang = lang;
        _str = str;
        _rights = rights;
        _recordURI = recordURI;
    }

    public String getString() {
        return _str;
    }

    public String getLanguage() {
        return _lang;
    }

    public String getURL() {
        return _url;
    }

    public String getResourceURL() {
        return _url;
    }

    public String getRights() {
        return _rights;
    }

    public String getRecordURI() {
        return _recordURI;
    }

    public FullTextResource getResource() {
        return this;
    }

    public void setString(String str) {
        _str = str;
    }

    public void setLanguage(String lang) {
        _lang = lang;
    }

    public void setURL(String url) {
        _url = url;
    }

    public void setRights(String rs) {
        _rights = rs;
    }

    public void setRecordURI(String rURI) {
        _recordURI = rURI;
    }


    public boolean hasLanguage() {
        return (_lang != null);
    }

    public boolean isLangOverriden(String lang) {
        if (lang == null) {
            return false;
        }
        return !lang.equals(_lang);
    }
}