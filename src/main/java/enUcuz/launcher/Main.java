package enUcuz.launcher;
import javax.swing.*;
import enUcuz.market.A101;

import enUcuz.dao.MongoRepository;
import enUcuz.launcher.swing;
import enUcuz.market.Caglarsoy;
import enUcuz.market.Carrefour;
import enUcuz.model.Product;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        //changing the interface look according to operating systems
        MongoRepository<Product> repo = MongoRepository.getInstance();
        /*try {
            A101 a101 = new A101();
            // Carrefour carrefour = new Carrefour();
            //Carrefour carrefour = new Carrefour();

            for(int i=0;i<a101.products.size();i++){
                repo.insert(a101.products.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swing2 mySwing = new swing2();
                mySwing.setVisible(true);
            }
        });
    }
}
