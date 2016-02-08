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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.MouseInfo;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import sun.font.Font2D;

/**
 *
 * @author Vladimir
 */
/** Игровой объект "Планета" 
 * используется как для игрока, так и для целей 
 * потом даже проще, наверное, будет ИИ реализовать
 */
public class Planet extends BaseClass {
    public boolean IsPlayer = false;            /*Метка планеты-игрока*/
    public boolean HaveGun = IsPlayer;          /*Наличие пушки у планеты*/
    private Vector Gun;                         /*Вектор пушки*/
    public int GunType = 1;                     /*Тип задействованного орудия*/
    public double Energy = 0;                   /*Энергия планеты*/
    public double MaxEnergy = 10000/*1000*/;    /*Максимальное количество запасаемой энергии планеты*/
    private double GunPowerNeed = 200;          /*Заряд в пушке для выстрела*/
    private double FireRate = 10;               /*Скорострельность орудия*/
    private double FireTimer;                   /*Таймер перезарядки*/
    private Point2D p_aim;
    public  String Name;
    public  Grenede GrenateEjected;             /*Запущенная */
    public  Color NameColor = Color.GREEN;
    public  boolean dw_name = true;
    public  int AI_level;
    
    String Names[] = new String[]{"Alderaan", "Anoat", "Bespin", "Corellia", "Coruscant", "D'Qar", "Dagobah", "Dantooine"
                                ,"Dathomir", "Endor", "Felucia", "Geonosis", "Hosnian Prime", "Hoth", "Jakku", "Kamino"
                                ,"Kashyyyk", "Kessel", "Lothal", "Mon Calamari", "Moraband", "Mustafar", "Mygeeto"
                                ,"Naboo", "Nal Hutta", "Rodia", "Ryloth", "Sullust", "Takodana", "Tatooine", "Umbara"
                                ,"Utapau", "Yavin"};
    
    /**Конструкторы класса*/
    Planet(){
        super();
        dw_orbit = true;
        dw_health = true;
        calc_orbit();
    }
    
