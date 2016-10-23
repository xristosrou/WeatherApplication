package gr.hua.android.weather.present;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = this.getIntent();//το intent που δέχεται 
        ArrayList<String> lista=i.getExtras().getStringArrayList("i1");//παίρνουμε την μεταβλητή i1 την τιμή της
        String response= "City:"+lista.get(3)+"<br>"+"Country:"+lista.get(0)+"<br>"+"Temprature:"+lista.get(1)+" kelvin"+"<br>" +"Humidity: "+lista.get(2)+" % ";//το string που θα τυπωθεί,θα μπορούσα να φτιάξω πολλά editText για να δείξω τα στοιχεία στον χρήστη αλλα αισθητικά πιστεύω είναι καλύτερα έτσι
        EditText etext=(EditText)findViewById(R.id.editText1);
       
        etext.setText(Html.fromHtml(response));
    }
}
