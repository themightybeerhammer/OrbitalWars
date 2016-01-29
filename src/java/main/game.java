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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Object;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Vladimir
 */

public class game extends Applet implements KeyListener, MouseListener, MouseMotionListener {
    
    public final byte FPS = 20;                /*Частота обновлений*/
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    protected DrawPanel Display;                /*Панель для отображения*/
    public Planet player;
    public Point2D p_Delta                        /*Изменение координат при нажатии правой кнопки мыши  */
               , p_display;                     /*Координаты центра экрана*/
    public int DisplayW = 800;                  /*Ширина экрана*/
    public int DisplayH = 600;                  /*Высота экрана*/
    public boolean RMBPressed = false;          /*Нажата правая кнопка мыши*/
    boolean overChild;                          /*Буфер для перемещения по контейнерам*/
    float mx, my;                               /*Последние известные координаты мыши*/
    public boolean ScrFlwPlr=false;             /*Экран следует за планетой игрока*/     
    public int un_end_X=1000,un_end_Y=1000;     /*Края вселеной вылетая за которые объект погибает*/
    public boolean pause = false;
    public boolean gamestarted = false;
    
    /*Параметры для дебугагинга начало*/
    public float Mltplr = 400f / (1000 / FPS);                   /*Множитель замедления*/      
    public boolean v_F = false;                 /*Рисовать вектор равнодействующей*/
    public boolean v_P = false;                 /*Рисовать вектор импульса*/ 
    /*Параметры для дебугагинга конец*/
    
