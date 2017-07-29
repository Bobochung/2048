package com.jikexueyuan.game2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
//我们为了让xml绑定该类，我们就把该类的全路径把原来的GridLayout替换掉
	/**
	 * 这三个构造函数的创建是为了可以访问到layout里面的Gridview控件
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//自己手动添加的类的入口方法
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		
		//自己手动添加的类的入口方法
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//自己手动添加的类的入口方法
		initGameView();
	}
	
	
	//自定义类的入口方法
	private void initGameView(){
		//由于在添加完卡片之后发现在GameView里创建的16个卡片都排成一行了
		//因此在这里设置有多少列
		/**
		 * ColumnCount is used only to generate default column/column indices 
		 * when they are not specified by a component's layout parameters.
		 */
		setColumnCount(4);
		
		//在给GameView的背景颜色加上颜色
		setBackgroundColor(0xffbbada0);
		
		//如何创建手势操作呢？
		setOnTouchListener(new OnTouchListener() {
			//如何判断用户的意图？判断用户按下的意图和判断用户手指离开的意图
			private float startX,startY,offsetX,offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX()-startX;
					offsetY = event.getY()-startY;
					//防止用户不是左右直线手势滑动而是斜方向滑动的判断代码
					if(Math.abs(offsetX)>(Math.abs(offsetY))){//表示手势往右滑动不太偏离水平的时候
						if(offsetX<-5){//手势往左,-5表示范围
							System.out.println("left操作");
							swipeLeft();//侦听用户操作后执行该方法
						}else if (offsetX>5){//往右
							System.out.println("rigth操作");
							swipeRight();//侦听用户操作后执行该方法
						}
					}else{//这个else判断用户的手势上下滑动不太偏离垂直线
						if(offsetY<-5){//手势往左,-5表示范围
							System.out.println("up操作");
							swipeUp();//侦听用户操作后执行该方法
						}else if (offsetY>5){//往右
							System.out.println("down操作");
							swipeDown();//侦听用户操作后执行该方法
						}
					}
					break;
				}
				
				return true;
			}
		});
	}
	
	//怎么动态地计算卡片的宽高呢？
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//该方法就是宽高发生改变的时候我们可以得到当前的宽高是多少
		//该方法也是在游戏一被创建的时候就调用，也就是用来初始宽高的方法
		
		//获取手机较窄的长度，-10是用来间隔每个卡片的距离，用手机的宽除以4就是每个卡片的长度了
		int cardWidth = (Math.min(w, h)-10)/4;
		
		//在该方法初始化的时候新建16个卡片，以下是方法
		addCards(cardWidth,cardWidth);
		
		 startGame();
		
	}
	
	
	private void addCards(int cardWidth, int cardHeight) {
		Card c;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				c.setNum(2);//给卡片设置初始化数字
				addView(c, cardWidth, cardHeight);
				
				//顺便把初始化时新建的卡片类存放到下面新建的二维数组里
				cardsMap[x][y] = c;
			}
		}
	}
	
	//现在可以开始使用这些随机数了
	private void startGame(){
		//重新开始的时候分数清零
		MainActivity aty = MainActivity.getMainActivity();
		aty.clearScore();
		//清理阶段：初始化阶段
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);
				
			}
		}
		//然后就是添加随机数阶段
		addRandomNum();
		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
	}

	//设置随机数的方法	
	private void addRandomNum(){
		//把这个point清空，每次调用添加随机数时就清空之前所控制的指针
		emptyPoints.clear();
		
		//对所有的位置进行遍历：即为每个卡片加上了可以控制的指针
		for(int y = 0;y<4;y++){
			for (int x = 0; x < 4;x++) {
				if(cardsMap[x][y].getNum()<=0){
					emptyPoints.add(new Point(x,y));//给List存放控制卡片用的指针（通过坐标轴来控制）
				}
			}
		}
		//一个for循环走完我们就从List里取出一个控制指针的point对象
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		//
		cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);//通过point对象来充当下标的角色来控制存放card的二维数组cardsMap，然后随机给定位到的card对象赋值
	}
	
	private void swipeLeft(){
		boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//		Toast.makeText(getContext(), "向左滑动了", 0).show();
		//以下两行for循环实现了一行一行的遍历，在向左滑动的时候
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				
				for (int x1 = x+1; x1 < 4; x1++) {
					//这是在水平上固定了一个格子之后再继续在水平上遍历别的格子，且当格子有数的时候进行以下的操作
					if (cardsMap[x1][y].getNum()>0) {
						//现在判断被固定的格子有没有数字，如果没有数字就进行以下的操作
						if (cardsMap[x][y].getNum()<=0) {
							//把与被固定的格子的同一水平上的格子的数字赋给被固定的格子
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							//把值赋给被固定的格子后自己归零
							cardsMap[x1][y].setNum(0);
							//第二层循环，即同一层的不同列退一格继续循环，这样做的原因是为了继续固定这个格子而去检查与它同一水平上的别的格子的内容，因为其他格子是什么个情况还需要继续在第二层进行判断
							x--;
							//只要有移动就要加随机数
							merge = true;
							
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {//这层判断是判断相邻两个数相同的情况
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							//在这要给MainActivity中的score加上分数
							//而这里MainActivity设计成了单例设计模式，所以要使用get方法获得对象
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//只要有移动就要加随机数
							merge = true;
						}
						//这个break为了在操作完这固定格子遍历的过程操作完后跳出遍历，因为只要有操作这个条件，就没有继续遍历下去的需要了
						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeRight(){
		boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//		Toast.makeText(getContext(), "向右滑动了", 0).show();
		for (int y = 0; y < 4; y++) {
			for (int x = 4-1; x >=0; x--) {

				for (int x1 = x-1; x1 >=0; x1--) {
					if (cardsMap[x1][y].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x++;
							//只要有移动就要加随机数
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//只要有移动就要加随机数
							merge = true;
						}
						break;
						
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeUp(){
		boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//		Toast.makeText(getContext(), "向上滑动了", 0).show();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {

				for (int y1 = y+1; y1 < 4; y1++) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							y--;
							//只要有移动就要加随机数
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//只要有移动就要加随机数
							merge = true;
						}
						break;
					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeDown(){
		boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//		Toast.makeText(getContext(), "向下滑动了", 0).show();
		for (int x = 0; x < 4; x++) {
			for (int y = 4-1; y >=0; y--) {

				for (int y1 = y-1; y1 >=0; y1--) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y++;
							//只要有移动就要加随机数
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//只要有移动就要加随机数
							merge = true;
						}
						break;

					}
				}
			}
		}
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	
	/*
	 * 
	 * 方法放在滑动手势里，每次滑动是用来判断每个格子的上下左右没有相同的数字，和格子是否为空，以此弹出对话框来提示用户并调用重新开始方法
	 */
	private void checkComplete(){

		boolean complete = true;

		ALL:
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {//遍历格子
					if (cardsMap[x][y].getNum()==0||
							(x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
							(x<4-1&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
							(y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
							(y<4-1&&cardsMap[x][y].equals(cardsMap[x][y+1]))) {

						complete = false;
						break ALL;
					}
				}
			}

		if (complete) {//通过标记来判断当前格子是否满足条件，满足则弹出对话框，并点击按钮后激活开始方法
			new AlertDialog.Builder(getContext())
			.setTitle("非常遗憾！")
			.setMessage("您的最终得分为："+MainActivity.showScore())
			.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			})
			.setNegativeButton("退出游戏",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {					
				  System.exit(0);
				}
				
			}).show();
		}

	}
	
	//我们需要定义一个二维数组来记录GameView初始化时生成的16个卡片类
	private Card[][] cardsMap = new Card[4][4];
	//我们添加一个list来存放Point来控制随机数方法里的随机数
	//注意这里有一个Point的类不熟悉
	private List<Point> emptyPoints = new ArrayList<Point>();
}
