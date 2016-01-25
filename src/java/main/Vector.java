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
    
    public float angle;    /*угол вектора, в радианах*/
    public float length;   /*длина вектора*/
    
    /*Конструкторы класса*/
    Vector(){
        angle = 0;
        length = 0;
    }   
    Vector(float na, float nl){
        angle = na;
        length = nl;
    }  
    Vector(Vector nv){
        angle = nv.angle;
        length = nv.length;
    }
    
    /*Установка угла вектора аналогично заданному*/
    public Vector AngleAs(Vector nv){
        angle = nv.angle;
        return this;
    }
    
    /*Проверка на сонаправленность вектору*/
    public Boolean IsCodirectional(Vector nv){
        return (angle == nv.angle);
    }
    
    /*Проверка на параллельность вектору*/
    public Boolean IsParallel(Vector nv){
        return (angle == nv.angle)|(angle == (360 - nv.angle));
    }
    
    /*Проверка на перпендикулярность вектору*/
    public Boolean IsPerpendicular(Vector nv){
        return (angle == abs(nv.angle - 90))|(angle == abs(nv.angle + 90));
    }
    
    /*Вычисление длины отрезка по двум координатам*/
    public float Length(float x1, float y1, float x2, float y2){
        return (float) sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    /*Установка длины вектора аналогично заданному*/
    public Vector LengthAs(Vector nv){
        length = nv.length;
        return this;
    }
    
    /*Вычитание вектора из вектора*/
    public Vector Minus(Vector nv){
        float nx, ny;   /*координаты конца суммарного вектора*/
        /*вычисление координат конца последовательности векторов (как при вычитании)*/
        nx = (float) (GetX() - nv.GetX());
        ny = (float) (GetY() - nv.GetY());
        /*вычисление новых значений*/
        length = (float) sqrt(nx * nx + ny * ny);
        angle = (float) acos(nx / length) * signum(ny);
        return this;
    }
    
    /*Прибавление к вектору вертора*/
    public Vector Plus(Vector nv){
        float nx, ny;   /*координаты конца суммарного вектора*/
        /*вычисление координат конца последовательности векторов (как при суммировании)*/
        nx = (float) (GetX() + nv.GetX());
        ny = (float) (GetY() + nv.GetY());
        /*вычисление новых значений*/
        length = (float) sqrt(pow(nx, 2) + pow(ny, 2));
        angle = (float) acos(nx / length) * signum(ny);
        return this;
    }
    
    /*Установка угла вектора по двум точкам*/
    public Vector SetAngle(float x1, float y1, float x2, float y2){
        angle = (float) acos((x2 - x1) / sqrt(pow((x2 - x1), 2) + pow((y2 - y1),2))) * signum(y2 - y1);
        return this;
    }
    
    /*Получение координаты X конца вектора*/
    public float GetX(){
        return (float)cos(angle) * length;
    }
    
    /*Получение координаты Y конца вектора*/
    public float GetY(){
        return (float)sin(angle) * length;
    }

}