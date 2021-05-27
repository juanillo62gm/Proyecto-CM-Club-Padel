package com.proyectocm.clubpadel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)

public class BookingActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<Pair<Integer, Integer>> lsTuple = new ArrayList<>();
    private RadioGroup radioGroup;
    private Button button;
    private LocalDateTime Today, Day_selected;
    private Button button1_1, button1_2, button1_3, button1_4, button1_5, button2_1, button2_2, button2_3, button2_4, button2_5, button3_1, button3_2, button3_3, button3_4, button3_5, button4_1, button4_2, button4_3, button4_4, button4_5, button5_1, button5_2, button5_3, button5_4, button5_5, button6_1, button6_2, button6_3, button6_4, button6_5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Today = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
        Day_selected = Today;
        espacioTemporal();
        obtenerDatos(Day_selected);

        radioGroup = findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_button_today) {
                Day_selected = Today;
                obtenerDatos(Day_selected);
            } else if (checkedId == R.id.radio_button_tomorrow) {
                Day_selected = Today.plusDays(1);
                obtenerDatos(Day_selected);
            } else {
                Day_selected = Today.plusDays(2);
                obtenerDatos(Day_selected);
            }
        });
        asociaButton();

        //generaCodigo();

        botonReserva(button1_1, 1, "pista 1", "9:00");
        botonReserva(button1_2, 2, "pista 2", "9:00");
        botonReserva(button1_3, 3, "pista 3", "9:00");
        botonReserva(button1_4, 4, "pista 4", "9:00");
        botonReserva(button1_5, 5, "pista 5", "9:00");

        botonReserva(button2_1, 1, "pista 1", "10:30");
        botonReserva(button2_2, 2, "pista 2", "10:30");
        botonReserva(button2_3, 3, "pista 3", "10:30");
        botonReserva(button2_4, 4, "pista 4", "10:30");
        botonReserva(button2_5, 5, "pista 5", "10:30");

        botonReserva(button3_1, 1, "pista 1", "12:00");
        botonReserva(button3_2, 2, "pista 2", "12:00");
        botonReserva(button3_3, 3, "pista 3", "12:00");
        botonReserva(button3_4, 4, "pista 4", "12:00");
        botonReserva(button3_5, 5, "pista 5", "12:00");

        botonReserva(button4_1, 1, "pista 1", "18:00");
        botonReserva(button4_2, 2, "pista 2", "18:00");
        botonReserva(button4_3, 3, "pista 3", "18:00");
        botonReserva(button4_4, 4, "pista 4", "18:00");
        botonReserva(button4_5, 5, "pista 5", "18:00");

        botonReserva(button5_1, 1, "pista 1", "19:30");
        botonReserva(button5_2, 2, "pista 2", "19:30");
        botonReserva(button5_3, 3, "pista 3", "19:30");
        botonReserva(button5_4, 4, "pista 4", "19:30");
        botonReserva(button5_5, 5, "pista 5", "19:30");

        botonReserva(button6_1, 1, "pista 1", "21:30");
        botonReserva(button6_2, 2, "pista 2", "21:30");
        botonReserva(button6_3, 3, "pista 3", "21:30");
        botonReserva(button6_4, 4, "pista 4", "21:30");
        botonReserva(button6_5, 5, "pista 5", "21:30");
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        findViewById(radioId);
    }

    public void espacioTemporal() {
        RadioButton day1 = findViewById(R.id.radio_button_today);
        RadioButton day2 = findViewById(R.id.radio_button_tomorrow);
        RadioButton day3 = findViewById(R.id.radio_button_nextDay);

        LocalDateTime currentDay = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
        String aux = currentDay.getDayOfMonth() + " " + currentDay.getMonth().toString();
        day1.setText(aux);
        aux = currentDay.plusDays(1).getDayOfMonth() + " " + currentDay.plusDays(1).getMonth().toString();
        day2.setText(aux);
        aux = currentDay.plusDays(2).getDayOfMonth() + " " + currentDay.plusDays(2).getMonth().toString();
        day3.setText(aux);
    }

    private void obtenerDatos(LocalDateTime day) {
        seleccionaButtonIdInversa();
        int d = day.getDayOfMonth();
        int m = day.getMonthValue();
        int y = day.getYear();
        String fecha1 = d + "/" + m + "/" + y + " 9:0";
        String fecha2 = d + "/" + m + "/" + y + " 10:30";
        String fecha3 = d + "/" + m + "/" + y + " 12:0";
        String fecha4 = d + "/" + m + "/" + y + " 18:0";
        String fecha5 = d + "/" + m + "/" + y + " 19:30";
        String fecha6 = d + "/" + m + "/" + y + " 21:0";
        final List<String> ls = new ArrayList<>();
        ls.add(fecha1);
        ls.add(fecha2);
        ls.add(fecha3);
        ls.add(fecha4);
        ls.add(fecha5);
        ls.add(fecha6);
        if (day == Today) {
            if (day.getHour() >= 21) {
                for (int i = 0; i < 6; i++) {
                    for (int j = 1; j < 6; j++) {
                        seleccionaButtonId(i, j);
                    }
                }
            } else if (day.getHour() >= 19) {
                for (int i = 0; i < 5; i++) {
                    for (int j = 1; j < 6; j++) {
                        seleccionaButtonId(i, j);
                    }
                }
            } else if (day.getHour() >= 18) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 1; j < 6; j++) {
                        seleccionaButtonId(i, j);
                    }
                }
            } else if (day.getHour() >= 12) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 1; j < 6; j++) {
                        seleccionaButtonId(i, j);
                    }
                }
            } else if (day.getHour() >= 10) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 1; j < 6; j++) {
                        seleccionaButtonId(i, j);
                    }
                }
            } else if (day.getHour() >= 9) {
                for (int j = 1; j < 6; j++) {
                    seleccionaButtonId(0, j);
                }
            }
        }
        db.collection("Bookings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot d1 : Objects.requireNonNull(task.getResult())) {
                    Timestamp time = (Timestamp) d1.getData().get("time");
                    LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(Objects.requireNonNull(time).getSeconds()), TimeZone.getDefault().toZoneId());
                    int e = date.getDayOfMonth();
                    int m1 = date.getMonthValue();
                    int y1 = date.getYear();
                    int hour = date.getHour();
                    int minute = date.getMinute();
                    String aux = e + "/" + m1 + "/" + y1 + " " + hour + ":" + minute;
                    String aux2 = Objects.requireNonNull(d1.getData().get("nFloor")).toString();
                    for (int i = 0; i < 6; i++) {
                        for (int j = 1; j < 6; j++) {
                            if (aux2.equals(Integer.toString(j)) && aux.equals(ls.get(i))) {
                                seleccionaButtonId(i, j);
                            }

                        }
                    }
                }
            }
        });


    }

    private void seleccionaButtonId(int i, int j) {
        Pair<Integer, Integer> tupla;
        switch (i) {
            case 0:
                switch (j) {
                    case 1:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 1:
                switch (j) {
                    case 1:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 2:
                switch (j) {
                    case 1:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 3:
                switch (j) {
                    case 1:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 4:
                switch (j) {
                    case 1:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 5:
                switch (j) {
                    case 1:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i, j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
        }
    }

    private void seleccionaButtonIdInversa() {
        for (int k = 0; k < lsTuple.size(); k++) {
            Integer i = lsTuple.get(k).first;
            Integer j = lsTuple.get(k).second;
            switch (i) {
                case 0:
                    switch (j) {
                        case 1:
                            button = findViewById(R.id.reserva_1_1);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 2:
                            button = findViewById(R.id.reserva_1_2);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 3:

                            button = findViewById(R.id.reserva_1_3);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 4:
                            button = findViewById(R.id.reserva_1_4);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 5:
                            button = findViewById(R.id.reserva_1_5);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                    }
                    break;
                case 1:
                    switch (j) {
                        case 1:
                            button = findViewById(R.id.reserva_2_1);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 2:
                            button = findViewById(R.id.reserva_2_2);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 3:
                            button = findViewById(R.id.reserva_2_3);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 4:
                            button = findViewById(R.id.reserva_2_4);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 5:
                            button = findViewById(R.id.reserva_2_5);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                    }
                    break;
                case 2:
                    switch (j) {
                        case 1:
                            button = findViewById(R.id.reserva_3_1);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 2:
                            button = findViewById(R.id.reserva_3_2);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 3:
                            button = findViewById(R.id.reserva_3_3);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 4:
                            button = findViewById(R.id.reserva_3_4);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 5:
                            button = findViewById(R.id.reserva_3_5);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));

                            button.setEnabled(true);
                            break;
                    }
                    break;
                case 3:
                    switch (j) {
                        case 1:
                            button = findViewById(R.id.reserva_4_1);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 2:
                            button = findViewById(R.id.reserva_4_2);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 3:
                            button = findViewById(R.id.reserva_4_3);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 4:
                            button = findViewById(R.id.reserva_4_4);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 5:
                            button = findViewById(R.id.reserva_4_5);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                    }
                    break;
                case 4:
                    switch (j) {
                        case 1:
                            button = findViewById(R.id.reserva_5_1);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 2:
                            button = findViewById(R.id.reserva_5_2);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 3:
                            button = findViewById(R.id.reserva_5_3);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 4:
                            button = findViewById(R.id.reserva_5_4);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 5:
                            button = findViewById(R.id.reserva_5_5);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                    }
                    break;
                case 5:
                    switch (j) {
                        case 1:
                            button = findViewById(R.id.reserva_6_1);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 2:
                            button = findViewById(R.id.reserva_6_2);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 3:
                            button = findViewById(R.id.reserva_6_3);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 4:
                            button = findViewById(R.id.reserva_6_4);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                        case 5:
                            button = findViewById(R.id.reserva_6_5);
                            button.setBackgroundColor(Color.parseColor("#99FF99"));
                            button.setEnabled(true);
                            break;
                    }
            }
        }
        lsTuple.clear();
    }

    private void asociaButton() {
        button1_1 = findViewById(R.id.reserva_1_1);
        button1_2 = findViewById(R.id.reserva_1_2);
        button1_3 = findViewById(R.id.reserva_1_3);
        button1_4 = findViewById(R.id.reserva_1_4);
        button1_5 = findViewById(R.id.reserva_1_5);
        button2_1 = findViewById(R.id.reserva_2_1);
        button2_2 = findViewById(R.id.reserva_2_2);
        button2_3 = findViewById(R.id.reserva_2_3);
        button2_4 = findViewById(R.id.reserva_2_4);
        button2_5 = findViewById(R.id.reserva_2_5);
        button3_1 = findViewById(R.id.reserva_3_1);
        button3_2 = findViewById(R.id.reserva_3_2);
        button3_3 = findViewById(R.id.reserva_3_3);
        button3_4 = findViewById(R.id.reserva_3_4);
        button3_5 = findViewById(R.id.reserva_3_5);
        button4_1 = findViewById(R.id.reserva_4_1);
        button4_2 = findViewById(R.id.reserva_4_2);
        button4_3 = findViewById(R.id.reserva_4_3);
        button4_4 = findViewById(R.id.reserva_4_4);
        button4_5 = findViewById(R.id.reserva_4_5);
        button5_1 = findViewById(R.id.reserva_5_1);
        button5_2 = findViewById(R.id.reserva_5_2);
        button5_3 = findViewById(R.id.reserva_5_3);
        button5_4 = findViewById(R.id.reserva_5_4);
        button5_5 = findViewById(R.id.reserva_5_5);
        button6_1 = findViewById(R.id.reserva_6_1);
        button6_2 = findViewById(R.id.reserva_6_2);
        button6_3 = findViewById(R.id.reserva_6_3);
        button6_4 = findViewById(R.id.reserva_6_4);
        button6_5 = findViewById(R.id.reserva_6_5);


    }

    private void botonReserva(Button numero, Integer numeroPista, String pista, String hora) {
        numero.setOnClickListener(v -> {

            String idUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            int d = Day_selected.getDayOfMonth();
            int m = Day_selected.getMonthValue();
            int y = Day_selected.getYear();
            String aux = d + "/" + m + "/" + y + " " + hora;
            String aux2 = d + "/" + m + "/" + y;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                    .setMessage("¿Desea reservar la " + pista + " a las " + hora + " el " + aux2 + "?").setTitle("Reserva de pista")
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        try {
                            Date date = formatter.parse(aux);
                            Timestamp time = new Timestamp(Objects.requireNonNull(date));
                            Booking booking = new Booking(numeroPista, idUser, time);
                            db.collection("Bookings").add(booking).addOnSuccessListener(documentReference -> {
                                Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                startActivity(jumpTo);

                            });
                        } catch (ParseException e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    })

                    .setNegativeButton(R.string.decline, (dialog, which) -> {

                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

}