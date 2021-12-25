package enUcuz.market;

import enUcuz.interfaces.IScrappable;
import enUcuz.model.Product;
import enUcuz.util.ProjectSettings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Carrefour implements IScrappable {
    private String  base_link = "https://www.carrefoursa.com/";
    private ArrayList<String> category_paths = new ArrayList<String>();
    private ArrayList<Elements> product_name_price_category = new ArrayList<Elements>();
    public ArrayList<Product> products = new ArrayList<Product>();
    int count_img = 0;
    int count_name =0;
    int count_price =0;
    int count_category=0;
    String market_name="Carrefour";
    public Carrefour() throws IOException {
        this.scrap();

    }

    public void scrap() throws IOException{

        Document doc = Jsoup.connect(base_link).get();
        Elements uls = doc.selectXpath("//*[@id=\"category-1\"]");
        Element cats = uls.first().getElementById("category-1");
        Elements lis = cats.children();

        int counter = 0;
        for(Element el : lis) {
            category_paths.add(el.child(0).attr("href"));
            if(++counter == 9) {
                break;
            }
        }

        ArrayList<String> page_paths = new ArrayList<String>();
        page_paths.add("/meyve-sebze/c/1014?q=%3Arelevance&page=1");


        for (int d=0; d<category_paths.size();d++){

            doc = Jsoup.connect(base_link+category_paths.get(d)).get();
            String default_page_path = category_paths.get(d)+"?q=%3Arelevance&page=1";
            page_paths.add(default_page_path);
            Elements page_div = doc.getElementsByClass("pagination-item");

            for(int page=0; page<page_div.size();page++){
                page_paths.add(page_div.get(page).getElementsByTag("a").attr("href"));
            }
        }


        for(int path_index=1;path_index<page_paths.size();path_index++){
            page_paths.remove("");
            doc = Jsoup.connect(base_link+page_paths.get(path_index)).get();

            Elements product_clicks = doc.getElementsByClass("product_click");
            //System.out.println(product_clicks);

            for(int k=0; k<product_clicks.size();k++){

                String product_category =  page_paths.get(path_index).split("/",4)[1];
                //System.out.println(product_category);
                count_category++;
                String product_name = product_clicks.get(k).getElementById("productNamePost").attr("value");
                count_name++;
                String product_price = product_clicks.get(k).getElementById("productPricePost").attr("value");
                count_price++;
                String product_image =product_clicks.get(k).getElementsByTag("img").attr("data-src");
                count_img++;

                //System.out.println(product_image);
                Product product = new Product(product_price,product_category,product_image,product_name,market_name);

                products.add(product);

                if(ProjectSettings.DEBUG){
                    System.out.println(product_image);
                    System.out.println(product_name);
                    System.out.println(product_price);
                }

            }

        }
        /*System.out.println(count_category);
        System.out.println(count_img);
        System.out.println(count_name);
        System.out.println(count_price);*/

    }
}
