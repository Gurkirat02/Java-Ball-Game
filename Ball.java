package com.dhatt;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    //color field
    Color colour;
    // creating colour object

    private double radius = 50; //radius of ball
    private double centerX = 100; //center X
    private double centerY = 100; //center Y
    private double speed = 3; //pixels/timer event
    private double directionX = 1;  //1=>right, -1=>left

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    private double directionY = 1; //1=>downward, -1=>upward,

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }
    //add whatever get methods are required

    public void advance() {
        centerX += speed * directionX;
        centerY += speed * directionY;
        //https://stackoverflow.com/questions/59223713/how-to-inherit-a-method-in-java-from-an-a-class-to-a-c-class
    }

    public void setDirectionX(double aDirection) {
        directionX = aDirection;
    }

    public void setDirectionY(double aDirection) {
        directionY = aDirection;
    }

    public void drawWith(GraphicsContext thePen, Color color) {
        thePen.setFill(this.colour);
        thePen.fillOval(centerX - radius, //upper left X
                centerY - radius, //upper left Y
                2 * radius, //width
                2 * radius); //height

    }


    Color getColor() {
        return colour;
    }

    void setColour(Color randomColor) {
        this.colour = randomColor;
    }
}