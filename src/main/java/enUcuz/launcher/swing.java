package enUcuz.launcher;

import enUcuz.dao.MongoRepository;
import enUcuz.model.Product;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class swing extends JFrame{
    private JPanel PanelMain;
    private JPanel Panel1;
    private JPanel Panel2;
    private JPanel Panel3;
    private JTextField SearchBar;
    private JButton SearchButton;
    private JLabel LogoLabel;
    private JLabel SearchLabel;
    private JPanel SubPanel3;
    private JPanel SubPanel1;
    private JPanel SubPanel2;
    private JScrollPane JScrollPane;
    private JLabel MarketLabel1;
    private JLabel NameLabel1;
    private JLabel CategoryLabel1;
    private JLabel ImageLabel1;
    private JLabel IconLabel1;
    private JLabel MarketLabel2;
    private JLabel NameLabel2;
    private JLabel CategoryLabel2;
    private JLabel ImageLabel2;
    private JLabel IconLabel2;
    private JLabel MarketLabel3;
    private JLabel NameLabel3;
    private JLabel CategoryLabel3;
    private JLabel ImageLabel3;
    private JLabel IconLabel3;


    public swing(){
        add(PanelMain);
        //PanelMain.setLayout(new GridBagLayout());
        setSize(600, 500);
        setTitle("EnUcuz");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //to close the window
        setLocationRelativeTo(null); //to opening panel in middle of the screen

        //adding action to SearchButton
        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word;
                word = SearchBar.getText();
                MongoRepository<Product> repo = MongoRepository.getInstance();
                Iterable<Document> result = repo.findByName(MongoRepository.By.NAME,word);
                for(Document doc : result ){
                    System.out.println(doc.getString("name"));
                    NameLabel1.setText(doc.getString("name"));
                    JPanel panel1 = new JPanel();
                    panel1.add(NameLabel1);

                        //System.out.println(k +" " + v);

                }

            }
        });
        //adding action to SearchBar (to searching with enter key)
        SearchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Enter) {
                String word;
                word = SearchBar.getText();
                SearchLabel.setText("Arattığınız kelime: " + word);
                System.out.println(word);


            }
        });

        //adding image
        LogoLabel.setIcon(new ImageIcon(new ImageIcon("logo/logo.png").getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT)));

        //For dynamically
        Panel1.revalidate();
        Panel2.revalidate();
        Panel3.revalidate();

        IconLabel1.setIcon(new ImageIcon(new ImageIcon("logo/star.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
    }

}
