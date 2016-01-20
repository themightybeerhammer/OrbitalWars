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
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.cos;
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
    private float GunPowerCurrent = 0;      /*Заряд в пушке напокленный*/
    private float GunPowerNeed;         /*Заряд в пушке для выстрела*/
    
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
        this.GunPowerNeed = 20;
    }
    
    @Override
    void draw_in_scr(Graphics g, float x, float y, boolean v_F, boolean v_P ){
        RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHints(rh);
        g2.setColor(Color.WHITE);
        g2.draw(new Ellipse2D.Float(x - this.RO, y - this.RO, this.RO * 2, this.RO * 2));
        
        
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
     }
    
    /*Нацеливание пушки на точку*/
    void Aim(float x, float y){
        this.Gun.SetAngle(this.X, this.Y, x, y);
    }
    
    /*Выстрел из пушки*/
    boolean Shoot(ArrayList<BaseClass> AL){
        try{
            /*вектор учитывает скорость и направление движения планеты-стрелка*/
            Vector ShotV = new Vector(this.P.angle, this.P.length/this.M).Plus(this.Gun);
            new Projectile(this.X + (float)(Math.cos(this.Gun.angle)) * this.Gun.length
                         , this.Y + (float)(Math.sin(this.Gun.angle)) * this.Gun.length
                         , 1, 1
                         , ShotV.angle
                         , ShotV.length, AL);
            return true;   
        }finally{
            return false;
        }
    }
}
