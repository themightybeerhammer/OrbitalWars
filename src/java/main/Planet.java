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
        IsPlayer = player;
        HaveGun = havegun;
        if(HaveGun){
            Gun = new Vector(0, 20);
        }  
        GunPowerNeed = 200;
        DeadSteps = 20;
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
          g2.draw(new Ellipse2D.Float(x - RO, y - RO, RO * 2, RO * 2));
        }
        
        /*Отрисовка прогрессбара зарядки планеты*/
        /*Заливка бара*/
        g2.setColor(Color.GRAY);
        g2.fillRect((int)(x - RO), (int)(y - RO - 5), (int)(RO * 2), (int)(2));
        /*Маркер необходимого для выстрела кол-ва энергии*/
        g2.setColor(Color.RED);
        g2.setBackground(Color.RED);
        g2.fillRect((int)(x - RO), (int)(y - RO - 5), (int)(RO * 2 * (GunPowerNeed / MaxEnergy)), (int)(2)); 
        /*Текущий уровень заряда*/
        g2.setColor(Color.BLUE);
        g2.setBackground(Color.BLUE);
        g2.fillRect((int)(x - RO), (int)(y - RO - 5), (int)(RO * 2 * (Energy / MaxEnergy)), (int)(2 )); 
        
        /*Отрисовка пушки*/
        float r = 20;
        if(IsPlayer){
            g2.setColor(Color.GRAY);
            float mouseX = MouseInfo.getPointerInfo().getLocation().x;
            float mouseY = MouseInfo.getPointerInfo().getLocation().y;
            g2.drawLine((int)x, (int)y, (int)x + (int)(Math.cos(Gun.angle) * r), (int)y + (int)(Math.sin(Gun.angle) * r));
        }
        
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
            Point2D center = new Point2D.Float(x, y); 
            float radius = RO * ((float)DeadSteps / 20) + 2;
            if(radius <= 0) radius = 1;
           
            float[] dist = { 0.6f, 1.0f};
            Color[] colors = { Color.YELLOW, new Color(1, 0, 0, 0) };
            RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
            g2.setPaint(p);
            g2.fill(new Ellipse2D.Float(x - radius, y - radius, radius * 2, radius * 2));
        }
     }
    
    /*Нацеливание пушки на точку*/
    void Aim(float x, float y){
        Gun.SetAngle(X, Y, x, y);
    }

    /*Выстрел из орудия*/
    boolean Shoot(){
        try{
            if(Energy >= GunPowerNeed){
                Energy -= GunPowerNeed;
                /*вектор учитывает скорость и направление движения планеты-стрелка*/
                Vector ShotV = new Vector(P.angle, P.length / M).Plus(Gun);
                switch(GunType){
                    case 1: new Projectile(X + (float)(Math.cos(Gun.angle)) * Gun.length
                                         , Y + (float)(Math.sin(Gun.angle)) * Gun.length
                                         , 1, 1
                                         , ShotV.angle
                                         , ShotV.length, ALBaseClass);
                            break;
                    case 2: int bullets = 8;    /*Кол-во "дробинок"*/
                            for(int i = -(bullets / 2); i <= (bullets / 2); i++){
                                /**В первых двух параметрах Math.PI / i / 5
                                 * это смещение появляющихся снарядов
                                 * относительно дула - чтобы не столкнулись сразу
                                 */
                                (new Projectile(X + (float)(Math.cos(Gun.angle + (float)(Math.PI / i / 5))) * (Gun.length)
                                             , Y + (float)(Math.sin(Gun.angle + (float)(Math.PI / i / 5))) * (Gun.length)
                                             , 1, 1
                                             /*Math.PI / i / 12 - угол разлета снарядов*/
                                             , ShotV.angle + (float)(Math.PI / i / 12)
                                             , ShotV.length, ALBaseClass)).Transparent = 5;  /*Сначала пули "эфирные" - чтобы не столкнулись в стволе*/
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
    void Charge(){
        if(Energy < MaxEnergy){
            for(int i = 0; i < ALBaseClass.size(); i++){
                if(ALBaseClass.get(i) != null){
                    if(ALBaseClass.get(i).getClass().getName() == "main.Star"){
                        Energy += 1 / Distance(ALBaseClass.get(i)) * 100000/*1000*/;
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
    boolean SwitchGun(int gt){
        try{
            GunType = gt;
            switch(gt){
                case 1: GunPowerNeed = 200;
                        break;
                case 2: GunPowerNeed = 1000;
            }
            return true;
        }finally{
            return false;
        }
    }
    
    /*Взрыв - объект разлетается на куски*/
    void Explode(){
        int objCount = (int)sqrt(RO);   /*Кол-во осколков, на которые распадется планета*/
        int objSize = RO / objCount;    /*Размер осколков*/
        float objMass = objSize; /*Масса осколков*/
        float nx, ny;       /*Координаты появления осколка*/
        Vector dirbuff;         /*Вектор полета осколка*/
        Projectile projbuff;    /*Осколок*/
        /*Расстановка осколков внутри периметра планеты*/
        for(double i = 0; i < objCount; i++){
            nx = X + (float)(RO / 2 * Math.cos(i));
            ny = Y + (float)(RO / 2 * Math.sin(i));
            dirbuff = (new Vector().SetAngle(X, Y, nx, ny));
            projbuff = new Projectile(nx, ny, objMass, objSize, dirbuff.angle, 30, ALBaseClass);
            projbuff.Transparent = DeadSteps;   /*Временно делает осколок "эфирным", чтобы сразу не взорвался*/
        }
    }
    
    /*Обработка гибели объекта*/
    @Override
    void Die(){
        DeadFlag = true;
        Explode();
    }
}
