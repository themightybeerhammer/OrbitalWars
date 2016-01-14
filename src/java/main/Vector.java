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
        this.angle = 0;
        this.length = 0;
    }   
    Vector(float na, float nl){
        this.angle = na;
        this.length = nl;
    }  
    Vector(Vector nv){
        this.angle = nv.angle;
        this.length = nv.length;
    }
    
    /*Установка угла вектора аналогично заданному*/
    public Vector AngleAs(Vector nv){
        this.angle = nv.angle;
        return this;
    }
    
    /*Проверка на сонаправленность вектору*/
    public Boolean IsCodirectional(Vector nv){
        return (this.angle == nv.angle);
    }
    
    /*Проверка на параллельность вектору*/
    public Boolean IsParallel(Vector nv){
        return (this.angle == nv.angle)|(this.angle == (360 - nv.angle));
    }
    
    /*Проверка на перпендикулярность вектору*/
    public Boolean IsPerpendicular(Vector nv){
        return (this.angle == abs(nv.angle - 90))|(this.angle == abs(nv.angle + 90));
    }
    
    /*Вычисление длины отрезка по двум координатам*/
    public float Length(float x1, float y1, float x2, float y2){
        return (float) sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
    
    /*Установка длины вектора аналогично заданному*/
    public Vector LengthAs(Vector nv){
        this.length = nv.length;
        return this;
    }
    
    /*Вычитание вектора из вектора*/
    public Vector Minus(Vector nv){
        float nx, ny;   /*координаты конца суммарного вектора*/
        /*вычисление координат конца последовательности векторов (как при вычитании)*/
        nx = (float) (nv.length * sin(nv.angle) - this.length * sin(this.angle));
        ny = (float) (nv.length * cos(nv.angle) - this.length * cos(this.angle));
        /*вычисление новых значений*/
        this.length = (float) sqrt((nx * nx + ny * ny));
        this.angle = (float) this.length / (nx * nv.length);
        return this;
    }
    
    /*Прибавление к вектору вертора*/
    public Vector Plus(Vector nv){
        float nx, ny;   /*координаты конца суммарного вектора*/
        /*вычисление координат конца последовательности векторов (как при суммировании)*/
        nx = (float) (this.length * sin(this.angle) + nv.length * sin(nv.angle));
        ny = (float) (this.length * cos(this.angle) + nv.length * cos(nv.angle));
        /*вычисление новых значений*/
        this.length = (float) sqrt((nx * nx + ny * ny));
        this.angle = (float) this.length / (max(1, nx) * nv.length);
        return this;
    }
    
    /*Установка угла вектора по двум точкам*/
    public Vector SetAngle(float x1, float y1, float x2, float y2){
        this.angle = (float) acos(Length(x1, y1, x2, y2) + abs(x2 - x1) / (abs(x2 - x1) * Length(x1, y1, x2, y2)));
        return this;
    }

    /*TEST START*//*
    public static void main(String[] args) {
        Vector a = new Vector(0, 5);
        Vector b = new Vector((float) 3.14, 2);
        System.out.println(a.angle + " " + a.length + " | " + b.angle + " " + b.length);
        a.Plus(b);
        System.out.println(a.angle + " " + a.length + " | " + b.angle + " " + b.length);
    }*/
    /*TEST END*/
}