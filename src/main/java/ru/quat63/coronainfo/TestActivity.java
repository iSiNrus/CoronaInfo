package ru.quat63.coronainfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.AnimatedImageProvider;
import com.yandex.runtime.image.ImageProvider;

import java.util.Random;


public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private MapObjectCollection mapObjects;
    private final Point TARGET_LOCATION = new Point(53.205949, 50.164954);
    private InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            ImageProvider provider =
                    ImageProvider.fromAsset(getApplicationContext(), "placemark.png");
            IconStyle iconStyle = new IconStyle().setScale(4.f);
            mapObjects.addPlacemark(point, provider, iconStyle);
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {
          mapObjects.clear();
        }
    };

    private final MapObjectTapListener mapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            mapview.getMap().getMapObjects().addPlacemark(point);
            Toast.makeText(TestActivity.this,
                    "Here",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }
    };

    private int question_num = 1;

    private LinearLayout q1;
    private LinearLayout q2;
    private LinearLayout q3;
    private LinearLayout q4;
    private LinearLayout q5;

    private Button bt_next;
    private Button bt_back;
    private MapView mapview;
    private EditText et_simptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapKitFactory.setApiKey("1c6518fb-dfaa-4693-94f5-660663f1c913");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_test);
        super.onCreate(savedInstanceState);
        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);

        mapObjects = mapview.getMap().getMapObjects();
        mapview.getMap().addInputListener(inputListener);

        q1 = findViewById(R.id.ques_1);
        q2 = findViewById(R.id.ques_2);
        q3 = findViewById(R.id.ques_3);
        q4 = findViewById(R.id.ques_4);
        q5 = findViewById(R.id.ques_5);

        et_simptoms = findViewById(R.id.et_simptoms);

        bt_next = findViewById(R.id.bt_next);
        bt_back = findViewById(R.id.bt_back);

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(question_num==1) { q1.setVisibility(View.INVISIBLE); q2.setVisibility(View.VISIBLE); }
                if(question_num==2) { q2.setVisibility(View.INVISIBLE); q3.setVisibility(View.VISIBLE); }
                if(question_num==3) { q3.setVisibility(View.INVISIBLE); q4.setVisibility(View.VISIBLE); }
                if(question_num==4) { q4.setVisibility(View.INVISIBLE); q5.setVisibility(View.VISIBLE); bt_next.setText("Завершить"); }
                if(question_num==5) {

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    DatabaseReference reference;
                    if(firebaseUser==null) {
                        Toast.makeText(
                                TestActivity.this,
                                "Ошибка!",
                                Toast.LENGTH_LONG
                        ).show();
                        startActivity(new Intent(TestActivity.this, RegActivity.class));
                        finish();
                    }
                    else{
                        String userId = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        int ch =(int) (new Random().nextInt(60)+20);
                        reference.child("chance").setValue(String.valueOf(ch)+"%");
                        reference.child("simptoms").setValue(et_simptoms.getText().toString());
                        startActivity(new Intent(TestActivity.this, HealthActivity.class));
                        finish();
                    }
                }
                question_num++;
            }
        });
        
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(question_num==1) { question_num++; }
                if(question_num==2) { q2.setVisibility(View.INVISIBLE); q1.setVisibility(View.VISIBLE); }
                if(question_num==3) { q3.setVisibility(View.INVISIBLE); q2.setVisibility(View.VISIBLE); }
                if(question_num==4) { q4.setVisibility(View.INVISIBLE); q3.setVisibility(View.VISIBLE); }
                if(question_num==5) { q5.setVisibility(View.INVISIBLE); q4.setVisibility(View.VISIBLE); bt_next.setText("Далее"); }

                question_num--;
            }
        });

        View bg = findViewById(R.id.test_id);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_simptoms.getWindowToken(), 0);
            }
        });

    }

    @Override
    protected void onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }


}