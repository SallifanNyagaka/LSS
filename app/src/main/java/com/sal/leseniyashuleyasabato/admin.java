package com.sal.leseniyashuleyasabato;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class admin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextInputEditText Content;
    Spinner Quarter;
    TextInputEditText Question;
    TextInputEditText Title;
    String currentDateString;
    Integer day;
    String friday;
    Integer month;
    String quarterDisplay;
    String saturday;
    Spinner week;
    String weekDisplay;
    Integer week_day;
    Integer year;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TITLE = "title";
    private final String DATE = "date";
    private final String CONTENT = FirebaseAnalytics.Param.CONTENT;
    private final String QUESTION = "question";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        Button datePicker = findViewById(R.id.date_picker);
        Button upload = findViewById(R.id.Upload);
        final TextView uploader = findViewById(R.id.uploaderS);
        this.Title = findViewById(R.id.title);
        this.Content = findViewById(R.id.content);
        this.Question = findViewById(R.id.day_question);
        this.Quarter = findViewById(R.id.quarter);
        this.week = findViewById(R.id.week);

        ArrayAdapter<CharSequence> quartersAdapter = ArrayAdapter.createFromResource(this, R.array.quarter, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        quartersAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> weeksAdapter = ArrayAdapter.createFromResource(this, R.array.week, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        weeksAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        this.Quarter.setAdapter(quartersAdapter);
        this.Quarter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.sal.leseniyashuleyasabato.admin.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                admin.this.quarterDisplay = adapterView.getItemAtPosition(i).toString();
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.week.setAdapter(weeksAdapter);
        this.week.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.sal.leseniyashuleyasabato.admin.2
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                admin.this.weekDisplay = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                admin.this.uploadContent(uploader);
            }
        });
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                admin.this.DatePicker(view);
            }
        });
    }

    public void uploadContent(TextView uploader) {
        String title = Objects.requireNonNull(this.Title.getText()).toString();
        String content = Objects.requireNonNull(this.Content.getText()).toString();
        String question = Objects.requireNonNull(this.Question.getText()).toString();
        Map<String, Object> LessonUpload = new HashMap<>();
        Map<String, String> weekTitle = new HashMap<>();
        String collectionName = this.quarterDisplay + "-" + this.year.toString();
        String weekDocument = this.weekDisplay;
        Calendar.getInstance();

        String dayDocumentName = weekDocument + "_" + this.month.toString() + "-" + this.day.toString();
        LessonUpload.put(DATE, this.currentDateString);
        LessonUpload.put(TITLE, title);
        LessonUpload.put(CONTENT, content);
        LessonUpload.put(QUESTION, question);

        weekTitle.put("title", title);
        weekTitle.put("saturday", this.saturday);
        weekTitle.put("friday", this.friday);

        this.db.collection(collectionName).document(weekDocument).set(weekTitle);
        this.db.collection(collectionName).document(weekDocument).collection("week").document(dayDocumentName).set(LessonUpload).addOnSuccessListener(new OnSuccessListener<Void>() { // from class: com.sal.leseniyashuleyasabato.admin.3
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public void onSuccess(Void unused) {
                Toast.makeText(admin.this.getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exc) {
                admin.this.uploadFail(exc);
            }
        });
        uploader.setText(this.currentDateString + "\n" + title + "\n" + content + "\n" + question + "\n" + this.year + "\n" + this.quarterDisplay + "\n" + this.weekDisplay);
    }


    public void uploadFail(Exception e) {
        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
        Log.w("tag", "ERROR:", e);
    }

    public void DatePicker(View view) {
        DialogFragment pickDate = new DatePickerFragment();
        pickDate.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(1, i);
        c.set(2, i1);
        c.set(5, i2);
        this.currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        this.year = i;
        this.month = i1 + 1;
        this.day = i2;
        int valueOf = c.get(7);
        this.week_day = valueOf;
        if (valueOf == 7) {
            this.saturday = DateFormat.getDateInstance().format(c.getTime());
        }
        if (this.week_day == 6) {
            this.friday = DateFormat.getDateInstance().format(c.getTime());
        }
        TextView showDate = findViewById(R.id.date_display);
        showDate.setText(this.currentDateString);
    }
}