    @Override
    public void init() {
        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseListener(this);
        JButton b_startgame = new JButton();
        add(b_startgame);
        b_startgame.setAction(new Action() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gamestarted = true;
                pause = false;
                b_startgame.setVisible(false);
                StartGame();
            }

            @Override
            public boolean isEnabled() {
                return !gamestarted;
            }
            
            @Override
            public Object getValue(String key) {
                return null;
            }

            
            
            @Override
            public void setEnabled(boolean b) {
            }
            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {
            }
            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {
            }
            @Override
            public void putValue(String key, Object value) {
            }
        });
        b_startgame.setText("Start game");
    }
    
    /*Инициализация игровой среды*/
    private void StartGame(){
        ALBaseClass = new ArrayList<>();
        CM = new CenterMass(ALBaseClass);
        setSize(DisplayW, DisplayH);
        p_Delta = new Point2D.Double(0,0);
        p_display = new Point2D.Double(DisplayW / 2, DisplayH / 2);
        
        /*тестовые болванки НАЧАЛО*/
        
        /*Одна звезда и 3 планеты*/
        new Star(0, 0, 100000, 150, 0, 0, ALBaseClass);
        
        player = new Planet(0, -400, 30, 10, 0, 480, ALBaseClass, true, true);
        new Planet(0, -450, 50, 10, 0, 745, ALBaseClass, false, true);
        /*new Planet(400, 400, 30, 10, (float)Math.PI *3/ 4, 395, ALBaseClass, false, false);
        new Planet(-350, -350, 30, 10, -(float)Math.PI / 4, 425, ALBaseClass, false, false);
        new Planet(-325, 325, 30, 10, (float)Math.PI*5/4, 440, ALBaseClass, false, false);
        new Planet(-250, -250, 30, 10, -(float)Math.PI/4, 510, ALBaseClass, false, false);
        new Planet(-250, 0, 30, 10, (float)Math.PI / 2, 600, ALBaseClass, false, false);
        */
        
        /*Система сиськи*/
      /*  player = new Planet(375, 375, 10, 10, (float)Math.PI*200/207,80, ALBaseClass, true, true);
        player.dw_orbit = true;
        new Star(250, 250, 8000, 40, 0, 0, ALBaseClass);
        new Star(500, 500, 8000, 40, 0, 0, ALBaseClass);
        */
      
      
         /*создаем новый игровой экран*/
        Display = new DrawPanel((int)(DisplayW), (int)(DisplayH));
        add(Display);
        Display.addMouseListener(this);
        Display.addMouseMotionListener(this);
        Display.setAlignmentX(0);
        /*тестовые болванки КОНЕЦ*/
        
        /*таймер обновления мира*/
        Timer oTimer = new Timer();
        TimerTask oTimerTask = new TimerTask(){
            /*в ране описываются периодические действия*/
            @Override 
            public void run(){
                    if(gamestarted && !pause ){
                    player.Aim((mx - p_display.getX()),( my - p_display.getY()));     /*постоянно нацеливаем орудие на последние координаты мыши*/
                    CM.CalcCenterMass();                                /*пересчет центра масс*/
                    Display.AssignList(ALBaseClass,player,p_display,v_F,v_P);  /*передача игровому экрану списка объектов для отрисовки*/ 
                    /*Перерасчет импульса объекта, движение объекта и удаление вылетивших за пределы*/
                    for(int i = 0; i < ALBaseClass.size(); i++){
                        if(ALBaseClass.get(i) != null){
                              /*пересчет импульса объекта*/
                            ALBaseClass.get(i).calc_F_ravn(Mltplr);   
                            /*движение объекта*/
                            ALBaseClass.get(i).move(Mltplr);   
                            /*Проверка что объект в приделах системы иначе СМЕРТЬ!!! ХА-ХА-ХА!!!!!!!*/
                            if ((ALBaseClass.get(i).X>un_end_X)
                               |(ALBaseClass.get(i).X<-un_end_X)
                               |(ALBaseClass.get(i).Y<-un_end_Y)
                               |(ALBaseClass.get(i).Y>un_end_Y))
                            {ALBaseClass.get(i).DeadFlag=true;}
                            if(ALBaseClass.get(i).getClass().getName() == "main.Planet"){
                                ((Planet)ALBaseClass.get(i)).Charge();  /*Подзарядка планеты*/
                                ((Planet)ALBaseClass.get(i)).Reload(1000 / FPS);  /*Перезарядка орудия*/
                            }
                        }
                    }
                    /*Следование экрана за объектом*/
                    if(ScrFlwPlr){
                          /*End - возрат камеры к планете игрока*/
                        /*p_display.x = DisplayW / 2-(int)Math.floor(player.X);  
                        p_display.y = DisplayH / 2-(int)Math.floor(player.Y);*/
                        double __x = (double)(DisplayW / 2-player.X);
                        double __y = (double)(DisplayH / 2-player.Y);

                        //System.out.println("X="+__x+"Y="+__y);
                        p_display.setLocation(__x,__y);

                    }        
                    /*Обработка столкновения*/
                    for(int i = 0 ;i < ALBaseClass.size(); i++){
                        for(int j = 0 ;j < ALBaseClass.size(); j++){
                            if((i!=j)&&(ALBaseClass.size()>i)
                                     &&(ALBaseClass.size()>j)
                                     &&(ALBaseClass.get(i)!=null)
                                     &&(ALBaseClass.get(j)!=null)
                                     &&(ALBaseClass.get(j).getClass().getName()!="main.CenterMass")
                                     &&(ALBaseClass.get(i).getClass().getName()!="main.CenterMass")
                                     &&(ALBaseClass.get(i).DeadFlag==false)
                                     &&(ALBaseClass.get(j).DeadFlag==false)
                                     &&(ALBaseClass.get(i).Transparent==0)
                                     &&(ALBaseClass.get(j).Transparent==0)
                                    ){
                                float rr = (float)(Math.sqrt(Math.pow((ALBaseClass.get(i).X-ALBaseClass.get(j).X),2)+ Math.pow((ALBaseClass.get(i).Y-ALBaseClass.get(j).Y),2)));

                                if(rr<(ALBaseClass.get(i).RO+ALBaseClass.get(j).RO)){

                                    ALBaseClass.get(i).HealthCur=(int)(ALBaseClass.get(i).HealthCur -  ALBaseClass.get(j).M);
                                    ALBaseClass.get(j).HealthCur=(int)(ALBaseClass.get(j).HealthCur -  ALBaseClass.get(i).M);

                                    if( ALBaseClass.get(i).HealthCur<=0) ALBaseClass.get(i).Die();//.DeadFlag=true;
                                    if( ALBaseClass.get(j).HealthCur<=0) ALBaseClass.get(j).Die();//.DeadFlag=true;
                                    /*if(ALBaseClass.get(i).getClass().getName()=="main.Projectile")
                                    {ALBaseClass.get(i).DeadFlag=true;}
                                    if(ALBaseClass.get(j).getClass().getName()=="main.Projectile")
                                    {ALBaseClass.get(j).DeadFlag=true;}*/

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

                    //System.out.println("---------------------------");
                    /*for(int i = 0; i < ALBaseClass.size(); i++){
                        if((i < ALBaseClass.size()) && (ALBaseClass.get(i) != null)){
                            if((ALBaseClass.get(i).F !=null) && (ALBaseClass.get(i).P != null))
                                System.out.print(ALBaseClass.get(i).F.length + " " + ALBaseClass.get(i).P.length + " | ");
                            ALBaseClass.get(i).calc_orbit();  
                        }
                    }*/

                }
            }
        };
        oTimer.schedule(oTimerTask, 0, 1000 / FPS);
        setFocusable(true);
        requestFocusInWindow();
    }
    
    /*Генерация случайной кометы на краю системы*/
    public void SendNewComet(){
        int NewRO;
        NewRO = (int)max(1, (random() * 3));
        double NewM;
        NewM = NewRO;
        /*Определяет с какой границы системы создать комету*/
        int SpawnSeed = (int)round(random() * 100);
        double NX = 1000, NY = 1000;
        if(SpawnSeed <= 50){
            NX = random() * un_end_X * signum(random() * 2 - 1);
            NY -= NewRO * signum(NX);
        }else{
            NY = random() * un_end_Y * signum(random() * 2 - 1);
            NX -= NewRO * signum(NY);
        }
        double NewDist;
        NewDist = sqrt(pow(NX, 2) + pow(NY, 2));
        /*Поиск радиуса системы для нацеливания кометы*/
        double MaxDist = 0; /*Расстояние до самой дальней планеты*/
        for(BaseClass AL : ALBaseClass){
            if(AL != null)
                if("main.Planet".equals(AL.getClass().getName()))
                    MaxDist = max(MaxDist, sqrt(pow(AL.X, 2) + pow(AL.Y, 2)));
        }
        /*Непосредственно создается комета, направляется и просчитывается*/
        Comet NewComet;
        NewComet = new Comet(NX, NY, NewM, NewRO, (new Vector().SetAngle(NX, NY, (random() * 2 - 1) * MaxDist, (random() * 2 - 1) * MaxDist)).angle /** ((random() - 0.5) / 3.14)*/, NewDist, ALBaseClass);
        NewComet.P.length = random() * NewM * 50;
        NewComet.calc_F_ravn(Mltplr);
        for(BaseClass AL : ALBaseClass)if("main.Planet".equals(AL.getClass().getName()))AL.calc_orbit();
        /*Коммент в консоли о создании кометы с основной инфой*/
        System.out.println(new StringBuilder()
                .append("Created new comet:")
                .append(" M=").append(NewComet.M)
                .append(" R=").append(NewComet.RO)
                .append(" X=").append(NewComet.X)
                .append(" Y=").append(NewComet.Y)
                .append(" angle(P)=").append(NewComet.P.angle)
                .append(" length(P)=").append(NewComet.P.length)
                .append(" angle(F)=").append(NewComet.F.angle)
                .append(" length(F)=").append(NewComet.F.length)
                .toString());
    }
    
    /*Добавляет новую планету в уже существующую систему*/
    public void SendNewPlanet(){
        double MaxDist = 0; /*Расстояние до самой дальней планеты*/
        double NewM;
        int NewRO;
        int MaxRO = 0;
        double NewDist;
        double StarMass = 0;
        double StarRO = 0;
        Planet NewPlanet;
        for(BaseClass AL : ALBaseClass){
            if(AL != null) 
            if("main.Planet".equals(AL.getClass().getName())){
                MaxDist = max(MaxDist, sqrt(pow(AL.X, 2) + pow(AL.Y, 2)));
                if(MaxDist == sqrt(pow(AL.X, 2) + pow(AL.Y, 2)))MaxRO = AL.RO;  /*смотрим на радиус самой дальней планеты - что не наступить на нее*/
            }else if("main.Star".equals(AL.getClass().getName())){
                StarMass += AL.M;
                StarRO = max(StarRO, AL.RO);
            }
        }
        NewM = random() * 50;
        NewRO = (int)max(5, (random() * 50));
        NewDist = max(StarRO * 1.5, (MaxDist + NewRO * 2 * (random() + 1) + MaxRO * 2) * signum(random() - 1));
        NewPlanet = new Planet(0, NewDist, NewM, NewRO, PI * (round(random() * 2) / 2), NewDist, ALBaseClass, false, false);
        NewPlanet.calc_F_ravn(Mltplr);
        NewPlanet.P.length = NewPlanet.M * sqrt(StarMass / abs(NewDist));
        System.out.println(new StringBuilder()
                .append("Created new planet '")
                .append(NewPlanet.Name)
                .append("' :")
                .append(" M=").append(NewPlanet.M)
                .append(" R=").append(NewPlanet.RO)
                .append(" X=").append(0)
                .append(" Y=").append(NewDist)
                .append(" angle(P)=").append(NewPlanet.P.angle)
                .append(" length(P)=").append(NewPlanet.P.length)
                .append(" angle(F)=").append(NewPlanet.F.angle)
                .append(" length(F)=").append(NewPlanet.F.length)
                .toString());
        for(BaseClass AL : ALBaseClass)if("main.Planet".equals(AL.getClass().getName()))AL.calc_orbit();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(gamestarted){ /*игра запущена и не на паузе*/
            switch(e.getKeyCode()){
                case 19:    pause = !pause; /*кнопка pause вкл/выкл паузу*/
                            System.out.println(pause);
                            break;
                case 32:    if(player.Shoot())  /*тест стрельбы по клавише ПРОБЕЛ*/
                            System.out.println(new StringBuilder().append("Player shoot!").toString());
                            break;
                case 35:    if(ScrFlwPlr){ScrFlwPlr = false;}else{ScrFlwPlr = true;} 
                            break;
                case 36:    /*Home - возрат камеры к центру системы*/
                            /*p_display.x = DisplayW / 2; 
                            p_display.y = DisplayH / 2;*/
                            p_display.setLocation(DisplayW / 2,DisplayH / 2);
                            break;
                /*клавиши 1-9 для переключения оружия*/
                case 49:    player.GunType = 1;
                            System.out.println(new StringBuilder().append("Player switch weapon to 1").toString());
                            break;
                case 50:    player.GunType = 2;
                            System.out.println(new StringBuilder().append("Player switch weapon to 2").toString());
                            break;
                case 51:    player.GunType = 3;
                            System.out.println(new StringBuilder().append("Player switch weapon to 3").toString());
                            break;
                case 112:   SendNewPlanet();    /*F1 - генерация новой планеты*/
                            break;
                case 113:   SendNewComet();     /*F2 - генерация новой кометы*/
                            break;
                default: System.err.println(new StringBuilder()
                        .append("Keycode ")
                        .append(e.getKeyCode())
                        .append(" not yet implemented!")
                        .toString());
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(gamestarted && !pause ){
            Point p = e.getPoint();
            Rectangle r = Display.getBounds();
            if(r.contains(p)){
                if(!overChild)
                    overChild = true;
                mx = p.x - r.x + 16;
                my = p.y - r.y + 16;
            }
            else if(overChild){
                overChild = false;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(e.getButton()){
            case 1: if(gamestarted && !pause)player.Shoot();
                    break;
            case 3: RMBPressed = true;
                    p_Delta  = e.getPoint();
                    break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
            if(RMBPressed){
              /*p_display.x = p_display.x + (e.getPoint().x - p_Delta.x);
              p_display.y = p_display.y + (e.getPoint().y - p_Delta.y);*/
              p_display.setLocation(p_display.getX() + (e.getPoint().x - p_Delta.getX()),p_display.getY() + (e.getPoint().y - p_Delta.getY()));
              p_Delta = e.getPoint();
           }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       if(RMBPressed){
         RMBPressed = false;
       }
    }
    
    
    /*Подвал для пустых методов*/
    @Override
    public void destroy(){
    }
    @Override 
    public void keyTyped(KeyEvent e) {
    }
    @Override 
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

}
