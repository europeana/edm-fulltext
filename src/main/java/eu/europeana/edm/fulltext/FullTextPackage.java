/**
 * 
 */
package eu.europeana.edm.fulltext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import eu.europeana.edm.fulltext.text.FullTextResource;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 5 Apr 2018
 */
public class FullTextPackage extends ArrayList<Annotation>
{
    private String           _id = null;
    private FullTextResource _resource;

    public FullTextPackage(String id, FullTextResource resource)
    {
        _resource = resource;
        _id       = id;
    }

    public FullTextResource getText() { return _resource; }
    public String getID()             { return _id;       }

    public void setText(FullTextResource text) { _resource = text; }
    public void setID(String id)               { _id       = id;   }

    public boolean isEmpty()
    {
        return _resource.getString().isEmpty();
    }

    public boolean isLangOverriden(String lang)
    {
        if ( lang  == null ) { return false; }
        return !lang.equals(_resource.getLanguage());
    }
}
