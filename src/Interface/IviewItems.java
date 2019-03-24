package Interface;

import Business.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Ramzah Rehman on 11/5/2016.
 */

public class IviewItems extends ImagePanel {

    JButton showCatalog;
    JTable tableShowData;
    JTextField search;
    JScrollPane scrollPane;
    JButton Back;
    ArrayList<Category> rootCategories;
    Category currentCategory;
    JFrame mainFrame;
    HashMap<Item,Integer> relatedItems;

    public IviewItems(JFrame mainFrame){

        relatedItems=new HashMap<Item,Integer>();

        rootCategories=new ArrayList<Category>();
        try {
            BufferedImage image = ImageIO.read(new File("src/Interface/k.jpg"));//returns BufferedImage
            setBackground(image);

        }
        catch(IOException e){
            e.printStackTrace();
        }
        this.mainFrame=mainFrame;
        showCatalog=new JButton();
        tableShowData=new JTable();
        search=new JTextField();
        scrollPane=new JScrollPane(tableShowData);
        Back=new JButton();
    }
    void setPanel(){

        GridBagLayout gbl=new GridBagLayout();
        setLayout(gbl);

        Dimension d=new Dimension(150, 30);
        Back.setPreferredSize(d);
        search.setPreferredSize(d);
        showCatalog.setPreferredSize(d);
        showCatalog.setText("Catalog");
        Back.setText("Back");
        Back.setVisible(false);


        tableShowData.setDefaultEditor(Object.class, null);  //not editable

        Dimension k=new Dimension(800,400);

        tableShowData.setPreferredSize(k);
        scrollPane.getViewport().setPreferredSize(k);
        scrollPane.setPreferredSize(k);

        tableShowData.setShowGrid(false);

        tableShowData.setOpaque(false);
        tableShowData.setForeground(new Color(0,0,0));
        tableShowData.setBackground(new Color(215,174,222,0x88));

        tableShowData.getTableHeader().setOpaque(false);
        tableShowData.getTableHeader().setBackground(new Color(215,174,222,0x88));
        tableShowData.getTableHeader().setForeground(new Color(0,0,0));
        tableShowData.setRowHeight(60);
        tableShowData.setFont(new Font("Comic Sans MS",Font.PLAIN ,15));
        tableShowData.setForeground(new Color(56,36,74));


        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());



        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        add(showCatalog,gbc);

        gbc.gridx=1;
        gbc.gridy=0;
        gbc.insets=new Insets(0,10,0,0);
        add(search,gbc);

        gbc.gridx=2;
        gbc.gridy=0;
        gbc.insets=new Insets(0,300,0,0);
        add(Back,gbc);


        gbc.gridx=0;
        gbc.gridy=1;
        gbc.gridwidth=3;
        gbc.insets=new Insets(20,0,0,0);
        add(scrollPane,gbc);

