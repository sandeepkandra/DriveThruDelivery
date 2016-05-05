package com.nanitesol.delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.felipecsl.gifimageview.library.GifImageView;
import com.nanitesol.delivery.gson.OrderParent;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseSwipeAdapter implements Filterable {

    private Context mContext ;
    // List<Order> mkit;

    private ArrayList<OrderParent.OrderArrayDetails> mkit;
    private ArrayList<OrderParent.OrderArrayDetails> searcheddata;

    //List<Order> searcheddata;
    private Dealfilter dealFilter;
    String orderid,sendStatus;
int checkShowTxtBoxVal=-1,consumerid;
    OrderParent orderParent;
   /* public GridViewAdapter(Context mContext) {
        this.mContext = mContext;
    }*/
    public GridViewAdapter(Context mContext, int layoutResourceId,ArrayList<OrderParent.OrderArrayDetails> kit) {
        // mkit = kit;
        this.mContext = mContext;
        this.mkit = kit;
        this.searcheddata=kit;

    }


    GifImageView gifView;

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        final View v =LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
        TextView t = (TextView)v.findViewById(R.id.txVwTokenNumber);

        TextView txtVwtokenid = (TextView)v.findViewById(R.id.txtVwtokenid);
        //gifView = (GifImageView) v.findViewById(R.id.gifImageView);
       // gifView.setBytes(bitmapData);

        //t.setText((position + 1 )+".");
       // final Order data = mkit.get(position);

        return v;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.txVwTokenNumber);
        TextView txtVwtokenid = (TextView)convertView.findViewById(R.id.txtVwtokenid);
        ImageView imgvwshowtxt = (ImageView)convertView.findViewById(R.id.imgvwshowtxt);
        ImageView imgVwConsumer = (ImageView)convertView.findViewById(R.id.imgVwConsumer);

        final ImageView imgVwPickup = (ImageView)convertView.findViewById(R.id.imgVwPickup);
        final ImageView imgVwSwipePickup = (ImageView)convertView.findViewById(R.id.imgVwSwipePickup);

        ImageView imgVwCar = (ImageView)convertView.findViewById(R.id.imgVwCar);
        TextView txtVwOrderDetails=(TextView)convertView.findViewById(R.id.txtVwOrderDetailsShow);
        final TextView txtVwStatus1=(TextView)convertView.findViewById(R.id.txtVwStatus1);
        final TextView txtVwStatus=(TextView)convertView.findViewById(R.id.txtVwStatus);
        final LinearLayout lnrOrderedPlaced=(LinearLayout)convertView.findViewById(R.id.lnrOrderedPlaced);

        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.drawable.alternate_background_color);
        } else {
            convertView.setBackgroundResource(R.drawable.alternate_color);
        }

       // Glide.with(mContext).load("http://goo.gl/gEgYUd").into(imgVwCar);
        convertView. findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.GONE) ;
       // final Order data = mkit.get(position);


        OrderParent.OrderArrayDetails data = null;

            data = mkit.get(position);
       // if(!(data.Status_id==5) && (data.Status_id==1)) {
            if (data.Status_id == 3) {
                txtVwStatus1.setText("Ready To Pickup");
                txtVwStatus.setText("Kiosk Ready");

                lnrOrderedPlaced.setBackgroundResource(R.drawable.backgroundgreenlite);
                imgVwSwipePickup.setBackgroundResource(R.drawable.readyforpickup3);
                imgVwPickup.setBackgroundResource(R.drawable.fordelivery);

            } else if (data.Status_id == 4) {
                txtVwStatus1.setText("Picked Up");
                lnrOrderedPlaced.setBackgroundResource(R.drawable.backgroundgreenlite);
                imgVwSwipePickup.setBackgroundResource(R.drawable.pickedup);
                imgVwPickup.setBackgroundResource(R.drawable.fordelivery);
            }

         /*   Glide
                    .with(mContext)
                    .load("http://res.cloudinary.com/nanitesolution/image/upload/v1456119063/car_arrived_2_ovz9zx.gif")
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .crossFade()
                    .into(imgVwCar);*/

            //t.setText((position + 1 )+".");


            t.setText( data.Token + "");
            txtVwtokenid.setText(data.Order_ID + "");


        if((MyApp.sharedPreferences.getString("CheckConsumer","")).contains( data.consumerid+"" ))
        {
            Glide
                    .with(mContext)
                    .load("http://res.cloudinary.com/nanitesolution/image/upload/v1456119063/car_arrived_2_ovz9zx.gif")
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher)
                    .crossFade()
                    .into(imgVwCar);
