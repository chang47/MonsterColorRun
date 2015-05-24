package com.brnleehng.worldrunner;

import android.location.Address;
import android.location.Geocoder;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brnleehng.worldrunner.StepDetector.SimpleStepDetector;
import com.brnleehng.worldrunner.StepDetector.StepListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// The running game mode. Will probably require a lot of changes
// to compensate for dungeons, routes, and monster encounters.
// Call in from the Hub most likely
public class Run extends Activity implements SensorEventListener, StepListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView textView;
    private TextView mCoinTextView;
    private TextView mMphTextView;
    private TextView mDistanceTextView;
    private TextView mCaloriesTextView;
    private EditText mDestinationEditView;
    private Button mResetButton;
    private Button mGoButton;
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private int coins;
    private int prevSteps;
    private float mph;
    private float distance;
    private float calories;

    private static final int SPEED_INTERVAL = 3;
    private static final int WALK_STRIDE_LENGTH_FEET = 2;
    private static final int RUN_STRIDE_LENGTH_FEET = 3;
    private static final int MILE = 5280;
    private static final int AVERAGE_WEIGHT = 185;
    private static final double RUNNING_CALORIES = 0.75;
    private static final double WALKING_CALORIES = 0.53;

    private String API_KEY = "&key=AIzaSyCMWX8NQfyZdt-d3Xm9fwvjW5K8rXbXUhM";
    private String URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    private Timer timer;
    private TimerTask timerTask;

    // Google Map
    private GoogleMap googleMap;

    @Override
    public void onConnectionSuspended(int cause){

    }
    double[] coordinates = new double[2];

    @Override
    public void onConnected (Bundle connectionHint){
        Toast.makeText(getApplicationContext(), "Connection Google API client", Toast.LENGTH_LONG).show();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            coordinates[0] = mLastLocation.getLatitude();
            coordinates[1] = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.steps);
        mCoinTextView = (TextView) findViewById(R.id.coins);
        mMphTextView = (TextView) findViewById(R.id.mph);
        mDistanceTextView = (TextView) findViewById(R.id.distance);
        mCaloriesTextView = (TextView) findViewById(R.id.calories);
        mDestinationEditView = (EditText) findViewById(R.id.destination);
        mResetButton = (Button) findViewById(R.id.reset);
        mGoButton = (Button) findViewById(R.id.dest);
        coins = 0;
        mph = 0;
        distance = 0;
        calories = 0;
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSteps = 0;
                coins = 0;
                distance = 0;
                calories = 0;
                textView.setText(TEXT_NUM_STEPS + numSteps);
                mDistanceTextView.setText("Distance traveled " + distance + " Miles");
                mCoinTextView.setText("Number of coins: " + coins);
                mCaloriesTextView.setText("Calories: 0");
                mDestinationEditView.setText("Here's your destination");
            }
        });

        buildGoogleApiClient();

        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSteps = 0;
                coins = 0;
                distance = 0;
                calories = 0;
                textView.setText(TEXT_NUM_STEPS + numSteps);
                mDistanceTextView.setText("Distance traveled " + distance + " Miles");
                mCoinTextView.setText("Number of coins: " + coins);
                mCaloriesTextView.setText("Calories: 0");
                String newText = URL + mDestinationEditView.getText().toString().replaceAll(" ", "+") + API_KEY;
                Geocoder gc = new Geocoder(getApplicationContext());

                if(Geocoder.isPresent()){
                    try{
                        List<Address> list = gc.getFromLocationName(mDestinationEditView.getText().toString(), 1);
                        Address address = list.get(0);

                        double lat = address.getLatitude();
                        double lng = address.getLongitude();

                        PolylineOptions path = new PolylineOptions()
                                .add(new LatLng(coordinates[0], coordinates[1]))
                                .add(new LatLng(lat, lng));

                        String location = "Lat: " + lat + " Lng: " + lng;
                        Toast.makeText(getApplicationContext(), location, Toast.LENGTH_LONG).show();
                        mCaloriesTextView.setText(location);
                        mCoinTextView.setText("Lat: " + coordinates[0] + " Lat: " + coordinates[1]);
                        googleMap.addPolyline(path);
                    }
                    catch(Exception ex) {

                    }

                }
                mDestinationEditView.clearFocus();
                mDestinationEditView.requestFocus(EditText.FOCUS_DOWN);
            }
        });

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new SimpleStepDetector();
        simpleStepDetector.registerListener(this);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(), "Creating Google Maps", Toast.LENGTH_LONG).show();
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            else{
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if(mGoogleApiClient != null) {
            Toast.makeText(getApplicationContext(),
                    "Created Client", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        numSteps = 0;
        coins = 0;
        textView.setText(TEXT_NUM_STEPS + numSteps);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    calculateSpeed();
                }
            };
            timer.schedule(timerTask, 0, SPEED_INTERVAL * 1000);
        } catch (IllegalStateException e) {
            Log.i("Darn", "Resume Error w/ MPH");
        }
    }

    private void calculateSpeed() {
        if (numSteps > prevSteps) {
            int stepsTaken = numSteps - prevSteps;
            prevSteps = numSteps;
            int strideLength = WALK_STRIDE_LENGTH_FEET;
            double calorieRatio = WALKING_CALORIES;
            if (stepsTaken > 2.2) {
                strideLength = RUN_STRIDE_LENGTH_FEET;
                calorieRatio = RUNNING_CALORIES;
            }
            mph = (float) stepsTaken * strideLength * (60 * (60 / SPEED_INTERVAL)) / MILE;
            distance += (float) stepsTaken * strideLength / MILE;

            // 1. Find avg calories burned / mile
            // 2. Find how many calories burned per feet
            // 3. Calculates how many feet were taken during the intervals
            calories += (float) calorieRatio * AVERAGE_WEIGHT / MILE * strideLength * stepsTaken;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        timer.cancel();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
        mMphTextView.setText("Speed: " + mph);
        mDistanceTextView.setText("Distance traveled " + distance + " Miles");
        mCaloriesTextView.setText("Calories: " + (int)calories);
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        if (Math.random() < 0.5) {
            coins++;
        }
        textView.setText(TEXT_NUM_STEPS + numSteps);
        mCoinTextView.setText("Number of coins: " + coins);
    }

}
