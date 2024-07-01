package com.sal.leseniyashuleyasabato;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

/* loaded from: classes3.dex */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {
    String Q;
    Spinner QWeeks;
    RecyclerView day;
    String day_content;
    String day_question;
    String day_title;
    String days;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DrawerLayout drawer;
    Adapter lesson_adapter;
    List<LessonModels> lesson_days;
    Integer month;
    String quarterCollectionPath;
    CollectionReference quarterWeekDays;
    String quarterWeekDocumentPath;
    int quarterWeeks;
    String title;
    Integer today;
    TextView verses;
    DocumentReference weekT;
    TextView weekTitle, keyText, week_summary_content;
    Integer weekday;
    RecyclerView.LayoutManager wk_day_manager;
    Integer year;
    
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        this.weekTitle = findViewById(R.id.weekTitle);
        this.verses = findViewById(R.id.verses);
        verses.setMovementMethod(LinkMovementMethod.getInstance());
        this.keyText = findViewById(R.id.memory_text);
        keyText.setMovementMethod(LinkMovementMethod.getInstance());
        this.week_summary_content = findViewById(R.id.wk_summary_content);
        week_summary_content.setMovementMethod(LinkMovementMethod.getInstance());
        String mafungu = getString(R.string.verses);
        String keytxt = "1Mambo_ya_Nyakati 22:6-10; Mungu alimwambia Daudi kwamba Suleimani atalijenga hekalu.";
        String wk_content = "Vyote ambavyo mtu hupokea kama fadhila za Mungu bado ni vya Mungu. Chochote ambacho Mungu amempatia. Chochote ambacho Mungu ameweka katika vitu vya thamani na vizuri vya Dunia kinawekwa mikononi mwa wanadamu ili kuwajaribu-kuonyesha kina cha upendo wao Kwake na uthamini wao kwa kibali chake. Iwe ni hazina za mali au za akili, zinapaswa kuwekwa, sadaka ya hiari, miguuni pa Yesu; mpaji akisema, wakati huo huo, pamoja na Daudi, 'Vitu vyote vyatoka kwako, na katika mali yako tumekutolea.' Mungu ametupatia kila kitu, na ametuweka kuwa mawakili Wake (Mwanzo 1:27-28;). Pia anategemea mwitikio wa upendo (Mathayo 22:37;). Tutaoneshaje kwamba tunampenda? Yohana alilielezea hilo: 'Kwa maana huku ndiko kumpenda Mungu, kwamba tuzishike amri zake; wala amri zake si nzito.' (1Yohana 5:3;) Kumpenda Mungu hujumuisha kuzitii amri zake. Hatuwezi kusema kwamba tunampenda Baba yetu wa mbinguni ikiwa hatumtii.";
        
        this.verses.setText(spannableBibleText(mafungu));
        this.keyText.setText(spannableBibleText(keytxt));
        this.week_summary_content.setText(spannableBibleText(wk_content));


        this.QWeeks = findViewById(R.id.quarterWeeks);
        Calendar currentCal = Calendar.getInstance();
        this.year = currentCal.get(1);
        this.month = currentCal.get(2) + 1;
        this.today = currentCal.get(5);
        this.weekday = currentCal.get(7);
        String str = Quarter() + "-" + this.year.toString();
        this.quarterCollectionPath = str;
        this.quarterWeekDocumentPath = "WK-1";
        this.weekT = this.db.collection(str).document(this.quarterWeekDocumentPath);
        this.quarterWeekDays = this.db.collection(this.quarterCollectionPath).document(this.quarterWeekDocumentPath).collection("week");
        this.drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();
        lesson_init();
        initDays();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        this.weekT.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { // from class: com.sal.leseniyashuleyasabato.MainActivity.1
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // com.google.firebase.firestore.EventListener
            public void onEvent(DocumentSnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Error while loading", Toast.LENGTH_SHORT).show();
                }
                if (value == null) {
                    throw new AssertionError();
                }
                if (value.exists()) {
                    MainActivity.this.title = value.getString("title");
                }
            }
        });
    }

    public String Quarter() {
        String[] Qs = {"Q1", "Q2", "Q3", "Q4"};
        if (this.month != 0 && this.month < 4) {
            this.Q = Qs[0];
        } else if (this.month >= 4 && this.month < 7) {
            this.Q = Qs[1];
        } else if (this.month >= 7 && this.month < 10) {
            this.Q = Qs[2];
        } else if (this.month >= 10 && this.month <= 12) {
            this.Q = Qs[3];
        }
        return this.Q;
    }

    @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.admin_section) {
            Intent intent = new Intent(getApplicationContext(), admin.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initDays() {
        this.lesson_adapter = new Adapter(this.lesson_days);
        this.day = findViewById(R.id.wk_day);
        this.wk_day_manager = new LinearLayoutManager(getApplicationContext());
        this.day.setAdapter(this.lesson_adapter);
        this.day.setLayoutManager(this.wk_day_manager);
    }

    private void lesson_init() {
        this.lesson_days = new ArrayList();
        String quarterCollectionPath = Quarter() + "-" + this.year.toString();
        ArrayList<String> titles = new ArrayList<>();
        this.db.collection(quarterCollectionPath).get().addOnSuccessListener(new AnonymousClass2(titles, quarterCollectionPath, R.drawable.share_today));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.sal.leseniyashuleyasabato.MainActivity$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements OnSuccessListener<QuerySnapshot> {
        final /* synthetic */ String val$quarterCollectionPath;
        final /* synthetic */ int val$share_icon;
        final /* synthetic */ ArrayList val$titles;

        AnonymousClass2(ArrayList arrayList, String str, int i) {
            this.val$titles = arrayList;
            this.val$quarterCollectionPath = str;
            this.val$share_icon = i;
        }

        @Override // com.google.android.gms.tasks.OnSuccessListener
        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            for (QueryDocumentSnapshot title : queryDocumentSnapshots) {
                this.val$titles.add(title.getString("title"));
            }
            ArrayAdapter<String> spinnerWks = new ArrayAdapter<>(MainActivity.this.getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, this.val$titles);
            spinnerWks.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            MainActivity.this.QWeeks.setAdapter(spinnerWks);
            MainActivity.this.QWeeks.setOnItemSelectedListener(new AnonymousClass1());
        }


        public class AnonymousClass1 implements AdapterView.OnItemSelectedListener {
            AnonymousClass1() {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String selectedTitle = adapterView.getItemAtPosition(i).toString();
                MainActivity.this.db.collection(AnonymousClass2.this.val$quarterCollectionPath).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // from class: com.sal.leseniyashuleyasabato.MainActivity.2.1.1
                    @Override // com.google.android.gms.tasks.OnSuccessListener
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                            if (selectedTitle.equals(snapshot.getString("title"))) {
                                MainActivity.this.lesson_days.clear();
                                MainActivity.this.db.collection(AnonymousClass2.this.val$quarterCollectionPath).document(snapshot.getId()).collection("week").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { // from class: com.sal.leseniyashuleyasabato.MainActivity.2.1.1.1
                                    @Override // com.google.android.gms.tasks.OnSuccessListener
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {
                                        for (QueryDocumentSnapshot snapshot2 : queryDocumentSnapshots2) {
                                            MainActivity.this.days = snapshot2.getString("date");
                                            MainActivity.this.day_title = snapshot2.getString("title");
                                            MainActivity.this.day_content = snapshot2.getString(FirebaseAnalytics.Param.CONTENT);
                                            MainActivity.this.day_question = snapshot2.getString("question");
                                            MainActivity.this.lesson_days.add(new LessonModels(MainActivity.this.days, AnonymousClass2.this.val$share_icon, MainActivity.this.day_title, MainActivity.this.day_content, MainActivity.this.day_question));
                                        }
                                        MainActivity.this.lesson_adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }
                });
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.calender) {
            DialogFragment pickDate = new DatePickerFragment();
            pickDate.show(getSupportFragmentManager(), "date picker");
            Toast.makeText(getApplicationContext(), "calender selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.bible){
            Intent bible = new Intent(getApplicationContext(), com.sal.leseniyashuleyasabato.bible.class);
            startActivity(bible);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }

    @Override // android.app.DatePickerDialog.OnDateSetListener
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(1, i);
        c.set(2, i1);
        c.set(5, i2);
        this.year = i;
        this.month = i1 + 1;
        this.today = i2;
    }
    
    public ArrayList<Integer> bible_Chapters(String fileName, int chaptr){
        
        ArrayList<Integer> chapters =new ArrayList<>();
        Integer prevline = 0;
        ArrayList<BibleMemory> chapterNlineNo = new ArrayList<>();
        ArrayList<Integer> preVerse = new ArrayList<>();
        
        try {
        	InputStream stream = getAssets().open(fileName);
            BufferedReader read = new BufferedReader(new InputStreamReader(stream));
            StringBuffer result = new StringBuffer();
            String buffer;
            chapterNlineNo = new ArrayList<>();
            Integer Chapt = 0;
            Integer ChaptLine = 0;
            
            while((buffer = read.readLine()) != null) {
            	result.append(buffer).append("\n");
                ChaptLine +=1 ;
                prevline = ChaptLine;
                
                if (!buffer.startsWith(" ")){
                    Chapt += 1;
                    chapterNlineNo.add(new BibleMemory(Chapt, ChaptLine));
                }    
            }
        
        } catch(IOException err) {
        	Toast.makeText(getApplicationContext(), err.toString(), Toast.LENGTH_LONG).show();
        }
        
        for (BibleMemory memory: chapterNlineNo) {
            int chapter = memory.getLineIncrement();
            Integer chapterNo = memory.getChapterLine();
            if(chapterNo > 1) {
                preVerse.add(chapterNo);

            }
            if (chapter == chaptr){
                chapters.add(chapterNo);
            }
        }
        preVerse.add(prevline+1);
        int stop = preVerse.get(chaptr-1);
        chapters.add(stop);
        
        return chapters;
    }
    
    public SpannableString spannableBibleText(String mafungu){
        ArrayList<BibleMemory> spannables = new ArrayList<>();
        SpannableString spannableString = new SpannableString(mafungu);
        
        
        
        try {
            
            String bks[] = {"Mwanzo", "1Yohana", "1mambo_ya_nyakati", "mathayo"};
        
            ArrayList<String> bksInTxt = new ArrayList<String>();
            
            
            for(String vs:bks){
                for(int i = 0; i < mafungu.length()-vs.length(); i++) {
                    
                    if(mafungu.startsWith(vs, i)){
                        bksInTxt.add(vs);
                    }
                	
                }
            }
            
            
            for (String verse:bksInTxt) {
                int start =0;
                
                for (int i = 0; i<mafungu.length()-verse.length(); i++){
                    StringBuilder comVerse = new StringBuilder();

                    if (mafungu.startsWith(verse, i)){
                        start = i;
                        comVerse.append(verse);
                        int contin = start+verse.length();
                
                        for(int j = contin; j<mafungu.length(); j++){
                            if(mafungu.charAt(j)!=';'){
                                comVerse.append(mafungu.charAt(j));
                            }else{
                                break;
                            }
                        }
                        
                    }
                    
                    int stop = start + (comVerse.length());
                    spannables.add(new BibleMemory(start, stop));
                }
            }
            
            
        }catch (StringIndexOutOfBoundsException E){
            Toast.makeText(getApplicationContext(), spannables.toString(), Toast.LENGTH_LONG).show();
        }


        for (BibleMemory spannable:spannables){
            int startIndex = spannable.getLineIncrement();
            int stopIndex = spannable.getChapterLine();
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
            spannableString.setSpan(colorSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(), startIndex,  stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(@NonNull View widget) {
                    Intent bible = new Intent(getApplicationContext(), Bible_Read.class);
                    String spanner = spannableString.toString().trim();
                    String compVerse = spanner.substring(startIndex, stopIndex);
                    Integer chapter = 1;
                    Integer verse = 1;
                    Integer to = 1;            
                    String book = "books/mwanzo.txt";
                        
                    try{
                      String []ChaptVerse = compVerse.split(":", 2);
                      String []booksection = ChaptVerse[0].split(" ", 2);
                      chapter = Integer.parseInt(booksection[1].trim());
                      book = "books/"+booksection[0].toLowerCase().trim()+".txt".trim();                  
                            
                      try{
                       String []versesection = ChaptVerse[1].split("-", 2);
                       verse = Integer.parseInt(versesection[0].trim());
                       to = Integer.parseInt(versesection[1].trim());           
                                  
                                
                      }catch(Exception e){
                        ChaptVerse = compVerse.split(":", 2); 
                        verse = Integer.parseInt(ChaptVerse[1].trim());         
                      }            
                        
                    }catch(Exception r){
                      String []ChaptVerse = compVerse.split(" ", 2); 
                      chapter = Integer.parseInt(ChaptVerse[1]);
                      book = "books/"+ChaptVerse[0].toLowerCase().trim()+".txt".trim();            
                    }    
                        
                    Toast.makeText(getApplicationContext(), compVerse + "\n"+ book + "\n"+ chapter + "\n"+ verse + "\n"+ to, Toast.LENGTH_LONG).show();  
                    ArrayList<Integer> StartStop = new ArrayList<>();
                    StartStop= bible_Chapters(book, chapter); 
                    bible.putExtra("chapter", book + " " + chapter);
                    bible.putExtra("startVerse", StartStop.get(0));
                    bible.putExtra("stopVerse", StartStop.get(1));
                    bible.putExtra("bookName", book);        
         
                        
                    startActivity(bible);   
 
                }

            }, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        
        return spannableString;
    }
}