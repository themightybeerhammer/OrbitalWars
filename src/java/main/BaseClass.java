/*
 * Copyright (C) 2016 Vladimir
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import javafx.scene.shape.Ellipse;
import main.Vector;
/**
 *
 * @author Vladimir
 */
public class BaseClass {
    public float X,Y; /*Координаты объекта*/
    float M;   /*Масса объекта */
    int RO;    /*Радиус сферы(объекта)*/
    Vector P;  /*Вектор импульса*/
    Vector F;  /*Вектор равнодействующей*/
    float MinMassOrbit;     /*Минимальная масса объекта для участия в расчете орбиты*/
    private ArrayList<Point> Orbit; /*Кординаты орбиты*/
    static ArrayList<BaseClass> aLBaseClass;
    boolean dw_orbit; /*Флаг рисования орбиты*/
    
    boolean DeadFlag=false; /*Флаг Гибели объекта*/
    int DeadSteps=0;        /*Количество итераций гибели объекта*/
    
   
    
     BaseClass(){
       this.X = 0;
       this.Y = 0;
       this.M = 1;
       this.RO = 1;
       P = new Vector(1,1);
       F = new Vector();
       dw_orbit = false;
     }
     
     
     BaseClass(
               float x
              ,float y
              ,float m
              ,int ro
              ,float vangle
              ,float vlength
              ,ArrayList<BaseClass> AL
              ){
       this.X = x;
       this.Y = y;
       this.M = m;
       this.RO = ro;
       this.P = new Vector(vangle,vlength);
       AL.add(this);
       aLBaseClass =new ArrayList<>(AL);
       /*Орбита*/
       MinMassOrbit = m*10;
       Orbit = new ArrayList<Point>();
       dw_orbit = false;
       DeadSteps = ro*20;
      
     }
    
     void draw(Graphics g
             ,Point p_display /*,ВОТ ЗДЕСЬ БУДЕТ ПЕРЕМЕНАЯ ЭКРАН*/
             ,boolean v_F  /*Рисовать вектор равнодействующей*/
             ,boolean v_P  /*Рисовать вектор импульса*/     
            ){
        
         /*ПЕРЕД ЗАПУСКОМ ПРОЦЕДУРЫ ДОЛЖНА БЫТЬ ОБРАБОТКА ЭКРАНА ОТОБРАЖЕНИЯ
         Т.Е. ВХОДИТ ЛИ ОБЪЕКТ В ОТОБРАЖАЕМУЮ ОБЛАСТЬ */
         
         
         /*Отрисовка объекта*/
         draw_in_scr(g,X+p_display.x,Y+p_display.y,v_F,v_P);
         /*Орбита объекта*/
         if(dw_orbit)draw_orbit(g,p_display.x,p_display.y); 
        
          
     }
     
   
     
     void draw_in_scr(Graphics g
                     ,float x
                     ,float y
                     ,boolean v_F  
                     ,boolean v_P ){
         
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setRenderingHints(rh);
         g2.setColor(Color.WHITE);
         g2.draw(new Ellipse2D.Float(x-RO, y-RO, RO*2, RO*2));
        // g2.drawOval((int)x-RO, (int)y-RO, RO*2, RO*2);

          /*Направление равнодействующей*/
         float r = 20;
         if((F.length!=0)&(v_F))
            {
                g2.setColor(Color.BLUE);
                g2.drawLine((int)x,(int) y, (int)x+(int)(Math.cos(F.angle)*r) ,(int) y+(int)(Math.sin(F.angle)*r));
            }
         
         /*Направление Импульса*/
          r = 20;
         if((P.length!=0)&(v_P))
            {
                g2.setColor(Color.GREEN);
                g2.drawLine((int)x, (int)y, (int)x+(int)(Math.cos(P.angle)*r) , (int)y+(int)(Math.sin(P.angle)*r));
            }
         
      
     }
     
