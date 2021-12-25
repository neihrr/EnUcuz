package enUcuz.launcher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import enUcuz.dao.MongoRepository;
import org.bson.Document;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;



public class swing2 extends JFrame{
    JTextField searchText = new JTextField();
    JButton searchButton = new JButton();
    JPanel panel_item;
    JLabel name_label, price_label,image_label, market_label;

    static final int PROGRAM_SIZE = 1200;

    static final int x_padding = 310;
    static final int y_padding = 310;
    static final int max_x_axis = 1500;
    static final int max_y_axis =1500;

    static final int Y_ALIGNMENT_HEADER = 100;
    static final int X_ALIGNMENT_HEADER = 150;

    static final int BUTTON_SIZE_X = 100;
    static final int BUTTON_SIZE_Y = 30;

    static final int SEARCH_SIZE_X = 900;
    static final int SEARCH_SIZE_Y = 30;

    static JFrame frame;
    static int x_axis = 150;
    static int y_axis = 250;



    MongoRepository repo = MongoRepository.getInstance();

    public swing2(){
        super();
        setLayout(new FlowLayout());
        setLayout(null);

        textFieldHandler handler = new textFieldHandler();

        searchButton.setText("Ara");
        searchButton.setSize(BUTTON_SIZE_X, BUTTON_SIZE_Y);
        searchButton.setLocation(SEARCH_SIZE_X + 160, Y_ALIGNMENT_HEADER);
        searchButton.addActionListener(handler);

        searchText.setSize(SEARCH_SIZE_X, SEARCH_SIZE_Y);
        searchText.setLocation(X_ALIGNMENT_HEADER, Y_ALIGNMENT_HEADER);
        searchText.addActionListener(handler);

        add(searchText);
        add(searchButton);
        setSize(PROGRAM_SIZE,PROGRAM_SIZE);

        frame = this;
    }

    private JLabel setNameLabel(String name){
        JLabel labelName = new JLabel();
        labelName.setText(name);
        labelName.setSize(30,50);
        labelName.setVisible(true);

        return labelName;
    }

    private JLabel setPriceLabel(String price){
        JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
        priceLabel.setSize(30,50);
        return priceLabel;
    }

    private JLabel setMarketLabel(String market){
        JLabel marketLabel = new JLabel();
        marketLabel.setText(market);

        return marketLabel;
    }

    private JPanel setProductPanel(JLabel name, JLabel market, JLabel image, JLabel price){
        JPanel panel = new JPanel();

        panel.add(name);
        panel.add(market);
        panel.add(image);
        panel.add(price,BorderLayout.SOUTH);
        panel.setVisible(true);

        panel.setBackground(Color.lightGray);
        panel.setSize(260,250);
        panel.setLocation(x_axis, y_axis);

        return panel;
    }

    private Image getImageURL(String imageURL){
        String image_url = imageURL.contains("https://") ? imageURL : "https://" + imageURL;
        Image image = null;

        try {
            image = ImageIO.read(new URL(image_url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    private void checkAndSetLocation(){
        if(x_axis + 260 >= max_x_axis){
            x_axis = 150;
            y_axis += y_padding;
            panel_item.setLocation(x_axis,y_axis);
        }
    }

    private class textFieldHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == searchButton){
                JFrame frame2 = new JFrame("JFrame Example");
                Iterable<Document> result = repo.findByName(MongoRepository.By.NAME, searchText.getText());
                long length = StreamSupport.stream(result.spliterator(),false).count();

                for(int i = 0; i < length; i++){
                    checkAndSetLocation();

                    List<Document> results = StreamSupport.stream(result.spliterator(),false).collect(Collectors.toList());
                    results.sort(Comparator.comparingInt(o -> o.getString("name").length()));

                    Image image = getImageURL(results.get(i).getString("image"));
                    String product_name = "<html>" + results.get(i).getString("name") + "</html>";

                    int where_to_split = product_name.lastIndexOf(" ",product_name.length() /3 );

                    String first_part = product_name;
                    String second_part = "";

                    if(where_to_split!=-1){
                        first_part = product_name.substring(0,where_to_split);
                        second_part = product_name.substring(where_to_split);
                    }

                    image_label = new JLabel(new ImageIcon(fitimage(image,150,150)));
                    market_label = setMarketLabel(results.get(i).getString("market"));
                    name_label = setNameLabel(first_part+"<br/>"+second_part);
                    name_label.add(image_label);
                    price_label = setPriceLabel(results.get(i).getString("price"));

                    panel_item = setProductPanel(name_label, market_label, image_label, price_label);

                    if(y_axis + 250 >= max_y_axis) {
                        System.out.println("small");
                        break;
                    }

                    x_axis += x_padding;

                    frame.add(panel_item);
                    frame.revalidate();
                    frame.repaint();
                }
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
