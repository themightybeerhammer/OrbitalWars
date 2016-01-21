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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author Vladimir
 */
/** Игровой объект "Планета" 
 * используется как для игрока, так и для целей 
 * потом даже проще, наверное, будет ИИ реализовать
 */
public class Planet extends BaseClass {
    public boolean IsPlayer = false;    /*Метка планеты-игрока*/
    public boolean HaveGun = IsPlayer;  /*Наличие пушки у планеты*/
    private Vector Gun;                 /*Вектор пушки*/
    public int GunType = 1;             /*Тип задействованного орудия*/
    public float Energy = 0;            /*Энергия планеты*/
    public float MaxEnergy = 10000/*1000*/;      /*Максимальное количество запасаемой энергии планеты*/
    private float GunPowerNeed = 200;   /*Заряд в пушке для выстрела*/
    
    /*Конструкторы класса*/
    Planet(){
        super();
    }
    Planet(float x, float y, float m, int ro, float vangle, float vlength, ArrayList<BaseClass> AL, boolean player, boolean havegun){
        super(x, y, m, ro, vangle, vlength, AL);
        this.IsPlayer = player;
        this.HaveGun = havegun;
        if(this.HaveGun){
            this.Gun = new Vector(0, 20);
        }  
        this.GunPowerNeed = 200;
        this.DeadSteps = 20;
    }
    
    @Override
    void draw_in_scr(Graphics g, float x, float y, boolean v_F, boolean v_P ){
        RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHints(rh);
        g2.setColor(Color.WHITE);
        if(DeadFlag==false){
          g2.draw(new Ellipse2D.Float(x - this.RO, y - this.RO, this.RO * 2, this.RO * 2));
        }
        /*Отрисовка прогрессбара зарядки планеты*/
        /*Заливка бара*/
        g2.setColor(Color.GRAY);
        g2.fillRect((int)(x - this.RO), (int)(y - this.RO - 5), (int)(RO * 2), (int)(2));
        /*Маркер необходимого для выстрела кол-ва энергии*/
        g2.setColor(Color.RED);
        g2.setBackground(Color.RED);
        g2.fillRect((int)(x - this.RO), (int)(y - this.RO - 5), (int)(RO * 2 * (this.GunPowerNeed / this.MaxEnergy)), (int)(2)); 
        /*Текущий уровень заряда*/
        g2.setColor(Color.BLUE);
        g2.setBackground(Color.BLUE);
        g2.fillRect((int)(x - this.RO), (int)(y - this.RO - 5), (int)(RO * 2 * (this.Energy / this.MaxEnergy)), (int)(2 )); 
        
        /*Отрисовка пушки*/
        float r = 20;
        if(this.IsPlayer){
            g2.setColor(Color.GRAY);
            float mouseX = MouseInfo.getPointerInfo().getLocation().x;
            float mouseY = MouseInfo.getPointerInfo().getLocation().y;
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(this.Gun.angle) * r), (int)y + (int)(Math.sin(this.Gun.angle) * r));
        }
        
        /*Направление равнодействующей*/
        if((this.F.length != 0) & (v_F)){
            g2.setColor(Color.BLUE);
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(this.F.angle) * r), (int)y + (int)(Math.sin(this.F.angle) * r));
        }
         
        /*Направление Импульса*/
        if((this.P.length != 0) & (v_P)){
            g2.setColor(Color.GREEN);
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(this.P.angle) * r), (int)y + (int)(Math.sin(this.P.angle) * r));
        }
        
        
     
         /*Прорисовка гибели объекта*/
         if(DeadFlag){
         
             Point2D center = new Point2D.Float(x, y); 
             float radius = RO*((float)DeadSteps/20)+2;
             if(radius<=0) radius = 1;
            
             float[] dist = { 0.6f, 1.0f};
             Color[] colors = { Color.YELLOW, new Color(1,0,0,0) };
             RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
             g2.setPaint(p);
             g2.fill(new Ellipse2D.Float(x-radius, y-radius, radius*2, radius*2));
         }
     }
    
    /*Нацеливание пушки на точку*/
    void Aim(float x, float y){
        this.Gun.SetAngle(this.X, this.Y, x, y);
    }

    /*Выстрел из орудия*/
    boolean Shoot(ArrayList<BaseClass> AL){
        try{
            if(this.Energy >= this.GunPowerNeed){
                this.Energy -= this.GunPowerNeed;
                /*вектор учитывает скорость и направление движения планеты-стрелка*/
                Vector ShotV = new Vector(this.P.angle, this.P.length / this.M).Plus(this.Gun);
                switch(this.GunType){
                    case 1: new Projectile(this.X + (float)(Math.cos(this.Gun.angle)) * this.Gun.length
                                         , this.Y + (float)(Math.sin(this.Gun.angle)) * this.Gun.length
                                         , 1, 1
                                         , ShotV.angle
                                         , ShotV.length, AL);
                            break;
                    case 2: for(int i = -3; i <= 3; i++){ /*Количество "дробинок" (напр. от (-3) до 3 = 6 "дробинок"*/
                                /**В первых двух параметрах Math.PI / i / 5
                                 * это смещение появляющихся снарядов
                                 * относительно дула - чтобы не столкнулись сразу
                                 */
                                
                                
                                new Projectile(this.X + (float)(Math.cos(this.Gun.angle + (float)(Math.PI / i / 5))) * (this.Gun.length)
                                             , this.Y + (float)(Math.sin(this.Gun.angle + (float)(Math.PI / i / 5))) * (this.Gun.length)
                                             , 1, 1
                                             /*Math.PI / i / 12 - угол разлета снарядов*/
                                             , ShotV.angle + (float)(Math.PI / i / 12)
                                             , ShotV.length, AL);
                            }
                            break;
                }
                return true;   
            }
        }finally{
            return false;
        }
    }
    
    /**Подзарядка планеты
     * текущий вариант использует только звезды как источники энергии
     * в будущем будем учитывать и другие (внутренние)
     */
    void Charge(ArrayList<BaseClass> AL){
        if(this.Energy < this.MaxEnergy){
            for(int i = 0; i < AL.size(); i++){
                if(AL.get(i) != null){
                    if(AL.get(i).getClass().getName() == "main.Star"){
                        this.Energy += 1 / this.Distance(AL.get(i)) * 100000/*1000*/;
                    }
                }
            }
            this.Energy = min(this.Energy, this.MaxEnergy);
        }
    }
    
    /**Переключение вооружения
     * Подменяет значения полей описывающих орудие и его снаряды
     * 
     * Возращает истину когда удалось переключится
     */
    boolean SwitchGun(int gt){
        try{
            this.GunType = gt;
            switch(gt){
                case 1: this.GunPowerNeed = 200;
                        break;
                case 2: this.GunPowerNeed = 1000;
            }
            return true;
        }finally{
            return false;
        }
    }
}
