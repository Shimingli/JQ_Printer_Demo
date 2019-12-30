package com.jq.ui.print_test.jqtek_mode2;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.R;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define;
import com.jq.printer.cpcl.CPCL;
import com.jq.printer.jpl.Barcode;
import com.jq.printer.jpl.Image;
import com.jq.printer.jpl.JPL;
import com.jq.printer.jpl.Page;
import com.jq.printer.jpl.Text;
import com.jq.ui.DemoApplication;

import java.text.SimpleDateFormat;

public class JQTEKMode2Activity extends Activity {
    private JQPrinter Printer = new JQPrinter(Printer_define.PRINTER_MODEL.JLP352Std);

    private Button mButtonJQTEKMode2Print = null;

    private EditText mEditText = null;

    boolean rePrint = false;//是否需要重新打印
    int startIndex;//开始打印的序号
    int amount;//打印的总数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jqtekmode2);

        mButtonJQTEKMode2Print = (Button)findViewById(R.id.ButtonJQTEKMode2Print);

        mEditText = (EditText)findViewById(R.id.EditText);

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
        getMenuInflater().inflate(R.menu.jqtekmodel2, menu);
        return true;
    }


    private boolean model2_Print() {//CPCL打印
        String InputText;
        if (!Printer.getCPCLsupport()) {
            Toast.makeText(this, "不支持CPCL，请设置正确的打印机型号！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        InputText=mEditText.getText().toString();
        CPCL cpcl = Printer.cpcl;
        cpcl.text.portSendCmd("! 0 200 200 755 1\r\n "  ,1                );
        cpcl.text.portSendCmd("PAGE-WIDTH 574\r\n "     ,1                );
        cpcl.text.portSendCmd("TEXT 24 20 10 530 " + InputText + "\r\n ",1);
        cpcl.text.portSendCmd("FORM\r\n"   ,1                              );
        cpcl.text.portSendCmd("PRINT\r\n"  ,1                              );
        return true;
    }

    public void ButtonJQTEKMode2Print_click(View view){
        mButtonJQTEKMode2Print.setText("发送数据");
        mButtonJQTEKMode2Print.setVisibility(Button.INVISIBLE);

        if (!Printer.isOpen)
        {
            this.finish();
            return;
        }
//		if (!getPrinterState())
//		{
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}
        if (!rePrint)
        {
            amount = 1;
            startIndex = 1;
        }
        else
        {
            //软件无法判断当前打印的内容是否打印完好，所以需要重新打印当前张。你可以增加一个按钮来决定是打当前张还是打下一张。
            rePrint = false;
        }
        if (model2_Print())
            mButtonJQTEKMode2Print.setVisibility(Button.VISIBLE);

    }
}

