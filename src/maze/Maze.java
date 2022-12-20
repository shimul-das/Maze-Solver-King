package maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import javax.swing.*;


public class Maze extends JFrame {
   
    
    final static int X = 1;
    
    final static int C = 0;
    
    final static int S = 2;
    
    final static int E = 8;
    
    final static int V = 9;

    
    final static int START_I = 1, START_J = 1;
    
    final static int END_I = 2, END_J = 9;

    int[][] maze = new int[][]{ 
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 2, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 1, 1, 0, 8},
        {1, 0, 1, 1, 1, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 0, 1, 0, 0, 0, 1},
        {1, 1, 0, 1, 0, 1, 1, 0, 0, 1},
        {1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 0, 1, 1, 1}
    };

    
    int[][] arr;

    
    JButton solveDFS;
    JButton solveBFS;
    JButton clear;
    JButton exit;
    JButton genRandom;

    
    JLabel elapsedDfs;
    JTextField textDfs;
    JLabel elapsedBFS;
    JTextField textBFS;

    boolean repaint = false;  

    
    long startTime;
    
    long stopTime;
    
    long duration;
    
    double dfsTime;
      
    double bfsTime;

   
   int[][] savedMaze = clone();

    
    public Maze() {

        setTitle("Maze Solver King");   
        setSize(960, 530);    
        

        URL urlIcon = getClass().getResource("flat-theme-action-maze-icon.png");    
        ImageIcon image = new ImageIcon(urlIcon);                                   
        setIconImage(image.getImage());                                             

        setLocationRelativeTo(null);                                                
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                            
        setLayout(null);                                                            

        
        elapsedDfs = new JLabel("Elapsed Time :");
        elapsedBFS = new JLabel("Elapsed Time :");
        textDfs = new JTextField();
        textBFS = new JTextField();

        
        solveDFS = new JButton("Solve DFS");
        solveBFS = new JButton("Solve BFS");
        clear = new JButton("Clear");
        exit = new JButton("Exit");
        genRandom = new JButton("Generate Random Maze");

       
        add(solveDFS);
        add(solveBFS);
        add(clear);
        add(elapsedDfs);
        add(textDfs);
        add(elapsedBFS);
        add(textBFS);
        add(exit);
        add(genRandom);

        
        setVisible(true);

        
        solveDFS.setBounds(500, 50, 100, 40);
        solveBFS.setBounds(630, 50, 100, 40);
        clear.setBounds(760, 50, 100, 40);
        exit.setBounds(760, 115, 100, 40);
        elapsedDfs.setBounds(500, 100, 100, 40);
        genRandom.setBounds(500, 180, 170, 40);
        elapsedBFS.setBounds(630, 100, 100, 40);
        textDfs.setBounds(500, 130, 100, 25);
        textBFS.setBounds(630, 130, 100, 25);

     
        genRandom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int x[][] = GenerateArray();  
                repaint = true;
                restore(x);   
                repaint();   
            }
        });

        
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);       
            }
        });

        
        clear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (arr == null) {      
                    repaint = true;      
                    restore(savedMaze);  
                    repaint();           
                } else {                 
                    repaint = true;      
                    restore(arr);        
                    repaint();           
                }

                textBFS.setText("");       
                textDfs.setText("");      

            }
        });

        
        solveDFS.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (arr == null) {       
                    restore(savedMaze);  
                    repaint = false;     
                    solve_DFS();        
                    repaint();           
                } else {                 

                    restore(arr);        
                    repaint = false;    
                    solve_DFS();        
                    repaint();           

                }

            }
        });

        
        solveBFS.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (arr == null) {        
                    restore(savedMaze);  
                    repaint = false;     
                    solve_BFS();        
                    repaint();           
                } else {                 
                    restore(arr);       
                    repaint = false;    
                    solve_BFS();       
                    repaint();          
                }

            }
        });

    }

   
    public int Size() {
        return maze.length;
    }

    
    public void Print() {
        for (int i = 0; i < Size(); i++) {      
            for (int J = 0; J < Size(); J++) {  
                System.out.print(maze[i][J]);   
                System.out.print(' ');          
            }
            System.out.println();              
        }
    }

    
    public boolean isInMaze(int i, int j) {  

        if (i >= 0 && i < Size() && j >= 0 && j < Size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isInMaze(MazePos pos) {   
        return isInMaze(pos.i(), pos.j());   
    }

    
    public int mark(int i, int j, int value) {
        assert (isInMaze(i, j));  
        int temp = maze[i][j];    
        maze[i][j] = value;       
        return temp;              
    }

    public int mark(MazePos pos, int value) {   
        return mark(pos.i(), pos.j(), value);   
    }

    
    public boolean isMarked(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] == V);

    }

    public boolean isMarked(MazePos pos) {   
        return isMarked(pos.i(), pos.j()); 
    }

  
    public boolean isClear(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] != X && maze[i][j] != V);

    }

    public boolean isClear(MazePos pos) {   
        return isClear(pos.i(), pos.j());   
    }

    
    public boolean isFinal(int i, int j) {

        return (i == Maze.END_I && j == Maze.END_J);
    }

    public boolean isFinal(MazePos pos) {  
        return isFinal(pos.i(), pos.j());   
    }

    
    public int[][] clone() { 
        int[][] mazeCopy = new int[Size()][Size()];
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                mazeCopy[i][j] = maze[i][j];
            }
        }
        return mazeCopy;
    }


    
    public void restore(int[][] savedMazed) {
        for (int i = 0; i < Size(); i++) {
            for (int j = 0; j < Size(); j++) {
                maze[i][j] = savedMazed[i][j];
            }
        }

        maze[1][1] = 2;  
        maze[2][9] = 8;  
    }

    
    public int[][] GenerateArray() {
        arr = new int[10][10];
        Random rnd = new Random();
        int min = 0;
        int high = 1;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int n = rnd.nextInt((high - min) + 1) + min;
                arr[i][j] = n;

            }
        }
          arr[0][1] = 0;arr[1][0] = 0;arr[2][1] = 0;arr[1][2] = 0;
          arr[1][9] = 0;arr[2][8] = 0;arr[3][9] = 0;               
 
        return arr;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(70, 70); 
        if (repaint == true) {  
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.darkGray;       
                            break;
                        case 8:
                            color = Color.RED;          
                            break;
                        case 2:
                            color = Color.YELLOW;     
                            break;
                        
                        default:
                            color = Color.WHITE;        
                    }
                    g.setColor(color);
                    g.fillRect(40 * col, 40 * row, 40, 40);    
                    g.setColor(Color.BLUE);                    
                    g.drawRect(40 * col, 40 * row, 40, 40);    

                }
            }
        }

        if (repaint == false) {   
            for (int row = 0; row < maze.length; row++) {
                for (int col = 0; col < maze[0].length; col++) {
                    Color color;
                    switch (maze[row][col]) {
                        case 1:
                            color = Color.darkGray;     
                            break;
                        case 8:
                            color = Color.RED;         
                            break;
                        case 2:
                            color = Color.YELLOW;      
                            break;
                        case 9:
                            color = Color.green;   
                            break;
                        default:
                            color = Color.WHITE;  
                    }
                    g.setColor(color);
                    g.fillRect(40 * col, 40 * row, 40, 40); 
                    g.setColor(Color.BLUE);                  
                    g.drawRect(40 * col, 40 * row, 40, 40);  

                }

            }

        }

    }

    public static void main(String[] args) { 

        SwingUtilities.invokeLater(new Runnable() {  
                                                      
            @Override                                
            public void run() {
                Maze maze = new Maze();              

            }
        });

    }

    public void solve_DFS() { 
        
        startTime = System.nanoTime();

        
        Stack<MazePos> stack = new Stack<MazePos>();

        
        stack.push(new MazePos(START_I, START_I));

        MazePos crt;   
        MazePos next;  
        while (!stack.empty()) {

            
            crt = stack.pop();
            if (isFinal(crt)) { 

                break;
            }

            
            mark(crt, V);

            
            next = crt.north();   
            if (isInMaze(next) && isClear(next)) {  
                stack.push(next);
            }
            next = crt.east();   
            if (isInMaze(next) && isClear(next)) {
                stack.push(next);
            }
            next = crt.west();    
            if (isInMaze(next) && isClear(next)) {
                stack.push(next);
            }
            next = crt.south();  
            if (isInMaze(next) && isClear(next)) {
                stack.push(next);
            }
        }

        if (!stack.empty()) {              
             stopTime = System.nanoTime();
            JOptionPane.showMessageDialog(rootPane, "You Got it");

        } else {                         
            JOptionPane.showMessageDialog(rootPane, "You Are stuck in the maze");
        }

        System.out.println("\nFind Goal By DFS : ");
        Print();
      
       
        duration = stopTime - startTime;    

        dfsTime =  (double)duration / 1000000;   
        System.out.println(String.format("Time %1.3f ms", dfsTime));

        textDfs.setText(String.format("%1.3f ms", dfsTime));

    }

    public void solve_BFS() { 
        
        startTime = System.nanoTime();

        
        LinkedList<MazePos> list = new LinkedList<MazePos>();

       
        list.add(new MazePos(START_I, START_J));

        MazePos crt, next;
        while (!list.isEmpty()) {

            
            crt = list.removeFirst();

            
            if (isFinal(crt)) { 
                break;
            }

            
            mark(crt, V);

            
            next = crt.north();    
            if (isInMaze(next) && isClear(next)) { 
                list.add(next);
            }
            next = crt.east();    
            if (isInMaze(next) && isClear(next)) {
                list.add(next);
            }
             next = crt.west();    
            if (isInMaze(next) && isClear(next)) {
                list.add(next);
            }
            next = crt.south();   
            if (isInMaze(next) && isClear(next)) {
                list.add(next);
            }

        }

        if (!list.isEmpty()) {               
            stopTime = System.nanoTime();    
            JOptionPane.showMessageDialog(rootPane, "You Got it");
        } else {                             
            JOptionPane.showMessageDialog(rootPane, "You Are stuck in the maze");
        }

        System.out.println("\nFind Goal By BFS : ");
        Print();
        
        duration = stopTime - startTime;      
       
        bfsTime = (double) duration / 1000000;    
        System.out.println(String.format("Time %1.3f ms", bfsTime));

        textBFS.setText(String.format("%1.3f ms", bfsTime));

        
    }

}