Log.wtf("enterimg","");
        }
        else {
            imgVwCar.setVisibility(View.GONE);

            Log.wtf("enterimgrem","");  //imgVwCar.setBackgroundResource(R.drawable.carwithoutblink);
               // Picasso.with(mContext).load(get).into(imgVwCar);

        }

            try {
                if(!data.user_profile_image.equalsIgnoreCase(""))
                Picasso.with(mContext).load(data.user_profile_image).into(imgVwConsumer);
                else
                    imgVwConsumer.setBackgroundResource(R.drawable.user_profile);
            } catch (Exception e) {

            }


            final OrderParent.OrderArrayDetails finalData1 = data;
            convertView.findViewById(R.id.lnrtrash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                 //   convertView.findViewById(R.id.lnrtrash).setBackgroundColor(Color.BLUE);

                    orderid = finalData1.Order_ID + "";
                    if (finalData1.Status_id == 3) {
                        finalData1.Status_id =4;

                        consumerid=finalData1.consumerid;
                        sendStatus = "4";
                        txtVwStatus1.setText("Picked Up");
                        txtVwStatus.setText("Picked Up");
                        lnrOrderedPlaced.setBackgroundResource(R.drawable.backgroundgreenlite);
                        imgVwSwipePickup.setBackgroundResource(R.drawable.pickedup);
                        imgVwPickup.setBackgroundResource(R.drawable.fordelivery);
                        setorderStatus();
                        notifyDataSetChanged();
                    } else if (finalData1.Status_id == 4) {
                        finalData1.Status_id =5;
                        sendStatus = "5";
                        consumerid=finalData1.consumerid;
                        txtVwStatus1.setText("Completed");
                        txtVwStatus.setText("Completed");
                        lnrOrderedPlaced.setBackgroundResource(R.drawable.backgroundgreenlite);
                        imgVwSwipePickup.setBackgroundResource(R.drawable.pickedup);
                        imgVwPickup.setBackgroundResource(R.drawable.fordelivery);
                        convertView.findViewById(R.id.lnrtrash).setEnabled(false);
                        setorderStatus();
                        notifyDataSetChanged();
                    }

                }
            });


            final OrderParent.OrderArrayDetails finalData = data;
            final OrderParent.OrderArrayDetails finalData2 = data;
            convertView.findViewById(R.id.imgvwshowtxt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();


                    String fVal = "";
                   // convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE);
                    if(finalData2.checkShowTxtBox) {
                        finalData2.checkShowTxtBox = false;

                        convertView.findViewById(R.id.imgvwshowtxt).setBackgroundResource(R.drawable.boxfilled);
                        convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.GONE);
                    }
                    else {
                        finalData2.checkShowTxtBox = true;
                        convertView.findViewById(R.id.imgvwshowtxt).setBackgroundResource(R.drawable.box_filled);
                        convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE);
                    }

                    /*try {

                        if (orderParent.Orders.OrderArray.get(position).checkShowTxtBox) {
                            orderParent.Orders.OrderArray.get(position).checkShowTxtBox = true;
                            convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE);

                        } else {
                            orderParent.Orders.OrderArray.get(position).checkShowTxtBox = false;
                            convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.GONE);
                        }

                    }
                    catch (Exception e)
                    {

                    }*/

                   // convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE);
                    //checkShowTxtBoxVal=position;
