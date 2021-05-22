package com.proyectocm.clubpadel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)

public class BookingActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth mAuth;

    // Firebase RealtimeDatabase
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RadioGroup radioGroup;
    private RadioButton radioButton,day1,day2,day3;
    private ImageButton imageButton;
    private Button button;
    private TableLayout table;
    private int day,month,year;
    private LocalDateTime currentDay;
    private int cont;
    private String prueba;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        currentDay = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
        espacioTemporal();
        obtenerDatos(currentDay);
        imageButton = findViewById(R.id.imageButton_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                startActivity(jumpTo);
            }
        });
        radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_today){

                }else if(checkedId==R.id.radio_button_tomorrow){
                    currentDay = currentDay.plusDays(1);
                }else{
                    currentDay = currentDay.plusDays(2);
                }
               obtenerDatos(currentDay);
            }
        });
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void espacioTemporal(){
        day1 = findViewById(R.id.radio_button_today);
        day2 = findViewById(R.id.radio_button_tomorrow);
        day3 = findViewById(R.id.radio_button_nextDay);

        LocalDateTime currentDay = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
        String aux = String.valueOf(currentDay.getDayOfMonth()) + " "+ currentDay.getMonth().toString();
        day1.setText(aux);
        aux = String.valueOf(currentDay.plusDays(1).getDayOfMonth()) + " "+ currentDay.plusDays(1).getMonth().toString();
        day2.setText(aux);
        aux = String.valueOf(currentDay.plusDays(2).getDayOfMonth())+ " "+ currentDay.plusDays(2).getMonth().toString();
        day3.setText(aux);


    }

    public void muestraPistas(LocalDateTime day){
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance("https://club-padel-cm-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        int d = day.getDayOfMonth();
        int m = day.getMonthValue();
        int y = day.getYear();
        String fecha1 = d+"-"+m+"-"+y+" 09:00";
        String fecha2 = d+"-"+m+"-"+y+" 10:30";
        String fecha3 = d+"-"+m+"-"+y+" 12:00";
        String fecha4 = d+"-"+m+"-"+y+" 18:00";
        String fecha5 = d+"-"+m+"-"+y+" 19:30";
        String fecha6 = d+"-"+m+"-"+y+" 21:00";
        List<String> ls = new ArrayList<String>();
        ls.add(fecha1);
        ls.add(fecha2);
        ls.add(fecha3);
        ls.add(fecha4);
        ls.add(fecha5);
        ls.add(fecha6);
        button = findViewById(R.id.reserva_1_1);

        /*for (DataSnapshot e : mReference.child("Bookings").get().getResult().getChildren()) {
            for(int i = 0;i<6;i++){
                for(Integer j=1;j<6;j++){
                    if(e.child("nFloor").getValue().toString().equals(j.toString()) && e.child("time").getValue().toString().equals(ls.get(i))){
                        String aux = "button = findViewById(R.id.reserva_"+i+"_"+j+");";
                        try{
                            Runtime.getRuntime().exec(aux);
                        }catch (Exception l){
                            Toast.makeText(getApplicationContext(),"Algo fue mal", Toast.LENGTH_LONG).show();
                        }
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                    }
                }
            }
        }*/

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
            /*mReference.child("Bookings")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot e : dataSnapshot.getChildren()){
                                for(int i=0;i<5;i++){
                                    for(Integer j=1;j<6;j++){
                                        if(e.child("nFloor").getValue().toString().equals(j.toString()) && e.child("time").getValue().toString().equals(ls.get(i))) {
                                            String aux = "button = findViewById(R.id.reserva_"+i+"_"+j+");";
                                            try{
                                                Runtime.getRuntime().exec(aux);
                                            }catch (Exception l){
                                                Toast.makeText(getApplicationContext(),"Algo fue mal", Toast.LENGTH_LONG).show();
                                            }
                                            button.setBackgroundColor(0xffff0000);
                                            button.setEnabled(false);
                                        }
                                    }
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),"Algo fue mal", Toast.LENGTH_LONG).show();
                        }
                    });
*/
    }

    private void obtenerDatos(LocalDateTime day){
        int d = day.getDayOfMonth();
        int m = day.getMonthValue();
        int y = day.getYear();
        String fecha1 = d+"/"+m+"/"+y+" 09:0 ";
        String fecha2 = d+"/"+m+"/"+y+" 10:30";
        String fecha3 = d+"/"+m+"/"+y+" 12:0";
        String fecha4 = d+"/"+m+"/"+y+" 18:0";
        String fecha5 = d+"/"+m+"/"+y+" 19:30";
        String fecha6 = d+"/"+m+"/"+y+" 21:0";
        final List<String> ls = new ArrayList<String>();
        ls.add(fecha1);
        ls.add(fecha2);
        ls.add(fecha3);
        ls.add(fecha4);
        ls.add(fecha5);
        ls.add(fecha6);
        db.collection("Bookings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot d : task.getResult()){
                        Timestamp time = (Timestamp) d.getData().get("time");
                        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.getSeconds()),TimeZone.getDefault().toZoneId());
                        int e = date.getDayOfMonth();
                        int m = date.getMonthValue();
                        int y = date.getYear();
                        int hour = date.getHour();
                        int minute = date.getMinute();
                        String aux = e+"/"+m+"/"+y+" "+hour+":"+minute;
                        String aux2 = d.getData().get("nFloor").toString();
                        for(int i=0; i<6;i++){
                            for (Integer j=1;j<5;j++){
                                if(aux2.equals(j.toString()) && aux.equals(ls.get(i))){
                                   seleccionaButtonId(i,j);
                                }
                            }
                        }
                    }
                }
            }
        });


    }

    private void seleccionaButtonId(int i, int j){
        switch (i){
            case 0:
                switch (j){
                    case 1 :
                        button = findViewById(R.id.reserva_1_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        button = findViewById(R.id.reserva_1_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        button = findViewById(R.id.reserva_1_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        button = findViewById(R.id.reserva_1_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        button = findViewById(R.id.reserva_1_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 1:
                switch (j){
                    case 1 :
                        button = findViewById(R.id.reserva_2_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        button = findViewById(R.id.reserva_2_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        button = findViewById(R.id.reserva_2_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        button = findViewById(R.id.reserva_2_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        button = findViewById(R.id.reserva_2_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 2:
                switch (j){
                    case 1 :
                        button = findViewById(R.id.reserva_3_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        button = findViewById(R.id.reserva_3_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        button = findViewById(R.id.reserva_3_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        button = findViewById(R.id.reserva_3_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        button = findViewById(R.id.reserva_3_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 3:
                switch (j){
                    case 1 :
                        button = findViewById(R.id.reserva_4_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        button = findViewById(R.id.reserva_4_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        button = findViewById(R.id.reserva_4_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        button = findViewById(R.id.reserva_4_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        button = findViewById(R.id.reserva_4_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 4:
                switch (j){
                    case 1 :
                        button = findViewById(R.id.reserva_5_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        button = findViewById(R.id.reserva_5_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        button = findViewById(R.id.reserva_5_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        button = findViewById(R.id.reserva_5_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        button = findViewById(R.id.reserva_5_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 5:
                switch (j){
                    case 1 :
                        button = findViewById(R.id.reserva_6_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        button = findViewById(R.id.reserva_6_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        button = findViewById(R.id.reserva_6_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        button = findViewById(R.id.reserva_6_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        button = findViewById(R.id.reserva_6_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
        }
    }
}