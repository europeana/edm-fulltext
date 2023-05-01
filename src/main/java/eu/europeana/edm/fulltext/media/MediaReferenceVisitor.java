package eu.europeana.edm.fulltext.media;

/**
 * @author Hugo
 * @since 4 Apr 2023
 */
public interface MediaReferenceVisitor {
    public void visit(ImageBoundary img);

    public void visit(TimeBoundary time);

    public void visit(MediaResource media);
}
