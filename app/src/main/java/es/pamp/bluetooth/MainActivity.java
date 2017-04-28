package es.pamp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    Button btnhabilitar;
    Button btnmostrar;
    Button btnvisible;
    //NECESITAMOS EL ADAPTADOR QUE SERÁ
    //EL ENCARGADO DE REALIZAR TODAS LAS
    //PETICIONES AL TELÉFONO
    BluetoothAdapter adaptadorbluetooth;
    TextView lbldispositivos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RECUPERAMOS EL DISPOSITIVO DEL TELÉFONO
        adaptadorbluetooth = BluetoothAdapter.getDefaultAdapter();
        if (adaptadorbluetooth == null) {
            //MOSTRAMOS UN MENSAJE PORQUE EL DISPOSITIVO NO
            //SOPORTA BLUETOOTH.
            Toast.makeText(getApplicationContext(),
                    "El dispositivo Bluetooth no está disponible", Toast.LENGTH_LONG);
        }

        btnhabilitar = (Button) findViewById(R.id.btnhablitar);
        btnmostrar = (Button) findViewById(R.id.btnmostrar);
        btnvisible = (Button) findViewById(R.id.btnvisible);
        lbldispositivos = (TextView) findViewById(R.id.lbldispositivos);

        btnhabilitar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                habilitarBluetooth();
            }

        });

        btnmostrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                mostrarDispositivosVinculados();
            }

        });

        btnvisible.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dispositivoVisible();
            }

        });
    }
    //LO QUE HACEMOS AQUÍ ES HABILITAR EL BLUETOOTH.
    //APARECERÁ UN MENSAJE DE CONFIRMACIÓN PARA EL USUARIO.
    //EN EL CASO DE QUE EL USUARIO LO HABILITE,
    //PODREMOS CAPTURAR LA ACCIÓN EN UNA ACTIVIDAD
    public void habilitarBluetooth()
    {
        if (!adaptadorbluetooth.isEnabled()) {
            Intent intenthabilitar = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intenthabilitar, REQUEST_ENABLE_BT);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "El dispositivo BLUETOOTH YA ESTÁ HABILITADO", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //CAPTURAMOS LA RESPUESTA DE LA ACTIVIDAD
        //EN NUESTRO CASO, HABILITAR EL BLUETOOTH
        switch (resultCode)
        {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK)
                {
//INDICAMOS AL USUARIO QUE SE HA HABILITADO EL BLUETOOTH
                    Toast toast2 =Toast.makeText(getApplicationContext(),
                            "El dispositivo Bluetooth está HABILITADO", Toast.LENGTH_LONG);
                    lbldispositivos.setText("El dispositivo Bluetooth está HABILITADO");
                    toast2.show();
                }
                break;
        }
    }

    public void mostrarDispositivosVinculados()
    {
        //PARA BUSCAR LOS DISPOSITIVOS VINCULADOS
        //AL TELÉFONO, UTILIZAMOS EL MÉTODO
        //GETBONDEDDEVICES(), DICHO MÉTODO
        //NOS DEVUELVE UNA COLECCIÓN SET
        //QUE CONTIENE TODOS LOS DISPOSITIVOS.
        Set<BluetoothDevice> listadispositivos = adaptadorbluetooth.getBondedDevices();
        if (listadispositivos.size() > 0) {
            String datos = "";
            //LO QUE HAREMOS SERÁ RECORRER TODOS LOS DISPOSITIVOS
            //Y ESCRIBIR EN NUESTRO TEXTVIEW
            //EL NOMBRE Y LA MAC DE CADA UNO DE ELLOS.
            for (BluetoothDevice dispositivo : listadispositivos) {
                datos += "Nombre: " + dispositivo.getName() + ", MAC: " + dispositivo.getAddress() + "\n";
            }
            lbldispositivos.setText("Dispositivos vinculados: \n" + datos);
        }
    }

    public void dispositivoVisible()
    {
        //ESTAS LÍNEAS LO QUE HACEN ES QUE EL DISPOSITIVO ESTÉ
        //VISIBLE DURANTE 300 SEGUNDOS, ES DECIR, 5 MINUTOS.
        //APARECERÁ UNA ADVERTENCIA DEL TELÉFONO INDICANDO
        //LA ACCIÓN QUE ESTAMOS REALIZANDO DESDE LA APLICACIÓN.
        //TAMBIÉN LLAMAMOS A STARTACTIVITY POR SI DESEAMOS
        //CAPTURAR LA RESPESTA CUANDO EL USUARIO
        //HAYA ACEPTADO LA SOLICITUD.
        Intent intentvisibilidad = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intentvisibilidad.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intentvisibilidad);
    }
}

