package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {
    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText oraEditText = null;
    private EditText minutEditText = null;
    private Button setButton = null;
    private Button resetButton = null;
    private Button pollButton = null;
    private TextView dataTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private GetDataButtonClickListener getDataButtonClickListener = new GetDataButtonClickListener();
    private class GetDataButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }


            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort)
            );
            clientThread.start();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        minutEditText = (EditText)findViewById(R.id.minut_edit_text);
        oraEditText = (EditText)findViewById(R.id.ora_edit_text);
        setButton = (Button)findViewById(R.id.set_button);
        resetButton = (Button)findViewById(R.id.reset_button);
        pollButton = (Button)findViewById(R.id.poll_button);
        dataTextView = (TextView)findViewById(R.id.data_text_view);

        connectButton.setOnClickListener(connectButtonClickListener);
//        getDataButton.setOnClickListener(getDataButtonClickListener);
    }
}
