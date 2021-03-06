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

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Integer.max;
import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Path;
import main.Vector;
/**
 *
 * @author Vladimir
 */
public class BaseClass {
    public double X,Y; /*Координаты объекта*/
    double M;   /*Масса объекта */
    int RO;    /*Радиус сферы(объекта)*/
    Vector P;  /*Вектор импульса*/
    Vector F;  /*Вектор равнодействующей*/
    double MinMassOrbit;     /*Минимальная масса объекта для участия в расчете орбиты*/
    private ArrayList<Point2D> Orbit; /*Кординаты орбиты*/
    static ArrayList<BaseClass> ALBaseClass;
    ArrayList<BaseClass> Hedges; /*Масив перпятствий и приближающихся объектов*/
    boolean dw_orbit; /*Флаг рисования орбиты*/
    
    boolean DeadFlag=false; /*Флаг Гибели объекта*/
    int DeadSteps=0;        /*Количество итераций гибели объекта*/
    
    boolean dw_health=false; /*Флаг рисования здоровья*/ 
    boolean dw_energy=false;    /*Флаг рисования энергии*/
    double HealthMax=1    /*Максимальное здоровье*/
          ,HealthCur=1;   /*Текущие здоровье*/
    
    int Transparent = 0;    /*Счетчик отсрочки расчета столкновений. Необходим для создания объекта-в-объекте*/
    
    BaseClass LeadingObject;    /*Ведущий объект*/
    double    rLeadingObject;   /*Растояние до ведущего объекта*/
    double    OrbitPosition;    /*Позиция на орбите у ведущего объекта*/
    
    
   
    
     BaseClass(){
       X = 0;
       Y = 0;
       M = 1;
       RO = 1;
       P = new Vector(1,1);
       F = new Vector();
       dw_orbit = false;
     }
     
     
     BaseClass(
               BaseClass _LeadingObject    /*Ведущий объект*/
              ,double    _rLeadingObject   /*Растояние до ведущего объекта*/
              ,double    _OrbitPosition    /*Позиция на орбите у ведущего объекта*/
              
              ,double m
              ,int ro
              ,double vangle
              ,double vlength
              ,ArrayList<BaseClass> AL
              ){
       
       
       
       M = m;
       RO = ro;
       P = new Vector(vangle,vlength);
       F= new Vector(0,0);
       AL.add(this);
       //ALBaseClass =new ArrayList<>(AL);
       ALBaseClass = AL;
       /*Орбита*/
       MinMassOrbit = m*10;
       Orbit = new ArrayList<>();
       dw_orbit = false;
       DeadSteps = ro*20;
       
       HealthMax = M;    /*Максимальное здоровье*/
       HealthCur = HealthMax;
       Hedges=new ArrayList<>();
       
       LeadingObject = _LeadingObject;    /*Ведущий объект*/
       rLeadingObject =  _rLeadingObject;   /*Растояние до ведущего объекта*/
       OrbitPosition = _OrbitPosition;    /*Позиция на орбите у ведущего объекта*/
       
       X =LeadingObject.X+(Math.cos(OrbitPosition)*rLeadingObject);
       Y =LeadingObject.Y+(Math.sin(OrbitPosition)*rLeadingObject);
      
     }
     
     
     BaseClass(
               double x
              ,double y
              ,double m
              ,int ro
              ,double vangle
              ,double vlength
              ,ArrayList<BaseClass> AL
              ){
       X = x;
       Y = y;
       M = m;
       RO = ro;
       P = new Vector(vangle,vlength);
       F= new Vector(0,0);
       AL.add(this);
       //ALBaseClass =new ArrayList<>(AL);
       ALBaseClass = AL;
       /*Орбита*/
       MinMassOrbit = m*10;
       Orbit = new ArrayList<>();
       dw_orbit = false;
       DeadSteps = ro*20;
       
       HealthMax = M;    /*Максимальное здоровье*/
       HealthCur = HealthMax;
       Hedges=new ArrayList<>();
      
     }
    
     void draw(Graphics g
             ,Point2D p_display /*,ВОТ ЗДЕСЬ БУДЕТ ПЕРЕМЕНАЯ ЭКРАН*/
             ,boolean v_F  /*Рисовать вектор равнодействующей*/
             ,boolean v_P  /*Рисовать вектор импульса*/     
            ){
        
         /*ПЕРЕД ЗАПУСКОМ ПРОЦЕДУРЫ ДОЛЖНА БЫТЬ ОБРАБОТКА ЭКРАНА ОТОБРАЖЕНИЯ
         Т.Е. ВХОДИТ ЛИ ОБЪЕКТ В ОТОБРАЖАЕМУЮ ОБЛАСТЬ */
        /*Орбита объекта*/
         if(dw_orbit)draw_orbit(g,p_display.getX(),p_display.getY()); 
         if(dw_health)draw_health(g,(X+p_display.getX()),(Y+p_display.getY())); 
         
         /*Отрисовка объекта*/
         draw_in_scr(g,(X+p_display.getX()),(Y+p_display.getY()),v_F,v_P);
         
         /*Маркировка препятствий и приближающихся объектов*/
       //  if(this.getClass().getName()=="main.Planet"){draw_Hedges(g,p_display.getX(),p_display.getY());}
          
          
     }
    
