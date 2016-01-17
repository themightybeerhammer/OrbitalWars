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

import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Object;

/**
 *
 * @author Vladimir
 */

public class game extends Applet implements KeyListener {
    
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    private DrawPanel Display;                  /*Панель для отображения*/
    public float Mltplr ;     /*Множитель замедления*/      
    public boolean v_F;       /*Рисовать вектор равнодействующей*/
    public boolean v_P;       /*Рисовать вектор импульса*/ 
    public Planet player; 
    
    @Override
    public void init() {
        /*Параметры для дебугагинга начало*/
        Mltplr = 3f;
        v_F=false;       
        v_P=false; 
        /*Параметры для дебугагинга конец*/
        addKeyListener(this);
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        this.setSize(800, 600);
        
        /*создаем новый игровой экран*/
        Display = new DrawPanel();
        add(Display);
        
        /*тестовые болванки НАЧАЛО*/
        player = new Planet(200,30 ,10,10, 0,78,ALBaseClass, true);
        player.dw_orbit = true;
        
     
          
        new BaseClass(100,200 ,10,10, (float)Math.PI*2/4,100,ALBaseClass).dw_orbit=true;
        new BaseClass(200,70 ,10,10, (float)Math.PI,90,ALBaseClass).dw_orbit=true;    
        
        new Star(200, 200, 10000, 40, 0, 0, ALBaseClass);
        
          for(int i = 0 ;i < ALBaseClass.size(); i++){
            ALBaseClass.get(i).calc_orbit();  /*Расчет орбит*/
         }            
        
       
        /*тестовые болванки КОНЕЦ*/
        
        /*таймер обновления мира*/
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            /*в ране описываются периодические действия*/
            @Override 
            public void run(){
                CM.CalcCenterMass();                            /*пересчет центра масс*/
                Display.AssignList(ALBaseClass,v_F,v_P);        /*передача игровому экрану списка объектов для отрисовки*/  
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    
                    ALBaseClass.get(i).calc_F_ravn(Mltplr);         /*пересчет импульса объекта*/
                    ALBaseClass.get(i).move(Mltplr);                  /*движение объекта*/
                }
            }
        };
        oTimer.schedule(oTimerTask, 0, 50);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    
    @Override
    public void destroy(){
    }
    
    @Override 
    public void keyTyped(KeyEvent e) {
    }
    
    @Override 
    public void keyPressed(KeyEvent e) {
        
        //System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 32){
            
            float x = player.X+(float)(Math.cos(player.P.angle)*player.P.length/player.M);
            float y = player.Y+(float)(Math.sin(player.P.angle)*player.P.length/player.M);
            new Projectile(x, y, 1, 1, player.P.angle, 5, ALBaseClass);
        }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
    }

}
