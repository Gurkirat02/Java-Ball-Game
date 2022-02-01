package com.dhatt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle {
    private double width = 100; //radius of ball
    private double height = 20; //radius of ball
    private double topLeftX = 200;
    private double topLeftY = 400;

    public double getTopLeftX(){return topLeftX;}
    public double getTopLeftY(){return topLeftY;}
    public double getWidth(){return width;}
    public double getHeight(){return height;}
    //add whatever get methods are required

    public void moveRight(double increment){topLeftX += increment;}
    public void moveLeft(double increment){topLeftX -= increment;}
    public void moveUp(double increment){topLeftY -= increment;}
    public void moveDown(double increment){topLeftY += increment;}


    public void drawWith(GraphicsContext thePen){
        thePen.setFill(Color.GREEN);
        thePen.fillRect(topLeftX, //upper left X
                topLeftY, //upper left Y
                width, //width
                height); //height

    }
}