/*
                    for (int i = 0; i < finalData2.Products.size(); i++) {
                        String output = "";
                        output = finalData2.Products.get(i).Product_Name + "                " + finalData2.Products.get(i).Quantity + "\n";
                        //Log.wtf("ii", data.Products.get(i).Product_Name);
                        //   if (data.Products.get(i).Customization.size() > 0) {
                        String customized = "";
                        for (int j = 0; j < finalData2.Products.get(i).Customization.size(); j++) {
                            //   final OrderParent.CustomisationCls da = data.Products.get(i).Customization.get(position);
                            customized += finalData2.Products.get(i).Customization.get(j).category + ": " + finalData2.Products.get(i).Customization.get(j).category_value + "\n";

                            // Log.wtf("cus", data.Products.get(i).Customization.get(i).category);
                            // holder.txVwCredentials.setText(output + "\n" + data.Products.get(i).Customization.get(position).category);
                        }
                        fVal += output + "\n" + customized;


                    }

                    // holder.txVwCredentials.setText(fVal);
                    try {

                        ((TextView) convertView.findViewById(R.id.txtVwOrderDetailsShow)).setText(fVal);
                    } catch (Exception e) {

                    }*/
                    for (int i = 0; i < finalData2.Products.size();i++ ) {
                        String output = "";

                        output =  "<b>"+finalData2.Products.get(i).Product_Name + "                " + finalData2.Products.get(i).Quantity + "</b> <br /> "+"\n----------------------------------------------------\n";
                        //Log.wtf("ii", data.Products.get(i).Product_Name);
                        //   if (data.Products.get(i).Customization.size() > 0) {
                        String customized = "";
                        for (int j = 0; j < finalData2.Products.get(i).Customization.size(); j++) {
                            //   final OrderParent.CustomisationCls da = data.Products.get(i).Customization.get(position);
                            customized +=  finalData2.Products.get(i).Customization.get(j).category + ": " + finalData2.Products.get(i).Customization.get(j).category_value+"<br /> ";

                            // Log.wtf("cus", data.Products.get(i).Customization.get(i).category);
                            // holder.txVwCredentials.setText(output + "\n" + data.Products.get(i).Customization.get(position).category);
                        }
                        fVal+="<br /> "+output+"<br /> "+customized;


                    }


                    ((TextView) convertView.findViewById(R.id.txtVwOrderDetailsShow)).setText(Html.fromHtml(fVal));



                            orderid = finalData.Order_ID + "";
                    //Log.wtf("pr", finalData.product_Name+"\n quantity"+ finalData.quantity+"");


                }
            });


        //}
    }


    @Override
    public Filter getFilter() {
        if (dealFilter == null)
            dealFilter = new Dealfilter();

        return dealFilter;
    }

    public void resetData() {
        mkit=searcheddata;
    }

    //for search of deals

    private class Dealfilter extends Filter
    {

        @SuppressLint("DefaultLocale") @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub
            //Log.wtf("constraint", "con" + constraint.length() + "c");
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                // results.values = searcheddata;
                // results.count = searcheddata.size();
                List<OrderParent.OrderArrayDetails> ndata = new ArrayList<>();

                //Log.wtf("length is",""+searcheddata.size());

                for(int i=0,count=searcheddata.size();i<count;i++)
                {
                    //  Log.wtf("dist",searcheddata.get(i).getdealDist()+" "+seekBarValue);
                    // if(searcheddata.get(i).getdealDist()<seekBarValue)
                    ndata.add(searcheddata.get(i));
                }
                results.values = ndata;
                results.count = ndata.size();
                Log.wtf("count is ",""+ndata.size());

            }
            else {


                // We perform filtering operation
                //List<Planet> nPlanetList = new ArrayList<Planet>();
                ArrayList<OrderParent.OrderArrayDetails> ndata = new ArrayList<>();


                for (OrderParent.OrderArrayDetails d : searcheddata) {
                    if ((d.Order_ID+"").startsWith(constraint.toString()) || (d.Token+"").startsWith(constraint.toString()))
                        ndata.add(d);
                }



                results.values = ndata;
                results.count = ndata.size();

            }
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
            {

                //	 Toast.makeText(getContext(), "No Results Found", Toast.LENGTH_LONG).show();
                mkit = (ArrayList<OrderParent.OrderArrayDetails>) results.values;
                notifyDataSetChanged();
            }
            else {
                mkit = (ArrayList<OrderParent.OrderArrayDetails>) results.values;
                notifyDataSetChanged();
            }

        }

    }


    @Override
    public int getCount() {
        return mkit.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    private void setorderStatus() {
        String url = "http://sqweezy.com/DriveThru/order_status_update.php?order_id="+orderid+"&status="+sendStatus+"&consumer_id="+ consumerid;;
        JsonObjectRequest req = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(mContext,"Sucess",Toast.LENGTH_LONG).show();
               /* Intent i=new Intent(mContext,GridViewDelivery.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setorderStatus();
            }
        });
        MyApp.reqstQ.add(req);
    }
}
