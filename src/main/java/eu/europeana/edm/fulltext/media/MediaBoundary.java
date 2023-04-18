/**
 * 
 */
package eu.europeana.edm.fulltext.media;

/**
 * @author hugom
 * @since May 31, 2019
 */
public abstract class MediaBoundary implements MediaReference
{
    public MediaReference _ref;

    public MediaBoundary(MediaReference ref) { _ref = ref; }


    public void           setMediaReference(MediaReference ref) { _ref = ref;  }
    public MediaReference getMediaReference()                   { return _ref; }

    public String getURL()         { return _ref.getURL() + getFragment();  }
    public String getResourceURL() { return _ref.getResourceURL();          }

    public abstract String getFragment();
}
