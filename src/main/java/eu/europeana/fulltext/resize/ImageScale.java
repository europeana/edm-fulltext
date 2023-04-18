/**
 * 
 */
package eu.europeana.fulltext.resize;

import eu.europeana.edm.fulltext.media.ImageBoundary;
import eu.europeana.edm.fulltext.media.ImageDimension;
import eu.europeana.edm.fulltext.media.MediaReferenceVisitor;
import eu.europeana.edm.fulltext.media.MediaResource;
import eu.europeana.edm.fulltext.media.TimeBoundary;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 17 Jun 2018
 */
public class ImageScale implements MediaReferenceVisitor
{
    public static ImageScale project(ImageDimension d1, ImageDimension d2)
    {
        return new ImageScale((float)d2.w / d1.w, (float)d2.h / d1.h);
    }

    public float x;
    public float y;

    public ImageScale(float x, float y)
    {
        this.x = x; this.y = y;
    }

    @Override
    public void visit(ImageBoundary img)
    {
        if ( img == null ) { return; }

        img.h = Math.round(((float)img.h * this.y));
        img.y = Math.round(((float)img.y * this.y));
        img.w = Math.round(((float)img.w * this.x));
        img.x = Math.round(((float)img.x * this.x));
    }

    @Override
    public void visit(TimeBoundary time) {}

    @Override
    public void visit(MediaResource media) {}
}