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
			HttpURLConnection connection=(HttpURLConnection)urls[0].openConnection();//��� ������� ��request ���� server
			InputStream  mStream= connection.getInputStream();//��� ������� �� ��� ������
			XmlPullParser parser =Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(mStream,null);//������� ���� parser ��� ������ ��� ����� �� response xml
			parser.nextTag();//document start
		    parser.require(XmlPullParser.START_TAG,null,"current");//���� �� �������� ��� �� tag current  ����������� ���� ��� ���� ���� ��� ������� exception
	        //���� ��� whhile ���� parse �� xml 
		    while(parser.next() != XmlPullParser.END_TAG){//��� ��� ������ ������ �� ����� ��� xml �� ��� .next()  ���� ��� ������� tag 
		    	if (parser.getEventType() != XmlPullParser.START_TAG) {//������������� ��� �������� ��� ��� ���� ��� tag ����������
		    		continue;
		        }
		     name=parser.getName();//������ �� ����� ��� tag ��� ����� �� ����� ����� ��� ���� ��������� ��� �� ����������
		     if(name.equals("city")){
		    	 country=readCountry(parser);
		    	 data.add(country);//�������� ��� arraylist  ��� ���� ��� ���������� � ��������� readcountry
		     }
		     else if(name.equals("temperature")){
		    	 temprature=readTemprature(parser);
		    	 data.add(temprature);
		     }
		     else if(name.equals("humidity")){
		    	 humidity=readHumidity(parser);
		    	 data.add(humidity);
		    	 break;//���� ������ ��� �� ��������� �������� ��� �� ���������� ��������� �� �������� ��� activity
		     }
		     else
		    	 skip(parser);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 // �� ������� �������� �� �� io
			 data.add("error");
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//�� ������� �������� �� �� response  ��� request
			return data;
		}
		// �� �������� ��� ������������� �� ��� ���� ����
		return data;
	}

//� ��������� ���������� ��� ���� ���������� ��� �����
 public String readCountry(XmlPullParser parser) throws XmlPullParserException, IOException{
	 String country=new String();
	 parser.require(XmlPullParser.START_TAG, null, "city");//�� ����� � parser  ���� ���� ��� tag city
	 while(parser.next() != XmlPullParser.END_TAG){
		 if (parser.getEventType() != XmlPullParser.START_TAG) {//���� ���� ��� ���� ��� ���� tag  �� �������� event ��� �� �����������
	            continue;
	        }
		 String name=parser.getName();//������ �� ����� ��� tag
		 if(name.equals("country")){
			 country=readText(parser);// �� �����  country �������� �� text ��� tag ��� string 
		 }
		 else
		  skip(parser);
		 
	 }
	 return country;
 }
//� ��������� ��������� �� ���� TEXT event  ��� ����� extract �� text ��� �� tag ��� ��� �����  ���  �� ���������� ��� string 
   public String readText(XmlPullParser parser) throws XmlPullParserException, IOException{
	 String result= new String("");
	 if(parser.next()==XmlPullParser.TEXT)
		 result=parser.getText(); 
	 parser.nextTag();
	 return result;
   }
   
   //� ��������� �������� ��� ���� ��� ������������� ��� �� tag Temprature ,��� �� �� attribure value  �� ���� ��� ���������� ��� String 
   private String readTemprature(XmlPullParser parser) throws XmlPullParserException, IOException{
	   parser.require(XmlPullParser.START_TAG,null,"temperature");
	   String value=parser.getAttributeValue(null,"value");
	   parser.nextTag();
	   parser.require(XmlPullParser.END_TAG,null,"temperature");
	   return value;
   }
   // � ��������� ������������� �� tag humidity ��� ����� extract  ��� ���� ��� property value �� ���� ��� ����������
   private String readHumidity(XmlPullParser parser) throws XmlPullParserException, IOException{
	   parser.require(XmlPullParser.START_TAG, null,"humidity");
	   String value=parser.getAttributeValue(null,"value");
	   parser.nextTag();
	   parser.require(XmlPullParser.END_TAG,null,"humidity");
	   return value;
   }
   
   
   //� ��������� ����� ��������� �� tags ��� �� ����� ��� ������������ 
   public void skip(XmlPullParser parser) throws XmlPullParserException, IOException{
	   int depth=1;
	   while(depth!=0){
		   switch(parser.next()){//������� ��� ������� event 
		   case XmlPullParser.END_TAG:
			   depth--;
			   break;
		   case XmlPullParser.START_TAG:
			   depth++;
			   break;
		   }//����� ������� ���� ������� �� ��� tag  �� ������ ��� ��� ���� ������������� ��������� tag ���
	   }
   }

  

}
