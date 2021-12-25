package enUcuz.model;

import java.util.HashMap;

public class Product extends AbstractDocument{
    private String price;
    private String category;
    private String image;
    private String name;
    private String market;

    public Product(String price, String category, String image, String name, String market) {
        this.price = price;
        this.category = category;
        this.image = image;
        this.name = name;
        this.name = market;

        this.attributes = new HashMap<String, Object>(){{
            put("price", price);
            put("image", image);
            put("name", name);
            put("category", category);
            put("market",market);
        }};
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getMarket(){return market;}
}
