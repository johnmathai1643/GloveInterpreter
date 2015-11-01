package com.example.john.gloveinterpreter;

import android.util.Log;
import java.text.NumberFormat;

/**
 * Created by john on 11/1/15.
 */
public class TestNeuralNetwork {

  public NeuralNetwork network;
  public NumberFormat percentFormat;

    double xorInput[][] =
            {
                    {0.0,0.0},
                    {1.0,0.0},
                    {0.0,1.0},
                    {1.0,1.0}};

    double xorIdeal[][] =
            { {0.0},{1.0},{1.0},{0.0}};

    public TestNeuralNetwork(){
        Log.i("Start Neural","Learn:");
        network = new NeuralNetwork(2,3,1,0.7,0.9);
    }

  public void run_network() {
      NumberFormat percentFormat = NumberFormat.getPercentInstance();
      percentFormat.setMinimumFractionDigits(4);

      for (int i = 0; i < 10000; i++) {
          for (int j = 0; j < xorInput.length; j++) {
              network.computeOutputs(xorInput[j]);
              network.calcError(xorIdeal[j]);
              network.learn();
          }
          Log.i("Trial", "Trial #" + i + ",Error:" +
                  percentFormat.format(network.getError(xorInput.length)));
      }

      Log.i("Recall", "Recall:");

      for (int i = 0; i < xorInput.length; i++) {

          for (int j = 0; j < xorInput[0].length; j++) {
              Log.i("Input", xorInput[i][j] + ":");
          }

          double out[] = network.computeOutputs(xorInput[i]);
          Log.i("Output", "=" + out[0]);
      }
  }

}
