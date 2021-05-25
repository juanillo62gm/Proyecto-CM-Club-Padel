package com.proyectocm.clubpadel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyBookingsActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView_pendiente, listView_finalizada;
    private ArrayList<Booking> ls_pendiente = new ArrayList<Booking>();
    private ArrayList<Booking> ls_finalizada = new ArrayList<Booking>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);


        listView_pendiente = (ListView) findViewById(R.id.list_view_pendiente);
        listView_finalizada = (ListView) findViewById(R.id.list_view_finalizada);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Bookings").whereEqualTo("idUser", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot d : task.getResult()) {
                        int nFloor = Integer.valueOf(d.getData().get("nFloor").toString());
                        Timestamp time = (Timestamp) d.getData().get("time");
                        String id = d.getData().get("idUser").toString();
                        Booking booking = new Booking(nFloor, id, time);
                        if (time.getSeconds() > Timestamp.now().getSeconds()) {
                            ls_pendiente.add(booking);
                        } else {
                            ls_finalizada.add(booking);
                        }
                    }
                    Collections.sort(ls_pendiente);
                    Collections.sort(ls_finalizada);
                    ArrayAdapter<Booking> adapter = new ArrayAdapter<Booking>(getApplicationContext(), android.R.layout.simple_list_item_1, ls_pendiente);
                    listView_pendiente.setAdapter(adapter);
                    listView_pendiente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int pista = ls_pendiente.get(position).getnFloor();
                            Pair<String, String> tupla = getDateString(ls_pendiente.get(position).getTime());
                            String hora = tupla.second;
                            String fecha = tupla.first;
                            AlertDialog.Builder builder = new AlertDialog.Builder(MyBookingsActivity.this)
                                    .setMessage("¿Desea cancelar la pista " + pista + " reservada a las " + hora + " del " + fecha + "?")
                                    .setTitle("Anular pista").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection("Bookings").whereEqualTo("idUser", ls_pendiente.get(position).getIdUser())
                                                    .whereEqualTo("nFloor", ls_pendiente.get(position).getnFloor()).whereEqualTo("time", ls_pendiente.get(position).getTime()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        String id = task.getResult().getDocuments().get(0).getId();
                                                        db.collection("Bookings").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(getApplicationContext(), "Pista anulada", Toast.LENGTH_LONG).show();
                                                                Intent jumpTo = new Intent(parent.getContext(), MainActivity.class);
                                                                startActivity(jumpTo);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Error al anular pista", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });

                    ArrayAdapter<Booking> adapter2 = new ArrayAdapter<Booking>(getApplicationContext(), android.R.layout.simple_list_item_1, ls_finalizada);
                    listView_finalizada.setAdapter(adapter2);
                }
            }
        });
    }


    private Pair<String, String> getDateString(Timestamp time) {
        String res;
        String min;
        Pair<String, String> tupla;
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.getSeconds()), TimeZone.getDefault().toZoneId());
        int e = date.getDayOfMonth();
        int m = date.getMonthValue();
        int y = date.getYear();
        int hour = date.getHour();
        int minute = date.getMinute();
        if (minute == 0) {
            min = "00";
        } else {
            min = "30";
        }
        res = e + "/" + m + "/" + y + " " + hour + ":" + min;
        String hora = hour + ":" + min;
        tupla = Pair.create(res, hora);
        return tupla;
    }

    private Boolean pistaCancelable(Timestamp time) {
        Boolean res = false;
        LocalDateTime aux = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.getSeconds()), TimeZone.getDefault().toZoneId());
        if (aux.isAfter(LocalDateTime.now())) {
            res = true;
        }
        return res;
    }

}