/*
 * Copyright (C) 2016 dns1
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author dns1
 */
public class Roket extends BaseClass {
     ArrayList<Point2D> flame; /*Пламя*/
     BaseClass Target;         /*Объект цель*/
     
     public double RocketFuel; /*Запас топлива ракеты*/
     long chetchik = 0;
     Vector pFP = new Vector(0,0); /*Значение импульса из пердыдущей итерации*/
     
    Roket(){
        super();
    }
    Roket(double x
            , double y
            , double m
            , int ro
            , double vangle
            , double vlength
            , ArrayList<BaseClass> AL
            , BaseClass target
            ){
        
        
        super(x, y, m, ro, vangle, vlength, AL);
        
        Target = target;
        
        DeadSteps = 4;
        flame=new ArrayList<>();
        RocketFuel=100;
        
        
    }
    
      void draw(Graphics g
             ,Point2D p_display /*,ВОТ ЗДЕСЬ БУДЕТ ПЕРЕМЕНАЯ ЭКРАН*/
             ,boolean v_F  /*Рисовать вектор равнодействующей*/
             ,boolean v_P  /*Рисовать вектор импульса*/     
            ){
        
         /*ПЕРЕД ЗАПУСКОМ ПРОЦЕДУРЫ ДОЛЖНА БЫТЬ ОБРАБОТКА ЭКРАНА ОТОБРАЖЕНИЯ
         Т.Е. ВХОДИТ ЛИ ОБЪЕКТ В ОТОБРАЖАЕМУЮ ОБЛАСТЬ */
         
         
        
         /*Прорисовка пламени*/
         draw_flame(g,p_display.getX(),p_display.getY());
         /*Отрисовка объекта*/
         draw_in_scr(g,(X+p_display.getX()),(Y+p_display.getY()),v_F,v_P);
         draw_aim(g,Target.X+p_display.getX(),Target.Y+p_display.getY());
        // draw_Hedges(g,p_display.getX(),p_display.getY());
          
     }
     
    
      
