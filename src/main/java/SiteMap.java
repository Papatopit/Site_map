import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class SiteMap {
    private volatile SiteMap parent;
    private volatile int depth;
    private final String url;
    private final CopyOnWriteArrayList<SiteMap> children;

    public SiteMap(String url){
        parent = null;
        depth = 0;
        this.url = url;
        children = new CopyOnWriteArrayList<>();
    }
    public String getUrl() {
        return url;
    }

    public CopyOnWriteArrayList<SiteMap> getChildren() {
        return children;
    }

    public int calculateDepth(){
        int result = 0;
        if (parent == null){
            return result;
        }
        result = 1 + parent.calculateDepth();
        return result;
    }

    public void setParent(SiteMap siteMap) {
        synchronized (this){
        this.parent = siteMap;
        this.depth = calculateDepth();
        }
    }

    public SiteMap getRootElement(){
        return parent == null ? this : parent.getRootElement();
    }

    public void addCildrenElement (SiteMap element){
        SiteMap root = getRootElement();
        if (!root.contains(element.getUrl())){
            element.setParent(this);
            children.add(element);
        }
    }
    public boolean contains (String url){
        if (this.url.equals(url)){
            return true;
        }
        for (SiteMap child : children){
            if (child.contains(url)){
                return true;
            }
        }
        return false;
    }
}
