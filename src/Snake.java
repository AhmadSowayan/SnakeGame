import java.awt.*;

public class Snake {
    private static class SnakeNode{
        Point location;
        SnakeNode next;
        SnakeNode prev;

        public SnakeNode(SnakeNode prev, Point location, SnakeNode next){
            this.location = location;
            this.next = next;
            this.prev = prev;
        }

        public SnakeNode(Point location, SnakeNode next){
            this(null, location, next);
        }
        public SnakeNode(SnakeNode prev, Point location){
            this(prev, location, null);
        }
        public SnakeNode(Point location){
            this(null, location, null);
        }
    }
    public static final Color BODY = Color.GREEN;
    private int size;
    private SnakeNode head;
    private SnakeNode tail;

    public Snake(Point location){
        head = tail = new SnakeNode(location);
        size = 1;
    }

    private void addFirst(Point location){
        SnakeNode newNode = new SnakeNode(location, head);
        head.prev = newNode;
        head = newNode;
        size++;
    }

    private void addLast(Point location){
        SnakeNode newNode = new SnakeNode(tail, location);
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    private void removeLast(){
        tail = tail.prev;
        tail.next = null;
        size--;
    }

    public void move(Point location){
        addFirst(location);
        removeLast();
    }

    public void grow(Point location){
        addFirst(location);
    }

    public int getSize(){
        return size;
    }

    public Point getHead(){
        return head.location;
    }

    public Point getTail(){
        return tail.location;
    }

    public void reset(Point location){
        head = tail = new SnakeNode(location);
        size = 1;
    }



}
