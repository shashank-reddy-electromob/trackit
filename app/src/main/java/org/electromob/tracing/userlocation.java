package org.electromob.tracing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class userlocation<_> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    GoogleMap mMap;
    FirebaseAuth auth;
    AutoCompleteTextView searchView;
    SupportMapFragment mapFragment;
    static GoogleApiClient client,client1;
    LocationRequest request;
    FirebaseStorage firebaseStorage;
    int raius = 6371;
    LatLng latLng, latlng1, latLng2;
    double latti, longi, latti1, longi1, valueresult, km, meter, distance;
    int kmIndec, kmIndec1, meterIndec;
    DatabaseReference reference, reference1, circlereference;
    FirebaseUser user;
    float time, time1;
    ImageView gps, addppl, myppl, share,bus,dp;
    FusedLocationProviderClient fusedLocationProviderClient;
    String userid, name, email;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    Marker mm, mm1, mm2;
    TextView t1, t2, info;
    private View header;
    private String join_user_id;
    private Location location1;
    ImageView edit;
    private Query query;
    static userlocation instance;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    public static userlocation getInstance() {
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlocation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        user = auth.getCurrentUser();
        searchView = (AutoCompleteTextView) findViewById(R.id.search);
        gps = (ImageView) findViewById(R.id.gps);
        addppl = (ImageView) findViewById(R.id.addpeople);
        myppl = (ImageView) findViewById(R.id.mypeople);
        share = (ImageView) findViewById(R.id.share);
        info = (TextView) findViewById(R.id.infolocation);
        //Lat_Lng_Bounds = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
        bus = (ImageView)findViewById(R.id.bus);
        firebaseStorage = FirebaseStorage.getInstance();


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);
        edit = header.findViewById(R.id.edit123);
        t1 = header.findViewById(R.id.tittle);
        t2 = header.findViewById(R.id.tittleemail);
        dp = header.findViewById(R.id.dp1);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userlocation.this,settings_activity.class));
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userlocation.this,bus_mycircle.class));
            }
        });

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        client1 = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {


                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(getApplicationContext(),"Location Service connected",Toast.LENGTH_SHORT).show();
                        updatelocation();
                    }

                    private void updatelocation() {

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(userlocation.this);
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(request, getPendingIntent());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(getApplicationContext(),"You must accept to proceed",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

        addppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userlocation.this,joincircle.class));
            }
        });

        myppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userlocation.this,mycircle.class));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,"My Location is : " + "https://www.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
                startActivity(i.createChooser(i,"Share using : "));
            }
        });

        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userlocation.this,settings_activity.class));
            }
        });*/

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("users").child(user.getUid()).child("dp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(dp);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);

                t1.setText(name);
                t2.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, client1,
                LAT_LNG_BOUNDS, null);

        searchView.setAdapter(mPlaceAutocompleteAdapter);


       searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                                    String location = searchView.getText().toString();
                                                    List<Address> addressList = null;

                                                    if (location != null || !location.equals("")) {
                                                        Geocoder geocoder = new Geocoder(userlocation.this);
                                                        try {
                                                            addressList = geocoder.getFromLocationName(location, 1);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (mm1 != null) {
                                                            mm1.remove();
                                                        }
                                                        try {
                                                            Address address = addressList.get(0);
                                                            latlng1 = new LatLng(address.getLatitude(), address.getLongitude());
                                                            MarkerOptions options1 = new MarkerOptions().position(latlng1).title(location);
                                                            //Toast.makeText(getApplicationContext(), (int) valueresult,Toast.LENGTH_LONG).show();

                                                            float results[] = new float[10];
                                                            Location.distanceBetween(latLng.latitude, latLng.longitude, latlng1.latitude, latlng1.longitude, results);
                                                            kmIndec = (int) results[0];
                                                            kmIndec = kmIndec / 1000;
                                                            options1.snippet("Distance =  " + kmIndec + " KMS");
                                                            mm1 = mMap.addMarker(options1);
                                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng1, 15), 100, null);

                                                            info.setVisibility(View.VISIBLE);

                                                            info.setText("Your selected location is: " + location + "\nDistance is: " + kmIndec + " KMS" );

                                                        }
                                                        catch(Exception e){
                                                            Toast.makeText(getApplicationContext(),"Not found",Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                    return false;
                                                }
                                            });
               mapFragment.getMapAsync(this);

        if (getIntent().hasExtra("name")) {
            name = getIntent().getStringExtra("name");
            //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

            reference1 = FirebaseDatabase.getInstance().getReference().child("users");

            query = reference1.orderByChild("name").equalTo(name);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        //Toast.makeText(joincircle.this,"Test 1",Toast.LENGTH_SHORT).show();
                       // Toast.makeText(userlocation.this, "TEST 1",Toast.LENGTH_SHORT).show();

                        createuser createuser = null;

                        for (DataSnapshot childDss : dataSnapshot.getChildren())
                        {
                            createuser = childDss.getValue(createuser.class);
                            join_user_id = createuser.userid;

                           // name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);

                            latti = dataSnapshot.child(join_user_id).child("lat").getValue(double.class);
                            longi = dataSnapshot.child(join_user_id).child("lng").getValue(double.class);

                           /* circlereference = FirebaseDatabase.getInstance().getReference().child("users").child(join_user_id);
                            circlereference.child("lat").setValue(latti);
                            circlereference.child("lng").setValue(longi);*/

                            //Toast.makeText(userlocation.this, "TEST 1",Toast.LENGTH_SHORT).show();

                            latLng2 = new LatLng(latti,longi);

                            if (mm2 != null)
                            {
                                mm2.remove();
                            }

                            //latLng = new LatLng(location1.getLatitude(),location1.getLongitude());

                            info.setVisibility(View.INVISIBLE);

                            MarkerOptions options2 = new MarkerOptions();
                            options2.position(latLng2);
                            name = getIntent().getStringExtra("name");
                            options2.title(name);
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            mm2 = mMap.addMarker(options2);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 15), 100, null);

                           /* float results1[] = new float[10];
                            Location.distanceBetween(latti1,longi1,latti,longi, results1);
                            kmIndec1 =  results1[0];
                           // kmIndec1 = kmIndec1/1000;
                            options2.snippet("Distance =  " + kmIndec1 + " Meters" );
                            mm2 = mMap.addMarker(options2);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 15), 100, null);

                            info.setText("Your circle member: " + name + "\nDistance is: " + kmIndec1 + " KMS"  );*/


                        }



                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(this,MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onLocationChanged(Location location) {

        if (location == null)
        {
            Toast.makeText(getApplicationContext(),"could not get location",Toast.LENGTH_LONG).show();
        }
        else
        {
            latti1 = location.getLatitude();
            longi1 = location.getLongitude();
            latLng = new LatLng(latti1,longi1);

            if (mm != null)
            {
                mm.remove();
            }

            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.title("Current Location");
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mm = mMap.addMarker(options);

           //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15),050,null);

            gps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    info.setVisibility(View.INVISIBLE);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15),100,null);
                }
            });



            user = auth.getCurrentUser();
            if (user!=null){
            userid = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
            reference.child("lat").setValue(latLng.latitude);
            reference.child("lng").setValue(latLng.longitude);}




        }
    }

    public void updatelocation(final double value)
    {
        userlocation.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                user = auth.getCurrentUser();
                circlereference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                circlereference.child("lat").setValue(value);
                //Toast.makeText(getApplicationContext(),"TEST 1",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updatelocation1 (final double value1)
    {
        userlocation.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                user = auth.getCurrentUser();
                circlereference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                circlereference.child("lng").setValue(value1);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        client.connect();

        mMap.setOnMarkerClickListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(500);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(client,request,this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userlocation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (id == R.id.nav_joincircle) {

            info.setVisibility(View.INVISIBLE);

            startActivity(new Intent(userlocation.this,joincircle.class));

        } else if (id == R.id.nav_invitemenbers) {

            info.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_mycircle) {

            info.setVisibility(View.INVISIBLE);

            startActivity(new Intent(userlocation.this,mycircle.class));

        } else if (id == R.id.nav_sharelocationtowhatsappmsg) {

            info.setVisibility(View.INVISIBLE);

            Intent i  = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,"My Location is : " + "https://www.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
            startActivity(i.createChooser(i,"Share using : "));

        } else if (id == R.id.nav_settings) {

            info.setVisibility(View.INVISIBLE);

            startActivity(new Intent(userlocation.this,settings_activity.class));

        }else if (id == R.id.nav_signout) {

            info.setVisibility(View.INVISIBLE);

            user = auth.getCurrentUser();
            if (user!=null)
            {
                auth.signOut();
                finish();
                startActivity(new Intent(userlocation.this,MainActivity.class));
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        info.setVisibility(View.INVISIBLE);
        return false;
    }
}
