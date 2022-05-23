import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;


public class Main {

    private static final String ROOT_SITE = "https://skillbox.ru/";
    private static final int numOfThread = Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) throws IOException {

        SiteMap siteMapRoot = new SiteMap(ROOT_SITE);
        ForkJoinPool fj = new ForkJoinPool(numOfThread);
        System.out.println(fj.invoke(new SiteMapRecursiveAction(siteMapRoot)));

//        try {
//            fj.awaitTermination(1, TimeUnit.HOURS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        FileOutputStream stream = new FileOutputStream("src/main/resources/sitemap.txt");
//        String result = createSiteMapString(siteMapRoot, 0);
//        stream.write(result.getBytes());
//        stream.flush();
//        stream.close();
    }

    public static String createSiteMapString(SiteMap node, int depth) {
        String tabs = String.join("", Collections.nCopies(depth, " "));
        StringBuilder result = new StringBuilder(tabs + node.getUrl());
        node.getChildren().forEach(child ->
                result.append("\n").append(createSiteMapString(child, depth + 2)));

        return result.toString();
    }
}
