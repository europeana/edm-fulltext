/**
 * 
 */
package eu.europeana.edm.fulltext;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.edm.fulltext.media.ImageBoundary;
import eu.europeana.edm.fulltext.media.MediaReference;
import eu.europeana.edm.fulltext.text.TextBoundary;
import eu.europeana.edm.fulltext.text.TextReference;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public class Annotation
{
    private String                    _id;
    private TextReference             _tr;
    private ArrayList<MediaReference> _targets = new ArrayList(1);
    private AnnotationType            _type;
    private String                    _lang;
    private Float                     _confidence;
//    private TextStyle           _style;

    public Annotation(String id, TextReference tr, MediaReference target
                    , AnnotationType type, String lang
                    , Float confidence)
    {
        _tr         = tr;
        if ( target != null ) { _targets.add(target); }
        _type       = type;
        _lang       = lang;
        _confidence = confidence;
        _id         = id;
    }

    public Annotation(String id, TextReference tr
                    , MediaReference target1, MediaReference target2
                    , AnnotationType type, String lang
                    , Float confidence)
    {
        _tr         = tr;
        _targets.ensureCapacity(2);
        if ( target1 != null ) { _targets.add(target1); }
        if ( target2 != null ) { _targets.add(target2); }
        _type       = type;
        _lang       = lang;
        _confidence = confidence;
        _id         = id;
    }

    public String               getID()             { return _id;              }
    public TextReference        getTextReference()  { return _tr;              }
    public List<MediaReference> getTargets()        { return _targets;         }
    public AnnotationType       getAnnotationType() { return _type;            }
    public Float                getConfidence()     { return _confidence;      }
    public String               getLanguage()       { return _lang;            }

    public void setID(String id)       { _id = id; }
    public void setConfidence(Float c) { _confidence = c; }

    public boolean hasConfidence()   { return _confidence != null; }
    public boolean hasTargets()      { return !_targets.isEmpty(); }
}

