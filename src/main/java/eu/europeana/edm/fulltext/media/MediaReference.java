package eu.europeana.edm.fulltext.media;

/**
 * @author hugom
 * @since May 31, 2019
 */
public interface MediaReference {
    public String getResourceURL();

    public String getURL();

    public void visit(MediaReferenceVisitor visitor);
}