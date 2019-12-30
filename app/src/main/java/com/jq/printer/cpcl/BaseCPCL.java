package com.jq.printer.cpcl;

import android.util.Log;

import com.jq.port.Port;
import com.jq.printer.PrinterParam;
import com.jq.printer.Printer_define.PRINTER_MODEL;

import java.io.UnsupportedEncodingException;

public class BaseCPCL {
	protected PRINTER_MODEL _model;
	public Port _port;
	protected boolean _support;
	protected PrinterParam _param;
	public byte[] _cmd = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	public BaseCPCL(PrinterParam param) {
		_param = param;
		_model = _param.model;
		_port = _param.port;
		_support = _param.cpclSupport;		
	}

	public boolean portSendCmd(String cmd)
	{
		cmd +="\r\n";
		byte[] data = null;
		try 
		{
			data = cmd.getBytes("GBK");
//			data = cmd.getBytes("UTF-16LE");
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.e("JQ", "Sting getBytes('GBK') failed");
			return false;
		}
		return _port.write(data, 0, data.length);
	}

	public boolean portSendCmd(String cmd,int type)
	{
		cmd +="\r\n";
		byte[] data = null;
		try
		{
			if(type==1){
				data = cmd.getBytes("UTF-16LE");
			}else{
				data = cmd.getBytes("GBK");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			Log.e("JQ", "Sting getBytes('GBK') failed");
			return false;
		}
		return _port.write(data, 0, data.length);
	}

}