package com.keeppieces.ninelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 吴昶 .
 * 时间: 2018/12/10 15:55
 * 功能简介：九宫格
 */
public class NineLockView extends View {

    private Paint paint;
    private boolean isInit=true;//是否初始化
    private boolean isDrawEnd=false;//是否画最后的点与触摸点之间的线
    private int offsetX;//x轴偏移量
    private int offsetY;//y轴偏移量
    private int step;//节点间的距离
    private int radius=30;//节点圆圈的半径大小
    private int lastPointX;//最后一个节点的 x 坐标
    private int lastPointY;//最后一个节点的 y 坐标
    private float linearEndX;//触摸点的最终位置坐标
    private float linearEndY;
    private List<Point> code=new ArrayList<Point>();//选中点的集合
    private NineLockListener lockListener;

    public NineLockView(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        paint=new Paint();
        paint.setColor(Color.parseColor("#00e5a8"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setLockListener(NineLockListener lockListener) {
        this.lockListener = lockListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isInit){
            isInit=false;
            int w=getWidth();
            int h=getHeight();
            if(w>h){
                offsetX=(w-h)/2;
                w=h;
            }else {
                offsetY=(h-w)/2;
                h=w;
            }
            step=w/4;
            code.clear();
            lastPointY=0;
            lastPointX=0;
        }
        for(int i=1;i<4;i++){
            for(int j=1;j<4;j++){
                canvas.drawCircle(
                        offsetX+step*i,
                        offsetY+step*j,
                        radius,
                        paint
                );
            }
        }
        paint.setStrokeWidth(8);//设置连线的宽度
        if(code.size()>=1){
            for(int i=1;i<code.size();i++){
                canvas.drawLine(code.get(i-1).x*step+offsetX,
                        code.get(i-1).y*step+offsetY,
                        code.get(i).x*step+offsetX,
                        code.get(i).y*step+offsetY,
                        paint);
            }
            lastPointX=code.get(code.size()-1).x*step+offsetX;
            lastPointY=code.get(code.size()-1).y*step+offsetY;
        }
        if(isDrawEnd && lastPointX!=0 && lastPointY!=0 && linearEndX > 9 && linearEndY>9){
            canvas.drawLine(lastPointX,lastPointY,linearEndX,linearEndY,paint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isInit=true;
                isDrawEnd=true;
                invalidate();
                standLinear(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                standLinear(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                isDrawEnd=false;
                setResultCode();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据手的滑动判断是否选中某个节点
     * 当触摸点与节点值之间的距离小于 radius 时默认选中
     * @param x
     * @param y
     */
    private void standLinear(float x,float y){
        boolean isStand=false;
        for(int i=1;i<4;i++){
            for(int j=1;j<4;j++){
                float tx=offsetX+step*i-x;
                float ty=offsetY+step*j-y;
                if(Math.sqrt(tx*tx+ty*ty)<=radius){
                    isStand=true;
                    if(code.size()==0){
                        linearEndX=i;
                        linearEndY=j;
                        code.add(new Point(i,j));
                    }else {
                        Point last=code.get(code.size() - 1);
                        //如果当前点与记录的最后一个点重复，则不再重复添加
                        if (last.x == i && last.y == j) {
                            linearEndX = x;
                            linearEndY = y;
                        } else {
                            //添加记录点,如果在竖直或水平方向上中间间隔一个点则间隔的点也要加上
                            //未考虑对角线间隔的情况
                            if(i==last.x && j==last.y+2){
                                code.add(new Point(i,j-1));
                            }else if(i==last.x && j==last.y-2){
                                code.add(new Point(i,j+1));
                            }else if(j==last.y && i==last.x+2){
                                code.add(new Point(i-1,j));
                            }else if(j==last.y && i==last.x-2){
                                code.add(new Point(i+1,j));
                            }
                            code.add(new Point(i,j));
                        }
                    }
                    break;
                }
            }
            if(isStand){
                break;
            }
        }
        if(!isStand){
            linearEndX=x;
            linearEndY=y;
        }
        invalidate();
    }

    private void setResultCode(){
        if(lockListener!=null){
            if(code.size()==0){
                lockListener.onError();
            }else {
                int[] result = new int[code.size()];
                for (int i = 0; i < code.size(); i++) {
                    result[i] = (code.get(i).y - 1 )* 3 + code.get(i).x;
                }
                lockListener.onLockResult(result);
            }
        }
    }

}