      /**Маркировка препятствий и приближающихся объектов*/
      public void draw_Hedges(Graphics g
                            ,double x
                            ,double y){
         
         Graphics2D g2 = (Graphics2D)g;/**/
         RenderingHints rh = new RenderingHints(
         RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);
         
         g2.setRenderingHints(rh);
         g2.setStroke(new BasicStroke(1f));
         g2.setColor(Color.GREEN);
         for(int i=0;i<Hedges.size();i++){
             g2.draw(new Ellipse2D.Double(Hedges.get(i).X+x-10,Hedges.get(i).Y+y-10,20,20));
         
         }
       } 
     
     /**Пламя за ракетой, кометой или ещё какой херней*/ 
     void draw_health(Graphics g, double x, double y){
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
         RenderingHints.KEY_ANTIALIASING,
         RenderingHints.VALUE_ANTIALIAS_ON);
     
        /*Отрисовка прогрессбара здоровья объекта*/
        /*Заливка бара*/
        g2.setRenderingHints(rh);
        g2.setColor(Color.GRAY);
        g2.fill(new Rectangle2D.Double(x-RO,y-RO-7,RO*2,2));
        //g2.fillRect((int)(x - RO), (int)(y - RO - 7), (int)(RO * 2), (int)(2));
      
        g2.setColor(Color.GREEN);
        g2.setBackground(Color.GREEN);
        g2.fill(new Rectangle2D.Double(x-RO,y-RO-7,RO*2*(HealthCur / HealthMax),2));
        //g2.fillRect((int)(x - RO), (int)(y - RO - 7), (int)(RO * 2 * (HealthCur / HealthMax)), (int)(2)); 
      
        
     
     }
   
     
     void draw_in_scr(Graphics g
                     ,double x
                     ,double y
                     ,boolean v_F  
                     ,boolean v_P ){
         
         Graphics2D g2 = (Graphics2D)g;
         RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         g2.setRenderingHints(rh);
         g2.setColor(Color.WHITE);
         g2.draw(new Ellipse2D.Double(x-RO, y-RO, RO*2, RO*2));
          /*Направление равнодействующей*/
         double r = 20;
         if((F.length!=0)&(v_F))
            {
                g2.setColor(Color.BLUE);
                g2.draw(new Line2D.Double(x,y,x+(Math.cos(F.angle)*r),y+(Math.sin(F.angle)*r)));
            }
         
         /*Направление Импульса*/
          r = 20;
         if((P.length!=0)&(v_P))
            {
                g2.setColor(Color.GREEN);
                g2.draw(new Line2D.Double(x,y,x+(Math.cos(P.angle)*r) , y+(Math.sin(P.angle)*r)));
            }
         
      
     }
     
     void calc_orbit(){
         
       
       if ((dw_orbit)&(LeadingObject==null)){ 
           
       Orbit.clear(); /*Чистим массив орбиты*/  
       /*Отбираем объекты масса которых значительно привышает массу объекта, которые явно влияют на траекторию полета */
        
       ArrayList<BaseClass> AL=new ArrayList<>();
        
       for(int i=0;i<ALBaseClass.size();i++){
            if((ALBaseClass.get(i).getClass().getName()!="main.CenterMass")
             &&(ALBaseClass.get(i)!=this)
             &&(ALBaseClass.get(i).M>=MinMassOrbit)
             &&(ALBaseClass.get(i)!=null)){ 
                 
            AL.add(ALBaseClass.get(i));
            }
        }
       
        double _r; /*Растояние между двумя объектами*/
        double _f; /*Сила притяжения между двумя объектами*/
        double _X =X,_Y=Y;   /*Координаты объекта текущего*/
        double _x,_y;   /*Координаты объекта взаимодействия*/
        double _m;      /*Масса объекта взаимодействия*/
        double _ro;     /*Радиус орбиты объекта взаимодействия*/
        double _a;      /*Направление вектора взаимодействия*/
        
        int j =0;
        
        double _xd,_yd;  /*Кординаты смещения*/
        
        Vector _F = new Vector(0,0); /*Вектор равнодействующей*/
        Vector _P = new Vector(P);   /*Импульс*/
       
       Orbit.add(new Point2D.Double(X,Y));
       
       if (AL.size()>0){
       do{
             
            _F = new Vector(0,0);
            
            for(int i=0;i<AL.size();i++){
              
                if(AL.get(i)!=null){
                
                _x=AL.get(i).X;
                _y=AL.get(i).Y;
                _m=AL.get(i).M;
                _ro=AL.get(i).RO;
                
                
                _r = Math.sqrt(Math.pow(_X-_x,2)+Math.pow(_Y-_y,2));
                if(_r<RO+_ro) _r = RO+_ro;
                if(_r<30) _r = 30;
                _f =  (_m*M/Math.pow(_r,2));
                _a = Math.asin((_y-_Y)/Math.sqrt((Math.pow(_x-_X,2)+Math.pow(_y-_Y,2))));
                
                 if((_x<_X)&&(_y>_Y))  { _a=_a*(-1)+Math.PI;}
                 if((_x<_X)&&(_y==_Y)) { _a=_a*(-1)+Math.PI;}
                 if((_x<_X)&&(_y<_Y))  { _a=_a*(-1)+Math.PI;}
                 if((_x>_X)&&(_y<_Y))  { _a=_a+Math.PI*2;}
         
                _F.Plus(new Vector(_a,_f));
                } 
         
            }
            _F.length=(_F.length/3);
            _P.Plus(_F);
            
            _xd=0; _yd=0;
            
            _xd = (Math.cos(_P.angle)*_P.length/M/3);
            _yd = (Math.sin(_P.angle)*_P.length/M/3);
            
            _X=_X+_xd;
            _Y=_Y+_yd;
            
            boolean flag = true;
            for(int i=0;i<Orbit.size();i++){
                if(Math.sqrt((Math.pow((Orbit.get(i).getX()-(_X-X)),2)+ Math.pow((Orbit.get(i).getY()-(_Y-Y)),2)))<10 ){
                    flag=false;
                }
            }
            
            if(flag){
             Orbit.add(new Point2D.Double((_X-X),(_Y-Y)));             
            }
             
            if((Math.sqrt(Math.pow(_X-X, 2)+Math.pow(_Y-Y, 2))<RO)&&(_xd*j>2*RO))break;
           j++;
       }while(j<3000);
       }         
      }
       
     }
     
     void draw_orbit(Graphics g, double x, double y){
         Graphics2D g2 = (Graphics2D)g;/**/
         RenderingHints rh = new RenderingHints(
             RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
         
         
         g2.setRenderingHints(rh);
         
         g2.setColor(Color.DARK_GRAY);
         
         if(LeadingObject==null){
         GeneralPath path= new GeneralPath();
         
         path.moveTo(Orbit.get(0).getX()+x, Orbit.get(0).getY()+y);
         
         for(int i=1;i<Orbit.size();i++){
            path.lineTo(Orbit.get(0).getX()+Orbit.get(i).getX()+x
                      , Orbit.get(0).getY()+Orbit.get(i).getY()+y);
         }
         
         path.closePath();
      
         g2.setStroke(new BasicStroke(2f));
         g2.draw(path);
         }else{
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new Ellipse2D.Double(LeadingObject.X-rLeadingObject+x,LeadingObject.Y-rLeadingObject+y,2*rLeadingObject,2*rLeadingObject));
         }
     }
     
     void calc_F_ravn(double Mtplr){
         
         if(LeadingObject!=null){
             if(LeadingObject.DeadFlag){DeadFlag=true;}
         }else{
         double r; /*Растояние между двумя объектами*/
         double f; /*Сила притяжения между двумя объектами*/
         double x,y;  /*Координаты объекта*/
         double m;    /*Масса объекта*/
         double ro;   /*Радиус орбиты объекта*/
         double a;    /*Направление вектора*/
         
        F=new Vector(0,0);
         
         for(int i=0;i<ALBaseClass.size();i++){
             if((ALBaseClass.get(i) != null)
              &&(ALBaseClass.get(i).getClass().getName()!="main.CenterMass")
              &&(ALBaseClass.get(i)!=this)
              &&(ALBaseClass.get(i).M>M*10)
              &&(ALBaseClass.get(i)!=null)       )
             {
                x=ALBaseClass.get(i).X;
                y=ALBaseClass.get(i).Y;
                m=ALBaseClass.get(i).M;
                ro=ALBaseClass.get(i).RO;
                
                
                r = Math.sqrt(Math.pow(X-x,2)+Math.pow(Y-y,2));
                if(r<RO+ro) r = RO+ro;
                if(r<30) r = 30;
                f =  (m*M/Math.pow(r,2));
                a = Math.asin((y-Y)/Math.sqrt((Math.pow(x-X,2)+Math.pow(y-Y,2))));
                
                 if((x<X)&&(y>Y))  { a=a*(-1)+Math.PI;}
                 if((x<X)&&(y==Y)) { a=a*(-1)+Math.PI;}
                 if((x<X)&&(y<Y))  { a=a*(-1)+Math.PI;}
                 if((x>X)&&(y<Y))  { a=a+Math.PI*2;}
         
                F.Plus(new Vector(a,f));
             }
         
         }
        F.length = F.length/Mtplr;
         
        P.Plus(F);
        
       } 
         
    }

     void move(double Mtplr){
         
        if((LeadingObject!=null)&(rLeadingObject>0)){
            int znak; 
            if(P.angle>=Math.PI){znak = -1;}else {znak = 1;}
              
            OrbitPosition=OrbitPosition+P.length/((double)(M*Mtplr*rLeadingObject))*znak;
            if(OrbitPosition<0){OrbitPosition=OrbitPosition+Math.PI*2;}
            if(OrbitPosition>Math.PI*2){OrbitPosition=OrbitPosition-Math.PI*2;}
            
            double xd ;
            double yd ;
         
            xd = (Math.cos(OrbitPosition)*rLeadingObject);
            yd = (Math.sin(OrbitPosition)*rLeadingObject);
          
            xd = xd; //Mtplr;
            yd = yd;//Mtplr;
          
            X =LeadingObject.X+xd;
            Y =LeadingObject.Y+yd;
           // System.out.println("X="+X+" Y="+Y);
        }else{
         double xd ;
         double yd ;
          
          xd = (Math.cos(P.angle)*P.length/M);
          yd = (Math.sin(P.angle)*P.length/M);
          
          xd = xd/Mtplr;
          yd = yd/Mtplr;
         
          
          X = X+xd;
          Y = Y+yd;
        }  
          
         if(DeadFlag){
           DeadSteps--;  
           if(DeadSteps<0) DeadSteps = 0;
           
         }
       
     Transparent = max(--Transparent, 0);
     
    }
     
    /**Расстояние от текущего до заданного объекта*/
    double Distance(BaseClass bc){
        return sqrt((X - bc.X) * (X - bc.X) + (Y - bc.Y) * (Y - bc.Y));
    }
    
    /**Взрыв - объект разлетается на куски*/
    void Explode(){
        int objCount = (int)sqrt(RO);   /*Кол-во осколков, на которые распадется планета*/
        int objSize = RO / objCount;    /*Размер осколков*/
        double objMass = objSize; /*Масса осколков*/
        double nx, ny;       /*Координаты появления осколка*/
        Vector dirbuff;         /*Вектор полета осколка*/
        Projectile projbuff;    /*Осколок*/
        /*Расстановка осколков внутри периметра планеты*/
        for(double i = 0; i < objCount; i++){
            nx = X + (RO / 2 * Math.cos(i));
            ny = Y + (RO / 2 * Math.sin(i));
            dirbuff = (new Vector().SetAngle(X, Y, nx, ny));
            projbuff = new Projectile(nx, ny, objMass, objSize, dirbuff.angle, RO, ALBaseClass);
            projbuff.Transparent = 1;   /*Временно делает осколок "эфирным", чтобы сразу не взорвался*/
        }
    }
    
    /**Взрыв - вариант распада на молекулы :) 4fun
     * Правда, а вдруг пригодится?
     */
    void Disruption(){
        double nx, ny;       /*Координаты появления осколка*/
        Vector dirbuff;         /*Вектор полета осколка*/
        Projectile projbuff;    /*Осколок*/
        /*Расстановка осколков внутри периметра планеты*/
        for(double i = 0; i < RO; i += 2){
            for(double j = 0; j < PI * 2; j += (PI * 2) / i / (2 * random())){
                dirbuff = new Vector(j, i);
                projbuff = new Projectile(X + dirbuff.GetX(), Y + dirbuff.GetY(), 1, 1, dirbuff.angle, RO, ALBaseClass);
                projbuff.Transparent = 1;   /*Временно делает осколок "эфирным", чтобы сразу не взорвался*/
            }
        }
    }
    
    /**Обработка гибели объекта*/
    void Die(){
        DeadFlag = true;
    }
     
      /**Запись в массив препятсвия*/
    public void setHedge(BaseClass b){
       
    }
    
    public void AI_do(){
        
       
    }
    

    
}
             
