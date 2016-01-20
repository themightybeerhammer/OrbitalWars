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
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Object;

/**
 *
 * @author Vladimir
 */

public class game extends Applet implements KeyListener, MouseListener, MouseMotionListener {
    
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    protected DrawPanel Display;                /*Панель для отображения*/
    public Planet player;
    public float DisplayX, DisplayY;            /*Координаты центра экрана*/
    public Point p_Delta, p_display;
    public boolean RMBPressed = false;          /*Нажата правая кнопка мыши*/
    boolean overChild;                          /*Буфер для перемещения по контейнерам*/
    float mx, my;                               /*Последние известные координаты мыши*/
    /*Параметры для дебугагинга начало*/
    public float Mltplr = 5f;                   /*Множитель замедления*/      
    public boolean v_F = false;                 /*Рисовать вектор равнодействующей*/
    public boolean v_P = false;                 /*Рисовать вектор импульса*/ 
    /*Параметры для дебугагинга конец*/
    
    @Override
    public void init() {
        this.ALBaseClass = new ArrayList<>();
        this.CM = new CenterMass(this.ALBaseClass);
        this.setSize(800, 600);
        this.p_Delta = new Point(0,0);
        this.p_display = new Point(0,0);
       
        /*тестовые болванки НАЧАЛО*/
        
        /*Одна звезда и 3 планеты*/
        this.player = new Planet(200, 30, 10, 10, 0, 78, this.ALBaseClass, true, true);
        this.player.dw_orbit = true;
        new Planet(100, 200, 10, 10, (float)Math.PI * 2 / 4, 100, this.ALBaseClass, false, false).dw_orbit = true;
        new Planet(200, 70, 10, 10, (float)Math.PI, 90, this.ALBaseClass, false, false).dw_orbit = true;    
        new Star(200, 200, 10000, 40, 0, 0, this.ALBaseClass);
        
        /*Система сиськи*/
      /*  player = new Planet(375, 375, 10, 10, (float)Math.PI*200/207,80, ALBaseClass, true, true);
        player.dw_orbit = true;
        new Star(250, 250, 8000, 40, 0, 0, ALBaseClass);
        new Star(500, 500, 8000, 40, 0, 0, ALBaseClass);
        */
      
      
         /*создаем новый игровой экран*/
        this.Display = new DrawPanel();
        add(this.Display);
        //Display.addMouseMotionListener(new MotionSensor(this));
        this.Display.addMouseMotionListener(this);
        addKeyListener(this);
        this.Display.addMouseListener(this);
        this.Display.addMouseMotionListener(this);
        
        for(int i = 0 ;i < this.ALBaseClass.size(); i++){
            this.ALBaseClass.get(i).calc_orbit();  /*Расчет орбит*/
        }
        /*тестовые болванки КОНЕЦ*/
        
        /*таймер обновления мира*/
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            /*в ране описываются периодические действия*/
            @Override 
            public void run(){
                
                player.Aim(mx - p_display.x, my - p_display.y);     /*постоянно нацеливаем орудие на последние координаты мыши*/
                CM.CalcCenterMass();                            /*пересчет центра масс*/
                Display.AssignList(ALBaseClass,p_display,v_F,v_P);        /*передача игровому экрану списка объектов для отрисовки*/ 
                for(int i = 0; i < ALBaseClass.size(); i++){
                    if(ALBaseClass.get(i) != null){
                        ALBaseClass.get(i).calc_F_ravn(Mltplr);     /*пересчет импульса объекта*/
                        ALBaseClass.get(i).move(Mltplr);            /*движение объекта*/
                    }
                }
                
                /*Обработка столкновения*/
                for(int i = 0 ;i < ALBaseClass.size(); i++){
                    for(int j = 0 ;j < ALBaseClass.size(); j++){
                        if((i!=j)&&(ALBaseClass.size()>i)
                                 &&(ALBaseClass.size()>j)
                                 &&(ALBaseClass.get(i)!=null)
                                 &&(ALBaseClass.get(j)!=null)){
                            float rr = (float)(Math.sqrt(Math.pow((ALBaseClass.get(i).X-ALBaseClass.get(j).X),2)+ Math.pow((ALBaseClass.get(i).Y-ALBaseClass.get(j).Y),2)));
                            
                            if(rr<(ALBaseClass.get(i).RO+ALBaseClass.get(j).RO)){
                                
                                if(ALBaseClass.get(i).getClass().getName()=="main.Projectile")
                                {ALBaseClass.get(i).DeadFlag=true;}
                                if(ALBaseClass.get(j).getClass().getName()=="main.Projectile")
                                {ALBaseClass.get(j).DeadFlag=true;}
                            
                            }
                        }
                    }  
                }
                
               /*Удаление погибших объектов*/ 
               int j=0; 
               do{
                  if((ALBaseClass.get(j).DeadFlag)&&(ALBaseClass.get(j).DeadSteps<=0)){
                     ALBaseClass.remove(j);
                  }else{
                   j++;
                  }
                   
               }while(j<ALBaseClass.size());
               
                
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
        /*тест стрельбы по клавише ПРОБЕЛ*/
        if(e.getKeyCode() == 32){
            this.player.Shoot(this.ALBaseClass);
        }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            this.player.Shoot(this.ALBaseClass);
        }
        
        if(e.getButton() == 3){
            this.RMBPressed = true;
            this.p_Delta  = e.getPoint();  
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       if(RMBPressed){
         RMBPressed = false;
       }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
            if(this.RMBPressed){
              this.p_display.x = this.p_display.x + (e.getPoint().x - this.p_Delta.x);
              this.p_display.y = this.p_display.y + (e.getPoint().y - this.p_Delta.y);
              this.p_Delta = e.getPoint();
           }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        Rectangle r = this.Display.getBounds();
        if(r.contains(p)){
            if(!this.overChild)
                this.overChild = true;
            this.mx = p.x - r.x;
            this.my = p.y - r.y;
        }
        else if(this.overChild){
            this.overChild = false;
        }
    }

}