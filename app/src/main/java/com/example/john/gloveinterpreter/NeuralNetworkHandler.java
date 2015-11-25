package com.example.john.gloveinterpreter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Created by john on 11/1/15.
 */
public class NeuralNetworkHandler {

  public NeuralNetwork network;
  public NumberFormat percentFormat;
  private Context context;
  private SQLiteHelper db;
  private static String TAG = "NeuralNetworkHandler";

    double xorInput[][] =
            {   {50.0,100.0,12.0,0.0,0.0},
                {11.0,12.0,1.0,70.0,0.0},
                {0.0,1.0,0.0,10.0,90.0},
                {12.0,32.0,92.0,2.0,33.0}};

//    double xorInput[][];

    double xorInputpred[][] =
            {     {67.0,89.0,32.0,3.0,5.0},
                  {20.0,24.0,7.0,80.0,3.0},
                  {3.0,0.0,3.0,15.0,85.0},
                  {92.0,22.0,120.0,12.0,37.0},
                  {74.0,21.0,120.0,72.0,37.0},
                  {78.0,99.0,12.0,23.0,37.0},
                  {0.0,0.0,20.0,99.0,97.0},
                  {1.0,2.0,0.0,99.0,99.0},
                  {100.0,2.0,12.0,1.0,0.0}
            };

    double xorIdeal[][] =
            { {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0}};
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0},
//              {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0}
//            };

    public NeuralNetworkHandler(Context context, double[][] xorInput, double[][] xorOutput){
        Log.i(TAG,"Start Neural Learn:");
        this.xorInput = new double[xorInput.length][];
        this.xorIdeal = new double[xorOutput.length][];
//        this.xorInput = xorInput;
//        this.xorIdeal = xorIdeal;
        this.context = context;
        db = new SQLiteHelper(context);
//        create_data_files(xorInputpred,xorIdeal);
        try {
            create_csv_files(xorInputpred,xorIdeal);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        storedata(xorInput,xorIdeal);
        network = new NeuralNetwork(5,10,26,0.7,0.9,context,this.xorInput,this.xorIdeal);
    }

    public void run_network() {
      NumberFormat percentFormat = NumberFormat.getPercentInstance();
      percentFormat.setMinimumFractionDigits(4);
      network.storedata(xorInput, xorIdeal);
      for (int i = 0; i < 10; i++) {
          for (int j = 0; j < this.xorInput.length; j++) {
              network.computeOutputs(this.xorInput[j]);
              network.calcError(xorIdeal[j]);
              network.learn();
          }
          Log.i(TAG, "Trial #" + i + ",Error:" + percentFormat.format(network.getError(this.xorInput.length)));
      }
      network.storeMemory();
  }
    public void output_network(){
        Log.i(TAG, "Recall:");
        for (int i = 0; i <  xorInputpred.length; i++) {
            for (int j = 0; j < xorInputpred[0].length; j++) {
                Log.i(TAG, xorInputpred[i][j] + ":");
            }
            double out[] = network.computeOutputs(xorInputpred[i]);
            System.out.println(Arrays.toString(out));
        }
    }

    public void create_csv_files(double[][] xorInput,double[][] xorIdeal) throws IOException {

        BufferedWriter br = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory()+"neuralinput.csv"));
        Log.d(TAG,"CSV");
        StringBuilder sb = new StringBuilder();
        for (double[] element : xorInput) {
            sb.append(element);
            sb.append(",");
        }
        assert br != null;
        try {
            br.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        BufferedWriter bro = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory()+"neuraloutput.csv"));

        StringBuilder sbo = new StringBuilder();
        for (double[] element : xorIdeal) {
            sb.append(element);
            sb.append(",");
        }
        assert bro != null;
        try {
            bro.write(sbo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bro.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void create_data_files(double[][] xorInput,double[][] xorIdeal){
//        Scanner scan_input = null;
//        Scanner scan_output = null;
//        try {
//            scan_input = new Scanner(new File("NeuralDataInput.txt"));
//            scan_output = new Scanner(new File("NeuralDataOutput.txt"));
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < xorInput.length; i++) {
//            for (int j = 0; j < xorInput[i].length; j++) {
//                xorInput[i][j] = scan_input.nextInt();
//            }
//            scan_input.nextLine();
//        }
//        for (int i = 0; i < xorIdeal.length; i++) {
//            for (int j = 0; j < xorIdeal[i].length; j++) {
//                xorInput[i][j] = scan_output.nextInt();
//            }
//            scan_output.nextLine();
//        }
//
//    }

}
