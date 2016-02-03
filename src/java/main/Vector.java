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

import static java.lang.Math.*;

/**
 *
 * @author Vladimir
 */
public class Vector {
    
    public double angle;    /*угол вектора, в радианах*/
    public double length;   /*длина вектора*/
    
    /*Конструкторы класса*/
    Vector(){
        angle = 0;
        length = 0;
    }   
    Vector(double na, double nl){
        angle = na;
        length = abs(nl);
    }  
    Vector(Vector nv){
        if(nv == null)throw new NullPointerException();
        angle = nv.angle;
        length = nv.length;
    }
    
    /*Прибавление к вектору вертора*/
    public Vector Plus(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        double nx, ny;   /*координаты конца суммарного вектора*/
        /*вычисление координат конца последовательности векторов (как при суммировании)*/
        nx = GetX() + nv.GetX();
        ny = GetY() + nv.GetY();
        /*вычисление новых значений*/
        length = sqrt(pow(nx, 2) + pow(ny, 2));
        try{
            angle = acos(nx / length) * signum(ny);
        }catch(Error e){
            System.err.println("[Vector Plus]: Error");
        }finally{
            return this;
        }
    }
    
    /*Вычитание вектора из вектора*/
    public Vector Minus(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        double nx, ny;   /*координаты конца суммарного вектора*/
        /*вычисление координат конца последовательности векторов (как при вычитании)*/
        nx = GetX() - nv.GetX();
        ny = GetY() - nv.GetY();
        /*вычисление новых значений*/
        length = sqrt(nx * nx + ny * ny);
        try{
            angle = acos(nx / length) * signum(ny);
        }catch(Error e){
            System.err.println("[Vector Minus]: Error");
        }finally{
            return this;
        }
    }
    
    /*Установка угла вектора по двум точкам*/
    public Vector SetAngle(double x1, double y1, double x2, double y2){
        try{
            angle = acos((x2 - x1) / sqrt(pow((x2 - x1), 2) + pow((y2 - y1),2))) * signum(y2 - y1);
        }catch(Error e){
            System.err.println("[Vector SetAngle]: Error");
        }finally{
            return this;
        }
    }
    
    /*Установка угла вектора аналогично заданному*/
    public Vector AngleAs(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        angle = nv.angle;
        return this;
    }
    
    /*Установка длины вектора аналогично заданному*/
    public Vector LengthAs(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        length = nv.length;
        return this;
    }
    
    /*Проверка на сонаправленность вектору*/
    public Boolean IsCodirectional(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        return (angle == nv.angle);
    }
    
    /*Проверка на параллельность вектору*/
    public Boolean IsParallel(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        return (angle == nv.angle)|(angle == (360 - nv.angle));
    }
    
    /*Проверка на перпендикулярность вектору*/
    public Boolean IsPerpendicular(Vector nv){
        if(nv == null)throw new NullPointerException("Vector cannot be NULL");
        return (angle == abs(nv.angle - 90))|(angle == abs(nv.angle + 90));
    }
    
    /*Получение координаты X конца вектора*/
    public double GetX(){
        return cos(angle) * length;
    }
    
    /*Получение координаты Y конца вектора*/
    public double GetY(){
        return sin(angle) * length;
    }
    
    /*Вычисление длины отрезка по двум координатам*/
    private float Length(double x1, double y1, double x2, double y2){
        return (float) sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

}
