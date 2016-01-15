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
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Vladimir
 */

public class game extends Applet implements ActionListener, KeyListener {
    
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    private DrawPanel Display;                  /*Панель для отображения*/
    
    public void init() {
        addKeyListener(this);
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        this.setSize(800, 600);
        
        /*создаем новый игровой экран*/
        Display = new DrawPanel();
        add(Display);
        
        /*тестовые болванки НАЧАЛО*/
        new Planet(100, 190, 10, 5, 85, 5, ALBaseClass, true);
        new Planet(122, 280, 10, 5, 136, 5, ALBaseClass, false);
        new Planet(155, 110, 10, 5, 74, 5, ALBaseClass, false);       
        new Planet(250, 150, 1, 5, 8, 5, ALBaseClass, false);
        new Planet(77, 260, 1, 5, 267, 5, ALBaseClass, false);
        new Planet(220, 100, 1, 5, 307, 5, ALBaseClass, false);
        new Star(200, 200, 10000, 10, 0, 0, ALBaseClass);
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
    }
    
    @Override
    public void destroy(){
    }
    
    @Override 
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override 
    public void keyTyped(KeyEvent e) {
                CM.CalcCenterMass();                            /*пересчет центра масс*/
                Display.AssignList(ALBaseClass);                /*передача игровому экрану списка объектов для отрисовки*/  
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    ALBaseClass.get(i).calc_F_ravn(CM);         /*пересчет импульса объекта*/
                    ALBaseClass.get(i).move();                  /*движение объекта*/
                }
    }
    
    @Override 
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
