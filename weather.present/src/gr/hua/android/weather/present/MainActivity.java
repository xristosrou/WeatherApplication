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
        Intent i = this.getIntent();//�� intent ��� ������� 
        ArrayList<String> lista=i.getExtras().getStringArrayList("i1");//��������� ��� ��������� i1 ��� ���� ���
        String response= "City:"+lista.get(3)+"<br>"+"Country:"+lista.get(0)+"<br>"+"Temprature:"+lista.get(1)+" kelvin"+"<br>" +"Humidity: "+lista.get(2)+" % ";//�� string ��� �� �������,�� �������� �� ������ ����� editText ��� �� ����� �� �������� ���� ������ ���� ��������� ������� ����� �������� ����
        EditText etext=(EditText)findViewById(R.id.editText1);
       
        etext.setText(Html.fromHtml(response));
    }
}
