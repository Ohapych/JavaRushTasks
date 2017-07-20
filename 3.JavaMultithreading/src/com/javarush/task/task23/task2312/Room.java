package com.javarush.task.task23.task2312;

/**
 * Created by ohape on 20.07.2017.
 */
public class Room {

    public static Room game;

    private int width;
    private  int height;
    private Snake snake;
    private Mouse mouse;

    public Room(int width, int height, Snake snake) {
        this.width = width;
        this.height = height;
        this.snake = snake;
    }


    public static void main(String[] args) {
        Snake newSnake = new Snake(100,100);
        game = new Room(300,300,newSnake);
        game.getSnake().setDirection(SnakeDirection.DOWN);




    }

    public void run(){

    }

    public void print(){

    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Snake getSnake() {
        return snake;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }
}
