package com.jq.ui.expresstest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jq.port.Port;
import com.jq.R;
import com.jq.printer.JQPrinter;
import com.jq.printer.Printer_define.ALIGN;
import com.jq.printer.Printer_define.FONT_ID;
import com.jq.printer.Printer_define.PRINTER_MODEL;
import com.jq.printer.cpcl.BaseCPCL;
import com.jq.printer.esc.BaseESC;
import com.jq.printer.esc.ESC;
import com.jq.printer.jpl.Barcode.BAR_ROTATE;
import com.jq.printer.jpl.Barcode.BAR_UNIT;
import com.jq.printer.jpl.Barcode.QRCODE_ECC;
import com.jq.printer.jpl.Image.IMAGE_ROTATE;
import com.jq.printer.jpl.JPL;
import com.jq.printer.jpl.JPL.ROTATE;
import com.jq.printer.jpl.Page.PAGE_ROTATE;
import com.jq.printer.jpl.Text.TEXT_ENLARGE;
import com.jq.ui.DemoApplication;

import com.jq.printer.cpcl.CPCL;
//import com.jq.printer.esc.ESC;

import java.text.SimpleDateFormat;


public class MainExpressTestActivity extends Activity {
	int startIndex;//开始打印的序号
    int amount;//打印的总数
	boolean rePrint = false;//是否需要重新打印
    private Port _port;
	private JQPrinter Printer = new JQPrinter(PRINTER_MODEL.JLP351);
	private Button buttonPrint = null;
    private Button buttonCPCLPrint = null;
    private Button buttonESCPrint = null;

    private char[] logotest_data = {
                    0xFF,0xFF,0xFF,0xFF,
                    0xFF,0xFF,0xFF,0xFF,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,
            0x00,0x00,0x00,0xC0,



    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_express_test);
		
