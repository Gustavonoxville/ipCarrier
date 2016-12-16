package easii.br.com.ipcarrier.telas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import easii.br.com.ipcarrier.R;
import easii.br.com.ipcarrier.objetos.Config;
import easii.br.com.ipcarrier.objetos.Funcionario;
import easii.br.com.ipcarrier.service.AtualizaLocalizacaoService;
import easii.br.com.ipcarrier.service.GPSService;

public class MainActivity extends AppCompatActivity {

    private EditText nome, telefone;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        nome = (EditText) findViewById(R.id.etNome);
        telefone = (EditText) findViewById(R.id.etTelefone);


        SharedPreferences sharedPreferences = this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        boolean logado = sharedPreferences.getBoolean("logado", false);

        if (logado) {
            Intent intent = new Intent(MainActivity.this, MenuPrincipalAdm.class);
            startActivity(intent);
        }

    }

    public void stopService(View view) {
        Intent i = new Intent(MainActivity.this, AtualizaLocalizacaoService.class);
        stopService(i);

    }

    public void fazerLogin(View view) {


        Firebase ref = new Firebase(Config.FIREBASE_URL);
        idUser = ref.push().getKey();
        Log.i("LOG", " idUser = " + idUser);

        String nomeDoUser = nome.getText().toString();
        String telefoneDoUser = telefone.getText().toString();
        Funcionario funcionario = new Funcionario(nomeDoUser, 0, telefoneDoUser, 0, true);

        SharedPreferences sharedPref = this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("idUser", idUser);
        editor.putBoolean("logado", true);
        editor.commit();

        ref.child(idUser).setValue(funcionario);

        // Intent t = new Intent(MainActivity.this, GPSService.class);
        //startService(t);

        Toast.makeText(MainActivity.this, "Adicionado", Toast.LENGTH_SHORT).show();

        Intent it = new Intent(MainActivity.this, MenuPrincipalAdm.class);
        startActivity(it);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
