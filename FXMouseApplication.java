package com.dhatt;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class FXMouseApplication extends Application {



    //constants
    //Direction
    public final double RIGHT = 1.0;
    public final double LEFT = -1.0;
    public final double UPWARDS = -1.0;
    public final double DOWNWARDS = 1.0;

    public boolean changeColor = false;
    //Score Fonts

    int colours = 9; // the 10 colours that the ball has
    int score = 0; // initial score

    ScoreKeeper keepScore = new ScoreKeeper();
    Text theText = new Text("Score: " + keepScore.getScore()); //create text object that can be measured
    //private instance variables
    //Model data -the data our app is about
//   private MovingWord greeting = new MovingWord("Score");
    private Ball ball = new Ball();
    private Paddle paddle = new Paddle();

    private AnimationTimer timer; //for animating frame based motion
    boolean animationIsRunning = false; //to test if animation is running

    //GUI elements
    Canvas canvas; //drawing canvas

    //GUI menus
    MenuBar menubar = new MenuBar();
    Menu fileMenu = new Menu("Learn More");
    Menu runMenu = new Menu("Play");
    Menu resetMenu = new Menu("Replay");
    ContextMenu contextMenu = new ContextMenu();


    private void buildMenus(Stage theStage) {
        //build the menus for the menu bar

        //Build Run menu items
        MenuItem startMenuItem = new MenuItem("Start Game");
        runMenu.getItems().addAll(startMenuItem);
        startMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                startAnimation();
                repaintCanvas(canvas);
            }
        });
        MenuItem stopMenuItem = new MenuItem("Pause Game");
        runMenu.getItems().addAll(stopMenuItem);
        stopMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stopAnimation();
                repaintCanvas(canvas);
            }
        });

        //Build File menu items
        MenuItem aboutMenuItem = new MenuItem("About This Game");

        fileMenu.getItems().addAll(aboutMenuItem);

        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Learn more");
                alert.setHeaderText(null);
                alert.setContentText("Ver 1.1"
                        + " Created by Gurkirat Singh Dhatt ");
                alert.showAndWait();
            }
        });

        MenuItem resetMenuItem = new MenuItem("Reset");

        resetMenu.getItems().addAll(resetMenuItem);

        resetMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                score = 0;
                theText.setText("Score: " + score);
                repaintCanvas(canvas);
