/**
 * 
 */
package eu.europeana.edm.fulltext.ocr;

import static eu.europeana.fulltext.alto.parser.EDMFullTextUtils.*;

import eu.europeana.edm.fulltext.Annotation;
import eu.europeana.edm.fulltext.AnnotationType;
import eu.europeana.edm.fulltext.FullTextPackage;
import eu.europeana.edm.fulltext.media.MediaReference;
import eu.europeana.edm.fulltext.text.FullTextResource;
import eu.europeana.edm.fulltext.text.TextBoundary;
import eu.europeana.fulltext.alto.model.AbsAltoVisitor;
import eu.europeana.fulltext.alto.model.AltoPage;
import eu.europeana.fulltext.alto.model.SubstitutionHyphen;
import eu.europeana.fulltext.alto.model.TextBlock;
import eu.europeana.fulltext.alto.model.TextHyphen;
import eu.europeana.fulltext.alto.model.TextLine;
import eu.europeana.fulltext.alto.model.TextSpace;
import eu.europeana.fulltext.alto.model.TextString;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public class AnnotationsGenerator extends AbsAltoVisitor
{
    private StringBuilder        _sb      = new StringBuilder(100 * 1024);
    private FullTextPackage      _page;

    public AnnotationsGenerator() {}

    /***************************************************************************
     * Public Methods
     **************************************************************************/

    public synchronized FullTextPackage process(AltoPage altoPage
                                              , MediaReference ref)
    {
        try
        {
            FullTextResource text = new FullTextResource();
            _page = new FullTextPackage(null, text);
            _page.add(new Annotation(null, text, ref, AnnotationType.Page, null, null));
            visit(altoPage);
            text.setString(_sb.toString());
            text.setLanguage(altoPage.getLanguage());
            return _page;
        }
        finally { _sb.setLength(0); }
    }

    /***************************************************************************
     * Interface AltoVisitor
     **************************************************************************/

    public void visit(TextBlock block)
    {
        if ( hasText() ) { newLine(); }

        int s = _sb.length();
        super.visit(block);
        TextBoundary tb = newTextBoundary(_page.getText(), s, _sb.length());

        if ( !block.hasLines() ) { return; }

        _page.add(new Annotation(null, tb, block.getImageBoundary()
                               , AnnotationType.Block, block.getLanguage()
                               , null));
    }

    public void visit(TextLine line)
    {
        if ( !endsWith(' ', '-', '\n') && hasText() ) { newSpace(); }

        int s = _sb.length();
        super.visit(line);
        TextBoundary tb = newTextBoundary(_page.getText(), s, _sb.length());
        _page.add(new Annotation(null, tb, line.getImageBoundary()
                               , AnnotationType.Line, line.getLanguage()
                               , null));
    }

    public void visit(TextString word)
    {
        int s = _sb.length();
        if ( !word.hasSubs() || word.getSubs().getWord2() == null )
        {
            //TODO: Check whether it is a word or line (check for the existance of a space but only if there is only one TextString in the TextLine)
            _sb.append(word.getText());
            TextBoundary tb = newTextBoundary(_page.getText(), s, _sb.length());
            _page.add(new Annotation(null, tb, word.getImageBoundary()
                                   , AnnotationType.Word, word.getLanguage()
                                   , word.getConfidence()));
            return;
        }

        SubstitutionHyphen subs  = word.getSubs();
        TextString         word2 = subs.getWord2();
        if ( word2 == word ) { return; }

        _sb.append(subs.getSubsText());
        TextBoundary tb = newTextBoundary(_page.getText(), s, _sb.length());

        Float confidence = getConfidence(word.getConfidence()
                                       , word2.getConfidence());

        _page.add(new Annotation(null, tb
                               , word.getImageBoundary()
                               , word2.getImageBoundary(), AnnotationType.Word
                               , word.getLanguage(), confidence));
    }

    public void visit(TextSpace space) { newSpace(); }

    public void visit(TextHyphen hyphen) {}


    /***************************************************************************
     * Private Methods
     **************************************************************************/

    private Float getConfidence(Float c1, Float c2)
    {
        if ( c1 == null ) { return c2; }
        if ( c2 == null ) { return c1; }
        return ((c1 + c2) / 2f);
    }

    private boolean endsWith(char... chars)
    {
        int len = _sb.length();
        if ( len <= 0 ) { return false; }

        int c1 = _sb.charAt(len-1);
        for ( char c2 : chars )
        {
            if ( c1 == c2 ) { return true; }
        }
        return false;
    }

    private boolean hasText() { return (_sb.length() > 0); }

    private void newSpace() { _sb.append(' '); }

    private void newLine()  { _sb.append('\n'); }
}
