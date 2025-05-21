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


public class EDFTestReader
{

public static void main(String[] args)
{
  int i, edfsignals, filetype;

  EDFreader hdl;

  if(args.length == 0)
  {
    System.out.println("need a filename\n");

    return;
  }

  if(args.length > 1)
  {
    System.out.println("too many arguments\n");

    return;
  }

  try
  {
    hdl = new EDFreader(args[0]);
  }
  catch(IOException e)
  {
    System.out.printf("An error occured: ");
    System.out.printf(e.getMessage());
    return;
  }
  catch(EDFException e)
  {
    System.out.printf("An error occured: ");
    System.out.printf(e.getMessage());
    return;
  }

  System.out.printf("Startdate: %02d-%02d-%02d\n", hdl.getStartDateDay(), hdl.getStartDateMonth(), hdl.getStartDateYear());
  System.out.printf("Starttime: %02d:%02d:%02d\n", hdl.getStartTimeHour(), hdl.getStartTimeMinute(), hdl.getStartTimeSecond());
  filetype = hdl.getFileType();
  if((filetype == EDFreader.EDFLIB_FILETYPE_EDF)  || (filetype == EDFreader.EDFLIB_FILETYPE_BDF))
  {
    System.out.printf("Patient: %s\n", hdl.getPatient());
    System.out.printf("Recording: %s\n", hdl.getRecording());
  }
  else
  {
    System.out.printf("Patient code: %s\n", hdl.getPatientCode());
    System.out.printf("Gender: %s\n", hdl.getPatientGender());
    System.out.printf("Birthdate: %s\n", hdl.getPatientBirthDate());
    System.out.printf("Patient name: %s\n", hdl.getPatientName());
    System.out.printf("Patient additional: %s\n", hdl.getPatientAdditional());
    System.out.printf("Admin. code: %s\n", hdl.getAdministrationCode());
    System.out.printf("Technician: %s\n", hdl.getTechnician());
    System.out.printf("Equipment: %s\n", hdl.getEquipment());
    System.out.printf("Recording additional: %s\n", hdl.getRecordingAdditional());
  }
  System.out.printf("Reserved: %s\n", hdl.getReserved());
  edfsignals = hdl.getNumSignals();
  System.out.printf("Number of signals: %d\n", hdl.getNumSignals());
  System.out.printf("Number of datarecords: %d\n", hdl.getNumDataRecords());
  System.out.printf("Datarecord duration: %f\n", (double)(hdl.getLongDataRecordDuration()) / 10000000.0);

  for(i=0; i<edfsignals; i++)
  {
    try
    {
      System.out.printf("Signal: %s\n", hdl.getSignalLabel(i));
      System.out.printf("Samplefrequency: %f Hz\n", hdl.getSampleFrequency(i));
      System.out.printf("Transducer: %s\n", hdl.getTransducer(i));
      System.out.printf("Physical dimension: %s\n", hdl.getPhysicalDimension(i));
      System.out.printf("Physical minimum: %f\n", hdl.getPhysicalMinimum(i));
      System.out.printf("Physical maximum: %f\n", hdl.getPhysicalMaximum(i));
      System.out.printf("Digital minimum: %d\n", hdl.getDigitalMinimum(i));
      System.out.printf("Digital maximum: %d\n", hdl.getDigitalMaximum(i));
      System.out.printf("Prefilter: %s\n", hdl.getPreFilter(i));
      System.out.printf("Samples per datarecord: %d\n", hdl.getSampelsPerDataRecord(i));
      System.out.printf("Total samples in file: %d\n", hdl.getTotalSamples(i));
      System.out.printf("Reserved: %s\n", hdl.getReserved(i));
    }
    catch(EDFException e)
    {
      System.out.printf("An error occured: ");
      System.out.printf(e.getMessage());
      return;
    }
  }

  double[] buf = new double[100];

  int err;

  try
  {
    err = hdl.readPhysicalSamples(0, buf);
  }
  catch(IOException e)
  {
    System.out.printf("An error occured: ");
    System.out.printf(e.getMessage());
    return;
  }
  catch(EDFException e)
  {
    System.out.printf("An error occured: ");
    System.out.printf(e.getMessage());
    return;
  }

  for(i=0; i<hdl.annotationslist.size(); i++)
  {
    System.out.printf("annotation: onset: %d:%02d:%02d    description: %s    duration: %d\n",
                      (hdl.annotationslist.get(i).onset / 10000000) / 3600,
                      ((hdl.annotationslist.get(i).onset / 10000000) % 3600) / 60,
                      (hdl.annotationslist.get(i).onset / 10000000) % 60,
                      hdl.annotationslist.get(i).description,
                      hdl.annotationslist.get(i).duration);
  }

//   for(i=0; i<100; i++)
//   {
//     System.out.printf("%3d: %f\n", i, buf[i]);
//   }
}


}