//https://docs.oracle.com/javafx/2/events/convenience_methods.htm, used something from here
            }
        });

        //Build Popup context menu items
        MenuItem pauseContextMenuItem = new MenuItem("Pause Animation");
        contextMenu.getItems().addAll(pauseContextMenuItem);
        pauseContextMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stopAnimation();
                repaintCanvas(canvas);
            }
        });

        MenuItem resumeContextMenuItem = new MenuItem("Resume Animation");
        contextMenu.getItems().addAll(resumeContextMenuItem);
        resumeContextMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                startAnimation();
                repaintCanvas(canvas);
            }
        });

    }

    //required by any Application subclass
    @Override
    public void start(Stage mainStage) {

        //Here we do most of the initialization for the application
        //This method is called automatically as a result of
        // launching the application
        mainStage.setTitle("Paddle Slashers"); //window title

        VBox root = new VBox(); //root node of scene graph
        Scene theScene = new Scene(root); //our GUI scene
        mainStage.setScene(theScene); //add scene to our app's stage

        //build application menus
        //add menus to menu bar object
        menubar.getMenus().add(fileMenu);
        menubar.getMenus().add(runMenu);
        menubar.getMenus().add(resetMenu);
        //add menu bar object to application scene root
        root.getChildren().add(menubar); //add menubar to GUI
        buildMenus(mainStage); //add menu items to menus

        canvas = new Canvas(500, 600); //GUI element we will draw on
        root.getChildren().add(canvas);

        //add mouse event handler (for popup menu)
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
                    handleMousePressedEvent(e);
                }
        );
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
                    handleMouseReleasedEvent(e);
                }
        );
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
                    handleMouseDraggedEvent(e);
                }
        );

        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                //listen for arrow keys using key press event
                //arrow keys don't show up in KeyTyped events
                String text = "";
                double moveIncrement = 10;
                if (ke.getCode() == KeyCode.RIGHT) {
                    text += "RIGHT";
                    if (paddle.getTopLeftX() + paddle.getWidth() + moveIncrement < canvas.getWidth()) {
                        paddle.moveRight(moveIncrement);
                    }
                } else if (ke.getCode() == KeyCode.LEFT) {
                    text += "LEFT";
                    if (paddle.getTopLeftX() > moveIncrement) {
                        paddle.moveLeft(moveIncrement);
                    }
                } else if (ke.getCode() == KeyCode.UP) {
                    text += "UP";
                    if (paddle.getTopLeftY() > moveIncrement) {
                        paddle.moveUp(moveIncrement);
                    }
                } else if (ke.getCode() == KeyCode.DOWN) {
                    text += "DOWN";
                    if (paddle.getTopLeftY() + paddle.getHeight() + moveIncrement < canvas.getHeight()) {
                        paddle.moveDown(moveIncrement);
                    }
                }

                System.out.println("key press: " + text);
                ke.consume(); //don't let keyboard event propogate
            }
        });

        timer = new AnimationTimer() { //e.g. of anonymous inner subclass
            @Override
            public void handle(long nowInNanoSeconds) {
                //this method will be called about 60 times per second
                //which is default behaviour of the AnimationTimer class

                //          greeting.advance();
                //check for collision of greeting with canvas bounds
                //and change direction if necessary
                //build a text object to represent the actual width of the greeting string
                theText.setFont(new Font("Courier New", 20)); //set font of the text object
//                //get the width and height of the text object
//                double textWidth = theText.getLayoutBounds().getWidth();
//                double textHeight = theText.getLayoutBounds().getHeight();
//
//                if(greeting.getLocationX() + textWidth > canvas.getWidth())
//                    greeting.setDirectionX(LEFT);
//                if(greeting.getLocationX() <0) greeting.setDirectionX(RIGHT);
//                if(greeting.getLocationY() > canvas.getHeight()) greeting.setDirectionY(UPWARDS);
//                if(greeting.getLocationY() < textHeight) greeting.setDirectionY(DOWNWARDS);
                //advance the ball
                ball.advance();
                //check if ball should bounce off canvas sides
                if (ball.getCenterX() + ball.getRadius() > canvas.getWidth()) {
                    ball.setDirectionX(LEFT);
                }
                if (ball.getCenterX() - ball.getRadius() < 0) {
                    ball.setDirectionX(RIGHT);
                }

                //show game over when the ball touch the bottom
                if (ball.getCenterY() + ball.getRadius() > canvas.getHeight()) {

                    changeColor = true;

                }
                if (ball.getCenterY() - ball.getRadius() < 0) {
                    ball.setDirectionY(DOWNWARDS);
                }

                //check if ball should bounce off canvas sides
                if (ball.getCenterX() + ball.getRadius() >= paddle.getTopLeftX()
                        && ball.getCenterX() + ball.getRadius() <= paddle.getTopLeftX() + paddle.getWidth()) {

                    if (ball.getCenterY() + ball.getRadius() >= paddle.getTopLeftY()
                            && ball.getCenterY() + ball.getRadius() <= paddle.getTopLeftY() + paddle.getHeight()) {
                        ball.setDirectionY(UPWARDS);
                        score++;
                        keepScore.setScore(score);
                        theText.setText("Score: " + keepScore.getScore()); //update score
                    }

                }

                if (ball.getCenterX() - ball.getRadius() >= paddle.getTopLeftX()
                        && ball.getCenterX() - ball.getRadius() <= paddle.getTopLeftX() + paddle.getWidth()) {

                    if (ball.getCenterY() + ball.getRadius() >= paddle.getTopLeftY()
                            && ball.getCenterY() + ball.getRadius() <= paddle.getTopLeftY() + paddle.getHeight()) {
                        ball.setDirectionY(UPWARDS);
                        score++;

                        keepScore.setScore(score);
                        theText.setText("Score: " + keepScore.getScore()); //update score

                    }

                }

                repaintCanvas(canvas); //refresh our canvas rendering
            }

        };

        startAnimation(); //start the animation timer

        mainStage.show(); //show the application window
        repaintCanvas(canvas); //do initial repaint

    }

    private void startAnimation() {
        timer.start();
        animationIsRunning = true;
    }

    private void stopAnimation() {
        timer.stop();
        animationIsRunning = false;
    }

    private void handleMousePressedEvent(MouseEvent e) {
        //mouse handler for canvas
        canvas.requestFocus(); //set canvas to receive keyboard events

        //Windows uses mouse release popup trigger
        //Mac uses mouse press popup trigger
        if (e.isPopupTrigger()) {
            contextMenu.show(canvas, e.getScreenX(), e.getScreenY());
        } else {
            contextMenu.hide(); //in case it was left open

            //print out mouse locations for inspection and debugging
            System.out.println("mouse scene: "
                    + e.getSceneX()
                    + ","
                    + e.getSceneY()
            );
            System.out.println("mouse screen: "
                    + e.getScreenX()
                    + ","
                    + e.getScreenY()
            );
            System.out.println("mouse get: "
                    + e.getX()
                    + ","
                    + e.getY()
            );
        }
        repaintCanvas(canvas); //update the GUI canvas
    }

    private void handleMouseReleasedEvent(MouseEvent e) {
        //Windows uses mouse release popup trigger
        //Mac uses mouse press popup trigger
        if (e.isPopupTrigger()) {
            contextMenu.show(canvas, e.getScreenX(), e.getScreenY());
        }

        repaintCanvas(canvas);
    }

    private void handleMouseDraggedEvent(MouseEvent e) {
        //nothing to do here
        repaintCanvas(canvas);

    }

    private void repaintCanvas(Canvas aCanvas) {
        //repaint the contents of our GUI canvas

        //obtain the graphics context for drawing on the canvas
        GraphicsContext thePen = aCanvas.getGraphicsContext2D();

        //clear the canvas
        double canvasWidth = aCanvas.getWidth();
        double canvasHeight = aCanvas.getHeight();
        thePen.clearRect(0, 0, canvasWidth, canvasHeight);

        //array of colors to change color when ball hits the bottom
        Color[] cor = {Color.BLACK, Color.AQUA, Color.BLUEVIOLET, Color.BROWN, Color.BLUE, Color.CHARTREUSE,
                Color.CORAL, Color.DARKORANGE, Color.YELLOWGREEN, Color.DARKGRAY};

        if (changeColor) {
//
            ball.setCenterX(100);
            ball.setCenterY(100);
//game ending and alert subclass
            stopAnimation();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Game Over!");
            alert.setHeaderText(null);
            alert.setContentText("Turn is over!\n"
                    + "Your score is: " + score);
            alert.show();
            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    ButtonType result = alert.getResult();
                    String resultText = result.getText();
                    if(resultText.equals("OK")){
// https://www.programcreek.com/java-api-examples/?class=javafx.scene.control.Alert&method=setTitle
                        score = 0;
                        keepScore.setScore(score);
                        theText.setText("Score: " + keepScore.getScore()); //update score

                        colours = new Random().nextInt(9);

                        thePen.setFill(cor[colours]);

//https://stackoverflow.com/questions/31540500/alert-box-for-when-user-attempts-to-close-application-using-setoncloserequest-in, used for user attempt to close application
                        startAnimation();
                        changeColor = false;
                    }
                }
            });

        } else {

            thePen.setFill(cor[colours]);
            thePen.setStroke(Color.BLACK);
            thePen.setLineWidth(2);
            thePen.setFont(theText.getFont());

        }
        //draw our model data
        thePen.fillText(theText.getText(), 350, 15);
//        thePen.strokeText(greeting.getText(), greeting.getLocationX(), greeting.getLocationY());
        //draw the ball

        ball.drawWith(thePen, ball.getColor());

        //draw the Paddle
        paddle.drawWith(thePen);

        canvas.requestFocus(); //request keyboard focus

    }

    public static void main(String[] args) {
        //entry point for javaFX application
        System.out.println("starting main application");
        launch(args); //will cause application's to start and
        // run it's start() method
        System.out.println("main application is finished");
    }

}
