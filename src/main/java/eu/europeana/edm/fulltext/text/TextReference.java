/**
 * 
 */
package eu.europeana.edm.fulltext.text;

/**
 * @author hugom
 * @since May 31, 2019
 */
public interface TextReference
{
    public FullTextResource getResource();

    public String getResourceURL();

    public String getURL();
}
