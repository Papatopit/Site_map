import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class SiteMapRecursiveAction extends RecursiveAction {

    private final SiteMap node;
    private static final Logger LOG = LogManager.getLogger("Logger");

    public SiteMapRecursiveAction(SiteMap node){
        this.node = node;
    }

    @Override
    protected void compute() {

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOG.info(e);
            }

        Document page;
        try {
            Connection connection = Jsoup.connect(node.getUrl()).timeout(10000);
            page = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info(e);
            return;

        }
        Elements elements = page.select("body").select("a");
            for (Element e: elements){
                String childURL = e.absUrl("href");
                if (isCorrectURL(childURL)){
                    childURL = stripParametrs(childURL);
                    node.addCildrenElement(new SiteMap(childURL));
                }
            }

        for (SiteMap child: node.getChildren()){
            SiteMapRecursiveAction task = new SiteMapRecursiveAction(child);
           task.fork();
           task.join();

        }
    }

    private boolean isCorrectURL (String url){
        Pattern patternRoot = Pattern.compile(node.getUrl());
        Pattern noFile = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|pdf))$)");
        Pattern noAnchor = Pattern.compile("#([\\w\\-]+)?$");

        return patternRoot.matcher(url).lookingAt() && !noFile.matcher(url).find() && !noAnchor.matcher(url).find();
    }
    private String stripParametrs (String url){
        return url.replaceAll("\\?.+","");
    }
}

