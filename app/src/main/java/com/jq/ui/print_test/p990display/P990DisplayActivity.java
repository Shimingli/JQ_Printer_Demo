package com.jq.ui.print_test.p990display;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jq.R;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define;
import com.jq.printer.esc.ESC;
import com.jq.printer.jpl.JPL;
import com.jq.printer.jpl.Page;
import com.jq.ui.DemoApplication;

import java.text.SimpleDateFormat;

public class P990DisplayActivity extends Activity {
    private Button mButtonUpdateDisplay = null;
    private Button mButtonCleanDisplay = null;

    private EditText mLine1SizeText = null;
    private EditText mLine2SizeText = null;
    private EditText mLine3SizeText = null;

    private EditText mLine1Text = null;
    private EditText mLine2Text = null;
    private EditText mLine3Text = null;

    private JQPrinter Printer = new JQPrinter(Printer_define.PRINTER_MODEL.JLP352Std);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p990_display);
        setTitle("P990水墨屏显示内容设置");

        mButtonUpdateDisplay = (Button)findViewById(R.id.ButtonUpdateDisplay);
        mButtonCleanDisplay = (Button)findViewById(R.id.ButtonCleanDisplay);

        mLine1SizeText = (EditText)findViewById(R.id.Line1SizeText);
        mLine2SizeText = (EditText)findViewById(R.id.Line2SizeText);
        mLine3SizeText = (EditText)findViewById(R.id.Line3SizeText);

        mLine1Text = (EditText)findViewById(R.id.Line1Text);
        mLine2Text = (EditText)findViewById(R.id.Line2Text);
        mLine3Text = (EditText)findViewById(R.id.Line3Text);

        DemoApplication app = (DemoApplication)getApplication();
        if (app.printer != null)
        {
            Printer = app.printer;
        }
        else
        {
            Log.e("JQ", "app.printer null");
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.p990_display, menu);
        return true;
    }


    private boolean eSC_P990Display() {//文本+二维码  //mLine1SizeText  mLine1Text
        int x,y,height,remote;
        String text;
        Printer.esc.wakeUp();
        Printer.esc.text.init();
        y=0;
        height=Integer.parseInt(mLine1SizeText.getText().toString());
        switch (height){
            case 1 :{
                height=16;
                break;
            }
            case 2 :{
                height=24;
                break;
            }
            case 3 :{
                height=32;
                break;
            }
            case 4 :{
                height=48;
                break;
            }
            default:{
                height=16;
                mLine3SizeText.setText("1");
            }
        }
        text=mLine1Text.getText().toString();
       Printer.esc.text.p990Display(0,0,height,0,text);

       y=height+4;
       height=Integer.parseInt(mLine2SizeText.getText().toString());
       switch (height){
           case 1 :{
               height=16;
               break;
           }
           case 2 :{
               height=24;
               break;
           }
           case 3 :{
               height=32;
               break;
           }
           case 4 :{
               height=48;
               break;
           }
           default:{
               height=16;
               mLine3SizeText.setText("1");
           }
       }
        text=mLine2Text.getText().toString();
        Printer.esc.text.p990Display(0,y,height,0,text);
        y=y+height+4;

        height=Integer.parseInt(mLine3SizeText.getText().toString());
        switch (height){
            case 1 :{
                height=16;
                break;
            }
            case 2 :{
                height=24;
                break;
            }
            case 3 :{
                height=32;
                break;
            }
            case 4 :{
                height=48;
                break;
            }
            default:{
                height=16;
                mLine3SizeText.setText("1");
            }
        }
        text=mLine3Text.getText().toString();
        Printer.esc.text.p990Display(0,y,height,0,text);

        Printer.esc.text.p990DisplayUpdate();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
       Printer.esc.text.p990DisplayClose();
        return true;
    }

    public void ButtonStartDisplay_click(View view){
//        mButtonCleanDisplay.setText("ESC打印");
        mButtonCleanDisplay.setVisibility(Button.INVISIBLE);

//        if (!Printer.isOpen) {
//            this.finish();
//            return;
 //       }

//		if (!getPrinterState()) {
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}

        if(Printer.esc.text.p990DisplayOpen())
            mButtonCleanDisplay.setVisibility(Button.VISIBLE);
        try {
            Thread.currentThread().sleep(2000);//阻断2秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ButtonUpdateDisplay_click(View view){
 //       mButtonUpdateDisplay.setText("ESC打印");
        mButtonUpdateDisplay.setVisibility(Button.INVISIBLE);

//        if (!Printer.isOpen) {
 //           this.finish();
 //           return;
//        }

//		if (!getPrinterState()) {
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}

        if(eSC_P990Display())//ESC_print1 ESC_print
            mButtonUpdateDisplay.setVisibility(Button.VISIBLE);

    }

    public void ButtonCleanDisplay_click(View view){
//        mButtonCleanDisplay.setText("ESC打印");
        mButtonCleanDisplay.setVisibility(Button.INVISIBLE);

//        if (!Printer.isOpen) {
 //           this.finish();
 //           return;
 //       }

//		if (!getPrinterState()) {
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}

        if(Printer.esc.text.p990DisplayOpen())
            mButtonCleanDisplay.setVisibility(Button.VISIBLE);

        try {
            Thread.currentThread().sleep(2000);//阻断2秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(Printer.esc.text.p990DisplayClose())
            mButtonCleanDisplay.setVisibility(Button.VISIBLE);
    }
}

