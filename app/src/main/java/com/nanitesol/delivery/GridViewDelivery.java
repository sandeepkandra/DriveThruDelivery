package com.nanitesol.delivery;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nanitesol.delivery.gson.OrderParent;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GridViewDelivery extends Activity implements View.OnClickListener{

    EditText edittxtSearchTokenId;
    String getData;
    ArrayList<Order> orders=new ArrayList<>();

    GridView gridView;
    GridViewAdapter adapter;
    PickUpAdapter pickUpAdapter;
    OtherAdapter otherAdapter;
    OrderParent orderParent;
    Button btnPickUp,btnDeliver,btnStale;
    ProgressDialog progressDialog;
    HashMap<Integer,OrderParent.OrderArrayDetails> consumerId = new HashMap<>();
    GeoFire geoFire;
    String baseurl="https://blistering-torch-3715.firebaseio.com";

    int checkPDS;int checkShowTxtBoxVal=-1;
    double clat,clng;


    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    //Declaring a Location Manager
    protected LocationManager locationManager;
    boolean isGPSEnabled,isNetworkEnabled;

    boolean canGetLocation = false;

    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview);

        Firebase.setAndroidContext(this);

        Firebase rootRef = new Firebase("https://blistering-torch-3715.firebaseio.com/storegeo");

       /* rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                Log.wtf("val",snapshot.getValue()+"");
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                Log.wtf("val1", firebaseError.getMessage() + "");
            }
        });*/



/*
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        // Toast.makeText(getApplicationContext(), "metrics"+metrics, Toast.LENGTH_LONG).show();
        //getSupportActionBar().setTitle("Provider Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        gridView = (GridView)findViewById(R.id.gridview);

        btnDeliver = (Button)findViewById(R.id.btnDeliver);
        btnDeliver.setOnClickListener(this);
        btnPickUp = (Button)findViewById(R.id.btnPickUp);
        btnPickUp.setOnClickListener(this);

        btnStale = (Button)findViewById(R.id.btnStale);
        btnStale.setOnClickListener(this);
       /* btnPickUp.setTextColor(getResources().getColor(R.color.selected));
        btnPickUp.setBackgroundColor(getResources().getColor(R.color.white));
*/
      //  adapter = new GridViewAdapter(g);
      //  adapter.setMode(Attributes.Mode.Multiple);
       // gridView.setAdapter(adapter);
     //   gridView.setSelected(false);
        progressDialog = ProgressDialog.show(GridViewDelivery.this, "Loading....",
                "Please wait", true);
        progressDialog.dismiss();

