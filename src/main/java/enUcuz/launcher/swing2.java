package enUcuz.launcher;
import javax.print.Doc;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import enUcuz.dao.MongoRepository;
import enUcuz.interfaces.IDocument;
import enUcuz.model.Product;
import org.bson.Document;
import java.net.URL;
import javax.imageio.ImageIO;
import java.net.MalformedURLException;
import java.awt.image.BufferedImage;
//re render fonksiyonu yaz,ekrandaki ürünleri sıfırlamalı
//search button dılındakşleri bi loop içinde clearla(JPanel remove components)

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;


public class swing2 extends JFrame{
    JTextField text = new JTextField();
    JButton searcButton = new JButton();
    JPanel panel_item;
    JLabel name,price,image_label,market;
    static ArrayList<JPanel> panels = new ArrayList<JPanel>();
    static JFrame frame;
    static int x_axis = 150;
    static int y_axis = 200;
    static int x_padding = 310;
    static int y_padding = 310;
    static int max_x_axis = 1500;
    static int max_y_axis =1500;

    public swing2(){
        super();
        setLayout(new FlowLayout());

        setLayout(null);

        searcButton.setText("Ara");

        searcButton.setSize(100,30);
        searcButton.setLocation(1280,100);

        text.setSize(1100,30);
        text.setLocation(150,100);

        textFieldHandler handler = new textFieldHandler();
        searcButton.addActionListener(handler);
        text.addActionListener(handler);

        add(text);
        add(searcButton);

        frame = this;
        frame.setSize(1500,1500);



    }
    private class textFieldHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==searcButton){
                String searched = text.getText();
                JFrame frame2 = new JFrame("JFrame Example");
                MongoRepository<Product> repo = MongoRepository.getInstance();
                Iterable<Document> result = repo.findByName(MongoRepository.By.NAME, searched);
                long length = StreamSupport.stream(result.spliterator(),false).count();


                System.out.println(searched);
                //System.out.println(result);
                System.out.println(length);
                for(int i = 0; i<length;i++){
                    if(x_axis-50>max_x_axis ){
                        x_axis = 150;
                        y_axis += y_padding;
                        panel_item.setLocation(x_axis,y_axis);
                    }

                    List<Document> results = StreamSupport.stream(result.spliterator(),false).collect(Collectors.toList());
                    results.sort(new Comparator<Document>() {
                        @Override
                        public int compare(Document o1, Document o2) {
                            int l1 = o1.getString("name").length();
                            int l2 = o2.getString("name").length();
                            return l1-l2;
                        }
                    });
                    Image image = null;
                    URL url = null;
                    String image_url;
                    try {
                        image_url = results.get(i).getString("image");
                        if((results.get(i).getString("image").contains("https://")==false)){
                            image_url = "https://"+image_url;
                        }
                        System.out.println(image_url);
                        url = new URL(image_url);
                        image = ImageIO.read(url);
                    } catch (MalformedURLException ex) {
                        System.out.println("Malformed URL");
                    } catch (IOException iox) {
                        System.out.println("Can not load file");
                    }
                    String product_name = results.get(i).getString("name");
                    String product_price = results.get(i).getString("price");
                    String product_market = results.get(i).getString("market");
                    System.out.println(product_name.length()/3);


                    int where_to_split = product_name.lastIndexOf(" ",product_name.length()/3 );
                    System.out.println(where_to_split);
                    String first_part = product_name;
                    String second_part = "";

                    if(where_to_split!=-1){
                        first_part = product_name.substring(0,where_to_split);
                        second_part = product_name.substring(where_to_split);
                    }

                    System.out.println(product_name);

                    panel_item = new JPanel();
                    //add(panel_item);
                    name = new JLabel();
                    image_label = new JLabel(new ImageIcon(fitimage(image,170,150)));

                    price = new JLabel(product_price,SwingConstants.CENTER);
                    market = new JLabel();
                    name.setSize(30,50);
                    market.setText(product_market);
                    name.setText("<html>"+first_part+"<br/>"+second_part+"</html>");
                    name.setVisible(true);
                    price.setSize(30,50);

                    //price.setLocation();
                    //price.setText(product_price);


                    panel_item.add(name);
                    panel_item.add(market);
                    panel_item.add(image_label);
                    panel_item.add(price,BorderLayout.SOUTH);
                    panel_item.setVisible(true);

                    panel_item.setBackground(Color.lightGray);
                    panel_item.setSize(260,250);
                    panel_item.setLocation(x_axis,y_axis);

                    if(y_axis>=max_y_axis){
                        break;
                    }
                    x_axis += x_padding;

                    //add(panel_item);
                    //panels.add(panel_item);
                    frame.add(panel_item);

                    frame.revalidate();
                    frame.repaint();


                }
                //System.out.println(panels.size());
            }
        }

        private Image fitimage(Image img , int w , int h)
        {
            BufferedImage resizedimage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = resizedimage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, 0, 0,w,h,null);
            g2.dispose();
            return resizedimage;
        }

    }


}
