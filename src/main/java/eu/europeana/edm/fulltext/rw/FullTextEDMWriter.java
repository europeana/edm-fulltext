/**
 * 
 */
package eu.europeana.edm.fulltext.rw;

import static org.apache.commons.lang3.StringEscapeUtils.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.edm.fulltext.Annotation;
import eu.europeana.edm.fulltext.FullTextPackage;
import eu.europeana.edm.fulltext.media.MediaReference;
import eu.europeana.edm.fulltext.text.FullTextResource;
import eu.europeana.edm.fulltext.text.TextReference;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 23 Jun 2018
 */
public class FullTextEDMWriter
{
    private static String MOTIVATION = "http://www.w3.org/ns/oa#transcribing";
    private static String CSS_STYLESHEET = "https://test-solr-mongo.eanadev.org/newspapers/fulltext/styles/fulltext.css";

    private boolean _printStyles = false;

    public FullTextEDMWriter() { this(false); }

    public FullTextEDMWriter(boolean printStyles) 
    { 
        _printStyles = printStyles; 
    }

    public void print(FullTextPackage page, PrintStream ps)
    {
        Map<String,String> entities = new LinkedHashMap();
        entities.put(MOTIVATION      , "motv");
        fillEntities(page, entities);
        if ( _printStyles ) { entities.put(CSS_STYLESHEET, "style"); }

        printHead(page.getID(), entities, ps);
        print(page.getText(), entities, ps);
        for ( Annotation anno : page ) { print(page, anno, entities, ps); }
        printFooter(ps);
        ps.flush();
    }

    private void fillEntities(FullTextPackage page, Map<String,String> entities)
    {
        Collection<String> urls = new HashSet();
        for ( Annotation a : page )
        {
            urls.add(a.getTextReference().getResourceURL());
            for ( MediaReference media : a.getTargets() )
            {
                urls.add(media.getResourceURL());
            }
        }

        int i = 0;
        for (String url : urls) { entities.put(url, "e" + (++i)); }
    }

