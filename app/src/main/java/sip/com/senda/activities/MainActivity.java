package sip.com.senda.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import sip.com.senda.R;
import sip.com.senda.objetos.Conexion;
import sip.com.senda.objetos.Stuff;
import sip.com.senda.objetos.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText textUsaurio, textPass;
    private Button botonIngresar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textPass =(EditText)findViewById(R.id.editTextPass_Main);
        textUsaurio = (EditText)findViewById(R.id.editTextUsuario_Main);
        botonIngresar = (Button)findViewById(R.id.botonIngresar_Main);


    }

    public void onClickIngresarLogin(View view){

        String usuario = textUsaurio.getText().toString().trim();
        String pass = textPass.getText().toString().trim();

        if (!usuario.equalsIgnoreCase("") || !pass.equalsIgnoreCase("")) {
            new OnBackIngreso(usuario,pass,view).execute();
        } else {
            Stuff.toastCsm(view,"El campo de usuario y el de constraseña no pueden estar vacios").show();
        }

    }




    public class OnBackIngreso extends AsyncTask<Boolean,Boolean,Boolean>{

        private String usuarioS, passS;
        private Conexion conn;
        private boolean respuesta;
        private Usuario usuario;
        private View view;

        public OnBackIngreso(String usuarioS, String passS, View view) {
            this.usuarioS = usuarioS;
            this.passS = passS;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            botonIngresar.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            HashMap<String, String> parametro = new HashMap();
            parametro.put("numPeticion", "1");
            parametro.put("nick", usuarioS);
            parametro.put("pass", passS);

            try {
                conn = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                conn.setParametros(parametro);
                conn.executar(Conexion.metodoPeticion.POST);
                String respuesta = conn.getRespuesta();
                if (Stuff.existe(respuesta)) {
                    usuario = new Usuario(respuesta);
                    Log.wtf("USU", usuario.toString());

                    return true;
                } else {
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            botonIngresar.setEnabled(true);
            if(aBoolean == true){
                Intent i = new Intent(MainActivity.this,MapaActivity.class);
                i.putExtra("usuario",(Parcelable)usuario);
                startActivity(i);
            }else{
                Stuff.toastCsm(view,"El usuario o la contraseña \nSon incorrectas").show();
            }
        }
    }

}