     void calc_orbit(){
         
       
       if (dw_orbit){ 
           
       Orbit.clear(); /*Чистим массив орбиты*/  
       /*Отбираем объекты масса которых значительно привышает массу объекта, которые явно влияют на траекторию полета */
        
       ArrayList<BaseClass> AL=new ArrayList<>();
        
       for(int i=0;i<aLBaseClass.size();i++){
            if((aLBaseClass.get(i).getClass().getName()!="main.CenterMass")
             &&(aLBaseClass.get(i)!=this)
             &&(aLBaseClass.get(i).M>=MinMassOrbit)
             &&(aLBaseClass.get(i)!=null)){ 
                 
            AL.add(aLBaseClass.get(i));
            }
        }
       
        float _r; /*Растояние между двумя объектами*/
        float _f; /*Сила притяжения между двумя объектами*/
        float _X =X,_Y=Y;   /*Координаты объекта текущего*/
        float _x,_y;   /*Координаты объекта взаимодействия*/
        float _m;      /*Масса объекта взаимодействия*/
        float _ro;     /*Радиус орбиты объекта взаимодействия*/
        float _a;      /*Направление вектора взаимодействия*/
        
        int j =0;
        
        float _xd,_yd;  /*Кординаты смещения*/
        
        Vector _F = new Vector(0,0); /*Вектор равнодействующей*/
        Vector _P = new Vector(P);   /*Импульс*/
       
       Orbit.add(new Point((int)(X),(int)(Y)));
       
       if (AL.size()>0){
       do{
             
            _F = new Vector(0,0);
            
            for(int i=0;i<AL.size();i++){
              
                if(AL.get(i)!=null){
                
                _x=AL.get(i).X;
                _y=AL.get(i).Y;
                _m=AL.get(i).M;
                _ro=AL.get(i).RO;
                
                
                _r = (float)Math.sqrt(Math.pow(_X-_x,2)+Math.pow(_Y-_y,2));
                if(_r<RO+_ro) _r = RO+_ro;
                if(_r<30) _r = 30;
                _f = (float) (_m*M/Math.pow(_r,2));
                _a = (float)Math.asin((_y-_Y)/Math.sqrt((Math.pow(_x-_X,2)+Math.pow(_y-_Y,2))));
                
                 if((_x<_X)&&(_y>_Y))  { _a=_a*(-1)+(float)Math.PI;}
                 if((_x<_X)&&(_y==_Y)) { _a=_a*(-1)+(float)Math.PI;}
                 if((_x<_X)&&(_y<_Y))  { _a=_a*(-1)+(float)Math.PI;}
                 if((_x>_X)&&(_y<_Y))  { _a=_a+(float)Math.PI*2;}
         
                _F.Plus(new Vector(_a,_f));
                } 
         
            }
            _F.length=(float)(_F.length/3);
            _P.Plus(_F);
            
            _xd=0; _yd=0;
            
            _xd = (float)(Math.cos(_P.angle)*_P.length/M/3);
            _yd = (float)(Math.sin(_P.angle)*_P.length/M/3);
            
            _X=_X+_xd;
            _Y=_Y+_yd;
            
            boolean flag = true;
            for(int i=0;i<Orbit.size();i++){
                if((float)Math.sqrt((Math.pow((Orbit.get(i).x-(_X-X)),2)+ Math.pow((Orbit.get(i).y-(_Y-Y)),2)))<10 ){
                    flag=false;
                }
            }
            
            if(flag){
             Orbit.add(new Point((int)(_X-X),(int)(_Y-Y)));             
            }
             
            if(((float)Math.sqrt(Math.pow(_X-X, 2)+Math.pow(_Y-Y, 2))<RO)&&(_xd*j>2*RO))break;
           j++;
       }while(j<1500);
       }         
      }
       
     }
     
     void draw_orbit(Graphics g,float x,float y){
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setRenderingHints(rh);
         g2.setColor(Color.WHITE);
         
         for(int i=0;i<Orbit.size();i++){
            if(i==0){
              g2.draw(new Ellipse2D.Float(Orbit.get(0).x+(int)x,Orbit.get(0).y+(int)y, 1, 1));
              //g2.drawOval(Orbit.get(0).x-1+(int)x,Orbit.get(0).y-1+(int)y, 2, 2);  
            }else{
              g2.draw(new Ellipse2D.Float(Orbit.get(0).x+Orbit.get(i).x+(int)x,Orbit.get(0).y+Orbit.get(i).y+(int)y, 1, 1));
              //g2.drawOval(Orbit.get(0).x+Orbit.get(i).x-1+(int)x,Orbit.get(0).y+Orbit.get(i).y-1+(int)y, 2, 2);  
              
            }
             
           
         }
     }
     
     void calc_F_ravn(float Mtplr){
         
         float r; /*Растояние между двумя объектами*/
         float f; /*Сила притяжения между двумя объектами*/
         float x,y;  /*Координаты объекта*/
         float m;    /*Масса объекта*/
         float ro;   /*Радиус орбиты объекта*/
         float a;    /*Направление вектора*/
         
        F=new Vector(0,0);
         
         for(int i=0;i<aLBaseClass.size();i++){
             if((aLBaseClass.get(i).getClass().getName()!="main.CenterMass")
              &&(aLBaseClass.get(i)!=this)
              &&(aLBaseClass.get(i).M>M*10)
              &&(aLBaseClass.get(i)!=null)       )
             {
                x=aLBaseClass.get(i).X;
                y=aLBaseClass.get(i).Y;
                m=aLBaseClass.get(i).M;
                ro=aLBaseClass.get(i).RO;
                
                
                r = (float)Math.sqrt(Math.pow(X-x,2)+Math.pow(Y-y,2));
                if(r<RO+ro) r = RO+ro;
                if(r<30) r = 30;
                f = (float) (m*M/Math.pow(r,2));
                a = (float)Math.asin((y-Y)/Math.sqrt((Math.pow(x-X,2)+Math.pow(y-Y,2))));
                
                 if((x<X)&&(y>Y))  { a=a*(-1)+(float)Math.PI;}
                 if((x<X)&&(y==Y)) { a=a*(-1)+(float)Math.PI;}
                 if((x<X)&&(y<Y))  { a=a*(-1)+(float)Math.PI;}
                 if((x>X)&&(y<Y))  { a=a+(float)Math.PI*2;}
         
                F.Plus(new Vector(a,f));
             }
         
         }
        F.length = F.length/Mtplr;
         
        this.P.Plus(F);
         
    }
         
     

     void move(float Mtplr){
         
         float xd ;
         float yd ;
          //xd = (float)(Math.cos(F.angle)*F.length/M);
          //yd = (float)(Math.sin(F.angle)*F.length/M);
         
          xd = (float)(Math.cos(P.angle)*P.length/M);
          yd = (float)(Math.sin(P.angle)*P.length/M);
          
          xd = xd/Mtplr;
          yd = yd/Mtplr;
         
          X = X+xd;
          Y = Y+yd;
          
         if(DeadFlag){
           DeadSteps--;  
           if(DeadSteps<0) DeadSteps = 0;
           
         }
       
     
     
    }
     
    float Distance(BaseClass bc){
        return (float)sqrt((this.X - bc.X) * (this.X - bc.X) + (this.Y - bc.Y) * (this.Y - bc.Y));
    }
     
}
