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
    float angle;
    float length;
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
    Vector Plus(Vector nv){
        float nx, ny;   //координаты конца суммарного вектора
        nx = (float) (this.length * sin(this.angle) + nv.length * sin(nv.angle));
        ny = (float) (this.length * cos(this.angle) + nv.length * cos(nv.angle));
        this.length = (float) sqrt((nx * nx + ny * ny));
        this.angle = (float) acos(this.length / (nx * nv.length));
        return this;
    }
    Vector Minus(Vector nv){
        float nx, ny;   //координаты конца суммарного вектора
        nx = (float) (nv.length * sin(nv.angle) - this.length * sin(this.angle));
        ny = (float) (nv.length * cos(nv.angle) - this.length * cos(this.angle));
        this.length = (float) sqrt((nx * nx + ny * ny));
        //this.angle = (float) acos(this.length / (nx * nv.length));
        return this;
    }
    Vector SetAlign(float x1, float y1, float x2, float y2){
        this.angle = (float) acos(sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) + abs(x2 - x1) / (abs(x2 - x1) * sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))));
        return this;
    }
}
