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
//����Ϊ����xml�󶨸��࣬���ǾͰѸ����ȫ·����ԭ����GridLayout�滻��
	/**
	 * ���������캯���Ĵ�����Ϊ�˿��Է��ʵ�layout�����Gridview�ؼ�
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//�Լ��ֶ���ӵ������ڷ���
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		
		//�Լ��ֶ���ӵ������ڷ���
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//�Լ��ֶ���ӵ������ڷ���
		initGameView();
	}
	
	
	//�Զ��������ڷ���
	private void initGameView(){
		//����������꿨Ƭ֮������GameView�ﴴ����16����Ƭ���ų�һ����
		//��������������ж�����
		/**
		 * ColumnCount is used only to generate default column/column indices 
		 * when they are not specified by a component's layout parameters.
		 */
		setColumnCount(4);
		
		//�ڸ�GameView�ı�����ɫ������ɫ
		setBackgroundColor(0xffbbada0);
		
		//��δ������Ʋ����أ�
		setOnTouchListener(new OnTouchListener() {
			//����ж��û�����ͼ���ж��û����µ���ͼ���ж��û���ָ�뿪����ͼ
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
					//��ֹ�û���������ֱ�����ƻ�������б���򻬶����жϴ���
					if(Math.abs(offsetX)>(Math.abs(offsetY))){//��ʾ�������һ�����̫ƫ��ˮƽ��ʱ��
						if(offsetX<-5){//��������,-5��ʾ��Χ
							System.out.println("left����");
							swipeLeft();//�����û�������ִ�и÷���
						}else if (offsetX>5){//����
							System.out.println("rigth����");
							swipeRight();//�����û�������ִ�и÷���
						}
					}else{//���else�ж��û����������»�����̫ƫ�봹ֱ��
						if(offsetY<-5){//��������,-5��ʾ��Χ
							System.out.println("up����");
							swipeUp();//�����û�������ִ�и÷���
						}else if (offsetY>5){//����
							System.out.println("down����");
							swipeDown();//�����û�������ִ�и÷���
						}
					}
					break;
				}
				
				return true;
			}
		});
	}
	
	//��ô��̬�ؼ��㿨Ƭ�Ŀ���أ�
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//�÷������ǿ�߷����ı��ʱ�����ǿ��Եõ���ǰ�Ŀ���Ƕ���
		//�÷���Ҳ������Ϸһ��������ʱ��͵��ã�Ҳ����������ʼ��ߵķ���
		
		//��ȡ�ֻ���խ�ĳ��ȣ�-10���������ÿ����Ƭ�ľ��룬���ֻ��Ŀ����4����ÿ����Ƭ�ĳ�����
		int cardWidth = (Math.min(w, h)-10)/4;
		
		//�ڸ÷�����ʼ����ʱ���½�16����Ƭ�������Ƿ���
		addCards(cardWidth,cardWidth);
		
		 startGame();
		
	}
	
	
	private void addCards(int cardWidth, int cardHeight) {
		Card c;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				c.setNum(2);//����Ƭ���ó�ʼ������
				addView(c, cardWidth, cardHeight);
				
				//˳��ѳ�ʼ��ʱ�½��Ŀ�Ƭ���ŵ������½��Ķ�ά������
				cardsMap[x][y] = c;
			}
		}
	}
	
	//���ڿ��Կ�ʼʹ����Щ�������
	private void startGame(){
		//���¿�ʼ��ʱ���������
		MainActivity aty = MainActivity.getMainActivity();
		aty.clearScore();
		//����׶Σ���ʼ���׶�
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);
				
			}
		}
		//Ȼ��������������׶�
		addRandomNum();
		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
