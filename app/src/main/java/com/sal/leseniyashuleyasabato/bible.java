package com.sal.leseniyashuleyasabato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class bible extends AppCompatActivity implements RecyclerViewClicks {
    RecyclerView bibleBooks;
    RecyclerView.LayoutManager bibleBooksManager;
    List<Books> bookTitles;
    bibleAdapter Adapter;

    String []files;

    public final String []books = {"Mwanzo", "Kutoka", "Mambo_ya_Walawi", "Hesabu", "KumbuKumbu_la_torati", "Yoshua",
    "Waamuzi", "Ruthu", "1Samweli", "2Samweli", "1Wafalme", "2Wafalme", "1Mambo_ya_Nyakati", "2Mambo_ya_Nyakati",
    "Ezra", "Nehemia", "Esta", "Ayubu", "Zaburi", "Mithali", "Mhubiri", "Wimbo_ulio_Bora", "Isaya", "Yeremia", "Maombolezo",
    "Ezekieli", "Danieli", "Hosea", "Yoeli", "Amosi", "Obadia", "Yona", "Mika", "Nahumu", "Habakuki", "Sefania", "Hagai",
    "Zekaria", "Malaki", "Mathayo", "Marko", "Luka", "Yohana", "Matendo_ya_Mitume", "Warumi", "1Wakorintho", "2Wakorintho",
    "Wagalatia", "Waefeso", "Wafilipi", "Wakolosai", "1Wathesalonike", "2Wathesalonike", "1Timotheo", "2Timotheo", "Tito",
    "Filemoni", "Waeberania", "Yakobo", "1Petro", "2Petro", "1Yohana", "2Yohana", "3Yohana", "Yuda", "Ufunuo_wa_Yohana"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);

        Toolbar myToolbar = findViewById(R.id.bible_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        try {
            fillBooks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initBooks();
    }

    private void fillBooks() throws IOException {
        this.bookTitles = new ArrayList<>();
        AssetManager assetManager = getApplicationContext().getAssets();
        files = assetManager.list("books");

        for (int i = 0; i < books.length; i ++){
            bookTitles.add(new Books(Integer.toString(i+1), books[i]));
        }

    }

    private void initBooks() {
        this.Adapter = new bibleAdapter(this.bookTitles, this);
        this.bibleBooks = findViewById(R.id.bible_books);
        this.bibleBooksManager = new LinearLayoutManager(getApplicationContext());
        this.bibleBooks.setAdapter(this.Adapter);
        this.bibleBooks.setLayoutManager(this.bibleBooksManager);
        this.Adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(bible.this, BibleChapters.class);

        String positions;
        positions = books[position].toLowerCase();
        intent.putExtra("book", positions);

        for (String book: files) {
            if (positions.equals(book.replace(".txt", "").trim()))
                intent.putExtra("bookName", book);
        }

        startActivity(intent);
    }
}