package enUcuz.market;

import enUcuz.interfaces.IScrappable;
import enUcuz.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Caglarsoy implements IScrappable {
    String base_link = "https://www.caglarsoyonline.com";
    String default_page_path_str = "?tp=";
    ArrayList<String> main_category_paths = new ArrayList<String>();
    public ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<Elements> paginate_contents_array = new ArrayList<Elements>();
    ArrayList<String> max_pages = new ArrayList<String>();
    ArrayList<String> urls = new ArrayList<String>();
    Elements category_liss;
    int count_img = 0;
    int count_name =0;
    int count_price =0;
    int count_category=0;
    String market_name = "Caglarsoy";

    public Caglarsoy() throws IOException {
        this.scrap();
    }

    public void scrap() throws IOException{
        Document doc = Jsoup.connect(base_link).get();
        Elements sub_categoires = doc.getElementsByClass("has-sub-category");

        for (int i=0;i<sub_categoires.size();i++){
            if(sub_categoires.get(i).attr("data-selector").length()>0){
                String main_category = sub_categoires.get(i).getElementsByTag("a").first().attr("href");
                main_category_paths.add(main_category);
            }
        }

        for(int i=0; i< main_category_paths.size();i++){
            doc = Jsoup.connect(base_link+main_category_paths.get(i)).get();
            Elements paginate_content = doc.getElementsByClass("paginate-content");
            paginate_contents_array.add(paginate_content);
        }

        for(int i=0;i<paginate_contents_array.size();i++){
            max_pages.add(paginate_contents_array.get(i).get(0).children().last().html());
        }

        for(int i=0; i< main_category_paths.size();i++){
            for(int k=1; k<Integer.valueOf(max_pages.get(i)); k++){
                String url = base_link+main_category_paths.get(i)+default_page_path_str+String.valueOf(k);
                urls.add(url);
            }
        }

        for(int i=0;i<urls.size();i++){
            String product_category = urls.get(i).split("/",5)[4].split("\\?",2)[0];

            doc = Jsoup.connect(urls.get(i)).get();

            Elements showcase_image = doc.getElementsByClass("showcase-image");
            Elements showcase_title = doc.getElementsByClass("showcase-title");
            Elements showcase_price= doc.getElementsByClass("showcase-price");

            for(int k=0;k<showcase_image.size();k++){
                String raw_image_url = showcase_image.get(k).getElementsByTag("img").attr("data-src");
                String for_image_url = raw_image_url.split("//",2)[1];
                String image_url = for_image_url.split("\\?",2)[0];
                count_img++;
                String product_name = showcase_title.get(k).getElementsByTag("a").html();
                count_name++;
                String product_price = showcase_price.get(k).getElementsByClass("showcase-price-new").html();
                count_price++;
                count_category++;

                Product product = new Product(product_price,product_category,image_url,product_name,market_name);
                products.add(product);
            }
        }
        System.out.println(count_category);
        System.out.println(count_img);
        System.out.println(count_name);
        System.out.println(count_price);

    }
}
