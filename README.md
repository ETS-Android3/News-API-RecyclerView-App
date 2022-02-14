
# News App

This is a News Android project (Java) with fingerprint login. Using Volley Rest API, RecyclerView, CartView, GSON, bumptech, biometric libraries.


## Screenshots

![main screen](https://user-images.githubusercontent.com/54929555/153862867-ac50c6f3-bf65-4be9-be2b-31d04a7254df.jpeg)



## Demo

Video is here 

https://user-images.githubusercontent.com/54929555/153862969-301dfe83-e4a8-4c9d-8f54-011aad18685d.mp4



## API & API key


`API: https://newsapi.org/v2/top-headlines`

`"x-api-key","Your get from this link https://newsapi.org"`


## Gradle

Add volley implementation

```bash
 implementation 'com.android.volley:volley:1.2.0'
```

Add GSON implementation

```bash
 implementation 'com.google.code.gson:gson:2.8.9'
```

Add bumptech implementation

```bash
  implementation 'com.github.bumptech.glide:glide:4.12.0'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
```

Add fingerprint biometric implementation

```bash
 implementation 'androidx.biometric:biometric:1.1.0'
```

Add recyclerview implementation

```bash
implementation "androidx.recyclerview:recyclerview:1.2.1"
```

Add cardview implementation

```bash
implementation "androidx.cardview:cardview:1.0.0"
```




## MainActivity

```bash

public class MainActivity extends AppCompatActivity {
    ListView newsListview;
    Context context = MainActivity.this;
    CheckBox bbcCheckbox;
    CheckBox otherCheckbox;
    Spinner countrySpinner;
    Spinner categorySpinner;
    private static String countryCode = "";
    private static String category = "";
    private static Boolean bbcSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo_white);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Fingerprint fingerprint = new Fingerprint(context);
        fingerprint.fingerprintFun();

        BBCNews bbcNews = new BBCNews(context);
        bbcNews.getNews(countryCode, category, bbcSelected);

    }

    public static DrawerLayout dl;
    public MenuItem setting;
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setting = menu.findItem(R.id.setting_menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(dl.isDrawerOpen(Gravity.LEFT)) {
                dl.closeDrawer(Gravity.LEFT);
            }else{
                dl.openDrawer(Gravity.LEFT);
            }
        }

        //setting layout
        if (item.getItemId() == R.id.setting_menu) {

            final Dialog dialog = new Dialog((context));
            dialog.setContentView(R.layout.settings_layout);//report_details
            dialog.setCancelable(false);
            Button closeSettingLayout = (Button) dialog.findViewById(R.id.close_layout);
            Button loadNews = (Button) dialog.findViewById(R.id.load_news_btn);
            bbcCheckbox = (CheckBox) dialog.findViewById(R.id.bbc_checkbox);
            otherCheckbox = (CheckBox) dialog.findViewById(R.id.other_checkbox);
            countrySpinner = (Spinner) dialog.findViewById(R.id.country_spinner);
            categorySpinner = (Spinner) dialog.findViewById(R.id.category_spinner);
            countrySpinner.setEnabled(false);
            categorySpinner.setEnabled(false);



            HashMap<String,String> spinnerMap = new HashMap<String, String>();
            spinnerMap.put("Portugal","pt");
            spinnerMap.put("Turkey","tr");
            spinnerMap.put("England","gb");
            spinnerMap.put("Japan","jp");

            ArrayList<String> values = new ArrayList<>(spinnerMap.keySet());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,values);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countrySpinner.setAdapter(adapter);

            countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    countryCode = spinnerMap.get(adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    category = parent.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            loadNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bbcCheckbox.isChecked())
                        bbcSelected = true;

                    dialog.dismiss();
                    BBCNews bbcNews = new BBCNews(context);
                    bbcNews.getNews(countryCode, category, bbcSelected);
                }
            });

            closeSettingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        }

        return super.onOptionsItemSelected(item);
    }


    //back button to exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.quit_message), Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void otherCheckboxClicked(View v) {
        //if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            bbcCheckbox.setChecked(false);
            countrySpinner.setEnabled(true);
            categorySpinner.setEnabled(true);
            bbcSelected = false;
        }else {
            bbcCheckbox.setChecked(true);
            countrySpinner.setEnabled(false);
            categorySpinner.setEnabled(false);
            bbcSelected = true;
            countryCode = "";
            category = "";
        }
    }

    public void bbcCheckboxClicked(View v) {
            //if this checkbox is checked!
            CheckBox checkBox = (CheckBox)v;
            if(checkBox.isChecked()){
                otherCheckbox.setChecked(false);
                countrySpinner.setEnabled(false);
                categorySpinner.setEnabled(false);
                bbcSelected = true;
                countryCode = "";
                category = "";
            }else {
                otherCheckbox.setChecked(true);
                countrySpinner.setEnabled(true);
                categorySpinner.setEnabled(true);
                bbcSelected = false;
            }
    }

}
 ```


## BBCNews class

```bash

public class BBCNews {
    Context context;
    public BBCNews(Context context){
        this.context = context;
    }
    RecyclerView recyclerView;
    Adapter adapter;
    BBCNewsClass news;
    ArrayList<BBCNewsClass> classList;
    public  static  JSONArray newsJSONArray;

    public void getNews(String countryCode, String category, Boolean bbcSelected){
        String selected = "";
        String apiKey = "WriteYourApiKey";
        if (!bbcSelected){
            selected = "country="+countryCode+"&category="+category;
        }else {
            selected = "sources=bbc-news";
        }
        final String URL_news = "https://newsapi.org/v2/top-headlines?"+selected+"&apiKey="+apiKey;
        final ProgressDialog loading = new ProgressDialog(context);
         loading.setMessage(context.getResources().getString(R.string.loading));
         loading.setCanceledOnTouchOutside(true);
         loading.show();
         JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_news, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    Log.d("JSON", String.valueOf(response));
                    loading.dismiss();
                    String totalResults = response.getString("totalResults");
                    String status = response.getString("status");
                    if (status.equals("ok")){
                        recyclerView = (RecyclerView) ((Activity)context).findViewById(R.id.recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        classList =  new ArrayList<>();

                        newsJSONArray = response.getJSONArray("articles");
                        for (int i=0; i<newsJSONArray.length(); i++) {
                            JSONObject params = newsJSONArray.getJSONObject(i);

                            JSONObject source = params.getJSONObject("source");
                            String id   = source.getString("id");
                            String name = source.getString("name");

                            String author = params.getString("author");
                            String title = params.getString("title");
                            String description = params.getString("description");
                            String url = params.getString("url");
                            String urlToImage = params.getString("urlToImage");
                            String publishedAt = params.getString("publishedAt");
                            String content = params.getString("content");

                            news = new BBCNewsClass();
                            news.author = author;
                            news.title = title;
                            news.description = description;
                            news.url = url;
                            news.urlToImage = urlToImage;
                            news.publishedAt = publishedAt;
                            news.content = content;

                            classList.add(news);
                        }
                        adapter = new Adapter(context, classList);
                        recyclerView.setAdapter(adapter);
                    }else if (status.equals("error")){
                        String message = response.getString("message");
                        String code = response.getString("code");
                        MSG(message, ((Activity)context).getResources().getString(R.string.error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                MSG(String.valueOf(error), ((Activity)context).getResources().getString(R.string.error));
                VolleyLog.d( ((Activity)context).getResources().getString(R.string.error),  ((Activity)context).getResources().getString(R.string.error)+" : " + error.getMessage());
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(context,
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
            }
        })
         {
             @Override
             public String getBodyContentType() {
                 // return "application/json; charset=utf-8";
                 return "application/x-www-form-urlencoded";
             }

             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("User-Agent", "ozilla/5.0");
                 return headers;
             }
         };

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(req);
    }


    public void MSG (String message,String title){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }



}

```
 
## News class

```bash
public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;

    @Override
    public String toString() {
        return  title + "\n" + author + " - " + publishedAt;
    }

```
 
 ## Fingerprint class

```bash

public class Fingerprint {
    private LinearLayout homeLy;
    private LinearLayout fingerprintLy;
    private static Boolean isSuccessLogin;
    Context context;

    public Fingerprint(Context context){
        this.context = context;
    }

    public boolean fingerprintFun(){
        // Initialising msgtext and loginbutton
        TextView msgtex = ((Activity)context).findViewById(R.id.msgtext);
        Button loginbutton = ((Activity)context).findViewById(R.id.login_btn);
        homeLy = (LinearLayout) ((Activity)context).findViewById(R.id.main);
        fingerprintLy = (LinearLayout) ((Activity)context).findViewById(R.id.fingerprint);

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("You can use the fingerprint sensor to login");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                if (isSuccessLogin != null && isSuccessLogin){
                    fingerprintLy.setVisibility(View.GONE);
                    homeLy.setVisibility(View.VISIBLE);
                }
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device doesnot have a fingerprint sensor");
                loginbutton.setVisibility(View.GONE);
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The biometric sensor is currently unavailable");
                loginbutton.setVisibility(View.GONE);
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Your device doesn't have fingerprint saved, \n please check your security settings");
                loginbutton.setVisibility(View.GONE);
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(context);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
                loginbutton.setText("Login Successful");
                isSuccessLogin = true;
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAuthenticationFailed()
            {
                super.onAuthenticationFailed();
                isSuccessLogin = false;
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("GFG")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });

        return true;
    }
}


```
 
 ## Adapter

```bash
package com.samim.bbcnewsdemoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<BBCNewsClass> newsClass;
    Context context;

    Adapter(Context context, List<BBCNewsClass> newsClass){
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.newsClass = newsClass;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //data received
        BBCNewsClass news = newsClass.get(position);

        holder.txtTitle.setText(news.title);

        holder.txtDesc.setText(news.description);

        holder.txtDate.setText(news.publishedAt);

        String imageUrl = news.urlToImage;

        Glide.with(context)
                .load(imageUrl.trim())
                .placeholder(R.drawable.bbc)
                .error(R.drawable.bbc)
                .override(130, 100)
                .into(holder.imageView);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int item = holder.getAbsoluteAdapterPosition();
                BBCNewsClass selectedNews = newsClass.get(item);
                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("title", selectedNews.title);
                intent.putExtra("author", selectedNews.author);
                intent.putExtra("desc", selectedNews.description);
                intent.putExtra("date", selectedNews.publishedAt);
                intent.putExtra("content", selectedNews.content);
                intent.putExtra("image", selectedNews.urlToImage);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsClass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtDesc, txtDate;
        ImageView imageView;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.n_title);
            txtDesc = itemView.findViewById(R.id.n_desc);
            txtDate = itemView.findViewById(R.id.n_date);
            imageView  = itemView.findViewById(R.id.imageView3);
            mainLayout = itemView.findViewById(R.id.custom_layout);
        }
    }
}


```
 
 ## SecondActivity

```bash

public class SecondActivity extends AppCompatActivity {
    ImageView imageViewl;
    TextView titleTxt, descTxt, dateTxt, authorTxt, contentTxt;
    String title, desc, date, author, content, imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imageViewl = (ImageView) findViewById(R.id.imageView);
        titleTxt = (TextView) findViewById(R.id.new_d_title);
        descTxt = (TextView) findViewById(R.id.new_d_desc);
        authorTxt = (TextView) findViewById(R.id.news_d_author);
        dateTxt = (TextView) findViewById(R.id.news_d_publish_date);
        contentTxt = (TextView) findViewById(R.id.news_d_content);

        getData();
        setData();

    }

    private void getData(){
        if (getIntent().hasExtra("title") && getIntent().hasExtra("desc") && getIntent().hasExtra("date") && getIntent().hasExtra("image")){
            title = getIntent().getStringExtra("title");
            desc  = getIntent().getStringExtra("desc");
            author  = getIntent().getStringExtra("author");
            date  = getIntent().getStringExtra("date");
            content  = getIntent().getStringExtra("content");
            imageUrl  = getIntent().getStringExtra("image");
        }else {
            Toast.makeText(getApplicationContext(), "There's no data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        if (title != null)
           titleTxt.setText(title);
        if (desc != null)
           descTxt.setText(desc);
        if (author != null)
           authorTxt.setText(author);
        if (date != null)
           dateTxt.setText(date);
        if (!content.equals("null"))
           contentTxt.setText(content);

        Glide.with(this)
                .load(imageUrl.trim())
                .placeholder(R.drawable.bbc)
                .error(R.drawable.bbc)
                .override(600, 400)
                .into(imageViewl);
    }

}

```

 
 ## SplashScreen

```bash

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private static final int REQUEST_ID =29 ;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screenn);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(SPLASH_DISPLAY_LENGTH);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

}

```

 

