package eu.europeana.edm.fulltext.ocr;

import eu.europeana.edm.fulltext.Annotation;
import eu.europeana.edm.fulltext.AnnotationType;
import eu.europeana.edm.fulltext.FullTextPackage;
import eu.europeana.edm.fulltext.media.ImageDimension;
import eu.europeana.edm.fulltext.media.MediaReference;
import eu.europeana.fulltext.alto.model.AltoPage;
import eu.europeana.fulltext.resize.IIIFImageInfoSupport;
import eu.europeana.fulltext.resize.ImageScale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 22 Jun 2018
 */
public class Alto2EDMTranslator {
    private static final Logger LOG = LogManager.getLogger(Alto2EDMTranslator.class);

    private static final String ERR_IMG = "Error obtaining image size from URL: ";
    private static final String ERR_ALTO = "Error obtaining image size from ALTO: ";
    private static final String INFO_SAME = "Image and Alto sizes are the same, skipping: ";
    private static final String ERR_MIS_COORD = "Annotation is missing coordinates, skipping: ";

    private boolean _resize = true;
    private IIIFImageInfoSupport _support = null;

    public Alto2EDMTranslator() {
        this(false);
    }

    public Alto2EDMTranslator(boolean resize) {
        this(resize, new IIIFImageInfoSupport());
    }

    public Alto2EDMTranslator(boolean resize, IIIFImageInfoSupport support) {
        _resize = resize;
        _support = support;
    }

    public FullTextPackage processPage(AltoPage altoPage, MediaReference ref) {
        AnnotationsGenerator gen = new AnnotationsGenerator();
        FullTextPackage fulltext = gen.process(altoPage, ref);
        if (!_resize) {
            return fulltext;
        }

        String imgURL = ref.getResourceURL();
        ImageDimension d1 = _support.getImageSize(imgURL);
        ImageDimension d2 = altoPage.getDimension();
        if (d1 == null) {
            LOG.error(" {} {} ", ERR_IMG, imgURL);
            return fulltext;
        }
        if (d2 == null) {
            LOG.error("{} {} ", ERR_ALTO, imgURL);
            return fulltext;
        }
        if (d1.isEquals(d2)) {
            LOG.error(" {} {} ", INFO_SAME , imgURL);
            return fulltext;
        }

        ImageScale scale = ImageScale.project(d2, d1);

        Iterator<Annotation> iter = fulltext.iterator();
        while (iter.hasNext()) {
            Annotation anno = iter.next();

            if (anno.hasTargets()) {
                applyScale(anno, scale);
                continue;
            }

            if (anno.getAnnotationType() == AnnotationType.Page) {
                continue;
            }

            LOG.error("{} : {}", ERR_MIS_COORD, imgURL);
            iter.remove();
        }

        return fulltext;
    }

    private void applyScale(Annotation anno, ImageScale scale) {
        for (MediaReference ref : anno.getTargets()) {
            ref.visit(scale);
        }
    }
}
