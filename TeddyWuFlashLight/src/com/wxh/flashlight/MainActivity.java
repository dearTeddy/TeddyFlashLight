package com.wxh.flashlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

@SuppressLint({ "NewApi", "ResourceAsColor" })
public class MainActivity extends Activity {
	private Button statusButton = null;
	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean statusFlag = true;
	private int back = 0;// 判断按几次back
	
	private RelativeLayout mylayout;
	private Resources myColor;
	private int li;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		HideStatusBase();
		setContentView(R.layout.activity_main);

		statusButton = (Button) findViewById(R.id.statusButton);
		statusButton.setOnClickListener(new Mybutton());
		statusButton.performClick();
		
		mylayout = (RelativeLayout) findViewById(R.id.relativelayout);
		setColor(R.color.white);
		//结束改变背景颜色
		//改变屏幕亮度
		li=0;
		SetBright(1.0f);
	}
	
	/**
	 * 屏幕点击事件显示菜单
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		openOptionsMenu();
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true; 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.setcolor:
			selectColor();
			break;
		case R.id.setbright:
			selectBright();
			break;
		case R.id.seteit:
			CloseApp();
			break;
		case R.id.about:
			about();
			break;

		default:
			break;
		}
		
		return false;
	}
	
	private void selectColor() {
		final String[] items = {"白色", "红色", "黑色","黄色","粉色"};
		new AlertDialog.Builder(this)
		.setTitle("选择颜色")
		.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					setColor(R.color.white);
					break;
				case 1:
					setColor(R.color.red);
					break;
				case 2:
					setColor(R.color.black);
					break;
				case 3:
					setColor(R.color.yellow);
					break;
				case 4:
					setColor(R.color.fs);
					break;

				default:
					setColor(R.color.white);
					break;
				}
			}
		}).show();
	}
	
	private void selectBright(){
		final String[] items = {"100%", "75%", "50%","25%","10%"};
		new AlertDialog.Builder(this)
		.setTitle("选择亮度")
		.setSingleChoiceItems(items, li, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				li = which;
				switch (which) {
				case 0:
					SetBright(1.0F);
					break;
				case 1:
					SetBright(0.75F);
					break;
				case 2:
					SetBright(0.5F);
					break;
				case 3:
					SetBright(0.25F);
					break;
				case 4:
					SetBright(0.1F);
					break;
				default:
					SetBright(1.0F);
					break;
				}
				dialog.cancel();
			}
		}).show();
	}
	
	private void about(){
		new AlertDialog.Builder(this)
		.setMessage("欢迎您使用Teddy wu手电筒 1.0版\n我的网站是:\nhttp://blog.csdn.net/u012911862\n作者：伍小惠 \n联系方式：18822806825\n邮件：706324216@qq.com")
		.setIcon(R.drawable.icon)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).show();
	}

	class Mybutton implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (statusFlag) {
				statusButton.setBackgroundResource(R.drawable.switchon);
				camera = Camera.open();
				parameters = camera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 开启
				camera.setParameters(parameters);
				statusFlag = false;
			} else {
				statusButton.setBackgroundResource(R.drawable.switchoff);
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭
				camera.setParameters(parameters);
				statusFlag = true;
				camera.release();
			}
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back++;

			CloseApp();

		}
		return false;// 设置成false让back失效 ，true表示 不失效

	}

	public void CloseApp() { // 关闭程序
		if (statusFlag) {// 开关关闭时
			this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
		} else if (!statusFlag) {// 开关打开时
			camera.release();
			this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());// 关闭进程
			statusFlag = true;// 避免，打开开关后退出程序，再次进入不打开开关直接退出时，程序错误
		}
	}
	
	/**
	 * 全屏设置
	 */
	private void HideStatusBase()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window myWindow=this.getWindow();
		myWindow.setFlags(flag,flag);
	}
	
	/**
	 * 设置屏幕颜色
	 * @param color_M
	 */
	private void setColor(int color){
		myColor = getBaseContext().getResources();
		Drawable color_M = myColor.getDrawable(color);
		mylayout.setBackground(color_M);
	}

	/**
	 * 设置屏幕亮度
	 * @param light
	 */
	private void SetBright(float light)
	{
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.screenBrightness=light;
		getWindow().setAttributes(lp);
	}
}
