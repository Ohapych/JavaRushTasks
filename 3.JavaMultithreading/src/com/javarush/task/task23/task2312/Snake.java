package com.javarush.task.task23.task2312;

import javax.management.monitor.MonitorSettingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ohape on 20.07.2017.
 */
public class Snake {
    private List<SnakeSection> sections;
    private boolean isAlive;
    private SnakeDirection direction;

    public Snake(int x, int y) {
        sections = new ArrayList<SnakeSection>();
        sections.add(new SnakeSection(x,y));
        isAlive = true;

    }

    public List<SnakeSection> getSections() {
        return sections;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public SnakeDirection getDirection() {
        return direction;
    }

    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
    }
        public int getX(){
            return sections.get(0).getX();
    }

    public int getY(){
        return sections.get(0).getY();
    }

    public void move(){
        if (isAlive== false)return; else
        if (direction.equals(SnakeDirection.UP)) move(0,-1); else
        if (direction.equals(SnakeDirection.DOWN)) move(0,1); else
        if (direction.equals(SnakeDirection.LEFT)) move(-1,0); else
        if (direction.equals(SnakeDirection.RIGHT)) move(1,0);

    }
   public void checkBorders(SnakeSection head){

        if (head.getX()< 0 || head.getX() >=  Room.game.getWidth() || head.getY() < 0 ||  head.getY()>= Room.game.getHeight()) isAlive = false;

   }

    public void checkBody(SnakeSection head){
       /*List<SnakeSection> body = new ArrayList<>();
       body.addAll(sections);
       body.remove(0);
       if (body.contains(head)) isAlive = false;*/
       sections.remove(0);
       if (sections.contains(head)) isAlive = false;
    }
    /*а) проверить, не вылезла ли она за границу комнаты (если да, то змея умирает)
    б) проверить, не совпадает ли она с уже существующими кусочками змеи (если да, то змея умирает)
    в) добавить голову к змее (со стороны головы) и удалить последний кусочек из хвоста.
    г) вызвать метод eatMouse у статического объекта game класса Room, если координаты мыши совпадают с координатами головы змеи.
    д) если змея поймала мышь (координаты головы совпадают с координатами мыши), то удалять кусок из хвоста не надо.*/
    public void move(int x, int y){

            SnakeSection head = new SnakeSection(sections.get(0).getX()+x, sections.get(0).getY()+y);
            checkBorders(head);
            checkBody(head);
            if (isAlive != false) {
                sections.add(0, head);
                if ((Room.game.getMouse().getX() == head.getX()) && (Room.game.getMouse().getY() == head.getY())) {
                    Room.game.eatMouse();
                } else {
                    sections.remove( sections.size() - 1);
                }
            }
        }

    }