package com.nanitesol.delivery.gson;

import java.util.ArrayList;

/**
 * Created by sandeep on 15-Feb-16.
 */
public class OrderParent {
    public int Merchant_ID;

    public OrdersChild Orders;

    public class OrdersChild
    {

        public ArrayList<OrderArrayDetails> OrderArray=new ArrayList<>();
    }

    public  class  OrderArrayDetails
    {

      // public ProductsCls Products;

        public String Order_Status;
        public int consumerid;
        public  String sequence;
        public  transient int consumerIdCheck;
        public int Order_ID;
        public int Token;
        public  String user_profile_image;
        public int Status_id;
        public transient  boolean checkShowTxtBox=false;
        public ArrayList<ProductsDetails> Products=new ArrayList<>();


    }


   /* public class ProductsCls
    {
        ArrayList<ProductsDetails> Products=new ArrayList<>();


    }*/
    public  class ProductsDetails
    {
       // public CustomisationCls Customization;

        public String Product_ID;
        public  String Product_Name;
        public int Quantity;
        public  String Customization_Required;
        public ArrayList<CustomisationCls> Customization=new ArrayList<>();


    }
    public  class CustomisationCls
    {
        public String category;
        public  String category_value;
    }
}
