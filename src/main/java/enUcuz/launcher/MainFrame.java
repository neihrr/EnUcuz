package enUcuz.launcher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.*;
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


public class MainFrame extends JFrame{
    JTextField searchText;
    JButton searchButton, nextButton;
    JPanel panel_item;
    JLabel name_label, price_label,image_label, market_label, counterText;

    static final int PROGRAM_SIZE = 1200;

    static final int PADDING_X = 310;
    static final int PADDING_Y = 310;

    static final int Y_ALIGNMENT_HEADER = 100;
    static final int X_ALIGNMENT_HEADER = 150;

    static final int BUTTON_SIZE_X = 100;
    static final int BUTTON_SIZE_Y = 30;

    static final int SEARCH_SIZE_X = 900;
    static final int SEARCH_SIZE_Y = 30;

    static final int Y_NEXT_ALIGNMENT = 1000;

    static final int max_x_axis = 1500;
    static final int max_y_axis =1500;

    static MainFrame frame;

    static int x_axis = 150;
    static int y_axis = 250;

    static Integer pageCount;
    static Long totalCount;

    static List<Document> documentsToRender;

    public void refresh(){
        Component[] componentList = frame.getContentPane().getComponents();

        for(Component c : componentList){
            if(c instanceof JPanel){
                frame.getContentPane().remove(c);
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    MongoRepository repo = MongoRepository.getInstance();

    public MainFrame(){
        super();
        setLayout(new FlowLayout());
        setLayout(null);


        nextButton = new JButton();
        nextButton.setText("NEXT");
        nextButton.setFont(new Font(Font.SANS_SERIF, 0, 15));
        nextButton.setSize(BUTTON_SIZE_X,BUTTON_SIZE_Y);
        nextButton.setLocation(SEARCH_SIZE_X - 375, Y_NEXT_ALIGNMENT + 125);
        nextButton.setVisible(false);
        nextButton.setBackground(Color.LIGHT_GRAY);

        searchButton = new JButton();
        searchButton.setText("SEARCH");
        searchButton.setFont(new Font(Font.SANS_SERIF, 0, 15));
        searchButton.setSize(BUTTON_SIZE_X, BUTTON_SIZE_Y);
        searchButton.setLocation(SEARCH_SIZE_X + 160, Y_ALIGNMENT_HEADER);
        searchButton.setBackground(Color.LIGHT_GRAY);

        searchText = new JTextField();
        searchText.setSize(SEARCH_SIZE_X, SEARCH_SIZE_Y);
        searchText.setFont(new Font(Font.SANS_SERIF, 0, 15));
        searchText.setLocation(X_ALIGNMENT_HEADER, Y_ALIGNMENT_HEADER);

        counterText = new JLabel();
        counterText.setLocation(SEARCH_SIZE_X - 350, Y_ALIGNMENT_HEADER + 50);
        counterText.setFont(new Font(Font.SANS_SERIF, 0, 15));
        counterText.setSize(BUTTON_SIZE_X, BUTTON_SIZE_Y);

        add(searchText);
        add(searchButton);
        add(nextButton);
        add(counterText);

        setSize(PROGRAM_SIZE,PROGRAM_SIZE);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterable<Document> result = repo.findByName(MongoRepository.By.NAME, searchText.getText());
                long length = StreamSupport.stream(result.spliterator(),false).count();

                documentsToRender = StreamSupport.stream(result.spliterator(),false).collect(Collectors.toList());
                documentsToRender.sort(Comparator.comparingInt(o -> o.getString("name").length()));

                pageCount = 1;
                totalCount = length / 9;

                if(totalCount > 1)
                    nextButton.setVisible(true);
                else
                    nextButton.setVisible(false);

                addProductsToPanel(documentsToRender);

            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(documentsToRender == null || documentsToRender.size() <= 0 || pageCount >= totalCount){
                    nextButton.setVisible(false);
                    return;
                }

                documentsToRender = documentsToRender.subList(3, documentsToRender.size());
                pageCount += 1;
                addProductsToPanel(documentsToRender);
            }
        });

        frame = this;
    }

    private Image imageFit(Image img , int w , int h)
    {
        BufferedImage resizedimage = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedimage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0,w,h,null);
        g2.dispose();
        return resizedimage;
    }

    public static Image makeColorTransparent(Image im, final Color color) {
            ImageFilter filter = new RGBImageFilter() {
                // the color we are looking for... Alpha bits are set to opaque
                public int markerRGB = color.getRGB() | 0xFF000000;

                public final int filterRGB(int x, int y, int rgb) {
                    if ( ( rgb | 0xFF000000 ) == markerRGB ) {
                        // Mark the alpha bits as zero - transparent
                        return 0x00FFFFFF & rgb;
                    }
                    else {
                        // nothing to do
                        return rgb;
                    }
                }
            };

            ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
            return Toolkit.getDefaultToolkit().createImage(ip);
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
            y_axis += PADDING_Y;
            panel_item.setLocation(x_axis,y_axis);
        }
    }

    private String splitProductName(String productName){
        int where_to_split = productName.lastIndexOf(" ", productName.length() /3 );

        String first_part = productName;
        String second_part = "";

        if(where_to_split!=-1){
            first_part = productName.substring(0,where_to_split);
            second_part = productName.substring(where_to_split);
        }

        return first_part+"<br/>"+second_part;
    }

    private void addProductsToPanel(List<Document> products){
        frame.refresh();
        for(Document product : products) {
            checkAndSetLocation();

            String product_name = "<html>" + product.getString("name") + "</html>";
            product_name = splitProductName(product_name);

            image_label = new JLabel(new ImageIcon(makeColorTransparent(imageFit(getImageURL(product.getString("image")), 150, 150), Color.WHITE)));
            market_label = setMarketLabel(product.getString("market"));
            name_label = setNameLabel(product_name);
            price_label = setPriceLabel(product.getString("price"));
            panel_item = setProductPanel(name_label, market_label, image_label, price_label);

            if (y_axis + 250 >= max_y_axis) {
                break;
            }

            x_axis += PADDING_X;

            counterText.setText(pageCount.toString() + "/" + totalCount.toString());
            frame.add(panel_item);
            frame.revalidate();
            frame.repaint();
        }
        x_axis = 150;
        y_axis = 250;
    }


}
