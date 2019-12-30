package com.jq.ui.print_test.table;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
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
import com.jq.printer.jpl.Image;
import com.jq.printer.jpl.JPL;
import com.jq.printer.jpl.Page;
import com.jq.printer.jpl.Text;
import com.jq.ui.DemoApplication;

import java.text.SimpleDateFormat;

public class TableExampleActivity extends Activity {

    private JQPrinter Printer = new JQPrinter(Printer_define.PRINTER_MODEL.JLP352Std);

    private Button mButtonTablePrint = null;

    private EditText mTableEditText1 = null;
    private EditText mTableEditText2 = null;
    private EditText mTableEditText3 = null;
    private EditText mTableEditText4 = null;
    private EditText mTableEditText5 = null;
    private EditText mTableEditText6 = null;
    private EditText mTableEditText7 = null;
    private EditText mTableEditText8 = null;

    boolean rePrint = false;//是否需要重新打印
    int startIndex;//开始打印的序号
    int amount;//打印的总数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_example);
        setTitle("自定义打印表格内容     ");
        mButtonTablePrint = (Button)findViewById(R.id.ButtonTablePrint);

        mTableEditText1 = (EditText)findViewById(R.id.TableEditText1);
        mTableEditText2 = (EditText)findViewById(R.id.TableEditText2);
        mTableEditText3 = (EditText)findViewById(R.id.TableEditText3);
        mTableEditText4 = (EditText)findViewById(R.id.TableEditText4);
        mTableEditText5 = (EditText)findViewById(R.id.TableEditText5);
        mTableEditText6 = (EditText)findViewById(R.id.TableEditText6);
        mTableEditText7 = (EditText)findViewById(R.id.TableEditText7);
        mTableEditText8 = (EditText)findViewById(R.id.TableEditText8);
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
        getMenuInflater().inflate(R.menu.table_example, menu);
        return true;
    }


    private boolean tablePrint(){
            String InputText;
            Paint paint= new Paint();
            Printer.jpl.intoGPL(1000);
            if (!Printer.getJPLsupport())
            {
                Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
                return false;
            }
            JPL jpl = Printer.jpl;

            SimpleDateFormat formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");

            int y = 0,n=0;
            if (!jpl.page.start(0, 0, 576, 576, Page.PAGE_ROTATE.x0))//设置打印页面
                return false;

            y=20;//横线1
            if (!jpl.graphic.line(20, 20, 550, 20, 3))
                return false;
            y=y+3;
            InputText=mTableEditText1.getText().toString();

            jpl.textarea.setArea(30, y+10, 120, 60);
            jpl.textarea.setSpace(0, 4);
            jpl.textarea.setFont(Printer_define.FONT_ID.ASCII_16x32, Printer_define.FONT_ID.GBK_32x32);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);

            InputText=mTableEditText2.getText().toString();
            jpl.textarea.setArea(170, y+10, 380, 60);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);

            y=y+60;//横线2
            if (!jpl.graphic.line(20, y, 550, y, 3))
                return false;
            y=y+3;

            InputText=mTableEditText3.getText().toString();
            jpl.textarea.setArea(30, y+10, 120, 60);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);

            InputText=mTableEditText4.getText().toString();
            jpl.textarea.setArea(170, y+10, 380, 60);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);

            y=y+60;//横线3
            if (!jpl.graphic.line(20, y, 550, y, 3))
               return false;
            y=y+3;

            InputText=mTableEditText5.getText().toString();

            jpl.textarea.setArea(30, y+10, 120, 60);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);

            InputText=mTableEditText6.getText().toString();

            n=60*((int)(paint.measureText(InputText))/132)+60;
            jpl.textarea.setArea(170, y+10, 380, n);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);
            y=y+n;//横线4
            if (!jpl.graphic.line(20, y, 550, y, 3))
                return false;
            y=y+3;

            InputText=mTableEditText7.getText().toString();
            jpl.textarea.setArea(30, y+10, 120, 60);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);

            InputText=mTableEditText8.getText().toString();

            n=60*((int)(paint.measureText(InputText))/132)+60;
  //          InputText=""+Integer.toString((int) (paint.measureText(InputText)))+InputText;
            jpl.textarea.setArea(170, y+10, 380, n);
            jpl.textarea.drawOut(Printer_define.ALIGN.LEFT,InputText);
            y=y+n;//横线5
            if (!jpl.graphic.line(20, y, 550, y, 3))
                return false;

            if (!jpl.graphic.line(20, 20, 20, y, 3))
                return false;
            if (!jpl.graphic.line(160, 20, 160, y, 3))
                return false;
            if (!jpl.graphic.line(550, 20, 550, y, 3))
                return false;
            jpl.page.print();
            jpl.feedNextLabelBegin();
            return Printer.jpl.exitGPL(1000);
        }


    public void ButtonTablePrint_click(View view){
        mButtonTablePrint.setText("打印");
        mButtonTablePrint.setVisibility(Button.INVISIBLE);

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
        if (tablePrint()) mButtonTablePrint.setVisibility(Button.VISIBLE);

    }
}
