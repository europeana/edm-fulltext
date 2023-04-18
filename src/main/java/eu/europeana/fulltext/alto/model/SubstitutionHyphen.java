/**
 * 
 */
package eu.europeana.fulltext.alto.model;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public class SubstitutionHyphen
{
    private String     _subsText;
    private TextString   _w1;
    private TextString   _w2;
    private TextHyphen _hyphen;

    public SubstitutionHyphen(String subsText)
    {
        _subsText = subsText;
    }

    public String     getSubsText() { return _subsText; }
    public TextString   getWord1()    { return _w1;       }
    public TextString   getWord2()    { return _w2;       }
    public TextHyphen getHyphen()   { return _hyphen;   }

    public void setWord1(TextString w)    { _w1 = w;     }
    public void setWord2(TextString w)    { _w2 = w;     }
    public void setHyphen(TextHyphen h) { _hyphen = h; }
}
