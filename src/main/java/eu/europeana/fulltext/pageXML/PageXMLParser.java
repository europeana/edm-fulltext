/**
 * 
 */
package eu.europeana.fulltext.pageXML;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

import eu.europeana.edm.fulltext.media.MediaReference;
import eu.europeana.fulltext.alto.model.AltoPage;
import eu.europeana.fulltext.alto.parser.AltoParser;

/**
 * @author Hugo
 * @since 4 Apr 2023
 */
public class PageXMLParser extends AltoParser
{
    private Transformer _transformer;
    private static String XSLT_PATH = "etc/PageToAlto.xsl";

    public PageXMLParser() throws TransformerConfigurationException
    {
        InputStream is = ClassLoader.getSystemClassLoader()
                                    .getResourceAsStream(XSLT_PATH);
        if ( is == null ) { is = this.getClass().getResourceAsStream(XSLT_PATH); }
        TransformerFactory tf = TransformerFactory.newInstance();
        _transformer = tf.newTransformer(new StreamSource(is));
    }

    public AltoPage processPage(InputSource source, MediaReference ref)
    {
        return processPage(new StreamSource(source.getByteStream()), ref);
    }

    public AltoPage processPage(Source source, MediaReference ref) 
    {
        try {
            DOMResult result = new DOMResult();
            _transformer.transform(source, result);
            return super.processPage(new DOMSource(result.getNode()), ref);
        }
        catch(TransformerException e) { throw new RuntimeException(e); }
    }
}