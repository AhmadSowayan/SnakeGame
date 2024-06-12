import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameFrame extends JFrame {
    final int COLS, ROWS;
    JPanel grid;
    JButton[][] gridElements;
    Point food;
    Snake snake;

    boolean canMove = false;
    int horizontalMovement = 0;
    int verticalMovement = 0;
    int gameSlowness = 60;


    public GameFrame() throws InterruptedException {
        COLS = 37;
        ROWS = 33;
        initializeGrid();
        this.add(grid);
        this.addKeyListener(new DirectionHandler());



        this.setTitle();
        this.setFocusable(true);
        this.setSize(800,800);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        play();

    }

    class DirectionHandler implements KeyListener{
        Map<Integer, Point> keyBindings = new HashMap<>();
        static final Point MOVE_UP = new Point(0,-1);
        static final Point MOVE_DOWN = new Point(0,1);
        static final Point MOVE_LEFT = new Point(-1,0);
        static final Point MOVE_RIGHT = new Point(1,0);

        static Point keyPressed;


        DirectionHandler(){
            keyBindings.put(KeyEvent.VK_UP, MOVE_UP);
            keyBindings.put(KeyEvent.VK_DOWN, MOVE_DOWN);
            keyBindings.put(KeyEvent.VK_LEFT, MOVE_LEFT);
            keyBindings.put(KeyEvent.VK_RIGHT, MOVE_RIGHT);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            keyPressed = keyBindings.get(keyCode);

            try{
                if(canMove){ changeMovement(); }
            }
            catch(NullPointerException exception){
                if(keyCode == KeyEvent.VK_F4){ System.exit(0); }
            }
        }                                                                                                                                                                                                   //useless but needed lines
                                                                                                                                                                                                            public void keyTyped(KeyEvent e) {}
                                                                                                                                                                                                            public void keyReleased(KeyEvent e) {}
        public void changeMovement(){
            if(!wouldMoveInOppositeDirection()){
                horizontalMovement = keyPressed.x;
                verticalMovement = keyPressed.y;
                canMove = false;
            }
        }

        public boolean wouldMoveInOppositeDirection(){
            boolean isNotStandingStill = horizontalMovement != 0 || verticalMovement != 0;

            return isNotStandingStill && (keyPressed.x == -horizontalMovement || keyPressed.y == -verticalMovement);
        }
    }



    private void initializeGrid(){
        grid = new JPanel(new GridLayout(ROWS, COLS,-1,-1));
        grid.setBackground(Color.BLACK);
        grid.setFocusable(false);


        gridElements = new JButton[COLS][ROWS];
        for(int y = 0; y < ROWS; y++)
            for(int x = 0; x < COLS; x++) {
                gridElements[x][y] = new JButton();
                gridElements[x][y].setBackground(Color.BLACK);
                gridElements[x][y].setFocusable(false);
                grid.add(gridElements[x][y]);
            }

        Point snakeLocation = getRandomPoint();
        snake = new Snake(snakeLocation);
        gridElements[snakeLocation.x][snakeLocation.y].setBackground(Snake.BODY);

        setFood();

    }

    private void reset() throws InterruptedException {

        for(Point coordinate : snake.getFullLocationInArray()){
            int x = coordinate.x;
            int y = coordinate.y;

            gridElements[x][y].setBackground(Color.BLACK);
        }
        gridElements[food.x][food.y].setBackground(Color.BLACK);

        snake.reset(getRandomPoint());
        setTitle();
        horizontalMovement = 0;
        verticalMovement = 0;

        setFood();
        play();
    }


    private Point getRandomPoint(){
        Random rand = new Random();
        int x = rand.nextInt(COLS);
        int y = rand.nextInt(ROWS);

        return new Point(x,y);
    }



    private void setFood(){
        do{ food = getRandomPoint(); }
        while(isOccupied(food));

        gridElements[food.x][food.y].setBackground(Color.RED);
    }


    public void play() throws InterruptedException {
        while(true){
            Point snakeHead = snake.getHeadLocation();
            Point snakeTail = snake.getTailLocation();

            int newHeadX = snakeHead.x + horizontalMovement, newHeadY = snakeHead.y + verticalMovement;
            Point newHead = new Point(newHeadX, newHeadY);

            if(outOfBounds(newHead) || (snake.getSize() > 1 && isOccupied(newHead)))
                break;

            gridElements[newHeadX][newHeadY].setBackground(Snake.BODY);
            if(snakeHead.equals(food)){
                snake.grow(newHead);
                setFood();
                setTitle();
            }
            else if (horizontalMovement != 0 || verticalMovement != 0){
                snake.move(new Point(newHeadX, newHeadY));
                gridElements[snakeTail.x][snakeTail.y].setBackground(Color.BLACK);
            }





            Thread.sleep(gameSlowness);
            canMove = true;



        }



        JOptionPane.showMessageDialog(grid, String.format("Length: %d", snake.getSize()), "GAME OVER", JOptionPane.PLAIN_MESSAGE );
        reset();


    }

    private boolean outOfBounds(Point head){
        int x = head.x;
        int y = head.y;

        return (x == -1 || x == COLS) || (y == -1 || y == ROWS);
    }

    private boolean isOccupied(Point location){
        JButton cell = gridElements[location.x][location.y];
        return cell.getBackground().equals(Snake.BODY);
    }




    private void setTitle(){
        setTitle( String.format("SNAKE  (Size: %d)", snake.getSize()) );
    }

}

class Boot{
    public static void main(String[] args) throws InterruptedException {
        new GameFrame();
    }
}
