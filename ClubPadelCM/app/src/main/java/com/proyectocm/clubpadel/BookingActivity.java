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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)

public class BookingActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth mAuth;

    // Firebase RealtimeDatabase
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;

    private RadioGroup radioGroup;
    private RadioButton radioButton,day1,day2,day3;
    private ImageButton imageButton;
    private Button button;
    private TableLayout table;
    private int day,month,year;
    private LocalDateTime currentDay;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        currentDay = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
        espacioTemporal();
        day = currentDay.getDayOfMonth();
        month = currentDay.getMonthValue();
        year = currentDay.getYear();
        //muestraPistas(currentDay);
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
               // muestraPistas(currentDay);
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

    /*public void muestraPistas(LocalDateTime day){
        mReference = FirebaseDatabase.getInstance("https://club-padel-cm-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        int d = day.getDayOfMonth();
        int m = day.getMonthValue();
        int y = day.getYear();
        String aux;
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

            mReference.child("Bookings")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot e : dataSnapshot.getChildren()){
                                for(Integer i=1;i<6;i++){
                                    for(int j=0;j<6;i++){
                                        if(e.child("nFloor").getValue().toString().equals(i.toString()) && e.child("time").getValue().toString().equals(ls.get(j))) {
                                            //aux = "R.id.reserva"+i+"_"+j+";";

                                        }
                                    }
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

    }*/
}