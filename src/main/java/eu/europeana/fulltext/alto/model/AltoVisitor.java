/**
 * 
 */
package eu.europeana.fulltext.alto.model;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public interface AltoVisitor
{
    public void visit(AltoPage page);

    public void visit(TextStyle style);

    public void visit(TextBlock block);

    public void visit(TextLine line);

    public void visit(TextString word);

    public void visit(TextSpace space);

    public void visit(TextHyphen hyphen);
}
