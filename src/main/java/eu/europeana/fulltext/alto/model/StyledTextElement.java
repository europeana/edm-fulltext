/**
 * 
 */
package eu.europeana.fulltext.alto.model;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public interface StyledTextElement extends TextElement
{
    public TextStyle getStyle();

    public void setStyle(TextStyle style);
}
