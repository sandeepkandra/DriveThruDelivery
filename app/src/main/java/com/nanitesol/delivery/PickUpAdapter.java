package com.nanitesol.delivery;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.felipecsl.gifimageview.library.GifImageView;
import com.nanitesol.delivery.gson.OrderParent;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PickUpAdapter extends BaseSwipeAdapter implements Filterable {

    private Context mContext ;
    ArrayList<OrderParent.OrderArrayDetails> mkit;
    List<Order> product;

    List<Order> customisation;

    ArrayList<OrderParent.OrderArrayDetails> searcheddata;
    private Dealfilter dealFilter;
    String orderid;

   /* public GridViewAdapter(Context mContext) {
        this.mContext = mContext;
    }*/
    public PickUpAdapter(Context mContext, int layoutResourceId, ArrayList<OrderParent.OrderArrayDetails> kit) {
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
//        final Order data = mkit.get(position);

        return v;
    }

    @Override
    public void fillValues(int position, final View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.txVwTokenNumber);
        TextView txtVwtokenid = (TextView)convertView.findViewById(R.id.txtVwtokenid);
        TextView txtVwStatus = (TextView)convertView.findViewById(R.id.txtVwStatus);
        TextView txtVwStatus1 = (TextView)convertView.findViewById(R.id.txtVwStatus1);
        ImageView imgVwConsumer = (ImageView)convertView.findViewById(R.id.imgVwConsumer);

        ImageView imgvwshowtxt = (ImageView)convertView.findViewById(R.id.imgvwshowtxt);
        ImageView imgVwPickup = (ImageView)convertView.findViewById(R.id.imgVwPickup);

        ImageView imgVwCar = (ImageView)convertView.findViewById(R.id.imgVwCar);
TextView txtVwOrderDetails=(TextView)convertView.findViewById(R.id.txtVwOrderDetailsShow);
        LinearLayout lnrOrderedPlaced=(LinearLayout)convertView.findViewById(R.id.lnrOrderedPlaced);

        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.drawable.alternate_background_color);
        } else {
            convertView.setBackgroundResource(R.drawable.alternate_color);
        }


       // Glide.with(mContext).load("http://goo.gl/gEgYUd").into(imgVwCar);
        convertView. findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.GONE) ;

        imgVwCar.setVisibility(View.GONE);
/*
        Glide
                .with(mContext)
                .load("http://res.cloudinary.com/nanitesolution/image/upload/v1456119063/car_arrived_2_ovz9zx.gif")
                .centerCrop()
                .placeholder(R.drawable.ic_launcher)
                .crossFade()
                .into(imgVwCar);*/

        //t.setText((position + 1 )+".");
       ;

        OrderParent.OrderArrayDetails data = null;
        try {
            data = mkit.get(position);

            txtVwStatus.setText("Picked Up");

            txtVwStatus1.setText("Picked UP");
            //lnrOrderedPlaced.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            imgVwPickup.setBackgroundResource(R.drawable.pickedup);

            lnrOrderedPlaced.setBackgroundResource(R.drawable.backgroundgreen);

            t.setText(data.Token + "");
        txtVwtokenid.setText(data.Order_ID + "");

            try {
                if(!data.user_profile_image.equalsIgnoreCase(""))
                    Picasso.with(mContext).load(data.user_profile_image).into(imgVwConsumer);
                else
                    imgVwConsumer.setBackgroundResource(R.drawable.user_profile);
            } catch (Exception e) {

            }

        }
        catch (Exception e)
        {

        }

        convertView.findViewById(R.id.lnrtrash).setEnabled(false);

        final OrderParent.OrderArrayDetails finalData = data;
        convertView.findViewById(R.id.imgvwshowtxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show();
                //convertView. findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE) ;

                String fVal = "";

                if(finalData.checkShowTxtBox) {
                    finalData.checkShowTxtBox = false;
                    convertView.findViewById(R.id.imgvwshowtxt).setBackgroundResource(R.drawable.box_filled);
                    convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.GONE);
                }
                else {
                    finalData.checkShowTxtBox = true;
                    convertView.findViewById(R.id.imgvwshowtxt).setBackgroundResource(R.drawable.boxfilled);
                    convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < finalData.Products.size();i++ ) {
                    String output = "";

                    output =  "<b>"+finalData.Products.get(i).Product_Name + "                " + finalData.Products.get(i).Quantity + "</b> <br /> "+"\n----------------------------------------------------\n";
                    //Log.wtf("ii", data.Products.get(i).Product_Name);
                    //   if (data.Products.get(i).Customization.size() > 0) {
                    String customized = "";
                    for (int j = 0; j < finalData.Products.get(i).Customization.size(); j++) {
                        //   final OrderParent.CustomisationCls da = data.Products.get(i).Customization.get(position);
                        customized +=  finalData.Products.get(i).Customization.get(j).category + ": " + finalData.Products.get(i).Customization.get(j).category_value+"<br /> ";

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
            Log.wtf("constraint", "con" + constraint.length() + "c");
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                // results.values = searcheddata;
                // results.count = searcheddata.size();
                List<OrderParent.OrderArrayDetails> ndata = new ArrayList<>();

                Log.wtf("length is",""+searcheddata.size());

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
                    //if ((d.Order_ID+"").startsWith(constraint.toString()))
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
        String url = "http://sqweezy.com/DriveThru/order_status_update.php?order_id="+orderid+"&status=5";
        JsonObjectRequest req = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(mContext,"Sucess",Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyApp.reqstQ.add(req);
    }
}
