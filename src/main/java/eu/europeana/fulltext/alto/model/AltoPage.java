/**
 * 
 */
package eu.europeana.fulltext.alto.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.europeana.edm.fulltext.media.ImageDimension;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public class AltoPage extends TextNode<TextBlock> implements StyledTextElement
{
    private Map<String,TextStyle> _styles = new HashMap();
    private Float                 _confidence;
    private Float                 _accuracy;
    private TextStyle             _style;
    private ImageDimension        _dim;

    public AltoPage() {}

    public void addStyle(String id, TextStyle style) { _styles.put(id, style); }

    public TextStyle             getStyle(String id) { return _styles.get(id); }
    public TextStyle             getStyle()          { return _style;          }
    public Collection<TextStyle> getStyles()         { return _styles.values();}
    public Float                 getConfidence()     { return _confidence;     }
    public Float                 getAccuracy()       { return _accuracy;       }
    public ImageDimension        getDimension()      { return _dim;            }

    public boolean hasConfidence() { return (_confidence != null); }

    public void setStyle(TextStyle style)        { _style = style;           }
    public void setConfidence(Float confidence)  { _confidence = confidence; }
    public void setAccuracy(Float accuracy)      { _accuracy = accuracy;     }
    public void setDimension(ImageDimension dim) { _dim = dim;               }


    public void visit(AltoVisitor visitor) { visitor.visit(this); }
}
