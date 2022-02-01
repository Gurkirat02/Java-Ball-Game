package com.dhatt;




import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MovingWord {
    private String word = "Hello World";
    private double wordX = 60, wordY = 50; //initial location
    private double wordDirectionX = 1;  //1=>right, -1=>left
    private double wordDirectionY = 1; //1=>downward, -1=>upward,
    private Font wordFont = Font.font( "Courier New", FontWeight.BOLD, 40 );

    public MovingWord(String aString){word = aString;}
    public MovingWord(String aString, double x, double y){
        word = aString;
        wordX = x;
        wordY = y;
    }

    public void setText(String aString){word = aString;}
    public String getText(){return word;}
    public Font getFont(){return wordFont;}
    public double getLocationX(){return wordX;}
    public double getLocationY(){return wordY;}
    public double getDirectionX(){return wordDirectionX;}
    public double getDirectionY(){return wordDirectionY;}
    public void setDirectionX(double aDirectionX){wordDirectionX = aDirectionX;}
    public void setDirectionY(double aDirectionY){wordDirectionY = aDirectionY;}

    public void advance(){
        final double increment = 3.0; //amount to move word

        wordX += increment*wordDirectionX;
        wordY += increment*wordDirectionY;
    }

}
