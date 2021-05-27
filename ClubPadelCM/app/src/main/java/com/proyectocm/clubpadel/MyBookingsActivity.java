package com.proyectocm.clubpadel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyBookingsActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ArrayList<Booking> ls_pendiente = new ArrayList<>();
    private final ArrayList<Booking> ls_finalizada = new ArrayList<>();
    private ListView listView_pendiente, listView_finalizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);


        listView_pendiente = findViewById(R.id.list_view_pendiente);
        listView_finalizada = findViewById(R.id.list_view_finalizada);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Bookings").whereEqualTo("idUser", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot d : Objects.requireNonNull(task.getResult())) {
                    int nFloor = Integer.valueOf(Objects.requireNonNull(d.getData().get("nFloor")).toString());
                    Timestamp time = (Timestamp) d.getData().get("time");
                    String id = Objects.requireNonNull(d.getData().get("idUser")).toString();
                    Booking booking = new Booking(nFloor, id, time);
                    assert time != null;
                    if (time.getSeconds() > Timestamp.now().getSeconds()) {
                        ls_pendiente.add(booking);
                    } else {
                        ls_finalizada.add(booking);
                    }
                }
                Collections.sort(ls_pendiente);
                Collections.sort(ls_finalizada);
                ArrayAdapter<Booking> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, ls_pendiente);
                listView_pendiente.setAdapter(adapter);
                listView_pendiente.setOnItemClickListener((parent, view, position, id) -> {
                    int pista = ls_pendiente.get(position).getnFloor();
                    Pair<String, String> tupla = getDateString(ls_pendiente.get(position).getTime());
                    String hora = tupla.second;
                    String fecha = tupla.first;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyBookingsActivity.this)
                            .setMessage("¿Desea cancelar la pista " + pista + " reservada a las " + hora + " del " + fecha + "?")
                            .setTitle("Anular pista").setPositiveButton("Sí", (dialog, which) -> db.collection("Bookings").whereEqualTo("idUser", ls_pendiente.get(position).getIdUser())
                                    .whereEqualTo("nFloor", ls_pendiente.get(position).getnFloor()).whereEqualTo("time", ls_pendiente.get(position).getTime()).get().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            String id1 = Objects.requireNonNull(task1.getResult()).getDocuments().get(0).getId();
                                            db.collection("Bookings").document(id1).delete().addOnSuccessListener(unused -> {
                                                Toast.makeText(getApplicationContext(), "Pista anulada", Toast.LENGTH_LONG).show();
                                                Intent jumpTo = new Intent(parent.getContext(), MainActivity.class);
                                                startActivity(jumpTo);
                                            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al anular pista", Toast.LENGTH_LONG).show());
                                        }
                                    })).setNegativeButton("No", (dialog, which) -> {

                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                });

                ArrayAdapter<Booking> adapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, ls_finalizada);
                listView_finalizada.setAdapter(adapter2);
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
        boolean res = false;
        LocalDateTime aux = LocalDateTime.ofInstant(Instant.ofEpochSecond(time.getSeconds()), TimeZone.getDefault().toZoneId());
        if (aux.isAfter(LocalDateTime.now())) {
            res = true;
        }
        return res;
    }

}