package enUcuz.market;

import enUcuz.interfaces.IScrappable;
import enUcuz.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class A101 implements IScrappable {
    String base_link = "https://www.a101.com.tr";
    ArrayList<String> main_category_paths = new ArrayList<String>();
    String starter_category_url = "/market/";
    ArrayList<Integer> max_num_of_pages_in_categories = new ArrayList<Integer>();
    public ArrayList<Product> products = new ArrayList<Product>();
    String default_page_path_str = "?page=";
    int count_img = 0;
    int count_name =0;
    int count_price =0;
    int count_category=0;
    String market_name = "A101";

    public A101() throws IOException {
        this.scrap();
    }

    public void scrap() throws IOException, SocketTimeoutException {
        main_category_paths.add(starter_category_url);

        Document doc = Jsoup.connect(base_link+starter_category_url).get();

        Elements pagiantion_lis = doc.getElementsByClass("pagination").get(0).child(0).getElementsByTag("li");
        Elements categories = doc.getElementsByClass("categories");
        Element categories_first = categories.get(0);
        Elements categories_children = categories_first.children();
        Element ul_tag = categories_children.get(1);
        Elements main_categories = ul_tag.getElementsByClass("main");


        for (int i = 0; i<main_categories.size(); i++){
            String main_category = main_categories.get(i).getElementsByTag("a").attr("href");
            if(main_category != ""){
                main_category_paths.add(main_category);
            }
        }

        //System.out.println(main_category_paths.size());
            /*for (int i=0;i< main_category_paths.size();i++){
                System.out.println(main_category_paths.get(i));
            }*/

        for(int k=0;k< main_category_paths.size();k++){

            doc = Jsoup.connect(base_link + main_category_paths.get(k)).get();

            Elements for_max_num_of_page = doc.getElementsByClass("pagination");

            for(int m=0;m< for_max_num_of_page.size();m++){
                Elements ul_pages = for_max_num_of_page.first().getElementsByTag("ul");
                Elements li_pages = ul_pages.get(m).children();
                String max_num_of_page = li_pages.get(li_pages.size()-2).getElementsByTag("a").html() ;
                max_num_of_pages_in_categories.add(Integer.valueOf(max_num_of_page));
                break;
            }

            for(int l=1;l<=max_num_of_pages_in_categories.get(k);l++){
                String url = base_link+main_category_paths.get(k)+default_page_path_str+String.valueOf(l);

                doc = Jsoup.connect(url).get();
                Elements for_product_attributes = doc.getElementsByClass("product-card js-product-wrapper");
                for(int i=0;i< for_product_attributes.size();i++){

                    String product_category = main_category_paths.get(k).split("/",3)[1];
                    count_category++;
                    count_img++;
                    count_name++;
                    count_price++;
                    String product_name = for_product_attributes.get(i).children().get(0).getElementsByTag("hgroup").get(0).child(0).html();
                    String product_price = for_product_attributes.get(i).children().get(0).getElementsByTag("section").first().getElementsByClass("current").html();
                    String product_image = for_product_attributes.get(i).children().get(1).getElementsByClass("lazyload").attr("data-src");

                    Product product = new Product(product_price,product_category,product_image,product_name,market_name);

                    products.add(product);
                }
            }
        }
        System.out.println(count_category);
        System.out.println(count_img);
        System.out.println(count_name);
        System.out.println(count_price);

    }


}
