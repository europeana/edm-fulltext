/**
 * 
 */
package eu.europeana.fulltext.alto.model;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 20 Dec 2018
 */
public interface LanguageElement extends TextElement
{
    public String getLanguage();
    public void   setLanguage(String lang);
}
