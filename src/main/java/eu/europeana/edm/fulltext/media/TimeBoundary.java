/**
 * 
 */
package eu.europeana.edm.fulltext.media;

import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 17 Jun 2018
 */
public class TimeBoundary extends MediaBoundary
{
    private static String FORMAT = "HH:mm:ss.SSS";

    public int start;
    public int end;

    public TimeBoundary(MediaReference ref, int start, int end)
    {
        super(ref);
        this.start = start; this.end = end;
    }

    public String getFragment()
    {
        String start = DurationFormatUtils.formatDuration(this.start, FORMAT);
        String end   = DurationFormatUtils.formatDuration(this.end  , FORMAT);
        return ("#t=" + start + "," + end);
    }

    public boolean isValid()
    {
        return ( this.start >= 0 && this.end >= 0 && this.start < this.end );
    }

    @Override
    public void visit(MediaReferenceVisitor visitor)
    {
        visitor.visit(this);
    }
}