        registerComponents();

    }

    void registerComponents(){

        ActionListener handler=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==search)
                {
                    currentCategory=null;
                    Back.setVisible(false);
                    mainFrame.setVisible(true);

                    String s=search.getText();

                    relatedItems.clear();


                    Category.searchRelatedItems(s,relatedItems,rootCategories);

                    Set<Map.Entry<Item, Integer>> set = relatedItems.entrySet();

                    List<Map.Entry<Item, Integer>> list = new ArrayList<Map.Entry<Item, Integer>>(set);
                    Collections.sort(list, new Comparator<Map.Entry<Item, Integer>>() {
                        public int compare(Map.Entry<Item, Integer> o1,
                                           Map.Entry<Item, Integer> o2) {
                            return o2.getValue().compareTo(o1.getValue());
                        }
                    });

                    Vector<Vector<String>> data = new Vector<Vector<String>>();
                    Vector<String> columnName = new Vector<String>();

                    Vector<String> vector;

                    for (Map.Entry<Item, Integer> entry : list) {
                        vector = new Vector<String>();
                        vector.add(entry.getKey().getCode());
                        vector.add(entry.getKey().getTitle());
                        data.add(vector);

                    }
                    columnName.add("Code");
                    columnName.add("Title");
                    DefaultTableModel tableModel = (DefaultTableModel) tableShowData.getModel();
                    tableModel.setDataVector(data, columnName);

                }

               if(e.getSource()==showCatalog){

                    Back.setVisible(true);
                    mainFrame.setVisible(true);

                    currentCategory=null;
                    showRootCategories();
                }
                else if(e.getSource()==Back){

                    if(currentCategory!=null){

                        if(!tableShowData.getColumnModel().getColumn(0).getHeaderValue().equals("#Code"))
                        {
                         currentCategory=currentCategory.getParentCategory();
                        }
                        if(currentCategory==null){
                           showRootCategories();
                        }
                        else {
                            Vector<Vector<String>> data = new Vector<Vector<String>>();
                            Vector<String> columnName = new Vector<String>();


                            MyInt p = new MyInt();
                            currentCategory.getSubcategoriesOrItems(data, p);

                            if (p.value == 1) {
                                columnName.add("Category");
                            } else if (p.value == 2) {
                                columnName.add("Code");
                                columnName.add("Title");
                            }
                            DefaultTableModel tableModel = (DefaultTableModel) tableShowData.getModel();
                            tableModel.setDataVector(data, columnName);
                        }
                    }
                }
            }
        };
        showCatalog.addActionListener(handler);
        Back.addActionListener(handler);
        search.addActionListener(handler);

        tableShowData.addMouseListener (new MouseAdapter(){
            @Override

            public void mouseClicked(MouseEvent e) {

                if(! tableShowData.getColumnModel().getColumn(0).getHeaderValue().equals("#Code"))
                {
                    MyInt b = new MyInt();

                    Vector<Vector<String>> data = new Vector<Vector<String>>();
                    Vector<String> columnName = new Vector<String>();

                    String selectedCategoryOrItemName=null;
                    try{
                        selectedCategoryOrItemName=tableShowData.getValueAt(tableShowData.getSelectedRow(), 0).toString();
                    }
                    catch(Exception p){
                        return;
                    }

                    if(currentCategory==null &&  tableShowData.getColumnModel().getColumn(0).getHeaderValue().equals("Category")){

                       for(Category category:rootCategories){
                           if(category.getCategoryTitle().equals(selectedCategoryOrItemName)){
                               currentCategory=category;
                               b.value=1;
                           }
                       }
                    }
                    else if(currentCategory==null &&  tableShowData.getColumnModel().getColumn(0).getHeaderValue().equals("Code")) {

                        Vector<String> myvector= new Vector<String>();

                        Item temp=null;
                        Item item=null;

                        for( Map.Entry entry:relatedItems.entrySet()){

                            temp =(Item) entry.getKey();
                            if(selectedCategoryOrItemName==temp.getCode()){
                                item=temp;
                            }

                        }

                        relatedItems.clear();

                        myvector.add(item.getCode());
                        myvector.add(item.getTitle());
                        myvector.add(item.getDescription());
                        myvector.add(String.valueOf(item.getPrice()));
                        myvector.add(String.valueOf(item.getAvailable()));
                        data.add(myvector);


                        columnName.add("#Code");
                        columnName.add("Title");
                        columnName.add("Description");
                        columnName.add("Price");
                        columnName.add("Available");


                        DefaultTableModel tableModel = (DefaultTableModel) tableShowData.getModel();
                        tableModel.setDataVector(data, columnName);

                        tableShowData.getColumnModel().getColumn(0).setPreferredWidth(50);
                        tableShowData.getColumnModel().getColumn(1).setPreferredWidth(120);
                        tableShowData.getColumnModel().getColumn(2).setPreferredWidth(400);
                        tableShowData.getColumnModel().getColumn(3).setPreferredWidth(50);
                        tableShowData.getColumnModel().getColumn(4).setPreferredWidth(50);
                        return;

                    }
                    else
                    {
                        currentCategory = currentCategory.findCurrentCategory(selectedCategoryOrItemName, b);
                    }
                    if(b.value == 1)
                    {

                        MyInt p = new MyInt();
                        currentCategory.getSubcategoriesOrItems(data, p);

                        if (p.value == 1) {
                            columnName.add("Category");
                        } else if (p.value == 2) {
                            columnName.add("Code");
                            columnName.add("Title");
                        }
                        DefaultTableModel tableModel = (DefaultTableModel) tableShowData.getModel();
                        tableModel.setDataVector(data, columnName);


                    }
                    else if (b.value == 2) {
                        currentCategory.showItemdetails(selectedCategoryOrItemName, data);

                        columnName.add("#Code");
                        columnName.add("Title");
                        columnName.add("Description");
                        columnName.add("Price");
                        columnName.add("Available");


                        DefaultTableModel tableModel = (DefaultTableModel) tableShowData.getModel();
                        tableModel.setDataVector(data, columnName);

                        tableShowData.getColumnModel().getColumn(0).setPreferredWidth(50);
                        tableShowData.getColumnModel().getColumn(1).setPreferredWidth(120);
                        tableShowData.getColumnModel().getColumn(2).setPreferredWidth(400);
                        tableShowData.getColumnModel().getColumn(3).setPreferredWidth(50);
                        tableShowData.getColumnModel().getColumn(4).setPreferredWidth(50);

                    }

                }
            }
        });

    }
    void loadData()
    {
        Category.loadCategories(rootCategories);
        Item.loadItems(rootCategories);
    }

    void showRootCategories(){

        Vector<Vector<String>> data=new Vector<Vector<String>>();

        Vector<String> vector;

        for(Category category:rootCategories){

            vector=new Vector<String>();
            vector.add(category.getCategoryTitle());
            data.add(vector);

        }

        Vector<String> columnName=new Vector<String>();
        columnName.add("Category");

        DefaultTableModel tableModel=(DefaultTableModel)tableShowData.getModel();
        tableModel.setDataVector(data,columnName);

    }
    public static void main(String [] args){

        //always search Like This
        JFrame mainFrame=new JFrame();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mainFrame.setBounds((int) toolkit.getScreenSize().getWidth()/8 , (int) toolkit.getScreenSize().getHeight()/8, (int) toolkit.getScreenSize().getWidth()*7/10 , (int) toolkit.getScreenSize().getHeight()*7/10 );

        IviewItems iviewItems =new IviewItems(mainFrame);
        iviewItems.setPanel();

        iviewItems.loadData();

        mainFrame.setContentPane(iviewItems);

        mainFrame.setVisible(true);
    }
}