    Planet(double x
         , double y
         , double m
         , int ro
         , double vangle
         , double vlength
         , ArrayList<BaseClass> AL
         , boolean player
         , boolean havegun){
        super(x, y, m, ro, vangle, vlength, AL);
        IsPlayer = player;
        if(havegun)GiveGun();
        GunPowerNeed = 200;
        DeadSteps = 20;
        p_aim = new Point2D.Double();
        dw_orbit = true;
        dw_health = true;
        dw_energy = true;
        
        /*if(IsPlayer){
            Name = "Earth";
        }else{
            Name = Names[(int)(random() * Names.length)];
        }*/
        Name = Names[(new Random()).nextInt(Names.length)];
        calc_orbit();
    }
    
    
    Planet(  
           BaseClass _LeadingObject    /*Ведущий объект*/
         , double    _rLeadingObject   /*Растояние до ведущего объекта*/
         , double    _OrbitPosition    /*Позиция на орбите у ведущего объекта*/
         , double m
         , int ro
         , double vangle
         , double vlength
         , ArrayList<BaseClass> AL
         , boolean player
         , boolean havegun){
        super(_LeadingObject 
         ,_rLeadingObject 
         , _OrbitPosition 
         , m, ro, vangle, vlength, AL);
        IsPlayer = player;
        if(havegun)GiveGun();
        GunPowerNeed = 200;
        DeadSteps = 20;
        p_aim = new Point2D.Double();
        dw_orbit = true;
        dw_health = true;
        dw_energy = true;
       /* if(IsPlayer){
            Name = "Earth";
        }else{
            Name = Names[(int)(random() * Names.length)];
        }*/
       
        Name = Names[(new Random()).nextInt(Names.length)];
        calc_orbit();
       
    }

    
    @Override
    public void draw_in_scr(Graphics g, double x, double y, boolean v_F, boolean v_P ){
        RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
        Graphics2D g2 = (Graphics2D)g;
        
        /*Отрисовка пушки*/
        double r = RO * 2;
        if(IsPlayer){
            g2.setColor(Color.GRAY);
            double mouseX = MouseInfo.getPointerInfo().getLocation().x;
            double mouseY = MouseInfo.getPointerInfo().getLocation().y;
            g2.draw(new Line2D.Double(x,y, x + (Math.cos(Gun.angle) * r), y + (Math.sin(Gun.angle) * r)));
            //g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(Gun.angle) * r), (int)y + (int)(Math.sin(Gun.angle) * r));
        }
        
        g2.setRenderingHints(rh);
        g2.setColor(Color.WHITE);
        if(DeadFlag==false){
          double _x  = (x - RO);
          double _y  = (y - RO);
         // System.out.println(" X="+_x+" Y="+_y);
          g2.fill(new Ellipse2D.Double(_x,_y, RO * 2, RO * 2));
        }
        
        /*Отрисовка имени*/
        if(dw_name){
            g2.setColor(NameColor);
            FontRenderContext fontContext = new FontRenderContext(null, false, false);
            Font font = new Font("Arial", Font.TYPE1_FONT, 15);
            GlyphVector gv = font.createGlyphVector(fontContext, Name);
            Shape sh;
            sh = gv.getOutline((float)(x - (Name.length() / 2) * 10), (float)(y - RO - 10));
            g2.fill(sh);
        }
        
        /*Отрисовка прогрессбара зарядки планеты*/
        /*Заливка бара*/
        g2.setColor(Color.GRAY);
        g2.fill(new Rectangle2D.Double(x - RO, y - RO - 5, RO * 2, 2));
        //g2.fillRect((int)(x - RO), (int)(y - RO - 5), (int)(RO * 2), (int)(2));
        
        /*Маркер необходимого для выстрела кол-ва энергии*/
        g2.setColor(Color.RED);
        g2.setBackground(Color.RED);
        g2.fill(new Rectangle2D.Double(x - RO, y - RO - 5, RO * 2 * GunPowerNeed / MaxEnergy, 2));
        //g2.fillRect((int)(x - RO), (int)(y - RO - 5), (int)(RO * 2 * (GunPowerNeed / MaxEnergy)), (int)(2)); 
        /*Текущий уровень заряда*/
        g2.setColor(Color.BLUE);
        g2.setBackground(Color.BLUE);
        g2.fill(new Rectangle2D.Double(x - RO, y - RO - 5, RO * 2 * Energy / MaxEnergy, 2));
        //g2.fillRect((int)(x - RO), (int)(y - RO - 5), (int)(RO * 2 * (Energy / MaxEnergy)), (int)(2 )); 
        
        /*Направление равнодействующей*/
        if((F.length != 0) & (v_F)){
            g2.setColor(Color.BLUE);
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(F.angle) * r), (int)y + (int)(Math.sin(F.angle) * r));
        }
         
        /*Направление Импульса*/
        if((P.length != 0) & (v_P)){
            g2.setColor(Color.GREEN);
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(P.angle) * r), (int)y + (int)(Math.sin(P.angle) * r));
        }
        
        /*Прорисовка гибели объекта*/
        if(DeadFlag){
            Point2D center = new Point2D.Double(x, y); 
            float radius = RO * (DeadSteps / 20) + 2;
            if(radius <= 0) radius = 1;
           
            float[] dist = { 0.6f, 1.0f};
            Color[] colors = { Color.YELLOW, new Color(1, 0, 0, 0) };
            RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(p);
            g2.fill(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
        }
     }
    

    /**Нацеливание пушки на точку*/
    public void Aim(double x, double y){
        
        Gun.SetAngle(X, Y, x, y);
        p_aim.setLocation(x,y);
    }

    /**Выстрел из орудия*/
    public boolean Shoot(){
        if((Energy >= GunPowerNeed) && (FireTimer <= 0)){
            try{
                /*вектор учитывает скорость и направление движения планеты-стрелка*/
                Vector ShotV = new Vector(P.angle, P.length / M).Plus(Gun);
                switch(GunType){
                    case 1: new Projectile(X + Math.cos(Gun.angle) * Gun.length
                                         , Y + Math.sin(Gun.angle) * Gun.length
                                         , 1, 1
                                         , Gun.angle//ShotV.angle
                                         , 50//ShotV.length
                                         , ALBaseClass);
                            break;
                    case 2: int bullets = 5;    /*Кол-во "дробинок"*/
                            for(int i = -(bullets / 2); i <= (bullets / 2); i++){
                                /**В первых двух параметрах Math.PI / i / 5
                                 * это смещение появляющихся снарядов
                                 * относительно дула - чтобы не столкнулись сразу
                                 */
                                 double delta=0;
                                 if(i!=0){delta=(Math.PI / i / 10);}
                                 Random rr = new Random();
                                 int r =0;
                                 r=rr.nextInt(2);
                                 if(r!=0)
                                 (new Projectile(X + (Math.cos(Gun.angle + delta)) * (Gun.length)
                                             , Y + (Math.sin(Gun.angle + delta)) * (Gun.length)
                                             , 1, 1
                                             /*Math.PI / i / 12 - угол разлета снарядов*/
                                             , /*ShotV.angle*/Gun.angle + delta/2
                                             , /*ShotV.length*/50+5, ALBaseClass)).Transparent = 5;  /*Сначала пули "эфирные" - чтобы не столкнулись в стволе*/
                                 r=rr.nextInt(2);
                                 if(r!=0)
                                 (new Projectile(X + (Math.cos(Gun.angle + delta)) * (Gun.length)
                                             , Y + (Math.sin(Gun.angle + delta)) * (Gun.length)
                                             , 1, 1
                                             /*Math.PI / i / 12 - угол разлета снарядов*/
                                             , /*ShotV.angle*/Gun.angle + delta/2
                                             , /*ShotV.length*/50+10, ALBaseClass)).Transparent = 5; 
                                 
                                 r=rr.nextInt(2);
                                 if(r!=0)
                                 (new Projectile(X + (Math.cos(Gun.angle + delta)) * (Gun.length)
                                             , Y + (Math.sin(Gun.angle + delta)) * (Gun.length)
                                             , 1, 1
                                             /*Math.PI / i / 12 - угол разлета снарядов*/
                                             , /*ShotV.angle*/Gun.angle + delta/2
                                             , /*ShotV.length*/50+15, ALBaseClass)).Transparent = 5; 
                                 
                                 r=rr.nextInt(2);
                                 if(r!=0)
                                 (new Projectile(X + (Math.cos(Gun.angle + delta)) * (Gun.length)
                                             , Y + (Math.sin(Gun.angle + delta)) * (Gun.length)
                                             , 1, 1
                                             /*Math.PI / i / 12 - угол разлета снарядов*/
                                             , /*ShotV.angle*/Gun.angle + delta/2
                                             , /*ShotV.length*/50+20, ALBaseClass)).Transparent = 5; 
                                 
                                 
                            }
                            break;
                            
                    case 3: for(int i=0;i<ALBaseClass.size();i++){
                                if((ALBaseClass.get(i).getClass().getName()=="main.Star")
                                  |(ALBaseClass.get(i).getClass().getName()=="main.Planet"))
                                    {
                                        double r = Math.sqrt(Math.pow((ALBaseClass.get(i).X-p_aim.getX()),2)+Math.pow((ALBaseClass.get(i).Y-p_aim.getY()),2));
                                        if(ALBaseClass.get(i).RO>r){
                                           new  Roket(X + Math.cos(Gun.angle) * Gun.length*1.5f
                                                         , Y + Math.sin(Gun.angle) * Gun.length*1.5f
                                                         , 1, 4
                                                         , Gun.angle
                                                         , 15
                                                         , ALBaseClass
                                                         ,ALBaseClass.get(i) );
                                           
                                          // System.out.println(p_aim.getX()+" "+p_aim.getY());
                                        }
                                    }
                                } 
                            break;
                    case 4: 
                         
                               GrenateEjected = new  Grenede(
                                           X + Math.cos(Gun.angle) * Gun.length
                                         , Y + Math.sin(Gun.angle) * Gun.length
                                         , 1, 3
                                         , ShotV.angle
                                         , 30//ShotV.length
                                         , ALBaseClass
                                         , 25);
                            
                            break;                            
                }
                Energy -= GunPowerNeed;
                FireTimer += 60000 / FireRate;
            }catch(Error e){
                System.err.println("ERROR: Failed to shoot!");
                return false;
            }
            return true;
        }else{
           // System.err.println("Failed to shoot!");
            //System.out.println("Reload time: " + FireTimer);
            return false;
        }
    }
    
    /**Перезарядка оружия
     * возвращает остаток времени до перезарядки
     */
    public double Reload(double elapsed){
        if(FireTimer > 0)FireTimer = max(0, FireTimer - elapsed);
        return FireTimer;
    }
    
    /**Подзарядка планеты
     * текущий вариант использует только звезды как источники энергии
     * в будущем будем учитывать и другие (внутренние)
     */
    public void Charge(){
        if(Energy < MaxEnergy){
            for(int i = 0; i < ALBaseClass.size(); i++){
                if(ALBaseClass.get(i) != null){
                    if(ALBaseClass.get(i).getClass().getName() == "main.Star"){
                        Energy += 1 / Distance(ALBaseClass.get(i)) * 10000/*1000*/;
                    }
                }
            }
            Energy = min(Energy, MaxEnergy);
        }
    }
    

    /**Переключение вооружения
     * Подменяет значения полей описывающих орудие и его снаряды
     * 
     * Возращает истину когда удалось переключится
     */
    public boolean SwitchGun(int gt){
        try{
            GunType = gt;
            switch(gt){
                case 1: GunPowerNeed = 200;
                        FireRate = 200;
                        break;
                case 2: GunPowerNeed = 4000;
                        FireRate = 500;
                        break;
                case 3: GunPowerNeed = 1500;
                        FireRate = 500;
                        break;
                case 4: GunPowerNeed = 2000;
                        FireRate = 500;
                        break;        
            }
            return true;
        }catch(Error e){
            System.err.println("ERROR: Failed to switch weapon!");
            return false;
        }
    }

    
    /**Выдать планете оружие*/
    public void GiveGun(){
        HaveGun = true;
        if(HaveGun){
            Gun = new Vector(0, RO * 2);
        }  
    }
    

    
    /**Обработка гибели объекта*/
    @Override
    public void Die(){
        DeadFlag = true;
        Disruption();//Explode();
    }
    
    /**После выстрела*/
    private void Shooted(){
        Energy -= GunPowerNeed;
        FireTimer += 60000 / FireRate;
       // System.out.println(new StringBuilder().append("Player shoot!").toString());
    }
    
 
    /**Запись в массив препятсвий и приближающихся объектов*/
    @Override
    public void setHedge(BaseClass b){
        if(b.LeadingObject!=this){
            Hedges.add(b);
        }
    }
    
    @Override
    public void AI_do(){
      
      if(IsPlayer) {
      BaseClass nearest=null; /*Ближайжий объект*/  
      for(int i=0;i<Hedges.size();i++){
          if(nearest ==null){nearest = Hedges.get(0);}
          if(Hedges.get(i).Distance(this)<nearest.Distance(this)){
              nearest = Hedges.get(i);
          }
          
      }
      
      if(nearest!=null){
          
          
          double angle_delta = (AI_level);  angle_delta = Math.PI*(double)(angle_delta)/720;  
          double angle_aim   = Vector.SetAngleD(X,Y,nearest.X, nearest.Y);
          double angle_plus   = Gun.angle+angle_delta;
          double angle_minus  = Gun.angle-angle_delta;
          
          if((Vector.AngleDiff(angle_aim, angle_plus)<=angle_delta)
            |(Vector.AngleDiff(angle_aim, angle_minus)<=angle_delta)){
              this.Aim(nearest.X, nearest.Y);
          }else{
              if(Vector.AngleDiff(angle_aim, angle_plus)<=Vector.AngleDiff(angle_aim, angle_minus)){
                  Gun.angle = angle_plus;
              }else{
                  Gun.angle = angle_minus;
              }
              
          }
          
                  
          this.SwitchGun(1);        
          this.Shoot();
          
      }else{
            BaseClass aim=null;
            for(int i=0;i<ALBaseClass.size();i++){
                BaseClass object = ALBaseClass.get(i);
                if((object!=null)
                 &&(object!=this)
                 &&(object.getClass().getName()=="main.Planet")){
                    if(aim==null) aim = object;
                    if(object.Distance(this)<aim.Distance(this)){
                        aim=object;
                    }
                }
            }
            if(aim!=null){
                double angle_delta = (AI_level);  angle_delta = Math.PI*(double)(angle_delta)/720;  
                 double angle_aim   = Vector.SetAngleD(X,Y,aim.X, aim.Y);
                 double angle_plus   = Gun.angle+angle_delta;
                 double angle_minus  = Gun.angle-angle_delta;
          
                 if((Vector.AngleDiff(angle_aim, angle_plus)<=angle_delta)
                   |(Vector.AngleDiff(angle_aim, angle_minus)<=angle_delta)){
                         this.Aim(aim.X, aim.Y);
                    }else{
                         if(Vector.AngleDiff(angle_aim, angle_plus)<=Vector.AngleDiff(angle_aim, angle_minus)){
                            Gun.angle = angle_plus;
                         }else{
                            Gun.angle = angle_minus;
                         }
          }
                
                
                this.SwitchGun(3);
                //this.Aim(aim.X, aim.Y);
                this.Shoot();
            }
            
      
      }
    }
      Hedges.removeAll(Hedges);
    }
 
}
