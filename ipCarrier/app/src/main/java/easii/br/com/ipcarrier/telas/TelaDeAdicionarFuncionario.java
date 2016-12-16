package easii.br.com.ipcarrier.telas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import easii.br.com.ipcarrier.R;
import easii.br.com.ipcarrier.objetos.Config;
import easii.br.com.ipcarrier.objetos.Funcionario;

public class TelaDeAdicionarFuncionario extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText nome,telefone;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_adicionar_funcionario);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" Adicionar Funcionario ");
        getSupportActionBar().setLogo(R.drawable.add);

        Firebase.setAndroidContext(this);
        nome = (EditText)findViewById(R.id.etNome);
        telefone= (EditText) findViewById(R.id.etTelefone);


    }
    public void adicionarFuncionario(View view){

        Firebase ref = new Firebase(Config.FIREBASE_URL);

        idUser = ref.push().getKey();

        Log.i("LOG", " idUser = " + idUser);

        String nomeDoUser = nome.getText().toString();
        String telefoneDoUser = telefone.getText().toString();

        Funcionario funcionario = new Funcionario(nomeDoUser,0,telefoneDoUser,0,true);

        ref.child(idUser).setValue(funcionario);
        ref.child(idUser).child("telefone").setValue("666");

       // addUserChangeListener();
       // ref.child("Funcionario").push().setValue(funcionario);
        Toast.makeText(TelaDeAdicionarFuncionario.this, "Adicionado", Toast.LENGTH_SHORT).show();
        finish();



    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tela_de_adicionar_funcionario, menu);
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
