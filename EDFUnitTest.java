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


public class EDFUnitTest
{

public static void main(String[] args)
{
  int i, j,
      tmp,
      chns=2,
      ival1,
      ival2,
      ok;

  int[] ibuf = new int[100],
        ibuf20 = new int[20],
        ibuf23 = new int[23],
        ibuf80 = new int[80],
        ibuf92 = new int[92];

  byte[] pbuf = new byte[300],
         str = new byte[1024],
         rbuf = new byte[2048],
         wrbuf= new byte[256];

  short[] sbuf = new short[100];

  long l_tmp;

  double[] dbuf = new double[11000],
           dbuf20 = new double[20],
           dbuf23 = new double[23];

  EDFwriter hdl_out=null;

  EDFreader hdl_in=null;

  RandomAccessFile fp=null;

/********************************** EDF writing ******************************/

  try
  {
    hdl_out = new EDFwriter("test4.edf", EDFwriter.EDFLIB_FILETYPE_EDFPLUS, 1);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.version() != 101)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<1; i++)
  {
    if(hdl_out.setSampleFrequency(i, 10239) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setPatientName("Xohn Doe") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientCode("X12 3") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setAdministrationCode("X8 9") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTechnician("Xard Roe") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setEquipment("Xvi ce") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<10239; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_out = new EDFwriter("test.edf", EDFwriter.EDFLIB_FILETYPE_EDFPLUS, 512);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<512; i++)
  {
    if(hdl_out.setSampleFrequency(i, 10240) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<10240; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != EDFwriter.EDFLIB_DATARECORD_SIZE_TOO_BIG)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_out = new EDFwriter("test.edf", EDFwriter.EDFLIB_FILETYPE_EDFPLUS, chns);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setSampleFrequency(0, 20) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setSampleFrequency(1, 23) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMaximum(0, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMinimum(0, -5000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMaximum(1, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMinimum(1, -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMaximum(0, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMinimum(0, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMaximum(1, 30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMinimum(1, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setSignalLabel(0, "trace1") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setSignalLabel(1, "trace2") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPreFilter(0, "qwerty") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPreFilter(1, "zxcvbn") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTransducer(0, "asdfgh") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTransducer(1, "poklhyg") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    strcpy(str, String.format("uVxxxxxxxxxxxxxxxxxxxx").getBytes("ISO-8859-1"));
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  str[0] = (byte)181;

  try
  {
    if(hdl_out.setPhysicalDimension(0, new String(str, 0, 22, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    strcpy(str, String.format("dCxxxxxxxxxxxxxxxxxxxx").getBytes("ISO-8859-1"));
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  str[0] = (byte)176;
  str[2] = (byte)248;

  try
  {
    if(hdl_out.setPhysicalDimension(1, new String(str, 0, 22, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setStartDateTime(2017, 12, 5, 12, 23, 8, 0) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientName("John Doe") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientCode("01234") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientGender(1) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientBirthDate(2010, 7, 4) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientAdditional("nop") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setAdministrationCode("789") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTechnician("Richard Roe") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setEquipment("device") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setNumberOfAnnotationSignals(3) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDataRecordDuration(130000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.writeAnnotation(0, -1, "Recording starts") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.writeAnnotation(9000, 1000, "Test 1") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.writeAnnotation(13000, -1, "Recording ends") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<20; i++)
  {
    dbuf[i] = -5100 + (i * 800);
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    dbuf[i] = -30100 + (i * 909);
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    dbuf[i] = -5100 + (i * 800);
  }

  for(i=0; i<23; i++)
  {
    dbuf[i + 20] = -30100 + (i * 909);
  }

  try
  {
    if(hdl_out.blockWritePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    sbuf[i] = (short)(-10100 + (i * 1053));
  }

  try
  {
    if(hdl_out.writeDigitalShortSamples(sbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    sbuf[i] = (short)(9900 + (i * 1053));
  }

  try
  {
    if(hdl_out.writeDigitalShortSamples(sbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    sbuf[i] = (short)(-10100 + (i * 1053));
  }

  for(i=0; i<23; i++)
  {
    sbuf[i + 20] = (short)(9900 + (i * 1053));
  }

  try
  {
    if(hdl_out.blockWriteDigitalShortSamples(sbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    ibuf[i] = -10100 + (i * 1053);
  }

  try
  {
    if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    ibuf[i] = 9900 + (i * 1053);
  }

  try
  {
    if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    ibuf[i] = -10100 + (i * 1053);
  }

  for(i=0; i<23; i++)
  {
    ibuf[i + 20] = 9900 + (i * 1053);
  }

  try
  {
    if(hdl_out.blockWriteDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  ival1 = -10100;

  ival2 = 9900;

  for(j=0; j<4; j++)
  {
    for(i=0; i<20; i++)
    {
      ibuf[i] = ival1;

      ival1 += 253;
    }

    try
    {
      if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
    catch(IOException e)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }

    for(i=0; i<23; i++)
    {
      ibuf[i] = ival2;

      ival2 += 253;
    }

    try
    {
      if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
    catch(IOException e)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

/********************************** BDF writing ******************************/

  try
  {
    hdl_out = new EDFwriter("test.bdf", EDFwriter.EDFLIB_FILETYPE_BDFPLUS, 512);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<512; i++)
  {
    if(hdl_out.setSampleFrequency(i, 10239) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<10239; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_out = new EDFwriter("test.bdf", EDFwriter.EDFLIB_FILETYPE_BDFPLUS, 512);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<512; i++)
  {
    if(hdl_out.setSampleFrequency(i, 10240) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<10240; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != EDFwriter.EDFLIB_DATARECORD_SIZE_TOO_BIG)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_out = new EDFwriter("test.bdf", EDFwriter.EDFLIB_FILETYPE_BDFPLUS, chns);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setSampleFrequency(0, 20) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setSampleFrequency(1, 23) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMaximum(0, 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMinimum(0, -5000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMaximum(1, -10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPhysicalMinimum(1, -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMaximum(0, 1000000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMinimum(0, -1000000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMaximum(1, 3000000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDigitalMinimum(1, 1000000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setSignalLabel(0, "trace1") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setSignalLabel(1, "trace2") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPreFilter(0, "qwerty") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPreFilter(1, "zxcvbn") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTransducer(0, "asdfgh") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTransducer(1, "poklhyg") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    strcpy(str, String.format("uVxxxxxxxxxxxxxxxxxxxx").getBytes("ISO-8859-1"));
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  str[0] = (byte)181;

  try
  {
    if(hdl_out.setPhysicalDimension(0, new String(str, 0, 22, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    strcpy(str, String.format("dCxxxxxxxxxxxxxxxxxxxx").getBytes("ISO-8859-1"));
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  str[0] = (byte)176;
  str[2] = (byte)248;

  try
  {
    if(hdl_out.setPhysicalDimension(1, new String(str, 0, 22, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setStartDateTime(2017, 12, 5, 12, 23, 8, 0) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientName("John Doe") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientCode("01234") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientGender(1) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientBirthDate(2010, 7, 4) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientAdditional("nop") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setAdministrationCode("789") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setTechnician("Richard Roe") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setEquipment("device") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setNumberOfAnnotationSignals(3) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setDataRecordDuration(130000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.writeAnnotation(0, -1, "Recording starts") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.writeAnnotation(6000, 2000, "Test 2") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.writeAnnotation(11700, -1, "Recording ends") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<20; i++)
  {
    dbuf[i] = -5100 + (i * 800);
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    dbuf[i] = -30100 + (i * 909);
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    dbuf[i] = -5100 + (i * 800);
  }

  for(i=0; i<23; i++)
  {
    dbuf[i + 20] = -30100 + (i * 909);
  }

  try
  {
    if(hdl_out.blockWritePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    tmp = -1010000 + (i * 105300);

    wrbuf[i*3] = (byte)(tmp & 0xff);
    wrbuf[i*3+1] = (byte)((tmp >> 8) & 0xff);
    wrbuf[i*3+2] = (byte)((tmp >> 16) & 0xff);
  }

  for(i=0; i<23; i++)
  {
    tmp = 990000 + (i * 105300);

    wrbuf[i*3+60] = (byte)(tmp & 0xff);
    wrbuf[i*3+61] = (byte)((tmp >> 8) & 0xff);
    wrbuf[i*3+62] = (byte)((tmp >> 16) & 0xff);
  }

  try
  {
    if(hdl_out.blockWriteDigital3ByteSamples(wrbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    ibuf[i] = -1010000 + (i * 105300);
  }

  try
  {
    if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    ibuf[i] = 990000 + (i * 105300);
  }

  try
  {
    if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    ibuf[i] = -1010000 + (i * 105300);
  }

  for(i=0; i<23; i++)
  {
    ibuf[i + 20] = 990000 + (i * 105300);
  }

  try
  {
    if(hdl_out.blockWriteDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  ival1 = -1010000;

  ival2 = 990000;

  for(j=0; j<4; j++)
  {
    for(i=0; i<20; i++)
    {
      ibuf[i] = ival1;

      ival1 += 25300;
    }

    try
    {
      if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
    catch(IOException e)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }

    for(i=0; i<23; i++)
    {
      ibuf[i] = ival2;

      ival2 += 25300;
    }

    try
    {
      if(hdl_out.writeDigitalSamples(ibuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
    catch(IOException e)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

/********************************** EDF reading ******************************/

  try
  {
    hdl_in = new EDFreader("test4.edf");
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  hdl_in = null;

/***********************************************************************/

  try
  {
    hdl_in = new EDFreader("test.edf");
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.version() != 102)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getFileType() != EDFreader.EDFLIB_FILETYPE_EDFPLUS)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getNumSignals() != 2)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(dblcmp_lim(hdl_in.getSampleFrequency(0), 153.8461538, 1e-6) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(dblcmp_lim(hdl_in.getSampleFrequency(1), 176.9230769, 1e-6) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_in.getTotalSamples(0) != 200)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_in.getTotalSamples(1) != 230)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if((hdl_in.getNumDataRecords() * hdl_in.getLongDataRecordDuration()) != 13000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartDateDay() != 5)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartDateMonth() != 12)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartDateYear() != 2017)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeSecond() != 8)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeMinute() != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeHour() != 12)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeSubSecond() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientName().equals("John Doe") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientCode().equals("01234") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientGender().equals("Male") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientBirthDate().equals("04 jul 2010") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientAdditional().substring(0, 3).equals("nop") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getAdministrationCode().equals("789") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getTechnician().equals("Richard Roe") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getEquipment().equals("device") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getLongDataRecordDuration() != 1300000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getNumDataRecords() != 10)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.size() != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_in.getSignalLabel(0).equals("trace1          ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getSignalLabel(1).equals("trace2          ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMaximum(0) != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMaximum(1) != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMinimum(0) != -5000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMinimum(1) != -30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMaximum(0) != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMaximum(1) != 30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMinimum(0) != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMinimum(1) != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getSampelsPerDataRecord(0) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getSampelsPerDataRecord(1) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalDimension(0).equals("uVxxxxxx") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalDimension(1).equals(" C0xxxxx") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPreFilter(0).substring(0, 9).equals("qwerty   ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPreFilter(1).substring(0, 9).equals("zxcvbn   ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getTransducer(0).substring(0, 9).equals("asdfgh   ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getTransducer(1).substring(0, 9).equals("poklhyg  ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.annotationslist.size() != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(0).onset != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(0).duration != -1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(0).description.equals("Recording starts") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(1).onset != 9000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(1).duration != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(1).description.equals("Test 1") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(2).onset != 13000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(2).duration != -1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(2).description.equals("Recording ends") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_in.fseek(1, 400, EDFreader.EDFSEEK_SET) == 400)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 412, EDFreader.EDFSEEK_SET) == 412)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 20, EDFreader.EDFSEEK_SET) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(0, dbuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(dblcmp(dbuf20[i], -5000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i == 19)
    {
      if(dblcmp(dbuf20[i], 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp_lim(dbuf20[i], -5100 + (i * 800), 0.75) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 23, EDFreader.EDFSEEK_SET) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(1, dbuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(dblcmp(dbuf23[i], -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp(dbuf23[i], -30100 + (i * 909)) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_in.rewind(0);
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(0, dbuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(dblcmp(dbuf20[i], -5000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i == 19)
    {
      if(dblcmp(dbuf20[i], 10000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp_lim(dbuf20[i], -5100 + (i * 800), 0.75) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_in.rewind(1);
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(1, dbuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(dblcmp(dbuf23[i], -30000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp(dbuf23[i], -30100 + (i * 909)) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 40, EDFreader.EDFSEEK_SET) != 40)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -10100 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 46, EDFreader.EDFSEEK_SET) != 46)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if((i == 20) || (i == 21))
    {
      if(ibuf23[i] != 30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i == 22)
    {
      if(ibuf23[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 9900 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 80, EDFreader.EDFSEEK_SET) != 80)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -10100 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 92, EDFreader.EDFSEEK_SET) != 92)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 20)
    {
      if(ibuf23[i] != 30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 9900 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 60, EDFreader.EDFSEEK_SET) != 60)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -10100 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 69, EDFreader.EDFSEEK_SET) != 69)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if((i == 20) || (i == 21))
    {
      if(ibuf23[i] != 30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i == 22)
    {
      if(ibuf23[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 9900 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 100, EDFreader.EDFSEEK_SET) != 100)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -10100 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 115, EDFreader.EDFSEEK_SET) != 115)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 20)
    {
      if(ibuf23[i] != 30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 9900 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf80) != 80)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<80; i++)
  {
    if(i == 0)
    {
      if(ibuf80[i] != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf80[i] != -10100 + (i * 253))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf92) != 92)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<92; i++)
  {
    if(i == 0)
    {
      if(ibuf92[i] != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 80)
    {
      if(ibuf92[i] != 30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf92[i] != 9900 + (i * 253))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 185, EDFreader.EDFSEEK_SET) != 185)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 15)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<15; i++)
  {
    if(ibuf20[i] != -10100 + ((i + 65) * 253))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 115, EDFreader.EDFSEEK_SET) != 115)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf80) != 80)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<5; i++)
  {

    if(ibuf80[i] != 5695 + (i * 1053))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  if(ibuf80[5] != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  for(i=6; i<80; i++)
  {
    if(ibuf80[i] != -10100 + ((i - 5) * 253))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  hdl_in = null;

  /****************************************/

  if(modify_and_try("test.edf", 1, new byte[]{'1'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 1, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 16, new byte[]{' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 16, new byte[]{'0'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0xaa, new byte[]{':'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0xaa, new byte[]{'.'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0xab, new byte[]{'9'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0xab, new byte[]{'1'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0xac, new byte[]{'q'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0xac, new byte[]{'2'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0xc4, new byte[]{'D'})  != 4)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0xc4, new byte[]{'C'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x12e, new byte[]{' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x12e, new byte[]{'s'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x1ac, new byte[]{(byte)181})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x1ac, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x308, new byte[]{' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x308, new byte[]{'-'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x30d, new byte[]{','})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x30d, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x3a5, new byte[]{'.'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x3a5, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x3bc, new byte[]{(byte)207})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x3bc, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x40b, new byte[]{(byte)247})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x40b, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x560, new byte[]{(byte)127})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x560, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x5ff, new byte[]{(byte)13})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x5ff, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x54a, new byte[]{'.'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x54a, new byte[]{' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0xad, new byte[]{'-'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0xad, new byte[]{'.'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x803, new byte[]{'0','.','1','2'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x803, new byte[]{'0','.','1','3','1'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x803, new byte[]{'0','.','1','3','0'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x802, new byte[]{'-'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x802, new byte[]{'+'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x750, new byte[]{(byte)0})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x750, new byte[]{(byte)0x14})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x751, new byte[]{(byte)0})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.edf", 0x358, new byte[]{'-','3','2','7','6','9'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x358, new byte[]{'-','1','0','0','0','0'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x380, new byte[]{'3','2','7','6','8'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.edf", 0x380, new byte[]{'1','0','0','0','0'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0x600);

    fp.read(rbuf, 0, 40);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 19)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(dblcmp_lim((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8), ((-5100 + (i * 800)) / 0.75) - 3333.333333, 1.0001) != 0)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0x628);

    fp.read(rbuf, 0, 46);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 19)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != (-30100 + (i * 909)) + 40000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0x7ac);

    fp.read(rbuf, 0, 40);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 19)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(dblcmp_lim((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8), ((-5100 + (i * 800)) / 0.75) - 3333.333333, 1.0001) != 0)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0x7d4);

    fp.read(rbuf, 0, 46);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 19)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != (-30100 + (i * 909)) + 40000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0x958);

    fp.read(rbuf, 0, 40);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10100 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0x980);

    fp.read(rbuf, 0, 46);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if((i == 0) || (i == 22))
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if((i == 20) || (i == 21))
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 30000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 9900 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0xb04);

    fp.read(rbuf, 0, 40);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10100 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0xb2c);

    fp.read(rbuf, 0, 46);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if((i == 0) || (i == 22))
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if((i == 20) || (i == 21))
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 30000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 9900 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0xcb0);

    fp.read(rbuf, 0, 40);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10100 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0xcd8);

    fp.read(rbuf, 0, 46);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 20)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 30000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 9900 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0xe5c);

    fp.read(rbuf, 0, 40);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != -10100 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.edf", "r");

    fp.seek(0xe84);

    fp.read(rbuf, 0, 46);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 10000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 20)
    {
      if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 30000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(((rbuf[i*2]&0xff) | (rbuf[i*2+1]<<8)) != 9900 + (i * 1053))
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

/********************************** BDF reading ******************************/

  try
  {
    hdl_in = new EDFreader("test.bdf");
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.getFileType() != EDFreader.EDFLIB_FILETYPE_BDFPLUS)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getNumSignals() != 2)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if((hdl_in.getNumDataRecords() * hdl_in.getLongDataRecordDuration()) != 11700000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartDateDay() != 5)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartDateMonth() != 12)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartDateYear() != 2017)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeSecond() != 8)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeMinute() != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeHour() != 12)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeSubSecond() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientName().equals("John Doe") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientCode().equals("01234") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientGender().equals("Male") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientBirthDate().equals("04 jul 2010") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientAdditional().substring(0, 3).equals("nop") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getAdministrationCode().equals("789") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getTechnician().equals("Richard Roe") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getEquipment().equals("device") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getLongDataRecordDuration() != 1300000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getNumDataRecords() != 9)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.size() != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_in.getSignalLabel(0).equals("trace1          ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getSignalLabel(1).equals("trace2          ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMaximum(0) != 10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMaximum(1) != -10000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMinimum(0) != -5000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalMinimum(1) != -30000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMaximum(0) != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMaximum(1) != 3000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMinimum(0) != -1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getDigitalMinimum(1) != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getSampelsPerDataRecord(0) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getSampelsPerDataRecord(1) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalDimension(0).equals("uVxxxxxx") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPhysicalDimension(1).equals(" C0xxxxx") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPreFilter(0).substring(0, 9).equals("qwerty   ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getPreFilter(1).substring(0, 9).equals("zxcvbn   ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getTransducer(0).substring(0, 9).equals("asdfgh   ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.getTransducer(1).substring(0, 9).equals("poklhyg  ") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.annotationslist.size() != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(0).onset != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(0).duration != -1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(0).description.equals("Recording starts") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(1).onset != 6000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(1).duration != 2000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(1).description.equals("Test 2") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(2).onset != 11700000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(2).duration != -1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.annotationslist.get(2).description.equals("Recording ends") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_in.fseek(1, 500, EDFreader.EDFSEEK_SET) == 500)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 333, EDFreader.EDFSEEK_SET) == 333)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 20, EDFreader.EDFSEEK_SET) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(0, dbuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(dblcmp_lim(dbuf20[i], -5000, 0.00001) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i == 19)
    {
      if(dblcmp_lim(dbuf20[i], 10000, 0.00001) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp_lim(dbuf20[i], -5100 + (i * 800), 0.0075) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 23, EDFreader.EDFSEEK_SET) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(1, dbuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(dblcmp_lim(dbuf23[i], -30000, 0.00001) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp(dbuf23[i], -30100 + (i * 909)) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_in.rewind(0);
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(0, dbuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(dblcmp_lim(dbuf20[i], -5000, 0.00001) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i == 19)
    {
      if(dblcmp_lim(dbuf20[i], 10000, 0.00001) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp_lim(dbuf20[i], -5100 + (i * 800), 0.0075) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    hdl_in.rewind(1);
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readPhysicalSamples(1, dbuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(dblcmp_lim(dbuf23[i], -30000, 0.00001) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(dblcmp(dbuf23[i], -30100 + (i * 909)) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 40, EDFreader.EDFSEEK_SET) != 40)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(ibuf20[i] != -1010000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 46, EDFreader.EDFSEEK_SET) != 46)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(ibuf23[i] != 990000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 80, EDFreader.EDFSEEK_SET) != 80)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -1010000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 92, EDFreader.EDFSEEK_SET) != 92)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 20)
    {
      if(ibuf23[i] != 3000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 990000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 60, EDFreader.EDFSEEK_SET) != 60)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -1010000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(1, 69, EDFreader.EDFSEEK_SET) != 69)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 20)
    {
      if(ibuf23[i] != 3000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 990000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 20)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(ibuf20[i] != -1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf20[i] != -1010000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf23) != 23)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(ibuf23[i] != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 20)
    {
      if(ibuf23[i] != 3000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf23[i] != 990000 + (i * 105300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf80) != 80)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<80; i++)
  {
    if(i == 0)
    {
      if(ibuf80[i] != -1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf80[i] != -1010000 + (i * 25300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(1, ibuf92) != 92)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<92; i++)
  {
    if(i == 0)
    {
      if(ibuf92[i] != 1000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(i >= 80)
    {
      if(ibuf92[i] != 3000000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

      continue;
    }

    if(ibuf92[i] != 990000 + (i * 25300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.fseek(0, 165, EDFreader.EDFSEEK_SET) != 165)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.readDigitalSamples(0, ibuf20) != 15)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<15; i++)
  {
    if(ibuf20[i] != -1010000 + ((i + 65) * 25300))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_in.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  hdl_in = null;

  /****************************************/

  if(modify_and_try("test.bdf", 1, new byte[]{'1'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 1, new byte[]{'B'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0, new byte[]{'0'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0, new byte[]{-1})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.bdf", "r");

    fp.seek(0x600);

    fp.read(rbuf, 0, 60);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<20; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*3]&0xff) | ((rbuf[i*3+1]<<8)&0xffff) | (rbuf[i*3+2]<<16)) != -1000000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(i >= 19)
    {
      if(((rbuf[i*3]&0xff) | ((rbuf[i*3+1]<<8)&0xffff) | (rbuf[i*3+2]<<16)) != 1000000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(dblcmp_lim(((rbuf[i*3]&0xff) | ((rbuf[i*3+1]<<8)&0xffff) | (rbuf[i*3+2]<<16)), ((-5100 + (i * 800)) / 0.0075) - 333333.333333, 1.0001) != 0)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  try
  {
    fp = new RandomAccessFile("test.bdf", "r");

    fp.seek(0x63c);

    fp.read(rbuf, 0, 69);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<23; i++)
  {
    if(i == 0)
    {
      if(((rbuf[i*3]&0xff) | ((rbuf[i*3+1]<<8)&0xffff) | (rbuf[i*3+2]<<16)) != 1000000)
      {
        goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
      }

      continue;
    }

    if(dblcmp_lim(((rbuf[i*3]&0xff) | ((rbuf[i*3+1]<<8)&0xffff) | (rbuf[i*3+2]<<16)), ((-30100 + (i * 909)) / 0.01) + 4000000.0, 1.0001) != 0)
    {
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }

  /****************************************/

  if(modify_and_try("test.bdf", 0x37f, new byte[]{'7'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x37f, new byte[]{'8'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0x39e, new byte[]{'6'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x39e, new byte[]{'7'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0x318, new byte[]{'1',' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x318, new byte[]{'-','1'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0x358, new byte[]{'2','0','0','0','0','0','0',' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x358, new byte[]{'1','0','0','0','0','0','0',' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x358, new byte[]{'-','1','0','0','0','0','0','0'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0xec, new byte[]{'+','9'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0xec, new byte[]{'-','9'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0xec, new byte[]{'-','1'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0xec, new byte[]{'0',' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0xec, new byte[]{' ','9'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0xec, new byte[]{'9',' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0x358, new byte[]{'-','8','3','8','8','6','0','9'})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x358, new byte[]{'-','1','0','0','0','0','0','0'})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  /****************************************/

  if(modify_and_try("test.bdf", 0x380, new byte[]{'8','3','8','8','6','0','8',' '})  != 3)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(modify_and_try("test.bdf", 0x380, new byte[]{'1','0','0','0','0','0','0',' '})  != 7)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

/********************************** EDF writing ******************************/

  try
  {
    hdl_out = new EDFwriter("test2.edf", EDFwriter.EDFLIB_FILETYPE_EDFPLUS, 1);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<1; i++)
  {
    if(hdl_out.setSampleFrequency(i, 100) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, 1000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -1000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 32767) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -32768) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setPatientAdditional("Test") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setAdditionalRecordingInfo("tEST") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<100; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

/********************************** EDF reading ******************************/

  try
  {
    hdl_in = new EDFreader("test2.edf");
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.getFileType() != EDFreader.EDFLIB_FILETYPE_EDFPLUS)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getNumSignals() != 1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientAdditional().substring(0, 4).equals("Test") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getRecordingAdditional().substring(0, 4).equals("tEST") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_in.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

/********************************** EDF writing ******************************/

  try
  {
    hdl_out = new EDFwriter("test3.edf", EDFwriter.EDFLIB_FILETYPE_EDFPLUS, 1);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setDataRecordDuration(777770) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setStartDateTime(2008, 12, 31, 23, 59, 58, 1234) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setNumberOfAnnotationSignals(3) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<60; i++)
  {
    l_tmp = 10000L * (i + 1);

    if(hdl_out.writeAnnotation(l_tmp, -1L, String.format("test %d sec", (int)(l_tmp / 10000L))) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    l_tmp += 3333L;

    if(hdl_out.writeAnnotation(l_tmp, -1L, String.format("test %d.%04d sec", (int)(l_tmp / 10000L), (int)(l_tmp % 10000L))) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<1; i++)
  {
    if(hdl_out.setSampleFrequency(i, 100) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, 1000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -1000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 32767) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -32768) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.setPatientName(new String(new byte[]{(byte)0xc3,'l','p','h','a'}, 0, 5, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPatientCode(new String(new byte[]{'B','r',(byte)0xe0,'v',(byte)0xf3}, 0, 5, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setAdministrationCode(new String(new byte[]{'D',(byte)0xeb,'l','t','a'}, 0, 5, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setTechnician(new String(new byte[]{(byte)0xcb,'c','h','o'}, 0, 4, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setEquipment(new String(new byte[]{'F','o','x','t','r',(byte)0xf6,'t'}, 0, 7, "ISO-8859-1")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setAdditionalRecordingInfo("Golf") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setPatientGender(1) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientBirthDate(2005, 7, 4) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setPatientAdditional("Charlie") != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<100; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    for(i=0; i<40; i++)
    {
      if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

/********************************** EDF reading ******************************/

  try
  {
    hdl_in = new EDFreader("test3.edf");
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.annotationslist.size() != 120)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  for(i=0; i<60; i++)
  {
    if(hdl_in.annotationslist.get(i * 2).onset != (10000000L * (i + 1)))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_in.annotationslist.get(i * 2).duration != -1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_in.annotationslist.get(i * 2 + 1).onset != ((10000000L * (i + 1)) + 3333000L))  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_in.annotationslist.get(i * 2 + 1).duration != -1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.getFileType() != EDFreader.EDFLIB_FILETYPE_EDFPLUS)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getNumSignals() != 1)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getStartTimeSubSecond() != 1234000)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientCode().substring(0, 5).equals("Bravo") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientGender().substring(0, 4).equals("Male") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientBirthDate().substring(0, 11).equals("04 jul 2005") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getPatientAdditional().substring(0, 7).equals("Charlie") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getAdministrationCode().substring(0, 5).equals("Delta") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getTechnician().substring(0, 4).equals("Echo") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getEquipment().substring(0, 7).equals("Foxtrot") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_in.getRecordingAdditional().substring(0, 4).equals("Golf") == false)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_in.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    fp = new RandomAccessFile("test3.edf", "r");

    fp.read(rbuf, 0, 256);

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(new String(rbuf, 8, 80).equals("Bravo M 04-JUL-2005 Alpha Charlie                                               ") == false)
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(new String(rbuf, 88, 80).equals("Startdate 31-DEC-2008 Delta Echo Foxtrot Golf                                   ") == false)
      goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

/********************************** BDF writing ******************************/

  try
  {
    hdl_out = new EDFwriter("test4.bdf", EDFwriter.EDFLIB_FILETYPE_BDFPLUS, 1);
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_out.setDataRecordDuration(110000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setStartDateTime(2008, 12, 31, 23, 59, 58, 1234) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  if(hdl_out.setNumberOfAnnotationSignals(3) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  try
  {
    if(hdl_out.writeAnnotation(10000L, -1L, new String(new byte[]{
      (byte)0xeb,(byte)0x8c,(byte)0x80,(byte)0xed,(byte)0x95,(byte)0x9c,(byte)0xeb,(byte)0xaf,(byte)0xbc,(byte)0xea,(byte)0xb5,(byte)0xad,(byte)0x00},
       0, 13, "UTF-8")) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(UnsupportedEncodingException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<50; i++)
  {
    l_tmp = 10000L * (i + 1);

    if(i != 0)
    {
      if(hdl_out.writeAnnotation(l_tmp, -1L, String.format("test %d sec", (int)(l_tmp / 10000L))) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }

    l_tmp += 3333L;

    if(hdl_out.writeAnnotation(l_tmp, -1L, String.format("test %d.%04d sec", (int)(l_tmp / 10000L), (int)(l_tmp % 10000L))) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<1; i++)
  {
    if(hdl_out.setSampleFrequency(i, 100) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMaximum(i, 1000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setPhysicalMinimum(i, -1000) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMaximum(i, 32767) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

    if(hdl_out.setDigitalMinimum(i, -32768) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  for(i=0; i<100; i++)
  {
    dbuf[i] = 0;
  }

  try
  {
    for(i=0; i<10; i++)
    {
      if(hdl_out.writePhysicalSamples(dbuf) != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
    }
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  try
  {
    if(hdl_out.close() != 0)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

/********************************** BDF reading ******************************/

  try
  {
    hdl_in = new EDFreader("test4.bdf");
  }
  catch(IOException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }
  catch(EDFException e)
  {
    goto_exit(new Throwable().getStackTrace()[0].getLineNumber());
  }

  if(hdl_in.annotationslist.size() != 30)  goto_exit(new Throwable().getStackTrace()[0].getLineNumber());

  System.exit(0);
}

private static void goto_exit(int line)
{
  System.out.printf("Error, line: %d\n", line);

  System.exit(1);
}

private static int strcpy(byte[] dest, byte[] src)
{
  int i, sz, srclen;

  sz = dest.length - 1;

  srclen = strlen(src);

  if(srclen > sz)  srclen = sz;

  if(srclen < 0)  return 0;

  for(i=0; i<srclen; i++)
  {
    dest[i] = src[i];
  }

  dest[srclen] = 0;

  return srclen;
}

private static int strlen(byte[] str)
{
  int i;

  for(i=0; i<str.length; i++)
  {
    if(str[i] == 0)
    {
      return i;
    }
  }

  return i;
}

private static int dblcmp(double val1, double val2)
{
  double diff = val1 - val2;

  if(diff > 1e-13)
  {
    return 1;
  }
  else if(-diff > 1e-13)
    {
      return -1;
    }
    else
    {
      return 0;
    }
}

private static int dblcmp_lim(double val1, double val2, double lim)
{
  double diff = val1 - val2;

  if(diff > lim)
  {
    return 1;
  }
  else if(-diff > lim)
    {
      return -1;
    }
    else
    {
      return 0;
    }
}

private static int modify_and_try(String path, int offset, byte[] b)
{
  int i, len;

  RandomAccessFile fp;

  EDFreader hdl=null;

  len = b.length;

  try
  {
    fp = new RandomAccessFile(path, "rw");

    fp.seek(offset);

    for(i=0; i<len; i++)
    {
      fp.write(b[i]);
    }

    fp.close();

    fp = null;
  }
  catch(IOException e)
  {
    return 1;
  }

  try
  {
    hdl = new EDFreader(path);
  }
  catch(IOException e)
  {
    return 2;
  }
  catch(EDFException e)
  {
    if(e.getErrNum() == EDFreader.EDFLIB_FILE_CONTAINS_FORMAT_ERRORS)
    {
      return 3;
    }

    if(e.getErrNum() == EDFreader.EDFLIB_FILE_IS_DISCONTINUOUS)
    {
      return 4;
    }

    return 5;
  }

  try
  {
    hdl.close();
  }
  catch(IOException e)
  {
    return 6;
  }

  return 7;
}

}



