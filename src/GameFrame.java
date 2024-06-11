import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameFrame extends JFrame {
    final int COLS, ROWS;
    JPanel grid;
    JButton[][] labelTable;
    Point food;
    Snake snake;

    boolean gameStarted = false;
    int horizontalMovement = 0;
    int verticalMovement = 0;


    public GameFrame() throws InterruptedException {
        COLS = 37;
        ROWS = 33;
        initializeGrid();
        add(grid);
        addKeyListener(new DirectionHandler());



        setTitle("SNAKE ( Size : " + snake.getSize() + " )");
        setFocusable(true);
        setSize(800,800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        play();

    }

    class DirectionHandler implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_UP:
                    horizontalMovement = (horizontalMovement != 0) ? 0 : horizontalMovement;
                    verticalMovement = (verticalMovement == 0) ? -1 : verticalMovement;
                    break;
                case KeyEvent.VK_DOWN:
                    horizontalMovement = (horizontalMovement != 0) ? 0 : horizontalMovement;
                    verticalMovement = (verticalMovement == 0) ? 1 : verticalMovement;
                    break;
                case KeyEvent.VK_RIGHT:
                    horizontalMovement = (horizontalMovement == 0) ? 1 : horizontalMovement;
                    verticalMovement = (verticalMovement != 0) ? 0 : verticalMovement;
                    break;
                case KeyEvent.VK_LEFT:
                    horizontalMovement = (horizontalMovement == 0) ? -1 : horizontalMovement;
                    verticalMovement = (verticalMovement != 0) ? 0 : verticalMovement;
                    break;
            }


        }                                                                                                                                                                                                   //useless but needed lines
                                                                                                                                                                                                            public void keyTyped(KeyEvent e) {}
                                                                                                                                                                                                            public void keyReleased(KeyEvent e) {}
    }

    private void initializeGrid(){
        grid = new JPanel(new GridLayout(ROWS, COLS,-1,-1));
        grid.setBackground(Color.BLACK);
        grid.setFocusable(false);


        labelTable = new JButton[COLS][ROWS];
        for(int y = 0; y < ROWS; y++)
            for(int x = 0; x < COLS; x++) {
                labelTable[x][y] = new JButton();
                labelTable[x][y].setBackground(Color.BLACK);
                labelTable[x][y].setFocusable(false);
                grid.add(labelTable[x][y]);
            }

        Point snakeLocation = getRandomPoint();
        snake = new Snake(snakeLocation);
        labelTable[snakeLocation.x][snakeLocation.y].setBackground(Snake.BODY);

        setFood();

    }


    private Point getRandomPoint(){
        Random rand = new Random();
        int x = rand.nextInt(COLS);
        int y = rand.nextInt(ROWS);

        return new Point(x,y);
    }

    private boolean isOccupied(Point location){
        JButton cell = labelTable[location.x][location.y];
        return cell.getBackground().equals(Snake.BODY);
    }

    private void setFood(){
        do{ food = getRandomPoint(); }
        while(isOccupied(food));

        labelTable[food.x][food.y].setBackground(Color.RED);
    }

    private boolean outOfBounds(Point head){
        int x = head.x;
        int y = head.y;

        return (x == -1 || x == COLS) || (y == -1 || y == ROWS);
    }

    public void play() throws InterruptedException {
        while(true){
            Point head = snake.getHead();
            Point tail = snake.getTail();
            int newHeadX = head.x + horizontalMovement, newHeadY = head.y + verticalMovement;
            Point newHead = new Point(newHeadX, newHeadY);

            if(outOfBounds(newHead) || (snake.getSize() > 1 && isOccupied(newHead)))
                break;

            labelTable[newHeadX][newHeadY].setBackground(Snake.BODY);
            if(head.equals(food)){
                snake.grow(newHead);
                setFood();
                setTitle("SNAKE ( Size : " + snake.getSize() + " )");
            }
            else if (horizontalMovement != 0 || verticalMovement != 0){
                snake.move(new Point(newHeadX, newHeadY));
                labelTable[tail.x][tail.y].setBackground(Color.BLACK);
            }



            Thread.sleep(60);



        }

        JOptionPane.showMessageDialog(grid, String.format("Length: %d", snake.getSize()), "GAME OVER", JOptionPane.PLAIN_MESSAGE );
        reset();


    }

    private void reset() throws InterruptedException {
        snake.reset(getRandomPoint());
        setTitle("SNAKE ( Size : " + 1 + " )");
        horizontalMovement = 0;
        verticalMovement = 0;
        for(JButton[] row : labelTable)
            for(JButton cell : row)
                cell.setBackground(Color.BLACK);

        setFood();
        play();
    }

}

class Boot{
    public static void main(String[] args) throws InterruptedException {
        new GameFrame();
    }
}
