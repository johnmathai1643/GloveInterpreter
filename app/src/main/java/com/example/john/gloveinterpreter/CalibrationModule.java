package com.example.john.gloveinterpreter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.UUID;

public class CalibrationModule extends Fragment implements TextToSpeech.OnInitListener {
    View rootView;
    private TextView alpha;
    private int i;
    private TextToSpeech tts;
    private static final char[] Alphabets = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

/*********** bluetooth components *****************************************************************************/
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    static private Handler bluetoothIn;
    final int handlerState = 0;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address;

    private  List<List<Double>> xorInputList;
    private List<Double> xorInputRow;
    public double[][] xorInput;
    private ConnectedThread mConnectedThread;
/*********** bluetooth components *****************************************************************************/


    public CalibrationModule() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calibration_fragment, container, false);
        alpha = (TextView) rootView.findViewById(R.id.alpha);
        tts = new TextToSpeech(getActivity(), this);
//      mHandler.postDelayed(mUpdateUITimerTask, 3000);
        alpha.setText("0");
        i=0;

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                calibrate_on_touch();
                return false;
            }
        });

/*****************************************************************/
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
/******************************************************************/
        return rootView;
    }

    private void calibrate_on_touch(){
        alpha.setText("" + Alphabets[i]);
        speakOut("" + Alphabets[i]);
        i++;
        if (i == 27){
            speakOut("Calibration complete.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /********************************************************************************************************************/
          bluetoothIn = new Handler() {
                 public void handleMessage(android.os.Message msg) {
                     if (msg.what == handlerState) {                                     //if message is what we want
                     String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                     recDataString.append(readMessage);
                     Log.d("DATA_2:", readMessage);

                     int endOfLineIndex = recDataString.indexOf("~");
                         if (endOfLineIndex > 0) {
                         String dataInPrint = recDataString.substring(0, endOfLineIndex);
                         if (recDataString.charAt(0) == '#')
                             {
                             dataInPrint = dataInPrint.substring(1);

                             StringTokenizer tokens = new StringTokenizer(dataInPrint, "+");
                             String sensor0 = tokens.nextToken().trim();
                             String sensor1 = tokens.nextToken().trim();
                             String sensor2 = tokens.nextToken().trim();
                             String sensor3 = tokens.nextToken().trim();
                             String sensor4 = tokens.nextToken().trim();

                             List<Double> xorInputRow = Arrays.asList(Double.parseDouble(sensor0), Double.parseDouble(sensor1), Double.parseDouble(sensor2), Double.parseDouble(sensor3), Double.parseDouble(sensor4));
                             xorInputList.add(xorInputRow);
                             }
                     recDataString.delete(0, recDataString.length());                    //clear all string data
                     dataInPrint = " ";
                 }

                 }
             }
          };

        /********************************************************************************************************************/
    }

    @Override
    public void onResume() {
        super.onResume();

        /************************************************************************************************************/
        address = getArguments().getString("section_number");
        if(address!=null) {
//            Log.d("Addressssssssssssssssssssss:",address);
              Log.d("Address:","Done");
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
            try {
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
                    xorInput = convert_array_list(xorInputList);
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
        alpha.setText("A");
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt("section_number"));
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut("Touch to start calibration.");
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

    private double[][] convert_array_list(List<List<Double>> xorInputList){

        final Double[][] xorInputDouble = new Double[xorInputList.size()][];
        int i = 0;
        for (List<Double> l : xorInputList)
            xorInputDouble[i++] = l.toArray(new Double[l.size()]);

        double [][] xorInput  = new double[xorInputDouble.length][];
        for (int j = 0; j < xorInputDouble.length; i++)
            for (int k = 0; i < xorInputDouble[0].length; i++)
                xorInput[j][k] = xorInputDouble[j][k];

        return xorInput;
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















//    private final Handler mHandler = new Handler();
//    private final Runnable mUpdateUITimerTask = new Runnable() {
//        public void run() {
//                mHandler.postDelayed(this, 2000);
//                alpha.setText("" + Alphabets[i]);
//                speakOut("" + Alphabets[i]);
//                i++;
//
//            if (i == 26){
//                stopRepeatingTask();
//                speakOut("Calibration complete.");
//            }
//        }
//    };
//
//    void startRepeatingTask() {
//        mUpdateUITimerTask.run();
//    }
//
//    void stopRepeatingTask() {
//        mHandler.removeCallbacks(mUpdateUITimerTask);
//    }
