package com.jikexueyuan.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	//���ȸÿ�Ƭ����һ��FrameLayout�ؼ��࣬���ڸ��������Ƕ�������ؼ��������ı��ؼ�ʲô�ģ����Ե�����һʵ�����Ժ�ͻ��ʼ���ı��ؼ�����ͨ�����캯����ĳ�ʼ����ͬʱ��ʼ�����ı��ؼ�������
	//���캯������ʼ��
	public Card(Context context) {
		super(context);
		//��ʼ���ı�
		label = new TextView(getContext());
		//�����ı��Ĵ�С
		label.setTextSize(32);
		
		label.setBackgroundColor(0x33DD2222);		
		//�ѷ��ڿؼ�����ı����д���
		label.setGravity(Gravity.CENTER);
		
		//���ֲ�����������
		LayoutParams lp = new LayoutParams(-1,-1);//����������ʼ��layout�ؼ�textView��ĸߺͿ�����
		//��ÿ��textView�����������margin���Һ��¾Ͳ���Ҫ��
		lp.setMargins(10, 10, 0, 0);
		addView(label, lp);//�÷�����������label�ؼ������Ѿ���ʼ���˵ĸߺͿ�����
		setNum(0);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		//����
		
		//����������Ҫ������������ֵķ�������������֣���Ϊ���ų�����0��������Ҫ��ԭ����������ж����
		if(num<=0){
			label.setText("");
		}else{
			label.setText(num+"");
		}
//		label.setText(num+"");
	}

	private int num = 0;
	
	//��Ҫ��������
	private TextView label;
	
	//�ж����ſ�Ƭ�����Ƿ�һ����
	public boolean equals(Card o) {
		return getNum()==o.getNum();
	}
	
}
