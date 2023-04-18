/**
 * 
 */
package eu.europeana.edm.fulltext.media;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 17 Jun 2018
 */
public class ImageBoundary extends MediaBoundary
{
    public int x;
    public int y;
    public int w;
    public int h;

    public ImageBoundary(MediaReference ref, int x, int y, int w, int h)
    {
        super(ref);
        this.x = x; this.y = y; this.w = w; this.h = h;
    }

    @Override
    public String getFragment()
    {
        return ("#xywh=" + x + "," + y + "," + w + "," + h);
    }

    //clip positions outside image boundaries
    public ImageBoundary clip(ImageDimension d)
    {
        //left boundary
        if ( this.x < 0 ) { this.w += this.x; this.x = 0; }
        //top boundary
        if ( this.y < 0 ) { this.h += this.y; this.y = 0; }

        //right boundary
        int x = this.x + this.w;
        if ( x > d.w ) { this.w = this.w - (x - d.w); }

        //bottom boundary
        int y = this.y + this.h;
        if ( y > d.h ) { this.h = this.h - (y - d.h); }

        return this;
    }

    public boolean isValid()
    {
        return ( this.x >= 0 && this.y >= 0 && this.w > 0 && this.h > 0 );
    }

    @Override
    public void visit(MediaReferenceVisitor visitor)
    {
        visitor.visit(this);
    }
}