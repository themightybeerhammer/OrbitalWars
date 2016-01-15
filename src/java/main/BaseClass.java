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
import java.util.ArrayList;
import main.Vector;
/**
 *
 * @author Vladimir
 */
public class BaseClass {
    float X,Y;   /*Координаты объекта*/
    float M;   /*Масса объекта */
    int RO;    /*Радиус сферы(объекта)*/
    Vector P;  /*Вектор импульса*/
    Vector F;  /*Вектор равнодействующей*/
    
     BaseClass(){
       this.X = 0;
       this.Y = 0;
       this.M = 1;
       this.RO = 1;
       P = new Vector(1,1);
       F = new Vector();
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
     }
    
     void draw(Graphics g/*,ВОТ ЗДЕСЬ БУДЕТ ПЕРЕМЕНАЯ ЭКРАН*/){
        
         /*ПЕРЕД ЗАПУСКОМ ПРОЦЕДУРЫ ДОЛЖНА БЫТЬ ОБРАБОТКА ЭКРАНА ОТОБРАЖЕНИЯ
         Т.Е. ВХОДИТ ЛИ ОБЪЕКТ В ОТОБРАЖАЕМУЮ ОБЛАСТЬ */
         
        
         
         
         draw_in_scr(g,X,Y);
         
         /*Направление равнодействующей*/
         float r = 20;
         if(F.length!=0)
            {
                g.setColor(Color.BLUE);
                g.drawLine((int)X,(int) Y, (int)X+(int)(Math.cos(F.angle)*r) ,(int) Y+(int)(Math.sin(F.angle)*r));
            }
         
         /*Направление Импульса*/
          r = 20;
         if(F.length!=0)
            {
                g.setColor(Color.GREEN);
                g.drawLine((int)X, (int)Y, (int)X+(int)(Math.cos(P.angle)*r) , (int)Y+(int)(Math.sin(P.angle)*r));
            }
         
     }
     
     void draw_in_scr(Graphics g,float x, float y){
         g.setColor(Color.BLACK);
         g.drawOval((int)x-RO, (int)y-RO, RO*2, RO*2);
     }
     
     void calc_F_ravn(CenterMass CM){
         /*Вычисление равнодействующий*/
         if(CM!=this){
         float mr = CM.M-M;
         float xr = CM.X + (int)(M/(mr)*(CM.X-X));
         float yr = CM.Y + (int)(M/(mr)*(CM.Y-Y));
         
         float fr = (float) (mr*M/(Math.pow(X-xr,2)+Math.pow(Y-yr,2)));
       
         
         float af = (float)Math.asin((yr-Y)/Math.sqrt((Math.pow(xr-X,2)+Math.pow(yr-Y,2))));
         
         
         
         if((xr<X)&&(yr>Y)) { af=af*(-1)+(float)Math.PI;}
         if((xr<X)&&(yr<Y)) { af=af*(-1)+(float)Math.PI;}
         if((xr>X)&&(yr<Y)) { af=af+(float)Math.PI*2;}
         
         
         this.F =new Vector(af,fr);
         this.P.Plus(F);
         
         }
         
       }
     
     void move(){
         System.out.println(F.length+" "+P.length);
         /*float xd = (float)(Math.cos(F.angle)*F.length);
         float yd = (float)(Math.sin(F.angle)*F.length);*/
         
         float xd = (float)(Math.cos(P.angle)*P.length);
         float yd = (float)(Math.sin(P.angle)*P.length);
        /* if((xd<1)&&(xd>0)) xd=1; 
         if((xd>-1)&&(xd<0)) xd=-1; */
         if(xd>10) xd=10;
         if(xd<-10) xd=-10;
         
        /* if((yd<1)&&(yd>0)) yd=1; 
         if((yd>-1)&&(yd<0)) yd=-1; */
         if(yd>10)yd=10;        
         if(yd<-10)yd=-10;
         
         this.X = this.X+(int)Math.round(xd);
         this.Y = this.Y+(int)Math.round(yd);
     
     
     }
        
}
