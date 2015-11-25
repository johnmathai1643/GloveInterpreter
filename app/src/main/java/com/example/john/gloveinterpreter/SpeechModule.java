package com.example.john.gloveinterpreter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

public class SpeechModule extends Fragment implements TextToSpeech.OnInitListener {
    View rootView;
    private TextToSpeech tts;
    private String text;
    public NeuralNetworkHandler myNeuralNetworkHandler;
    private final String TAG = "Speech Module";
    private int i,count;
    private TextView alpha;
    /*********** bluetooth components *****************************************************************************/
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    static private Handler bluetoothIn;
    final int handlerState = 0;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;

    private List<List<Double>> xorInputList,xorOutputList;
    private List<Double> xorInputRow;
    private ArrayList<Double> xorOutputRow;
    public double[][] xorInput,xorOutput;
    private ConnectedThread mConnectedThread;
    /*********** bluetooth components *****************************************************************************/

    public SpeechModule(){
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.speech_fragment, container, false);
        Bundle bundle = this.getArguments();
        text = bundle.getString("text", "empty");
        i=0;
        count = 0;


        alpha = (TextView) rootView.findViewById(R.id.alpha);
        tts = new TextToSpeech(getActivity(), this);
        alpha.setText("");
        tts = new TextToSpeech(getActivity(), this);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        return rootView;
    }


    private void speak_on_receive(String text_from_phone) {
        count=0;
            alpha.setText(text_from_phone);
            speakOut(text_from_phone);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onSTART");
        count = 0;
        /********************************************************************************************************************/
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                   //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);
                    Log.d("DATA_2:", readMessage);
                    int endOfLineIndex = recDataString.indexOf("#");
                    Log.d("DATA_3:", String.valueOf(endOfLineIndex));
                    if (endOfLineIndex > 0) {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        if (recDataString.charAt(0) == '~')
                        {
                            dataInPrint = dataInPrint.substring(1);
                            StringTokenizer tokens = new StringTokenizer(dataInPrint, "~");
                            String sensor0 = tokens.nextToken().trim();
//                            String sensor1 = tokens.nextToken().trim();
//                            String sensor2 = tokens.nextToken().trim();
//                            String sensor3 = tokens.nextToken().trim();
//                            String sensor4 = tokens.nextToken().trim();
                              Log.d("TEXTTTT:", sensor0);
                              speak_on_receive(sensor0);
                              count = 1;
//                            Toast.makeText(getActivity(), "Complete"+count, Toast.LENGTH_SHORT).show();
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        dataInPrint = "";

                    }

                }
            }
        };

        /********************************************************************************************************************/
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

        /************************************************************************************************************/
        address = getArguments().getString("device_address");
        if(address!=null) {
            Log.d(TAG,address);
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
            try {
                Log.d(TAG,"Start Bluetooth");
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) { // exception

                }
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
            mConnectedThread.write("x");
            /************************************************************************************************************/
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        /*************************************************************************************************************/
        if(btSocket!=null)
            try{
                btSocket.close();
            } catch (IOException e2) {
            }
        /*************************************************************************************************************/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alpha.setText("0");
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        /*************************************************************************************************************/
        if(btSocket!=null)
            try{
                btSocket.close();
            } catch (IOException e2) {
            }
        /*************************************************************************************************************/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach");
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt("section_number"));
    }

    @Override
    public void onInit(int status) {
//        myNeuralNetworkHandler.run_network();
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut("Your Personal Assistant");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {
        tts.setSpeechRate(0.8f);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /****************************************************************************************************************************/
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        //Connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
//                    Log.d(TAG,"Sending to handler");
                    // Send to UI
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "Connection Failure", Toast.LENGTH_LONG).show();
//                finish();
            }
        }
    }

    private void checkBTState() {
        if(btAdapter==null) {
            Toast.makeText(getActivity(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
    /****************************************************************************************************************************/

}
