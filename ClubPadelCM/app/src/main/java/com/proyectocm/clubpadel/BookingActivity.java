package com.proyectocm.clubpadel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private LocalDateTime Today,Day_selected;
    private int cont;
    private String prueba;
    private Pair<Integer,Integer> tupla;
    private List<Pair<Integer,Integer>> lsTuple = new ArrayList<Pair<Integer, Integer>>();
    private Button button1_1,button1_2,button1_3,button1_4,button1_5,button2_1,button2_2,button2_3,button2_4,button2_5;
    private Button button3_1,button3_2,button3_3,button3_4,button3_5,button4_1,button4_2,button4_3,button4_4,button4_5;
    private Button button5_1,button5_2,button5_3,button5_4,button5_5,button6_1,button6_2,button6_3,button6_4,button6_5;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Today = LocalDateTime.now(ZoneId.of("Europe/Madrid"));
        espacioTemporal();
        obtenerDatos(Today);
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
                    Day_selected = Today;
                    obtenerDatos(Day_selected);
                }else if(checkedId==R.id.radio_button_tomorrow){
                    Day_selected = Today.plusDays(1);
                    obtenerDatos(Day_selected);
                }else{
                    Day_selected = Today.plusDays(2);
                    obtenerDatos(Day_selected);
                }
            }
        });
        asociaButton();
        button1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 09:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                .setMessage("¿Desea reservar la pista 1 a las 09:00 el "+aux2+"?").setTitle("Reserva de pista")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Date date = formatter.parse(aux);
                            Timestamp time = new Timestamp(date);
                            Booking booking = new Booking(1,idUser,time);
                            db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                    Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                    startActivity(jumpTo);

                                }
                            });
                        } catch (ParseException e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                })

                .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        button1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 09:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 2 a las 09:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(2,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })

                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        button1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 09:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 3 a las 09:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(3,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })

                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        button1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 09:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 4 a las 09:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(4,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })

                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        button1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 09:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 5 a las 09:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(5,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })

                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        button2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 10:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 1 a las 10:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(1,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })

                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        button2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 10:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 2 a las 10:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(2,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 10:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 3 a las 10:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(3,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 10:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 4 a las 10:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(4,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 10:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 5 a las 10:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(5,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 12:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 1 a las 12:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(1,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 12:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 2 a las 12:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(2,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 12:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 3 a las 12:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(3,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 12:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 4 a las 12:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(4,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button3_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 12:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 5 a las 12:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(5,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        button4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 18:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 1 a las 18:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(1,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 18:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 2 a las 18:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(2,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button4_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 18:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 3 a las 18:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(3,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button4_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 18:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 4 a las 18:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(4,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button4_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 18:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 5 a las 18:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(5,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 19:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 1 a las 19:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(1,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 19:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 2 a las 19:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(2,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button5_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 19:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 3 a las 19:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(3,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button5_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 19:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 4 a las 19:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(4,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button5_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 19:30";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 5 a las 19:30 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(5,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button6_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 21:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 1 a las 21:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(1,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button6_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 21:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 2 a las 21:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(2,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button6_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 21:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 3 a las 21:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(3,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button6_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 21:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 4 a las 21:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(4,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        button6_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                int d = Day_selected.getDayOfMonth();
                int m = Day_selected.getMonthValue();
                int y = Day_selected.getYear();
                String aux = d+"/"+m+"/"+y+" 21:00";
                String aux2 = d+"/"+m+"/"+y;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingActivity.this)
                        .setMessage("¿Desea reservar la pista 5 a las 21:00 el "+aux2+"?").setTitle("Reserva de pista")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Date date = formatter.parse(aux);
                                    Timestamp time = new Timestamp(date);
                                    Booking booking = new Booking(5,idUser,time);
                                    db.collection("Bookings").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Pista reservada", Toast.LENGTH_LONG).show();
                                            Intent jumpTo = new Intent(v.getContext(), MainActivity.class);
                                            startActivity(jumpTo);
                                        }
                                    });
                                } catch (ParseException e) {
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.decline, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
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


    private void obtenerDatos(LocalDateTime day){
        seleccionaButtonIdInversa();
        int d = day.getDayOfMonth();
        int m = day.getMonthValue();
        int y = day.getYear();
        String fecha1 = d+"/"+m+"/"+y+" 9:0";
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
                            for (Integer j=1;j<6;j++){
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
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_1_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 1:
                switch (j){
                    case 1 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_2_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 2:
                switch (j){
                    case 1 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_3_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 3:
                switch (j){
                    case 1 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_4_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 4:
                switch (j){
                    case 1 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_5_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
                break;
            case 5:
                switch (j){
                    case 1 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_1);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 2 :
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_2);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 3:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_3);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 4:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_4);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                    case 5:
                        tupla = Pair.create(i,j);
                        lsTuple.add(tupla);
                        button = findViewById(R.id.reserva_6_5);
                        button.setBackgroundColor(0xffff0000);
                        button.setEnabled(false);
                        break;
                }
        }
    }

    private void seleccionaButtonIdInversa(){
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

    private void asociaButton(){
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

}