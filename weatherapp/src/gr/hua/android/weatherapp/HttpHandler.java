package  gr.hua.android.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Xml;

public class HttpHandler extends AsyncTask<URL, Void, List<String> > {

	@Override
	protected List doInBackground(URL... urls) {
		// TODO Auto-generated method stub
		String position;
		List<String> data=new ArrayList<String>();
		String name=new String();
		//String city=new String();
		String country=new String();
		String temprature=new String();
		String humidity=new String();
		
		try {
			HttpURLConnection connection=(HttpURLConnection)urls[0].openConnection();//εδώ γίνεται τοrequest στον server
			InputStream  mStream= connection.getInputStream();//νέα σύδδεση με την είσοδο
			XmlPullParser parser =Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(mStream,null);//δίνουμε στον parser την είσοδο που είναι το response xml
			parser.nextTag();//document start
		    parser.require(XmlPullParser.START_TAG,null,"current");//θέλω να ξεκινάει απο το tag current  διαφορετικά κάτι δεν πάει καλά και λαμβάνω exception
	        //μέσα στο whhile κάνω parse το xml 
		    while(parser.next() != XmlPullParser.END_TAG){//οσο δεν έχουμε φτάσει το τέλος του xml με την .next()  πάμε στο επόμενο tag 
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {//βεβαιονόμαστε οτι ξεκινάμε απο την αρχή του tag προληπτικά
		    		continue;
		        }
		     name=parser.getName();//παίρνω το όνομα του tag και ψάχνω αν είναι κάπιο που έχει πληροφρία που με ενδιαφέρει
		     if(name.equals("city")){
		    	 country=readCountry(parser);
		    	 data.add(country);//προσθέτω στο arraylist  την τιμή που επιστρέφει η συνάρτηση readcountry
		     }
		     else if(name.equals("temperature")){
		    	 temprature=readTemprature(parser);
		    	 data.add(temprature);
		     }
		     else if(name.equals("humidity")){
		    	 humidity=readHumidity(parser);
		    	 data.add(humidity);
		    	 break;//αφου βρίσκω και το τελευταίο στοιχείο που με ενδιαφέρει επιστρέφω τα δεδομένα στο activity
		     }
		     else
		    	 skip(parser);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 // αν υπάρχει πρόβλημα με τι io
			 data.add("error");
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//αν υπάρχει πρόβλημα με το response  του request
			return data;
		}
		// τα δεδομένα που επιστρέφονται αν όλα πάνε καλά
		return data;
	}

//η συνάρτηση επιστρέφει την χώρα προέλευσης της πόλης
 public String readCountry(XmlPullParser parser) throws XmlPullParserException, IOException{
	 String country=new String();
	 parser.require(XmlPullParser.START_TAG, null, "city");//να είναι ο parser  στην αρχή του tag city
	 while(parser.next() != XmlPullParser.END_TAG){
		 if (parser.getEventType() != XmlPullParser.START_TAG) {//θέλω μόνο την αρχή του κάθε tag  τα υπόλοιπα event δεν τα χρησιμοποιώ
	            continue;
	        }
		 String name=parser.getName();//παίρνω το όνομα του tag
		 if(name.equals("country")){
			 country=readText(parser);// αν είναι  country διαβάζει το text του tag σαν string 
		 }
		 else
		  skip(parser);
		 
	 }
	 return country;
 }
//η συνάρτηση περιμένει να βρεί TEXT event  και κάνει extract το text απο τα tag που του ζητάω  και  το επιστρέφει σαν string 
   public String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
	 String result= new String("");
	 if(parser.next()==XmlPullParser.TEXT)
		 result=parser.getText(); 
	 parser.nextTag();
	 return result;
   }
   
   //η συνάρτηση διαβάζει την τιμή της θερμοκαρασίας απο το tag Temprature ,απο το το attribure value  το οπίο και επιστρέφει σαν String 
   private String readTemprature(XmlPullParser parser) throws XmlPullParserException, IOException{
	   parser.require(XmlPullParser.START_TAG,null,"temperature");
	   String value=parser.getAttributeValue(null,"value");
	   parser.nextTag();
	   parser.require(XmlPullParser.END_TAG,null,"temperature");
	   return value;
   }
   // η συνάρτηση επεξεργάζεται το tag humidity και κάνει extract  την τιμή του property value το οπίο και επιστρέφει
   private String readHumidity(XmlPullParser parser) throws XmlPullParserException, IOException{
	   parser.require(XmlPullParser.START_TAG, null,"humidity");
	   String value=parser.getAttributeValue(null,"value");
	   parser.nextTag();
	   parser.require(XmlPullParser.END_TAG,null,"humidity");
	   return value;
   }
   
   
   //η συνάρτηση απλώς προσπερνά τα tags για τα οποία δεν ενδιαφέρομαι 
   public void skip(XmlPullParser parser) throws XmlPullParserException, IOException{
	   int depth=1;
	   while(depth!=0){
		   switch(parser.next()){//προχωρά στο επόμενο event 
		   case XmlPullParser.END_TAG:
			   depth--;
			   break;
		   case XmlPullParser.START_TAG:
			   depth++;
			   break;
		   }//απλως κοιτάει οταν μπαίνει σε ένα tag  να βγάνει και απο αυτο προσπερνόντας εσωτερικά tag κτλ
	   }
   }

  

}