/*
        // check if GPS enabled
        GpsTracker gpsTracker = new GpsTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {

            Log.wtf("lat",(gpsTracker.latitude+(gpsTracker.longitude)+""));
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }*/
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemLongClick", "onItemLongClick:" + position);
                return false;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemClick", "onItemClick:" + position);

             /*   if(checkShowTxtBoxVal!=-1 && orderParent.Orders.OrderArray.get(checkShowTxtBoxVal).checkShowTxtBox) {
                    orderParent.Orders.OrderArray.get(checkShowTxtBoxVal).checkShowTxtBox = false;
                    //convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.GONE);

                }
                else {
                    orderParent.Orders.OrderArray.get(position).checkShowTxtBox = true;
                    // convertView.findViewById(R.id.txtVwOrderDetailsShow).setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
                pickUpAdapter.notifyDataSetChanged();
*/
            }
        });


        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemSelected", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getOrders();

        Pusher pusher = new Pusher("1f006f9bd40000fbe5e8");

        Channel channel = pusher.subscribe("md_channel");
        // getOrders();

        channel.bind("md_event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                System.out.println(data);
                getData = data;

                Log.i("channel", channelName + "");
                try {
                    JSONObject jsonObj = new JSONObject(data);
                    String message = jsonObj.getString("message");
                    String order_status = jsonObj.getString("order_status");
                   // getData = jsonObj.getInt("order_id");


                   /* try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    getOrders();
                    generateNotification(GridViewDelivery.this, message);
                   // getOrderidDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //JSONObject obj = data.getJSONObject("OrderDetails");

                // Toast.makeText(getApplicationContext(),"channel"+data, Toast.LENGTH_LONG).show();
                //getDefaultConditions();
            }
        });

        pusher.connect();

        getOrders();
        // filter


        final Set<String> runnersNearby = new HashSet<String>();
// query around current user location with 1.6km radius

        //Firebase f = new Firebase(baseurl+"/storegeo/"+ "store");
        // GeoFire gf=new GeoFire(f);

        // Firebase f = new Firebase(baseurl);
        geoFire = new GeoFire( new Firebase("https://blistering-torch-3715.firebaseio.com/storegeo/store1"));
        // GeoQuery geoQuery = geoFire.queryAtLocation(currentUserLocation, 1.6);0
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(12.9294324,77.5924236), 5);

    /*    geoFire.getLocation("https://blistering-torch-3715.firebaseio.com/storegeo", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    Log.wtf("getloc", key+location.latitude+location.longitude);
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    Log.wtf("getno l", key);
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.wtf("get", firebaseError.toString());
                System.err.println("There was an error getting the GeoFire location: " + firebaseError);
            }
        });*/
        final Order o=new Order();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            String conCheck="";
            @Override


            public void onKeyEntered(String key, GeoLocation location) {

                Log.wtf("enter", key + location.latitude + location.longitude);
                if(!(MyApp.sharedPreferences.getString("CheckConsumer","").equalsIgnoreCase("")))
                {
                    conCheck+=key+",";
                    SharedPreferences.Editor editor=MyApp.sharedPreferences.edit();
                    editor.remove("CheckConsumer").commit();

                  //  conCheck=conCheck+key;
                    SharedPreferences.Editor editor1 = MyApp.sharedPreferences.edit();
                    editor1.putString("CheckConsumer", conCheck).commit();
                }
                else {
                    conCheck+=key+",";
                    SharedPreferences.Editor editor = MyApp.sharedPreferences.edit();
                    editor.putString("CheckConsumer", conCheck).commit();
                }
                adapter.notifyDataSetChanged();

              /*  for(int i=0;orderParent.Orders.OrderArray.size()<0;i++)
                {

                    o.consumerid=orderParent.Orders.OrderArray.get(i).consumerid;
                    orders.add(o);
                }*/
                //orderParent.Orders.OrderArray.
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                Log.wtf("exited", key);

                if(!(MyApp.sharedPreferences.getString("CheckConsumer","").equalsIgnoreCase("")))
                {

                    String ss=MyApp.sharedPreferences.getString("CheckConsumer","").replaceAll(key, "");

                    //  conCheck=conCheck+key;
                    SharedPreferences.Editor editor1 = MyApp.sharedPreferences.edit();
                    editor1.putString("CheckConsumer", ss).commit();
                    adapter.notifyDataSetChanged();
                }



                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.wtf("moved", key+ location.latitude+ location.longitude);
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                Log.wtf("enter", "All initial data has been loaded and events have been fired!");
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(FirebaseError error) {
                Log.wtf("There was an error with this query:",error.toString());
                System.err.println("There was an error with this query: " + error);
            }
        });
        edittxtSearchTokenId=(EditText)findViewById(R.id.edittxtSearchTokenId);
        if(!edittxtSearchTokenId.getText().toString().equals(""))
            adapter.getFilter().filter(edittxtSearchTokenId.getText().toString());


        try
        {



            edittxtSearchTokenId.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    try {

                            if (count < before) {
                                // We're deleting char so we need to reset the adapter data
                                adapter.resetData();
                            }
                            adapter.getFilter().filter(s.toString());
                            //dealDetailslstVw.setEmptyView(txtVw);

                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            pickUpAdapter.resetData();
                        }
                        pickUpAdapter.getFilter().filter(s.toString());

                        if (count < before) {
                            // We're deleting char so we need to reset the adapter data
                            otherAdapter.resetData();
                        }
                        otherAdapter.getFilter().filter(s.toString());


                        /*
                            if (count < before) {
                                // We're deleting char so we need to reset the adapter data
                                pickUpAdapter.resetData();
                            }
                            pickUpAdapter.getFilter().filter(s.toString());
                        *//*
                            if (count < before) {
                                // We're deleting char so we need to reset the adapter data
                                pickUpAdapter.resetData();
                            }
                            pickUpAdapter.getFilter().filter(s.toString());
                        */

                    }
                    catch(Exception e)
                    {
                        Log.e("err edit",e.toString());
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }
            });
        }
        catch(Exception e)
        {
            Log.e("err in editsearch", e.toString());
        }

    }


    private void getOrders() {
        //
        try {

            progressDialog.show();
        }
        catch (Exception e)
        {
            Log.wtf("err",e.toString());
        }
        String url = "http://sqweezy.com/DriveThru/Get_OrderDetails.php?merchant_id=1&delivery=true";
        // String url = "http://sqweezy.com/DriveThru/getOrderDetailsGrouped.php?merchant_id=20";
        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson1 = builder.create();
                orderParent = gson1.fromJson(response,OrderParent.class);

                    //orders.clear();
                try {


                    progressDialog.dismiss();
                }
                catch (Exception e)
                {

                }

                    adapter = new GridViewAdapter(getApplicationContext(), R.layout.grid_item,orderParent.Orders.OrderArray);
                    gridView.setAdapter(adapter);



                        btnDeliver.setTextColor(getResources().getColor(R.color.white));
                        btnDeliver.setBackgroundColor(getResources().getColor(R.color.selected));

                        btnPickUp.setTextColor(getResources().getColor(R.color.selected));
                        btnPickUp.setBackgroundColor(getResources().getColor(R.color.white));
                        btnStale.setTextColor(getResources().getColor(R.color.selected));
                        btnStale.setBackgroundColor(getResources().getColor(R.color.white));




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            progressDialog.dismiss();
                //getOrders();
            }
        });
        MyApp.reqstQ.add(req);
    }


    private void getOrderedPickup() {
        //
        progressDialog.show();
        String url = "http://sqweezy.com/DriveThru/Get_OrderDetails.php?merchant_id=1&pickedup=true";
        // String url = "http://sqweezy.com/DriveThru/getOrderDetailsGrouped.php?merchant_id=20";
        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson1 = builder.create();
                orderParent = gson1.fromJson(response,OrderParent.class);


                progressDialog.dismiss();
                    btnDeliver.setTextColor(getResources().getColor(R.color.selected));
                    btnDeliver.setBackgroundColor(getResources().getColor(R.color.white));

                    btnStale.setTextColor(getResources().getColor(R.color.selected));
                    btnStale.setBackgroundColor(getResources().getColor(R.color.white));

                btnPickUp.setTextColor(getResources().getColor(R.color.white));
                    btnPickUp.setBackgroundColor(getResources().getColor(R.color.selected));

                    pickUpAdapter = new PickUpAdapter(getApplicationContext(), R.layout.grid_item, orderParent.Orders.OrderArray);
                    gridView.setAdapter(pickUpAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                getOrderedPickup();
            }
        });
        MyApp.reqstQ.add(req);
    }

    private void getOrderedOther() {
        //
        progressDialog.show();
        String url = "http://sqweezy.com/DriveThru/Get_OrderDetails.php?merchant_id=1&others=true";
        // String url = "http://sqweezy.com/DriveThru/getOrderDetailsGrouped.php?merchant_id=20";
        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson1 = builder.create();
                orderParent = gson1.fromJson(response,OrderParent.class);


                progressDialog.dismiss();
                btnDeliver.setTextColor(getResources().getColor(R.color.selected));
                btnDeliver.setBackgroundColor(getResources().getColor(R.color.white));

                btnPickUp.setTextColor(getResources().getColor(R.color.selected));
                btnPickUp.setBackgroundColor(getResources().getColor(R.color.white));

                btnStale.setTextColor(getResources().getColor(R.color.white));
                btnStale.setBackgroundColor(getResources().getColor(R.color.selected));

                otherAdapter = new OtherAdapter(getApplicationContext(), R.layout.grid_item, orderParent.Orders.OrderArray);
                gridView.setAdapter(otherAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                getOrderedOther();
            }
        });
        MyApp.reqstQ.add(req);
    }



    private static void generateNotification(Context context, String message) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.cooking)
                        .setContentTitle("DriveThru")
                        .setContentText("This is a DriveThru notification Delivery"+"\n"+message);


        Intent notificationIntent = new Intent(context, GridViewDelivery.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        builder.setStyle(new NotificationCompat.InboxStyle());
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
// Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

    }


    public void getOrderidDetails() {


        String url = "http://sqweezy.com/DriveThru/Get_OrderDetail.php?order_id="+getData;

        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    GsonBuilder builder = new GsonBuilder();
                    Gson gson1 = builder.create();
                    orderParent = gson1.fromJson(response, OrderParent.class);


                    for (final OrderParent.OrderArrayDetails order : orderParent.Orders.OrderArray) {

                        //Log.wtf("merch",   order.Order_ID+"");
                        Order o = new Order();
                        o.id = order.Order_ID;
                        o.id1 = order.Token;
                        o.STATUS = order.Order_Status;

                        o.userprofileimage=order.user_profile_image;
                        o.Status_id = order.Status_id;

                        //orders.add(o);
                        //final OrderParent.OrdersChild m = OrderArray;
                        for (final OrderParent.ProductsDetails orde : order.Products) {

                            o.product_ID = orde.Product_ID;
                            o.product_Name = orde.Product_Name;
                            o.customization_Required = orde.Customization_Required;
                            o.quantity = orde.Quantity;
                            orders.add(o);
                            for(final OrderParent.CustomisationCls ord : orde.Customization) {
                                o.category = ord.category;
                                o.category_value = ord.category_value;
                                orders.add(o);
                            }
                            Log.wtf("merch", orde.Product_Name + "");

                        }


                    }
                    HashSet hs = new HashSet();
                    hs.addAll(orders);
                    orders.clear();
                    orders.addAll(hs);
                    btnDeliver.setTextColor(getResources().getColor(R.color.white));
                    btnDeliver.setBackgroundColor(getResources().getColor(R.color.selected));

                    btnPickUp.setTextColor(getResources().getColor(R.color.selected));
                    btnPickUp.setBackgroundColor(getResources().getColor(R.color.white));
                    btnStale.setTextColor(getResources().getColor(R.color.selected));
                    btnStale.setBackgroundColor(getResources().getColor(R.color.white));

                    adapter=new GridViewAdapter(getApplicationContext(),R.layout.grid_item,orderParent.Orders.OrderArray);
                    gridView.setAdapter(adapter);
                }
                catch(Exception e)
                {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // getOrders();
            }
        });
        MyApp.reqstQ.add(req);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
              case R.id.btnPickUp:
/*
                  SharedPreferences.Editor editor = MyApp.sharedPreferences.edit();

                  editor.putString("Check", "Y");

                  editor.commit();*/
                  orders.clear();
                  checkPDS=1;
              /*    btnPickUp.setTextColor(getResources().getColor(R.color.selected));
                  btnPickUp.setBackgroundColor(getResources().getColor(R.color.white));

                  btnDeliver.setTextColor(getResources().getColor(R.color.white));
                  btnDeliver.setBackgroundColor(getResources().getColor(R.color.selected));*/
                          getOrderedPickup();

                  break;
            case R.id.btnDeliver:
                orders.clear();
                checkPDS=2;

                getOrders();

                break;
            case R.id.btnStale:
                orders.clear();
                checkPDS=2;

                getOrderedOther();

                break;



        }


    }




}
