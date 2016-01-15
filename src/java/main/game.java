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
    private Planet player;                      /*Указатель на планету игрока*/
    
    @Override
    public void init() {
        addKeyListener(this);
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        this.setSize(800, 600);
        
        /*создаем новый игровой экран*/
        Display = new DrawPanel();
        add(Display);
        
        /*тестовые болванки НАЧАЛО*/
        player = new Planet((float)Math.random()*700, 190, 10, 5, 85, 5, ALBaseClass, true);
        new Planet((float)Math.random()*700, (float)Math.random()*500, 10, 5, 136, 5, ALBaseClass, false);
        new Planet((float)Math.random()*700, (float)Math.random()*500, 10, 5, 74, 5, ALBaseClass, false);       
        new Planet((float)Math.random()*700, (float)Math.random()*500, 1, 5, 8, 5, ALBaseClass, false);
        new Planet((float)Math.random()*700, (float)Math.random()*500, 1, 5, 267, 5, ALBaseClass, false);
        new Planet((float)Math.random()*700, (float)Math.random()*500, 1, 5, 307, 5, ALBaseClass, false);
        new Star(350, 250, 100000, 10, 0, 0, ALBaseClass);
        /*тестовые болванки КОНЕЦ*/
        
        /*таймер обновления мира*/
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            /*в ране описываются периодические действия*/
            @Override 
            public void run(){
                CM.CalcCenterMass();                            /*пересчет центра масс*/
                Display.AssignList(ALBaseClass);                /*передача игровому экрану списка объектов для отрисовки*/  
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    ALBaseClass.get(i).calc_F_ravn(CM);         /*пересчет импульса объекта*/
                    ALBaseClass.get(i).move();                  /*движение объекта*/
                }
            }
        };
        oTimer.schedule(oTimerTask, 0, 100);
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
        System.out.println(e.getKeyCode());
        if(e.getKeyCode() == 32){
            new Projectile(player.X, player.Y, 1, 1, player.P.angle, 5, ALBaseClass);
        }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
    }

}