//		addRandomNum();
	}

	//����������ķ���	
	private void addRandomNum(){
		//�����point��գ�ÿ�ε�����������ʱ�����֮ǰ�����Ƶ�ָ��
		emptyPoints.clear();
		
		//�����е�λ�ý��б�������Ϊÿ����Ƭ�����˿��Կ��Ƶ�ָ��
		for(int y = 0;y<4;y++){
			for (int x = 0; x < 4;x++) {
				if(cardsMap[x][y].getNum()<=0){
					emptyPoints.add(new Point(x,y));//��List��ſ��ƿ�Ƭ�õ�ָ�루ͨ�������������ƣ�
				}
			}
		}
		//һ��forѭ���������Ǿʹ�List��ȡ��һ������ָ���point����
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		//
		cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);//ͨ��point�������䵱�±�Ľ�ɫ�����ƴ��card�Ķ�ά����cardsMap��Ȼ���������λ����card����ֵ
	}
	
	private void swipeLeft(){
		boolean merge = false;//����ÿ����һ�λ���ͼ�һ������������棬Ҳ��������������forѭ��֮��
//		Toast.makeText(getContext(), "���󻬶���", 0).show();
		//��������forѭ��ʵ����һ��һ�еı����������󻬶���ʱ��
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				
				for (int x1 = x+1; x1 < 4; x1++) {
					//������ˮƽ�Ϲ̶���һ������֮���ټ�����ˮƽ�ϱ�����ĸ��ӣ��ҵ�����������ʱ��������µĲ���
					if (cardsMap[x1][y].getNum()>0) {
						//�����жϱ��̶��ĸ�����û�����֣����û�����־ͽ������µĲ���
						if (cardsMap[x][y].getNum()<=0) {
							//���뱻�̶��ĸ��ӵ�ͬһˮƽ�ϵĸ��ӵ����ָ������̶��ĸ���
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							//��ֵ�������̶��ĸ��Ӻ��Լ�����
							cardsMap[x1][y].setNum(0);
							//�ڶ���ѭ������ͬһ��Ĳ�ͬ����һ�����ѭ������������ԭ����Ϊ�˼����̶�������Ӷ�ȥ�������ͬһˮƽ�ϵı�ĸ��ӵ����ݣ���Ϊ����������ʲô���������Ҫ�����ڵڶ�������ж�
							x--;
							//ֻҪ���ƶ���Ҫ�������
							merge = true;
							
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {//����ж����ж�������������ͬ�����
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							//����Ҫ��MainActivity�е�score���Ϸ���
							//������MainActivity��Ƴ��˵������ģʽ������Ҫʹ��get������ö���
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//ֻҪ���ƶ���Ҫ�������
							merge = true;
						}
						//���breakΪ���ڲ�������̶����ӱ����Ĺ��̲������������������ΪֻҪ�в��������������û�м���������ȥ����Ҫ��
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
		boolean merge = false;//����ÿ����һ�λ���ͼ�һ������������棬Ҳ��������������forѭ��֮��
//		Toast.makeText(getContext(), "���һ�����", 0).show();
		for (int y = 0; y < 4; y++) {
			for (int x = 4-1; x >=0; x--) {

				for (int x1 = x-1; x1 >=0; x1--) {
					if (cardsMap[x1][y].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);
							x++;
							//ֻҪ���ƶ���Ҫ�������
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//ֻҪ���ƶ���Ҫ�������
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
		boolean merge = false;//����ÿ����һ�λ���ͼ�һ������������棬Ҳ��������������forѭ��֮��
//		Toast.makeText(getContext(), "���ϻ�����", 0).show();
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {

				for (int y1 = y+1; y1 < 4; y1++) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);
							y--;
							//ֻҪ���ƶ���Ҫ�������
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//ֻҪ���ƶ���Ҫ�������
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
		boolean merge = false;//����ÿ����һ�λ���ͼ�һ������������棬Ҳ��������������forѭ��֮��
//		Toast.makeText(getContext(), "���»�����", 0).show();
		for (int x = 0; x < 4; x++) {
			for (int y = 4-1; y >=0; y--) {

				for (int y1 = y-1; y1 >=0; y1--) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y++;
							//ֻҪ���ƶ���Ҫ�������
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							//ֻҪ���ƶ���Ҫ�������
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
	 * �������ڻ��������ÿ�λ����������ж�ÿ�����ӵ���������û����ͬ�����֣��͸����Ƿ�Ϊ�գ��Դ˵����Ի�������ʾ�û����������¿�ʼ����
	 */
	private void checkComplete(){

		boolean complete = true;

		ALL:
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {//��������
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

		if (complete) {//ͨ��������жϵ�ǰ�����Ƿ����������������򵯳��Ի��򣬲������ť�󼤻ʼ����
			new AlertDialog.Builder(getContext())
			.setTitle("�ǳ��ź���")
			.setMessage("�������յ÷�Ϊ��"+MainActivity.showScore())
			.setPositiveButton("���¿�ʼ", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			})
			.setNegativeButton("�˳���Ϸ",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {					
				  System.exit(0);
				}
				
			}).show();
		}

	}
	
	//������Ҫ����һ����ά��������¼GameView��ʼ��ʱ���ɵ�16����Ƭ��
	private Card[][] cardsMap = new Card[4][4];
	//�������һ��list�����Point�����������������������
	//ע��������һ��Point���಻��Ϥ
	private List<Point> emptyPoints = new ArrayList<Point>();
}
