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
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author Vladimir
 */
/** Центр солнечной системы
 */

public class Star extends BaseClass {
    
    /*Конструкторы класса*/
    Star(){
        super();
    } 
    Star(float x, float y, float m, int ro, float vangle, float vlength, ArrayList<BaseClass> AL){
        super(x, y, m, ro, vangle, vlength, AL);
        HealthMax = 100;    /*Максимальное здоровье*/
        HealthCur = HealthMax;
        DeadFlag = false;
        DeadSteps=30; 
    }
    
    @Override
    void move(float Mtplr){
          if(DeadFlag){
           DeadSteps--;  
           if(DeadSteps<0) DeadSteps = 0;}
    }
    
    @Override
    void draw_in_scr(Graphics g
                    ,float x
                    ,float y
                    ,boolean v_F  
                    ,boolean v_P ){
        Graphics2D g2 = (Graphics2D)g;
        Point2D center = new Point2D.Float(x, y);
        float radius = RO;
        float rr = (float)(Math.random()*4+20)/100;
        float[] dist = {rr, 0.6f, 1.0f};
        Color[] colors = {Color.RED, Color.YELLOW, new Color(1,0,0,0) };
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        g2.setPaint(p);
        if(DeadFlag==false){
        g2.fill(new Ellipse2D.Float(x-RO, y-RO, RO*2, RO*2));
        }
        
         /*Прорисовка гибели объекта*/
         if(DeadFlag){
         
             Point2D _center = new Point2D.Float(x, y); 
             float _radius = RO*((float)DeadSteps/30)+2;
             if(_radius<=0) _radius = 1;
            
             float[] _dist = { 0.6f, 1.0f};
             Color[] _colors = { Color.YELLOW, new Color(1,0,0,0) };
             RadialGradientPaint _p = new RadialGradientPaint(_center, _radius, _dist, _colors);
             g2.setPaint(_p);
             g2.fill(new Ellipse2D.Float(x-radius, y-radius, radius*2, radius*2));
         }
          
        /*RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.setColor(Color.YELLOW);
        g2.draw(new Ellipse2D.Float(x-RO, y-RO, RO*2, RO*2));*/
         
        // g2.drawOval((int)x-RO, (int)y-RO, RO*2, RO*2);

        /*Направление равнодействующей*/
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