		buttonPrint = (Button)findViewById(R.id.buttonExpressTestPrint);
        buttonCPCLPrint = (Button)findViewById(R.id.buttonCPCLTestPrint);
        buttonESCPrint= (Button)findViewById(R.id.buttonESCTestPrint);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_express_test, menu);
		return true;
	}
	
	private boolean getPrinterState()
	{
		if (!Printer.getPrinterState(3000))
		{
			Toast.makeText(this, "获取打印机状态失败", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (Printer.isCoverOpen)
		{
			Toast.makeText(this, "打印机纸仓盖未关闭", Toast.LENGTH_SHORT).show();
			return false;
		}
		else if (Printer.isNoPaper)
		{
			Toast.makeText(this, "打印机缺纸", Toast.LENGTH_SHORT).show();			
			return false;
		}
		return true;
	}

	public boolean printtest(String order_num)
	{
		if (!Printer.getJPLsupport())
    	{
    		Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show(); 
    		return false;
    	}
		JPL jpl = Printer.jpl;
		if (startIndex<=0)
			startIndex = 1;
		for (; startIndex<= amount;startIndex++)
		{
			jpl.page.start(0,0,576,424,PAGE_ROTATE.x90);
			jpl.image.drawOut(0,0,this.getResources(), R.drawable.ic_launcher,IMAGE_ROTATE.x0);
			String bar_str = String.format(order_num+"-%03d-%03d",startIndex, amount);
			jpl.barcode.code128(ALIGN.CENTER,0,575, 0, 64, BAR_UNIT.x2, BAR_ROTATE.ANGLE_0, bar_str);//printer.jpl.barcode.code128(16, 0, 64, BAR_UNIT.x3, BAR_ROTATE.ANGLE_0, bar_str);
		//	printer.jpl.barcode.PDF417(16, 68, 5, 3, 4, BAR_UNIT.x2, ROTATE.x0, bar_str);
			jpl.text.drawOut(ALIGN.CENTER,0,575, 66, bar_str, 16, false, false, false, false, TEXT_ENLARGE.x2, TEXT_ENLARGE.x1, ROTATE.x0);//printer.jpl.text.drawOut(96, 64, bar_str);
		//	printer.jpl.barcode.QRCode(0, 120, 0, QRCODE_ECC.LEVEL_M, BAR_UNIT.x3, ROTATE.x0, bar_str);
			
			
			jpl.graphic.line(8,96,568,96, 3);
			jpl.graphic.line(8, 160, 568, 160, 3);
			jpl.graphic.line(8, 224,568, 224, 3);
			jpl.graphic.line(8, 288,568, 288, 3);
			
			jpl.graphic.line(8,96,8, 288, 3);
			jpl.graphic.line(568, 96,568, 288, 3);
			jpl.graphic.line(304, 96,304, 224, 3);
			jpl.graphic.line(456, 96,456, 160, 3);
			
			jpl.text.drawOut(14, 104, "杭州——" + "上海东", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x2, ROTATE.x0);			
			jpl.text.drawOut(320, 104, String.valueOf(startIndex)+"/"+amount, 48, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(464, 104, "特快", 48, true, true, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(14, 168, "上海济强电子科技有限公司", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(320, 168, "张三", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(320, 168+26, "021-61645760", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.text.drawOut(14, 232, "上海浦东金藏路258号2号楼2楼", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);			
			jpl.text.drawOut(14, 296, "全速物流www.qs-express.com", 32, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
			jpl.page.end();
			jpl.page.print();
			jpl.feedMarkOrGap(0);//printer.jpl.feedNextLabelEnd(48);//printer.jpl.feedNextLabelBegin();
			int i = 0;
			for(i=0;i<10;i++)
			{
				try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
				if (!Printer.getPrinterState(4000))//此处的读超时需要算上打印内容的时间。请根据打印内容调整,如果你打印的内容更多，就需要设置更多的时间。
				{
					Toast.makeText(this, "获取打印机状态失败", Toast.LENGTH_SHORT).show(); 
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
					continue;
				}			
				if (Printer.isCoverOpen)
				{
					buttonPrint.setText("纸仓未关--重新打印");
					rePrint = true;
					return true;
				}
				else if (Printer.isNoPaper )
				{
					buttonPrint.setText("缺纸--重新打印");
					rePrint = true;
					return true;
				}				
				if ( Printer.isPrinting)
				{
					Toast.makeText(this, "正在打印", Toast.LENGTH_SHORT).show(); 
					try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
				}
				else
				{
					break;
				}				
			}
			
		}
		Toast.makeText(this, "打印结束", Toast.LENGTH_SHORT).show(); 
		return true;
	}
	
    private boolean printExpressTest1()
    {
    	Printer.jpl.intoGPL(1000);
    	if (!Printer.getJPLsupport())
    	{
    		Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show(); 
    		return false;
    	}
    	JPL jpl = Printer.jpl;
    	
    	SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");    	
    	
        int y = 0, y1, y2, y3, y4, x, x1, x2, x3;
        if (!jpl.page.start(0, 0, 576, 655+28+13+70, PAGE_ROTATE.x0))
            return false;
        jpl.image.drawOut(175, 0, this.getResources(), R.drawable.shenzhenhangkong, IMAGE_ROTATE.x0);
        y += 48;
        if (!jpl.text.drawOut(0, y, "4008-999-999", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        
        y += 32;
        if (!jpl.text.drawOut(0, y, "www.jqtek.com", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        y =100;
        y += 2;
        
        y += 30;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;

        String bar_str = "101213-12312312-231313";
        y += 8;
        if (!jpl.barcode.code128(ALIGN.CENTER, 0, 575, y, 80, BAR_UNIT.x2, BAR_ROTATE.ANGLE_0, bar_str))
            return false;           
        y += 81;
        jpl.text.drawOut(ALIGN.CENTER,0,575, y, bar_str, 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 25;
        y1 = y;
        if (!jpl.graphic.line(0, y1, 575, y1, 3))
            return false;
        /////////////////////
        y += 4;
        if (!jpl.text.drawOut(3, y, "寄件方信息:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(200, y, "自寄□ 自取□ 原寄地:021CA", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        boolean textarea_enable = true;
        y += 28;
        if (!textarea_enable)
        {        
        	if (!jpl.text.drawOut(3, y, "上海济强电子科技有限公司", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
        		return false;

        	y += 20;
        	jpl.text.drawOut(3, y, "上海浦东新区金藏路258号2号楼2楼东区", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        	y += 20;
        	if (!jpl.text.drawOut(3, y, "西门吹风 133-3333-3333", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
        		return false;
        	y += 20;            
        }
        else
        {
        	int height =58;
        	jpl.textarea.setArea(3, y, 576, height);
        	jpl.textarea.setSpace(0, 4);
        	jpl.textarea.setFont(FONT_ID.ASCII_8x16, FONT_ID.GBK_16x16);
        	jpl.textarea.drawOut("上海济强电子科技有限公司\r" + "上海浦东新区金藏路258号2号楼2楼东区\n" + "西门吹风 133-3333-3333");
        	y += height; 
        }

        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        //////////////////////
        y += 4;
        if (!jpl.text.drawOut(3, y, "收件方信息:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(350, y, "目的地:488", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 26;
		if (!textarea_enable) {
			if (!jpl.text.drawOut(3, y, "北京济强电子科技有限公司"))
				return false;
			y += 26;
			jpl.text.drawOut(3, y, "北京海淀区金藏路258号2号楼2楼西区");
			y += 26;
			jpl.text.drawOut(3, y, "独孤求胜 188-8888-8888");
			y += 27;
		}
		else
		{
			int height =24*4+4*4;
        	jpl.textarea.setArea(3, y, 576, height);
        	jpl.textarea.setSpace(0, 4);
        	jpl.textarea.setFont(FONT_ID.ASCII_12x24, FONT_ID.GBK_24x24);
        	jpl.textarea.drawOut("ShangHai New JiQiang MicroElectroinic Supper Technology Ltd.\r\n" + "上海浦东新区金藏路258号2号楼2楼东区\n" + "西门吹风 133-3333-3333");
        	y += height; 
		}
        y2 = y;
        if (!jpl.graphic.line(0, y2, 575, y2, 3))
            return false;
        //////////////////////
        y += 4;
        x1 = 288 - 3;
        if (!jpl.text.drawOut(3, y, "物品信息:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        if (!jpl.text.drawOut(x1, y, "附加服务:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;

        y += 26;
        if(!textarea_enable)
        {
        	if (!jpl.text.drawOut(3, y, "  电脑一台", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
        		return false;
        	jpl.text.drawOut(x1, y, "  保价10000元", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        	y += 48;            
        }
        else
        {
        	int height =(16+4)*3;
        	jpl.textarea.setArea(4, y, 288-15, height);
        	jpl.textarea.setSpace(0, 4);
        	jpl.textarea.setFont(FONT_ID.ASCII_8x16, FONT_ID.GBK_16x16);
        	jpl.textarea.drawOut(ALIGN.LEFT,"笔记本电脑一台,全新电脑桌子一只,阿迪达斯鞋子一双,未知东西三枚\r还有东西待定");
        	
        	jpl.textarea.setArea(x1, y, 576, height);
        	jpl.textarea.drawOut(ALIGN.CENTER,"保价一千二百三十四万五千六百元整");
        	
        	y += height; 
        }
        
        y3 = y;
        if (!jpl.graphic.line(0, y3, 575, y3, 3))
            return false;
        //////////////////////
        y += 5;
        jpl.text.drawOut(3, y, "业务类型:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        if (!jpl.text.drawOut(3 + 6 * 16, y, "标准快递", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;

        if (!jpl.text.drawOut(x1, y, "件散:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        x2 = 430;
        if (!jpl.text.drawOut(x2, y, "标准快递", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;

        y += 20;
        if (!jpl.text.drawOut(3, y, "付款方式:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(3 + 6 * 16, y, "寄付", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        if (!jpl.text.drawOut(x1, y, "实际重量:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(x2, y, "1", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        if (!jpl.text.drawOut(3, y, "月结帐号:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(3 + 6 * 16, y, "02178782212", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        if (!jpl.text.drawOut(x1, y, "费用:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(x2, y, "1", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        if (!jpl.text.drawOut(3, y, "第三方地区:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(3 + 6 * 16, y, "\\", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        if (!jpl.text.drawOut(x1, y, "计算重量:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(x2, y, "12", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        //printer.jpl.text.drawOut(3, y, " ", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        //printer.jpl.text.drawOut(3 + 6 * 16, y, " ", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        if (!jpl.text.drawOut(x1, y, "费用合计:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(x2, y, "12", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        y4 = y;
        if (!jpl.graphic.line(0, y4, 575, y4, 3))
            return false;
        //////////////////////
        y += 4;
        x3 = 150;
        if (!jpl.text.drawOut(3, y, "寄方签名:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(x3, y, "收件员:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x1, y, "收方签名:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x2, y, "派件员:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 30;
        if (!jpl.text.drawOut(x3, y, "王二", 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;

        y += 30;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        jpl.graphic.line(x2 - 5, y3, x2 - 5, y, 2);//竖线    
        jpl.graphic.line(x3 - 5, y4, x3 - 5, y, 2);//竖线 
        //////////////////////
        y += 4;
        if (!jpl.text.drawOut(3, y, "寄件日期:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(3 + 16 * 5, y, formatter.format(System.currentTimeMillis()), 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x1, y, "收件日期:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        //////////////////////
        jpl.graphic.line(x1 - 5, y2, x1 - 5, y, 2);//竖线

        if (!jpl.graphic.line(0, y1, 0, y, 3))
            return false;//竖线
        jpl.graphic.line(575, y1, 575, y, 3);//竖线 
        jpl.page.end();
        jpl.page.print();
        if (!Printer.esc.feedLines(1))
            return false;
        // 第一联结束          
        // printer.jpl.feedNextLabelBegin();
        if (!jpl.page.start(0, 0, 576, 500, PAGE_ROTATE.x0))
            return false;

        y = 2;
        x = 3;
        if (!jpl.text.drawOut(x, y, "4008-999-999", 32, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        x1 = x + 16 * 13;
        jpl.text.drawOut(x1, y, "www.jqtek.com", 32, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        x2 = x1 + 16 * 14;
        jpl.text.drawOut(x2, y, "客户联", 32, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 36;
        y1 = y;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        y += 4;
        jpl.text.drawOut(x, y, "运单号码:", 24, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x + 5 * 24, y, bar_str, 24, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 28;
        jpl.graphic.line(0, y, 575, y, 3);

        y += 4;
        if (!jpl.text.drawOut(x, y, "寄件方信息:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(200, y, "自寄□ 自取□", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        if (!jpl.text.drawOut(x, y, "上海济强电子科技有限公司", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;

        y += 20;
        jpl.text.drawOut(x, y, "上海浦东新区金藏路258号2号楼2楼东区", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 20;
        if (!jpl.text.drawOut(x, y, "西门吹风 133-3333-3333", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;

        y += 20;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        //////////////////////
        y += 4;
        jpl.text.drawOut(x, y, "收件方信息:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 20;
        jpl.text.drawOut(x, y, "北京济强电子科技有限公司", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 20;
        jpl.text.drawOut(x, y, "北京海淀区金藏路258号2号楼2楼西区", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 20;
        jpl.text.drawOut(x, y, "独孤求胜 188-8888-8888", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 22;
        y2 = y;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        //////////////////////
        y += 4;
        x1 = x + 6 * 16;
        jpl.text.drawOut(x, y, "物品信息:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x1, y, "电脑一台", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        x2 = 576 * 2 / 3 - 5;
        jpl.text.drawOut(x2, y, "请扫描2维码下载APP", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y3 = y + 25;
        if (!jpl.barcode.QRCode(x2 + 8, y3, 0, QRCODE_ECC.LEVEL_M, BAR_UNIT.x4, ROTATE.x0, "http://www.jqsh.com/front/bin/ptdetail.phtml?Part=pro_08&Rcg=1"))
            return false;
        //printer.jpl.barcode.QRCode(x2 + 8, y3, 0, QRCODE_ECC.LEVEL_M, BAR_UNIT.x3, ROTATE.x0, "13413");
        y += 40;
        jpl.text.drawOut(x, y, "附加服务:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x1, y, "保价10000元", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 48;
        if (!jpl.graphic.line(0, y, x2 - 3, y, 3))
            return false;
        //////////////////////
        y += 5;
        jpl.text.drawOut(x, y, "业务类型:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.text.drawOut(x1, y, "标准快递", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 20;
        jpl.text.drawOut(x, y, "付款方式:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x1, y, "寄付", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 20;
        if (!jpl.text.drawOut(x, y, "费用合计:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0))
            return false;
        jpl.text.drawOut(x1, y, "12", 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        y += 20;
        jpl.text.drawOut(x, y, "寄件日期:", 16, true, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(x1, y, formatter.format(System.currentTimeMillis()), 16, false, false, false, false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        y += 22;
        if (!jpl.graphic.line(0, y, 575, y, 3))
            return false;
        jpl.graphic.line(x2 - 3, y2, x2 - 3, y, 2);//竖线

        jpl.graphic.line(0, y1, 0, y, 2);//竖线
        jpl.graphic.line(575, y1, 575, y, 2);//竖线
//        return   jpl.page.print();
        
    	

//      return  jpl.feedNextLabelBegin();
        jpl.page.print();
       return Printer.jpl.exitGPL(1000);
        //return Printer.esc.feedLines(2);
    }


    private boolean printmiandian()//缅甸文字图片打印
    {
        Printer.jpl.intoGPL(1000);
        if (!Printer.getJPLsupport())
        {
            Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JPL jpl = Printer.jpl;

        SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");

        int y = 0, y1, y2, y3, y4, x, x1, x2, x3;
        if (!jpl.page.start(0, 0, 576, 600, PAGE_ROTATE.x0))//设置打印页面
            return false;


       jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,16 ,"缅甸文字打印" ,40 ,
               false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        /*打印信息*/
//       jpl.image.drawOut(80, 0, this.getResources(), R.drawable.shenzhenhangkong, IMAGE_ROTATE.x0);
      jpl.image.drawOut(50, 80, this.getResources(), R.drawable.miandian1, IMAGE_ROTATE.x0);
        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,100 ,"你好" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.image.drawOut(50, 150, this.getResources(), R.drawable.miandian2, IMAGE_ROTATE.x0);
        jpl.text.settextgird(2,2,0);
        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,260,"很高兴见到你" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.settextgird(0,0,0);
        jpl.image.drawOut(50, 310, this.getResources(), R.drawable.miandian3, IMAGE_ROTATE.x0);


//        jpl.text.drawOut(ALIGN.LEFT , 0 , 576 ,70,"图章" ,40 ,
//                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
//        jpl.text.drawOut(ALIGN.LEFT , 10 , 576 ,140 -30,"右边" ,40 ,
//                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
 //       jpl.text.drawOut(ALIGN.LEFT , 10 , 576 ,185-30 ,"是" ,40 ,
  //              false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
 //       jpl.text.drawOut(ALIGN.LEFT , 10 , 576 ,230-30 ,"图章" ,40 ,
  //              false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.text.drawOut(ALIGN.CENTER , 50 , 576 ,420 ,"有志者事竟成" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

 //       jpl.image.drawOut(0, 470, this.getResources(), R.drawable.test2, IMAGE_ROTATE.x0);

        jpl.page.print();
        return Printer.jpl.exitGPL(1000);
    }

    private boolean printtest()//图片打印
    {
        Printer.jpl.intoGPL(1000);
        if (!Printer.getJPLsupport())
        {
            Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JPL jpl = Printer.jpl;

        SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");

        int y = 0, y1, y2, y3, y4, x, x1, x2, x3;
        if (!jpl.page.start(0, 0, 576, 600, PAGE_ROTATE.x0))//设置打印页面
            return false;


        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,16 ,"图片打印" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.image.drawOut(150, 80, this.getResources(), R.drawable.logotest, IMAGE_ROTATE.x0);

        jpl.text.drawOut(ALIGN.LEFT , 10 , 576 ,140 -30,"右边" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(ALIGN.LEFT , 10 , 576 ,185-30 ,"是" ,40 ,
                      false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(ALIGN.LEFT , 10 , 576 ,230-30 ,"图章" ,40 ,
                      false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.text.drawOut(ALIGN.CENTER , 50 , 576 ,500 ,"JPL图片打印" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);


        jpl.page.print();
        return Printer.jpl.exitGPL(1000);
    }

    private boolean miandiantest()//缅甸图片打印
    {
        Printer.jpl.intoGPL(1000);
        if (!Printer.getJPLsupport())
        {
            Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JPL jpl = Printer.jpl;

        SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");

        int y = 0, y1, y2, y3, y4, x, x1, x2, x3;
        if (!jpl.page.start(0, 0, 576, 1200, PAGE_ROTATE.x0))//设置打印页面
            return false;
        jpl.image.drawOut(0, 30, this.getResources(), R.drawable.miandiantest2, IMAGE_ROTATE.x0);
        jpl.page.print();
        return Printer.jpl.exitGPL(1000);
    }

    private boolean JLP451PrintTest()//TEST
    {
        Printer.jpl.intoGPL(1000);
        if (!Printer.getJPLsupport())
        {
            Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JPL jpl = Printer.jpl;

        SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy年MM月dd日");

        int y = 0, y1, y2, y3, y4, x, x1, x2, x3;
        if (!jpl.page.start(0, 0, 864, 840, PAGE_ROTATE.x0))//设置打印页面
            return false;

        jpl.image.drawOut(80,50 , this.getResources(), R.drawable.test1, IMAGE_ROTATE.x0);

        jpl.page.print();
//        jpl.feedNextLabelBegin();
        return Printer.jpl.exitGPL(1000);
    }

    private boolean printTestcode()//unicode 测试
    {
        Printer.jpl.intoGPL(1000);
        if (!Printer.getJPLsupport())
        {
            Toast.makeText(this, "不支持JPL，请设置正确的打印机型号！", Toast.LENGTH_SHORT).show();
            return false;
        }
        JPL jpl = Printer.jpl;

        if (!jpl.page.start(0, 0, 576, 300, PAGE_ROTATE.x0))//设置打印页面
            return false;

        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,10 ,"123456789" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,60 ,"987654321" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,110 ,"ABCDEFG" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);
        jpl.text.drawOut(ALIGN.CENTER , 0 , 576 ,160 ,"上海济强电子科技有限公司" ,40 ,
                false,false,false,false, TEXT_ENLARGE.x1, TEXT_ENLARGE.x1, ROTATE.x0);

        jpl.page.print();
        return Printer.jpl.exitGPL(1000);
    }

    private boolean ESC_print() {

        Printer.esc.wakeUp();
        Printer.esc.text.init();
        Printer.esc.text.setXY(0,20);
        Printer.esc.text.printOut(ALIGN.CENTER, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL,"图章前打印文字");

//        Printer.esc.text.printOut(50, 60, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL,"右侧");

//        Printer.esc.text.printOut(50, 100, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL,"打印");

//        Printer.esc.text.printOut(50, 140, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL,"图章");
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logotest);
        Printer.esc.image.drawOut(150,50, bitmap);
        Printer.esc.text.setXY(0,500);
        Printer.esc.text.printOut(ALIGN.CENTER, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL,"图章后打印文字\r\n");
        Printer.esc.feedLines(3);

        return true;
    }

    private boolean ESC_print1() {//文本+二维码

        Printer.esc.wakeUp();
        Printer.esc.text.init();
        Printer.esc.text.setXY(20, 40);
        Printer.esc.text.printOut(ALIGN.CENTER, ESC.FONT_HEIGHT.x32, true, ESC.TEXT_ENLARGE.NORMAL, "每日优鲜  " + "0039-微信-水产(1/1)");
        Printer.esc.text.printOut(ALIGN.LEFT, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL, "编码: " + "absdferr");
        Printer.esc.text.printOut(ALIGN.LEFT, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL, "名称: " + "草鱼");
        Printer.esc.text.printOut(ALIGN.LEFT, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL, "服务: " + "宰杀");
        Printer.esc.text.printOut(ALIGN.LEFT, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL, "规格: " + "大杯/加糖/不加奶");
        Printer.esc.text.printOut(ALIGN.LEFT, ESC.FONT_HEIGHT.x24, true, ESC.TEXT_ENLARGE.NORMAL, "订单: " + "DD19032621214456879");
        //二维码
        Printer.esc.barcode.barcode2D_QRCode(80, 230, ESC.BAR_UNIT.x4, 0, 0, "http://www.jqsh.com/front/bin/ptdetail.phtml?Part=pro_08&Rcg=1");
        Printer.esc.feedLines(3);

        return true;
    }

    private boolean cpcl_print() {
        if (!Printer.getCPCLsupport()) {
            Toast.makeText(this, "不支持CPCL，请设置正确的打印机型号！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        // 记录打印机SDK的每一次调用结果，如果为false，立刻返回，不再继续调用SDK指令
        Printer.esc.feedDots(32);
        CPCL cpcl = Printer.cpcl;
        int orgX = 0, orgY = 0;
        int x = 0, y = 0;
        // EXP341.jp_page_create(78, 86, false);
        if (!cpcl.page.start(250, 560, 1))
            return false;
//		cpcl.page.bar_sense_right();
//		cpcl.page.bar_sense_left();
        cpcl.page.gap_sense();
//		cpcl.page.setPageWidth(576);

        cpcl.graphic.line(480, 20, 480, 180, 1);
        cpcl.text._setFontSetBold(false);
        cpcl.text.setFont(28, 0, false, false);
        cpcl.text._setFontSetMagCPCL(0, 0);
        cpcl.text.drawOut(20, 15, "样例名称");
        cpcl.text.drawOut(20, 60,"数量："+"12345");
        cpcl.text.drawOut(200, 50,"罐："+"gg12345");
        cpcl.barcode.code128(20,100,60,4,"1234567");
        if(!cpcl.page.print())
            return false;
        return true;
    }

    private boolean cpcl_print1() {//CPCL横向打印
        if (!Printer.getCPCLsupport()) {
            Toast.makeText(this, "不支持CPCL，请设置正确的打印机型号！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        // 记录打印机SDK的每一次调用结果，如果为false，立刻返回，不再继续调用SDK指令
        Printer.esc.feedDots(32);
        CPCL cpcl = Printer.cpcl;
        cpcl.text.portSendCmd("! 0 200 200 755 1\r\n "                 );
        cpcl.text.portSendCmd("PAGE-WIDTH 574\r\n "                    );
        cpcl.text.portSendCmd("VTEXT 24 20 10 530 company_name\r\n "   );
        cpcl.text.portSendCmd("VTEXT 5 10 40 750 入仓日期：\r\n "      );
        cpcl.text.portSendCmd("VTEXT 5 10 40 640 20190513\r\n "        );
        cpcl.text.portSendCmd("VTEXT 24 12 40 500 工单号：\r\n "        );
        cpcl.text.portSendCmd("VTEXT 5 10 40 400  20190513001\r\n"     );
        cpcl.text.portSendCmd("VTEXT 24 12 80 750 客  户：\r\n "        );
        cpcl.text.portSendCmd("VTEXT 5 12 80 640 KN\r\n "              );
        cpcl.text.portSendCmd("VTEXT 24 12 80 500 收货方：\r\n "        );
        cpcl.text.portSendCmd("VTEXT 24 12 80 400 KN-SZ\r\n "          );
        cpcl.text.portSendCmd("VTEXT 5 10 120 750 SO#：\r\n "          );
        cpcl.text.portSendCmd("VTEXT 5 10 120 700 SO1234567\r\n "      );
        cpcl.text.portSendCmd("VTEXT 24 12 120 500 总件数：\r\n "       );
        cpcl.text.portSendCmd("VTEXT 5 10 120 400 50\r\n"              );
        cpcl.text.portSendCmd("VTEXT 5 10 200 750 T-code\r\n"          );
        cpcl.text.portSendCmd("VTEXT 5 10 200 550 PO#\r\n"             );
        cpcl.text.portSendCmd("VTEXT 5 10 200 350 Item#\r\n"           );
        cpcl.text.portSendCmd("VTEXT 5 10 200 150 Ctns/Plts\r\n"       );
        cpcl.text.portSendCmd("VTEXT 5 10 200 750 T-code\r\n"          );
        cpcl.text.portSendCmd("VTEXT 5 10 250 550 Item1234567890\r\n"  );
        cpcl.text.portSendCmd("VTEXT 5 10 250 350 Item1234567890\r\n"  );
        cpcl.text.portSendCmd("VTEXT 5 10 250 150 40Ctns\r\n"          );
        cpcl.text.portSendCmd("B QR 20 5 M 2 U 6\r\n"                  );
        cpcl.text.portSendCmd( "MA, ls_PLTno \r\n"                     );
        cpcl.text.portSendCmd("ENDQR\r\n"                              );
        cpcl.text.portSendCmd("VTEXT 4 12 150 90  ls_PLTno \r\n"       );
        cpcl.text.portSendCmd("VT 4 17 750 27  ls_so\r\n"              );
       // cpcl.text.portSendCmd("FORM\r\n"                               );
        cpcl.text.portSendCmd("PRINT\r\n"                              );
        return true;
    }

    public void ButtonExpressTestPrint_click(View view)//JPL
	{
		buttonPrint.setText("JLP打印");
		buttonPrint.setVisibility(Button.INVISIBLE);
				
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
		if (miandiantest())//JLP451PrintTest  printExpressTest printTestcode printtest miandiantest
			buttonPrint.setVisibility(Button.VISIBLE);
	}

    public void ButtonCPCLTestPrint_click(View view) {
        buttonCPCLPrint.setText("CPCL打印");
        buttonCPCLPrint.setVisibility(Button.INVISIBLE);

        if (!Printer.isOpen) {
            this.finish();
            return;
        }

//		if (!getPrinterState()) {
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}

        if(cpcl_print1())//cpcl_print1  cpcl_print
            buttonCPCLPrint.setVisibility(Button.VISIBLE);

    }

    public void ButtonESCTestPrint_click(View view) {
        buttonESCPrint.setText("ESC打印");
        buttonESCPrint.setVisibility(Button.INVISIBLE);

        if (!Printer.isOpen) {
            this.finish();
            return;
        }

//		if (!getPrinterState()) {
//			buttonPrint.setVisibility(Button.VISIBLE);
//			return;
//		}

        if(ESC_print1())//ESC_print1 ESC_print
            buttonESCPrint.setVisibility(Button.VISIBLE);

    }

}

