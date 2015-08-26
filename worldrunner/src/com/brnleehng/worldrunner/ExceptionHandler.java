package com.brnleehng.worldrunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ExceptionHandler implements UncaughtExceptionHandler{

	//Utils utils;
	Context context;
	private final String LINE_SEPARATOR = "\n";
	
	public ExceptionHandler(Context con) {
		context = con;
	}
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		StringWriter stackTrace = new StringWriter();
	    arg1.printStackTrace(new PrintWriter(stackTrace));
	    StringBuilder errorReport = new StringBuilder();
	    errorReport.append("************ CAUSE OF ERROR ************\n\n");
	    errorReport.append(stackTrace.toString());

	    errorReport.append("\n************ DEVICE INFORMATION ***********\n");
	    errorReport.append("Brand: ");
	    errorReport.append(Build.BRAND);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("Device: ");
	    errorReport.append(Build.DEVICE);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("Model: ");
	    errorReport.append(Build.MODEL);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("Id: ");
	    errorReport.append(Build.ID);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("Product: ");
	    errorReport.append(Build.PRODUCT);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("\n************ FIRMWARE ************\n");
	    errorReport.append("SDK: ");
	    errorReport.append(Build.VERSION.SDK);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("Release: ");
	    errorReport.append(Build.VERSION.RELEASE);
	    errorReport.append(LINE_SEPARATOR);
	    errorReport.append("Incremental: ");
	    errorReport.append(Build.VERSION.INCREMENTAL);
	    errorReport.append(LINE_SEPARATOR);


	    Intent i = new Intent(context, ExceptionActivity.class);
	    i.putExtra("exception",errorReport.toString());
	    context.startActivity(i);


	    android.os.Process.killProcess(android.os.Process.myPid());
	    System.exit(10);
	}

}
