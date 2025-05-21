/*
*****************************************************************************
*
* Copyright (c) 2020 Teunis van Beelen
* All rights reserved.
*
* Email: teuniz@protonmail.com
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the copyright holder nor the names of its
*       contributors may be used to endorse or promote products derived from
*       this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ''AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
*****************************************************************************
*/


import java.io.*;
import java.nio.*;
import java.lang.*;


public class EDFTestWriter
{

public static void main(String[] args)
{
  int i, j, chan, err,
      sf1=1000,
      sf2=1500,
      edfsignals = 2,
      datrecs=20;

  double q1, q2, sine_17, sine_26;

  double[] buf1 = new double[sf1];
  double[] buf2 = new double[sf2];

  EDFwriter hdl;

  try
  {
    hdl = new EDFwriter("sinewave.edf", EDFwriter.EDFLIB_FILETYPE_EDFPLUS, edfsignals);
  }
  catch(IOException e)
  {
    System.out.printf("An error occured while opening the file: ");
    System.out.printf(e.getMessage());
    return;
  }
  catch(EDFException e)
  {
    System.out.printf("An error occured while opening the file: ");
    System.out.printf(e.getMessage());
    return;
  }

  for(chan=0; chan<edfsignals; chan++)
  {
    hdl.setPhysicalMaximum(chan, 3000);
    hdl.setPhysicalMinimum(chan, -3000);
    hdl.setDigitalMaximum(chan, 32767);
    hdl.setDigitalMinimum(chan, -32768);
    hdl.setPhysicalDimension(chan, String.format("uV"));
  }

  hdl.setSampleFrequency(0, sf1);
  hdl.setSampleFrequency(1, sf2);
  hdl.setSignalLabel(0, String.format("sine 1.7Hz", chan + 1));
  hdl.setSignalLabel(1, String.format("sine 2.6Hz", chan + 1));

  sine_17 = Math.PI * 2.0;
  sine_17 /= (sf1 / 1.7);
  sine_26 = Math.PI * 2.0;
  sine_26 /= (sf2 / 2.6);
  q1 = 0;
  q2 = 0;

  try
  {
    for(i=0; i<datrecs; i++)
    {
      for(j=0; j<sf1; j++)
      {
        q1 += sine_17;
        buf1[j] = 2000.0 * Math.sin(q1);
      }

      err = hdl.writePhysicalSamples(buf1);
      if(err != 0)
      {
        System.out.printf("writePhysicalSamples() returned error: %d\n", err);
        return;
      }

      for(j=0; j<sf2; j++)
      {
        q2 += sine_26;
        buf2[j] = 2000.0 * Math.sin(q2);
      }

      err = hdl.writePhysicalSamples(buf2);
      if(err != 0)
      {
        System.out.printf("writePhysicalSamples() returned error: %d\n", err);
        return;
      }
    }
  }
  catch(IOException e)
  {
    System.out.printf("An error occured while writing to the file: ");
    System.out.printf(e.getMessage());
    return;
  }

  hdl.writeAnnotation(0, -1, "Recording starts");

  hdl.writeAnnotation(datrecs * 10000, -1, "Recording ends");

  try
  {
    hdl.close();
  }
  catch(IOException e)
  {
    System.out.printf("An error occured while closing the file: ");
    System.out.printf(e.getMessage());
    return;
  }
  catch(EDFException e)
  {
    System.out.printf("An error occured while closing the file: ");
    System.out.printf(e.getMessage());
    return;
  }
}

}