     public void draw_aim(Graphics g
                     ,double x
                     ,double y){
         
         if(!Target.DeadFlag){
             Graphics2D g2 = (Graphics2D)g;/**/
             RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         
             g2.setRenderingHints(rh);
             g2.setColor(Color.RED);
             
             g2.draw(new Ellipse2D.Double(x-10,y-10,20,20));
             g2.draw(new Ellipse2D.Double(x-1,y-1,2,2));
             g2.draw(new Line2D.Double(x,y-5,x,y-15));
             g2.draw(new Line2D.Double(x,y+5,x,y+15));
             g2.draw(new Line2D.Double(x-5,y,x-15,y));
             g2.draw(new Line2D.Double(x+5,y,x+15,y));
         }
         
     }
        
        
    public void draw_in_scr(Graphics g
                     ,double x
                     ,double y
                     ,boolean v_F  
                     ,boolean v_P ){
        
        
          Graphics2D g2 = (Graphics2D)g;
          RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         
         
         g2.setRenderingHints(rh);
         
         g2.setColor(Color.DARK_GRAY);
         
         GeneralPath path= new GeneralPath();
         double r = RO;     
         
         path.moveTo(x+(Math.cos(P.angle))*r ,y+(Math.sin(P.angle))*r);
         
         path.lineTo(x+(Math.cos(P.angle+Math.PI/6))*r/2 ,y+(Math.sin(P.angle+Math.PI/6))*r/2);
         
         
         path.lineTo(x+(Math.cos(P.angle-Math.PI*7/6))*r/2 ,y+(Math.sin(P.angle-Math.PI*7/6))*r/2);
         path.lineTo(x+(Math.cos(P.angle+Math.PI*7/6))*r/2 ,y+(Math.sin(P.angle+Math.PI*7/6))*r/2);
         
         path.lineTo(x+(Math.cos(P.angle-Math.PI/6))*r/2 ,y+(Math.sin(P.angle-Math.PI/6))*r/2);
         
         path.closePath();
      
         g2.setStroke(new BasicStroke(1f));
         g2.fill(path);
         
           /*Прорисовка гибели объекта*/
         if(DeadFlag){
             Point2D center = new Point2D.Double(x, y);
             
             float radius = RO*2-DeadSteps+2;
             if(radius<=0) radius = 1;
            
             float[] dist = { 0.6f, 1.0f};
             Color[] colors = { Color.YELLOW, new Color(1,0,0,0) };
             RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
             g2.setPaint(p);
             g2.fill(new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2));
         }
        
    }
    
     void draw_flame(Graphics g, double x, double y){
      
         
          Graphics2D g2 = (Graphics2D)g;/**/
          RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         
         
         g2.setRenderingHints(rh);
         g2.setColor(Color.YELLOW);
         
         GeneralPath path= new GeneralPath();
         
         if(flame.size()>0)
         {
            path.moveTo(flame.get(0).getX()+x, flame.get(0).getY()+y);
            for(int i = 1;i<flame.size();i++ ){
                path.lineTo(flame.get(i).getX()+x, flame.get(i).getY()+y);
            }
            
            for(int i = 1;i<flame.size();i++ ){
                int j = flame.size()-i;
                path.lineTo(flame.get(j).getX()+x, flame.get(j).getY()+y);
            }
       //  path.closePath();
         
         
         }
         
         g2.setStroke(new BasicStroke(1f));
         g2.draw(path);
         
     }
     
    
    public void move(double Mtplr){
         
         chetchik++;
         if(Target.DeadFlag){RocketFuel = 0;}
         
         
         
         if((RocketFuel>0)){
           
               Vector FP = new Vector(0,P.length);
             chetchik = 0;
            /*Если растояние до объекта меньше 2 радиусов то насрать на препятсвия*/
            if(Target.Distance(this)<Target.RO*10) {Hedges.removeAll(Hedges);}
            
            if(Hedges.size()==0) /*Если массив препятсвий пуст то движемся в сторону цели*/
            {  
              
                /*Движение в сторону цели*/  
                FP.SetAngle(X, Y, Target.X, Target.Y);
                
                if(Math.abs(FP.angle-P.angle)>Math.PI/12){
                    FP.Minus(P);
                    
                }else{
                    if (P.length<40) {FP.length=2.5;} else FP.length = 0; 
                }
                
               
                
           }else{
               
                double[] angles = new double[Hedges.size()];
                for(int j=0;j<Hedges.size();j++){
                        Vector v=new Vector(1,1);
                        v.SetAngle(X, Y, Hedges.get(j).X, Hedges.get(j).Y);
                        angles[j] = v.angle;
                    }
                
                double min_angle = Math.PI;
                double tar_angle = Vector.SetAngleD(X, Y, Target.X, Target.Y);
                double cur_angleP
                      ,cur_angleM;
                for(int i=0;i<180;i++){
                    
                    cur_angleP = P.angle+Math.PI*2*i/180;
                    if(cur_angleP>Math.PI*2){cur_angleP=cur_angleP-Math.PI*2;}
                    
                    boolean fP=true;
                    for(int j=0;j<Hedges.size();j++){
                        if(Vector.AngleDiff(angles[j],cur_angleP)<Math.PI/2){fP=false;}
                    }
                    
                    cur_angleM = P.angle-Math.PI*2*i/180;
                    if(cur_angleM<0){cur_angleM=cur_angleM+Math.PI*2;}
                    
                    boolean fM=true;
                    for(int j=0;j<Hedges.size();j++){
                        if(Vector.AngleDiff(angles[j],cur_angleM)<Math.PI/2){fM=false;}
                    }
                   
                    
                    if((fP)&(fM)){
                        if(Vector.AngleDiff(P.angle,cur_angleP)<Vector.AngleDiff(P.angle,cur_angleM)){
                            min_angle = cur_angleP;
                        }else{
                            min_angle = cur_angleM;
                        }
                        break;
                    }
                    
                    if((fP)&(!fM)){
                        min_angle = cur_angleP;
                        break;
                    }
                    
                    if((!fP)&(fM)){
                        min_angle = cur_angleM;
                        break;
                    }
                    
                }
                
                FP = new Vector(min_angle,P.length);
                if(Math.abs(FP.angle-P.angle)>Math.PI/12){
                    FP.Minus(P);
                   
                }else{
                    if (P.length<40) {FP.length=2.5;} else FP.length = 0; 
                }
               
            
           }
             
             FP.length=FP.length/2;
             
             if(FP.length>0.05){P.Plus(FP);}//System.out.println("FP.length="+FP.length+" FP.angle="+FP.angle+" P.length="+P.length+" P.angle="+P.angle);
             RocketFuel = RocketFuel-FP.length/Mtplr;
            
         }
         
        Hedges.removeAll(Hedges);
         
         flame.add(new Point2D.Double(X,Y));
         if((flame.size()>Mtplr/4)&&(flame.get(0)!=null)){flame.remove(0);}
         if((RocketFuel<=0)
          &&(flame.size()>0)
          &&(flame.get(0)!=null)){flame.remove(0);} 
         
        
         double xd ;
         double yd ;
         
          xd = (Math.cos(P.angle)*P.length/M);
          yd = (Math.sin(P.angle)*P.length/M);
          
          xd = xd/Mtplr;
          yd = yd/Mtplr;
         
          
          
          X = X+xd;
          Y = Y+yd;
         
          //System.out.println("X= "+X+" Y="+Y+" xd="+xd+" yd="+yd+" P.angle="+P.angle+" P.length="+P.length);
         
         if(DeadFlag){
           DeadSteps--;  
           if(DeadSteps<0) DeadSteps = 0;
         }
    }
    
    
    
    
    /**Запись в массив препятсвия*/
    public void setHedge(BaseClass b){
        if(b!=Target)
        {
           Hedges.add(b);
        }
    }
    
  
    
}
