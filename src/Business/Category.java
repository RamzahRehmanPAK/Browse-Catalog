package Business;

import Interface.MyInt;
import Interface.MyInt;
import Storage.*;

import java.util.*;

/**
 * Created by Ramzah Rehman on 11/5/2016.
 */



public class Category {
    private int id;
    private String title;
    private Category parentCategory;
    private ArrayList<Category> subcategories;
    private ArrayList<Item> items;

    public Category(int id, String title) {

        this.id = id;
        this.title = title;
        subcategories = new ArrayList<Category>();
        items = new ArrayList<Item>();

    }

    public static void loadCategories(ArrayList<Category> rootCategories) {

        IDAO accessObject = new DAO();
        HashMap<String, String> hashMap = accessObject.getCategories();


        ArrayList<Category> poolOfCategories = new ArrayList<Category>();

        Category category = null;

        int id;
        String title;
        int comma;
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

            comma = entry.getKey().indexOf('~', 0);
            id = Integer.parseInt(entry.getKey().substring(0, comma));
            title = entry.getKey().substring(comma + 1);

            category = new Category(id, title);
            poolOfCategories.add(category);


            if (Integer.parseInt(entry.getValue()) == 0) {
                rootCategories.add(category);
            }
        }

        Category parentCategory = null;
        int i = 0;
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

            if (Integer.parseInt(entry.getValue()) != 0) {

                for (Category temp : poolOfCategories) {
                    if (temp.id == Integer.parseInt(entry.getValue())) {
                        parentCategory = temp;
                    }
                }
                poolOfCategories.get(i).parentCategory = parentCategory;
                parentCategory.subcategories.add( poolOfCategories.get(i));
            }

            i++;
        }
    }

    boolean addItem(Item item, int categoryid) {

        if (id == categoryid) {
            items.add(item);
            return true;
        }
        for (Category category : subcategories) {
            if (category.addItem(item, categoryid) == true) {
                return true;
            }
        }
        return false;
    }

    public String getCategoryTitle() {
        return title;
    }

    public Category findCurrentCategory(String selectedCategoryOrItemName, MyInt b) {

        Category currentCategory = null;

        if (!subcategories.isEmpty()) {
            for (Category category : subcategories) {
                if (category.title.equals(selectedCategoryOrItemName)) {
                    currentCategory = category;
                    b.value = 1;

                }
            }

        } else if (!items.isEmpty()) {
            currentCategory = this;
            b.value = 2;
        }
        return currentCategory;
    }


    public void getSubcategoriesOrItems(Vector<Vector<String>> data, MyInt p) {

        Vector<String> vector;
        if (!subcategories.isEmpty()) {
            for (Category category : subcategories) {
                vector = new Vector<String>();
                vector.add(category.title);
                data.add(vector);
            }
            p.value = 1;
        } else if (!items.isEmpty()) {
            for (Item item : items) {
                vector = new Vector<String>();
                vector.add(item.getCode());
                vector.add(item.getTitle());
                data.add(vector);
            }

            p.value = 2;
        }

    }

    public void showItemdetails(String selectedItemName, Vector<Vector<String>> data) {

        Vector<String> vector;
        for (Item item : items) {
            if (item.getCode().equals(selectedItemName)) {

                vector = new Vector<String>();
                vector.add(item.getCode());
                vector.add(item.getTitle());
                vector.add(item.getDescription());
                vector.add(String.valueOf(item.getPrice()));
                vector.add(String.valueOf(item.getAvailable()));
                data.add(vector);
                return;
            }
        }
    }
    public  Category getParentCategory(){
        return parentCategory;
    }

  public static void searchRelatedItems(String s,HashMap<Item,Integer> relatedItems, ArrayList<Category> insideCategories) {

      ArrayList<String> wordsOfItemTitle;
      StringTokenizer tokenizer;


      ArrayList<String> wordsOfCategoryTitle;
      StringTokenizer categoryTokenizer;

      for (Category category : insideCategories) {

          wordsOfCategoryTitle=new ArrayList<String>();
          categoryTokenizer=new StringTokenizer(category.title, " ,");


          while (categoryTokenizer.hasMoreTokens()) {
              wordsOfCategoryTitle.add(categoryTokenizer.nextToken());
          }

          int crelevant=0;
          for (String word :wordsOfCategoryTitle) {
              if (s.contains(word)) {
                  crelevant++;
              }
          }
          if(crelevant>0){
              category.addAllItemsToRelevant(relatedItems);

          }



          if (!category.items.isEmpty()) {

              for(Item item:category.items){

                  if(item.getCode().equals(s)){

                      relatedItems.put(item,2147483647);
                      return;
                  }
                  else{
                      wordsOfItemTitle = new ArrayList<String>();
                      tokenizer = new StringTokenizer(item.getTitle(), " ");

                      while (tokenizer.hasMoreTokens()) {
                          wordsOfItemTitle.add(tokenizer.nextToken());
                      }

                      int relevant=0;
                      for (String word :wordsOfItemTitle) {
                          if (s.contains(word)) {
                              relevant++;
                          }
                      }
                      if(relevant>0){
                          relatedItems.put(item,relevant);
                      }
                  }

              }

          }
          else
          {
              searchRelatedItems(s,relatedItems,category.subcategories);
          }
      }
  }


    private  void addAllItemsToRelevant(HashMap<Item,Integer>relatedItems){

        if(!items.isEmpty()){
            for(Item item:items){
                relatedItems.put(item,0);
            }
        }
        else{
            for(Category category:subcategories){
                category.addAllItemsToRelevant(relatedItems);
            }
        }

    }
}