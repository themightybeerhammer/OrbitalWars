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
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.Math.signum;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Object;
import java.util.Random;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Vladimir
 */
public class game extends Applet implements KeyListener, MouseListener, MouseMotionListener {
    
    public final byte FPS = 20;                 /*Частота обновлений*/
    private ArrayList<BaseClass> ALBaseClass;   /*Коллекция всех объектов*/
    private CenterMass CM;                      /*Центр масс*/ 
    protected DrawPanel Display;                /*Панель для отображения*/
    public Planet player;
    public Point2D p_Delta                      /*Изменение координат при нажатии правой кнопки мыши  */
               , p_display;                     /*Координаты центра экрана*/
    public int DisplayW = 800;                  /*Ширина экрана*/
    public int DisplayH = 600;                  /*Высота экрана*/
    private boolean RMBPressed = false;          /*Нажата правая кнопка мыши*/
    boolean overChild;                          /*Буфер для перемещения по контейнерам*/
    private float mx, my;                               /*Последние известные координаты мыши*/
    public boolean ScrFlwPlr=false;             /*Экран следует за планетой игрока*/     
    public int un_end_X=1000,un_end_Y=1000;     /*Края вселеной вылетая за которые объект погибает*/
    public boolean pause = false;
    public boolean gamestarted = false;
    public int gameend = 0;                     /*Флаг окончания игры 1 - победа, -1 - поражение*/
    public int GoNextLevel=0
              ,CurLevel = 6;
    
        
    private int keymod = 0;
    
    /*Параметры для дебугагинга начало*/
    public float Mltplr = 400f / (1000 / FPS);  /*Множитель замедления*/      
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
    
