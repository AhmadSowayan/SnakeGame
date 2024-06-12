import java.awt.*;

public class Snake {
    public static final Color BODY = Color.GREEN;
    private int size;
    private SnakeNode head;
    private SnakeNode tail;

    public Snake(Point location){
        head = tail = new SnakeNode(location);
        size = 1;
    }

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

    public void move(Point location){
        addFirst(location);
        removeLast();
    }

    public void grow(Point location){
        addFirst(location);
    }

    private void addFirst(Point location){
        SnakeNode newNode = new SnakeNode(location, head);
        head.prev = newNode;
        head = newNode;
        size++;
    }

    private void removeLast(){
        tail = tail.prev;
        tail.next = null;
        size--;
    }

    public Point[] getFullLocationInArray(){
        Point[] snakeArr = new Point[getSize()];

        int i;
        SnakeNode node;
        for(i = 0, node = head; node != null; i++, node = node.next){
            snakeArr[i] = node.location;
        }

        return snakeArr;
    }

    public int getSize(){
        return size;
    }

    public void reset(Point location){
        head = tail = new SnakeNode(location);
        size = 1;
    }

    public Point getHeadLocation(){
        return head.location;
    }

    public Point getTailLocation(){
        return tail.location;
    }




}