    private void printHead(String baseUri, Map<String,String> entities
                         , PrintStream ps)
    {
        ps.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        ps.println("<!DOCTYPE RDF [");
        for ( Map.Entry<String,String> entry : entities.entrySet() )
        {
            ps.println("<!ENTITY " + entry.getValue()
                     + " \"" + escapeEntity(entry.getKey()) + "\" >");
        }
        ps.println("]>");
        
        ps.println("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"");
        ps.println("         xmlns:edm=\"http://www.europeana.eu/schemas/edm/\"");
        ps.println("         xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
        ps.println("         xmlns:oa=\"http://www.w3.org/ns/oa#\"");
        ps.println("         xmlns:nif=\"http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#\"");
        ps.println();
        if ( baseUri != null ) {
            ps.println("         xml:base=\"" + escapeString(baseUri) + "\"");
        }
        ps.println("         >");
    }

    private void printFooter(PrintStream ps)
    {
        ps.println();
        ps.println("</rdf:RDF>");
    }

    private void print(FullTextPackage page, Annotation anno
                     , Map<String,String> entities, PrintStream ps)
    {
        String uri = escapeString(anno.getID());

        ps.println();
        ps.println("<oa:Annotation rdf:ID=\"/" + uri + "\">");
        ps.println("\t<dc:type>" + anno.getAnnotationType().name() + "</dc:type>");

        if ( anno.hasConfidence() ) {
            ps.println("\t<nif:confidence>" + anno.getConfidence()
                     + "</nif:confidence>");
        }
        ps.println("\t<oa:motivatedBy rdf:resource=\"&motv;\"/>");

        /*
        if ( isStyleOverriden(page, anno.getStyle()) )
        {
            ps.println("\t<oa:styledBy rdf:resource=\"&style;\"/>");
        }
        */

        printBody(anno, entities, ps);
        printTarget(anno, entities, ps);

        ps.println("</oa:Annotation>");
    }

    private void printBody(Annotation anno
                         , Map<String,String> entities, PrintStream ps)
    {
        TextReference    tr  = anno.getTextReference();
        FullTextResource ftr = tr.getResource();

        String url = getAbbrUrl(ftr.getURL(), entities);
        if ( tr instanceof FullTextResource )
        {
            ps.println("\t<oa:hasBody rdf:resource=\"" + url + "\"/>");
            return;
        }

        String       uriBody = getAbbrUrl(tr, entities);

        String       lang    = anno.getLanguage();
      //TextStyle    style   = anno.getStyle();
        if ( !ftr.isLangOverriden(lang)/* && !isStyleOverriden(page, style)*/ )
        {
            ps.println("\t<oa:hasBody rdf:resource=\"" + uriBody + "\"/>");
            return;
        }

        ps.println("\t<oa:hasBody>");
        if ( tr == null ) { ps.println("\t\t<oa:SpecificResource>"); }
        else {
            ps.println("\t\t<oa:SpecificResource rdf:about=\"" + uriBody + "\">");
        }
        ps.println("\t\t\t<oa:hasSource rdf:resource=\"" + url + "\"/>");

        if ( ftr.isLangOverriden(lang) )
        {
            String langTxt = escapeString(lang);
            ps.println("\t\t\t<dc:language>" + langTxt + "</dc:language>");
        }

      //if ( isStyleOverriden(page, style) ) { printStyle(style, ps); }

        ps.println("\t\t</oa:SpecificResource>");
        ps.println("\t</oa:hasBody>");
    }

    private void printTarget(Annotation anno, Map<String,String> entities
                           , PrintStream ps)
    {
        for ( MediaReference mr : anno.getTargets() )
        {
            String url = getAbbrUrl(mr, entities);
            //String uriTarget = escapeString(mr.getFragment());
            ps.println("\t<oa:hasTarget rdf:resource=\"" + url + "\"/>");
        }
    }

    /*
    private void printStyle(TextStyle style, PrintStream ps)
    {
        Float size = style.getSize();
        if ( size != null )
        {
            int s = size.intValue();
            ps.println("\t\t\t<oa:styleClass>size-" + s + "</oa:styleClass>");
        }

        for ( TextType type : style.getTypes() )
        {
            ps.println("\t\t\t<oa:styleClass>" + type.name()
                     + "</oa:styleClass>");
        }
    }

    private boolean isStyleOverriden(AnnotationPage page, TextStyle style)
    {
        return (_printStyles && (style != null)
            && ((style.getSize() != null) || !style.getTypes().isEmpty()));
    }
    */

    private void print(FullTextResource r, Map<String,String> entities, PrintStream ps)
    {
        String text = escapeString(r.getString()).replaceAll("\\n", "&#xA;");
        String url  = getAbbrUrl(r.getURL(), entities);

        ps.println();
        ps.println("<edm:FullTextResource rdf:about=\"" + url + "\">");
        if ( r.hasLanguage() )
        {
            ps.println("\t<dc:language>" + escapeString(r.getLanguage())
                     + "</dc:language>");
        }
        ps.println("\t<edm:rights rdf:resource=\"" + r.getRights() + "\"/>");
        ps.println("\t<dc:source rdf:resource=\"" + r.getRecordURI() + "\"/>");
        ps.println("\t<rdf:value>" + text + "</rdf:value>");
        ps.println("</edm:FullTextResource>");
    }

    private String getAbbrUrl(String url, Map<String,String> entities)
    {
        String abrv = entities.get(url);
        return ( abrv == null ? url : ("&" + abrv + ";") );
    }

    private String getAbbrUrl(MediaReference mr, Map<String,String> entities)
    {
        String rurl = mr.getResourceURL();
        String abbv = getAbbrUrl(rurl, entities);
        String url  = mr.getURL();
        if ( !rurl.equals(url) ) { return url.replace(rurl, abbv); }
        return abbv;
    }

    private String getAbbrUrl(TextReference tr, Map<String,String> entities)
    {
        String rurl = tr.getResourceURL();
        String abbv = getAbbrUrl(rurl, entities);
        String url  = tr.getURL();
        if ( !rurl.equals(url) ) { return url.replace(rurl, abbv); }
        return abbv;
    }

    private static String escapeString(String str)
    {
        return escapeXml11(str);
    }

    private static String escapeEntity(String str)
    {
        return escapeString(str).replaceAll("%([A-Z0-9]{2,4})", "&#037;$1");
    }
    

    public static final void main(String args[])
    {
        String str = "https://iiif.europeana.eu/image/LMQDDTFVCT26KESMGA5NUQGH4LI4KRJDYI2XCIP6KJJATD3GOM7A/presentation_images/6fd68c50-0211-11e6-a696-fa163e2dd531/node-1/image/NLP/Orędownik_%5BIlustrowany_Dziennik_Narodowy_i_Katolicki%5D/1939/09/01/0001/full/full/0/default.jpg";
        System.out.println(escapeEntity(str));
    }
    
}
