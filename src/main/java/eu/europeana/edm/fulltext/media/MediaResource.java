package eu.europeana.edm.fulltext.media;

/**
 * @author hugom
 * @since May 31, 2019
 */
public class MediaResource implements MediaReference {
    public String _url;

    public MediaResource(String url) {
        _url = url;
    }

    public void setURL(String url) {
        _url = url;
    }

    public String getURL() {
        return _url;
    }

    public String getResourceURL() {
        return _url;
    }


    @Override
    public void visit(MediaReferenceVisitor visitor) {
        visitor.visit(this);
    }
}