    /**Инициализация игровой среды*/
    private void StartGame(){
        pause = true;
        gameend = 0;
    
        
        GenerateLevel(CurLevel);
        
        /*тестовые болванки НАЧАЛО*/
        
        /*Одна звезда и 3 планеты*/
        
        /*Star star =  new Star(0, 0, 100000, 150, 0, 0, ALBaseClass);
        
        player = new Planet(star, 400 ,0, 50, 10, Math.PI, 1000, ALBaseClass, true, true);
        (new Planet(player, 40 ,Math.PI, 50, 5, Math.PI, 480, ALBaseClass, false, false)).dw_orbit=false;
        
         
        new Planet(star, 200 ,Math.PI*3/4, 50, 10, 0, 500, ALBaseClass, true, true);
        new Planet(star, 250 ,Math.PI*2/4, 50, 10, 0, 500, ALBaseClass, true, true);
        new Planet(star, 300 ,Math.PI*3/4, 50, 10, 0, 500, ALBaseClass, true, true);
        new Planet(star, 350 ,Math.PI*2/4, 50, 10, Math.PI, 500, ALBaseClass, true, true);*/
       
       
        
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
                    if(/*gamestarted && !pause &&*/ player!=null)
                    {
                    if(GoNextLevel>0)GoNextLevel--;
                    if((GoNextLevel==0)&(gameend==1)){ CurLevel++; GenerateLevel(CurLevel); }
                    if(player.IsPlayer){player.Aim((mx - p_display.getX()),( my - p_display.getY()));}     /*постоянно нацеливаем орудие на последние координаты мыши*/
                    CM.CalcCenterMass();                                /*пересчет центра масс*/
                    Display.AssignList(ALBaseClass
                                      ,player
                                      ,p_display
                                      ,v_F
                                      ,v_P
                                      ,game.this);  /*передача игровому экрану списка объектов для отрисовки*/ 
                    /*Выполнение действий искуственного интелекта*/
                    for(int i = 0; i < ALBaseClass.size(); i++){
                        if((ALBaseClass.get(i) != null)&(ALBaseClass.get(i) != player)){
                            ALBaseClass.get(i).AI_do();
                        }
                    }
                    
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
                        double __x = (double)(DisplayW / 2-player.X);
                        double __y = (double)(DisplayH / 2-player.Y);
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
                                    
                                    if(ALBaseClass.get(i).getClass().getName()=="main.Roket"){
                                        ALBaseClass.get(i).HealthCur=(int)(ALBaseClass.get(i).HealthCur -  ALBaseClass.get(j).M);
                                        ALBaseClass.get(j).HealthCur=(int)(ALBaseClass.get(j).HealthCur -  10);
                                    }else{
                                        ALBaseClass.get(i).HealthCur=(int)(ALBaseClass.get(i).HealthCur -  ALBaseClass.get(j).M);
                                        ALBaseClass.get(j).HealthCur=(int)(ALBaseClass.get(j).HealthCur -  ALBaseClass.get(i).M);
                                    }        
                                    if( ALBaseClass.get(i).HealthCur<=0) ALBaseClass.get(i).Die();
                                    if( ALBaseClass.get(j).HealthCur<=0) ALBaseClass.get(j).Die();
                                }
                                
                                /*Обработка облета ракетой препятствий*/
                                if((ALBaseClass.get(i).getClass().getName()=="main.Roket")
                                &&((ALBaseClass.get(i).RO*5+ALBaseClass.get(j).RO)>rr)){
                                       ALBaseClass.get(i).setHedge(ALBaseClass.get(j));
                                }
                                
                                /*Обработка приблежающихся к планете объектов*/
                                if((ALBaseClass.get(i).getClass().getName()=="main.Planet")
                                 &((ALBaseClass.get(i).RO*5+ALBaseClass.get(j).RO)>rr)
                                 &((ALBaseClass.get(i)!=player))){
                                    /*Направленение от объекта к текущей планете*/
                                    BaseClass object = ALBaseClass.get(j);
                                    Planet planet = (Planet) ALBaseClass.get(i);
                                    if(planet.IsPlayer){
                                        
                                        double _angle = Vector.SetAngleD(object.X, object.Y, planet.X, planet.Y);
                                        if((Vector.AngleDiff(_angle, object.P.angle))<Math.PI/2){
                                          planet.setHedge(object);
                                        }
                                    }
                                }
                                
                               /*Обработка взрыва гранаты при приближения к ней планеты или звезды*/
                                 if((ALBaseClass.get(i).getClass().getName()=="main.Grenede")
                                 &&((50+ALBaseClass.get(j).RO)>rr)
                                 &&(ALBaseClass.get(i).Transparent==0)){
                                     if((ALBaseClass.get(j).getClass().getName() =="main.Planet")
                                       |(ALBaseClass.get(j).getClass().getName() =="main.Star")){
                                       ALBaseClass.get(i).Explode();
                                     }
                                     
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
                    checkEndgame();

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
        gamestarted = true;
        pause = false;
    }
    
    /**Генерация случайной кометы на краю системы*/
    public Comet SendNewComet(){
        int NewRO;
        NewRO = (int)max(1, (random() * 3));
        double NewM;
        NewM = NewRO;
        /*Определяет с какой границы системы создать комету*/
        int SpawnSeed = (int)round(random() * 100);
        double NX = un_end_X, NY = un_end_Y;
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
        return NewComet;
    }
    
    /**Добавляет новую планету в уже существующую систему*/
    public Planet SendNewPlanet(){
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
                MaxDist = max(MaxDist, max(100, StarRO * 1.5));
            }
        }
        NewRO = (int)max(5, StarRO / 2);
        NewM = max(10, min(StarMass / 10, random() * 10 * NewRO));
        NewDist = max(StarRO * 1.5, (MaxDist + NewRO * 2 * (random() + 1) + MaxRO * 2)) * signum(random() - 0.5);
        NewPlanet = new Planet(0
                            , NewDist
                            , NewM
                            , NewRO
                            , PI * (round(random() * 2) / 2)
                            , NewDist
                            , ALBaseClass
                            , false //true
                            , false //true
                            );
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
        un_end_X = (int)MaxDist * 2;
        un_end_Y = (int)MaxDist * 2;
        return NewPlanet;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case 17:    keymod += 1;    /*Зажата CTRL*/
                        break;
        }
        if(gamestarted){ /*игра запущена и не на паузе*/
            switch(e.getKeyCode()){
                case 19:    pause = !pause; /*кнопка pause вкл/выкл паузу*/
                            //System.out.println(pause);
                            break;
                case 32:    player.Shoot();  /*тест стрельбы по клавише ПРОБЕЛ*/
                            break;
                case 35:    if(ScrFlwPlr){ScrFlwPlr = false;}else{ScrFlwPlr = true;} 
                            break;
                case 36:    /*Home - возрат камеры к центру системы*/
                            /*p_display.x = DisplayW / 2; 
                            p_display.y = DisplayH / 2;*/
                            p_display.setLocation(DisplayW / 2,DisplayH / 2);
                            break;
                /*клавиши 1-9 для переключения оружия*/
                case 49:    player.SwitchGun(1);
                            System.out.println(new StringBuilder().append("Player switch weapon to 1").toString());
                            break;
                case 50:    player.SwitchGun(2);
                            System.out.println(new StringBuilder().append("Player switch weapon to 2").toString());
                            break;
                case 51:    player.SwitchGun(3);
                            System.out.println(new StringBuilder().append("Player switch weapon to 3").toString());
                            break;
                case 52:    player.SwitchGun(4);
                            System.out.println(new StringBuilder().append("Player switch weapon to 4").toString());
                            break;            
                case 112:   GenerateLevel(CurLevel);
                            //if((keymod & 1) != 0){
                            //    GenerateLevel(1);        /*CTRL + F1 - рестарт*/
                            //}else{
                            //    SendNewPlanet();    /*F1 - генерация новой планеты*/
                            //}
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
    
    /**Генератор уровня*/
    private void GenerateLevel(int diff){
        /*int MaxPlanets = max(2, (int)sqrt(diff));
        Planet planet;
        Star star = new Star(0, 0, random() * max(100000, pow(diff, 2)), (int)max(max(20, diff), (random() * 2 * diff)), 0, 0, ALBaseClass);
        double MaxDist = 0;
        for(int i = 1; i <= MaxPlanets; i++){
            planet = SendNewPlanet();
            MaxDist = max(MaxDist, sqrt(pow(planet.X, 2) + pow(planet.Y, 2)));
        }
        for(BaseClass AL : ALBaseClass)
            if(AL.getClass().getName() == "main.Planet"){
                if(player == null)
                    if(sqrt(pow(AL.X, 2) + pow(AL.Y, 2)) >= min(250d, MaxDist)){
                        player = (Planet)AL;
                        player.IsPlayer = true;
                        player.GiveGun();
                        System.out.println("Player's planet set!");
                    }
            }
        */
        gameend = 0;
        if(ALBaseClass == null){
            ALBaseClass = new ArrayList<>();
        }else{
            ALBaseClass.removeAll(ALBaseClass);
        }
        CM = new CenterMass(ALBaseClass);
        setSize(DisplayW, DisplayH);
        p_Delta = new Point2D.Double(0,0);
        if(p_display==null) p_display= new Point2D.Double(DisplayW / 2, DisplayH / 2);else p_display.setLocation(DisplayW / 2, DisplayH / 2);
        player = null;
        
        
        if(diff<1) diff=1;
        diff++;
        Star star = new Star(0, 0, 100000, 100, 0, 0, ALBaseClass);
        Random random = new Random();
        int NumPlayer =0;
        NumPlayer=random.nextInt(diff);
        for(int i=0;i<diff;i++){
            double OrbitPosition = random.nextInt(36)+1; OrbitPosition = Math.PI*2*OrbitPosition/36;
            
            Planet planet =new Planet(
                                    star                                       /*Ведущий объект*/
                                   ,200+i*50                                   /*Растояние от ведущего объекта*/
                                   ,OrbitPosition                              /*Положение на орбите*/ 
                                   ,50+random.nextInt(diff)*25                                         /*Масса*/
                                   ,10+random.nextInt(diff)                                         /*Радиус*/
                                   ,Math.PI*random.nextInt(2)                  /*Направление Math.PI- по часовой;>Math.PI против часовой*/
                                   ,200*diff                                   /*Импульс движения*/
                                   ,ALBaseClass, true,true);
            planet.NameColor=Color.RED;
            planet.MaxEnergy = planet.MaxEnergy+random.nextInt(diff)*1000;
            planet.AI_level = diff;
            /*Спутники после 6 уровня*/
            if((diff>6)&(random.nextInt(2)==1)){
                Planet sputnik = new Planet(
                                    planet                                     /*Ведущий объект*/
                                   ,planet.RO*1.2+5                            /*Растояние от ведущего объекта*/
                                   ,OrbitPosition                              /*Положение на орбите*/ 
                                   ,25+random.nextInt(diff)*10                 /*Масса*/
                                   ,5                                          /*Радиус*/
                                   ,Math.PI*random.nextInt(2)                  /*Направление Math.PI- по часовой;>Math.PI против часовой*/
                                   ,150                                   /*Импульс движения*/
                                   ,ALBaseClass, false,false);
                sputnik.dw_orbit = false;
                sputnik.dw_name  = false;
                
                
            }
            if(i==NumPlayer){
                player = planet;
                player.NameColor=Color.GREEN;
            }
            
            
        }
        
        
        
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if(gamestarted && !pause ){
            Point p = e.getPoint();
            Rectangle r = Display.getBounds();
            if(r.contains(p)){
                mx = p.x - r.x + 16;
                my = p.y - r.y + 16;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(e.getButton()){
            case 1: /*if(gamestarted && !pause)*/player.Shoot();
                    break;
            case 3: RMBPressed = true;
                    p_Delta  = e.getPoint();
                    break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
            if(RMBPressed){
              p_display.setLocation(p_display.getX() + (e.getPoint().x - p_Delta.getX()),p_display.getY() + (e.getPoint().y - p_Delta.getY()));
              p_Delta = e.getPoint();
           }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case 17:    keymod -= 1; /*Отжата CTRL*/
                        break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       if(RMBPressed){
         RMBPressed = false;
       }
    }
    
    public boolean checkEndgame(){
        int pcount = 0;
        if((GoNextLevel==0)&&(gameend==0)){
        for(int i = 0; i < ALBaseClass.size(); i++)
            if((ALBaseClass.get(i).getClass().getName() == "main.Planet")
              &&(ALBaseClass.get(i).LeadingObject!=player))
                pcount++;
      //  System.out.println(pcount);
        if(pcount <= 1){
            if(player.HealthCur <= 0){
                gameend = -1;
            }else{
                gameend = 1;
                GoNextLevel=150;
            }
            //pause = true;
            Display.gameend = this.gameend;
            
            return true;
            
        }
        }
        return false;
    }
    
    
    
    /*Подвал для пустых методов*/
    @Override
    public void destroy(){
    }
    @Override 
    public void keyTyped(KeyEvent e) {
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
