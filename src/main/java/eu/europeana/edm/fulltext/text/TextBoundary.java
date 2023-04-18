/**
 * 
 */
package eu.europeana.edm.fulltext.text;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 17 Jun 2018
 */
public class TextBoundary implements TextReference
{
    private TextReference _ref;
    public int s;
    public int e;

    public TextBoundary(TextReference ref, int s, int e)
    { 
        _ref = ref;
        this.s = s; this.e = e; 
    }

    public String getFragment()
    {
        return ("#char=" + s + "," + e);
    }

    public void shift(int chars)
    {
        this.s += chars;
        this.e += chars;
    }

    public FullTextResource getResource() { return _ref.getResource();    }
    public String getResourceURL()        { return _ref.getResourceURL(); }

    public String getURL()
    {
        return _ref.getURL() + getFragment();
    }
}

