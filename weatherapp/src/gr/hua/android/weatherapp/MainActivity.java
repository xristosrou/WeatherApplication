package gr.hua.android.weatherapp;



import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButton=(Button)findViewById(R.id.button1);
        mButton.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//EditText etext=(EditText)findViewById(R.id.editText1);
		Toast toast;
		List<String> lista=new ArrayList<String>();;
		EditText etext=(EditText)findViewById(R.id.editText1);
		String urltext="http://api.openweathermap.org/data/2.5/weather?q="+etext.getText().toString()+"&mode=xml";//Φτιάχνω το url που είναι  το standar κομμάτι συν το  input χρήστη
		try {
			HttpHandler mHandler=new  HttpHandler();//για να μην κάνω block το ui thread έφτιαξα την κλάση που χειρίχεται το http request και κάνει parse το xml για να μου επισρέψει σαν arraylist απο string τα δεδομένα που ζητάω
			URL url=new URL(urltext);
			lista= mHandler.execute(url).get();//επιστρέφεται η λίστα με τα strig
			if( lista.size()==0 ){
		      toast=Toast.makeText(getApplicationContext(),"There is no info for this city available",Toast.LENGTH_LONG);
		      toast.show();// αν επισταφεί άδεια λίστα τότε ξέρω ότι έχει υπάρξει λάθος πιθανότατα λάθος στην πόλη που ψάχνει
		      return;
			}
			else if(lista.get(0).equals("error")){
				toast=Toast.makeText(getApplicationContext(), " Coult not find openweatherapp", Toast.LENGTH_LONG);
				toast.show();
				return;
			    //ξέρω ότι αν επιστραφέι error στην πρώτη θέση τότε δεν μπορεί να εντοπιστεί να ο πόρος http://api.openweathermap...... πιθανο πρόβλημα δικτύου για αυτο και δίνω το συγκεκριμένο μήνυμα
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lista.add(etext.getText().toString());//δίνω και στην λίστα και την πόλη
		Intent i = new Intent();//φτιάνω νέο intent
		i.setAction("gr.hua.android.weather.present.");//του περνάω το action name 
		i.putStringArrayListExtra("i1",(ArrayList<String>) lista);//περνάω και την λίστα 
	    startActivity(i);// και το στέλνω το intent στον προορισμό που ορίζει το action name δηλαδή στο άλλο activity 
	}
}
