import java.io.*;
import java.util.*;
class Product {
    String id;
    String name;
    String store;
    int price;
    Product(String id,String name,String store,int price) {
        this.id=id;
        this.name=name;
        this.store=store;
        this.price=price;
    }
}
public class ProductPriceComparator {
    static Product[] products=new Product[1000];
    static int count=0;
    static int nextId=101;
    static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) {
        loadData();
        while(true) {
            System.out.println("\n--- Product Price Comparator ---");
            System.out.println("1. Add Listing");
            System.out.println("2. Display All");
            System.out.println("3. Sort by Price");
            System.out.println("4. Search Product");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());
            } catch(NumberFormatException e) {
                System.out.println("Invalid menu choice.");
                continue;
            }
            switch(choice) {
                case 1:
                    addListing();
                    break;
                case 2:
                    displayAll();
                    break;
                case 3:
                    sortByPrice();
                    break;
                case 4:
                    searchProduct();
                    break;
                case 5:
                    saveData();
                    System.out.println("Data saved. Exiting...");
                    return;
                default:
                    System.out.println("Invalid menu choice.");
            }
        }
    }
    static void addListing() {
        if(count==products.length) {
            System.out.println("Product storage is full.");
            return;
        }
        System.out.print("Product: ");
        String name=sc.nextLine().trim();
        System.out.print("Store: ");
        String store=sc.nextLine().trim();
        System.out.print("Price: ");
        int price;
        try {
            price=Integer.parseInt(sc.nextLine());
        } catch(NumberFormatException e) {
            System.out.println("Invalid price. Enter a valid number.");
            return;
        }
        if(name.isEmpty() || store.isEmpty()) {
            System.out.println("Product and store cannot be empty.");
            return;
        }
        if(price<=0) {
            System.out.println("Invalid price. Price must be greater than 0.");
            return;
        }
        String id="P"+nextId;
        products[count]=new Product(id,name,store,price);
        count++;
        nextId++;
        System.out.println("Product added: "+id+" | "+name+" | "+store+" | "+price);
    }
    static void displayAll() {
        if(count==0) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("\nID | Product | Store | Price");
        for(int i=0;i<count;i++) {
            System.out.println(products[i].id+" | "+
            products[i].name+" | "+
            products[i].store+" | "+
            products[i].price);
        }
    }
    static void sortByPrice() {
        if(count==0) {
            System.out.println("No products available.");
            return;
        }
        for(int i=0;i<count-1;i++) {
            for(int j=0;j<count-i-1;j++) {
                if(products[j].price>products[j+1].price) {
                    Product temp=products[j];
                    products[j]=products[j+1];
                    products[j+1]=temp;
                }
            }
        }
        System.out.println("Products sorted by price (Low to High):");
        displayAll();
    }
    static void searchProduct() {
        System.out.print("Enter product name: ");
        String name=sc.nextLine().trim();
        Product cheapest=null;
        for(int i=0;i<count;i++) {
            if(products[i].name.equalsIgnoreCase(name)) {
                if(cheapest==null || products[i].price<cheapest.price) {
                    cheapest=products[i];
                }
            }
        }
        if(cheapest==null) {
            System.out.println("Product not found.");
        } else {
            System.out.println("Cheapest: "+cheapest.id+" | "+
            cheapest.name+" | "+cheapest.store+" | "+cheapest.price);
        }
    }
    static void loadData() {
        File file=new File("products.txt");
        if(!file.exists()) {
            return;
        }
        try {
            Scanner reader=new Scanner(file);
            while(reader.hasNextLine() && count<products.length) {
                String line=reader.nextLine();
                String[] data=line.split("\\|",4);
                if(data.length==4) {
                    try {
                        String id=data[0];
                        String name=data[1];
                        String store=data[2];
                        int price=Integer.parseInt(data[3]);
                        products[count]=new Product(id,name,store,price);
                        count++;
                        int number=Integer.parseInt(id.substring(1));
                        if(number>=nextId) {
                            nextId=number+1;
                        }
                    } catch(Exception e) {
                        continue;
                    }
                }
            }
            reader.close();
        } catch(IOException e) {
            System.out.println("Error loading file.");
        }
    }
    static void saveData() {
        try {
            PrintWriter writer=new PrintWriter(new FileWriter("products.txt"));
            for(int i=0;i<count;i++) {
                writer.println(products[i].id+"|"+
                products[i].name+"|"+
                products[i].store+"|"+
                products[i].price);
            }
            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving file.");
        }
    }
}
