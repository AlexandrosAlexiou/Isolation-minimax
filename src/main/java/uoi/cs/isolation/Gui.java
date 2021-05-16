package uoi.cs.isolation;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Semaphore;
import javax.swing.*;


public class Gui {

    Semaphore s = new Semaphore(1);
    static int[] dx = {2, 1, -1, -2, -2, -1, 1, 2};
    static int[] dy = {1, 2, 2, 1, -1, -2, -2, -1};
    static int human = 1;
    static int robot = 2;
    boolean humanchance = true;
    boolean gameover = false;

    Path black_path = new File(ApplicationConstants.BLACK_QUEEN).toPath();
    Path white_path = new File(ApplicationConstants.WHITE_QUEEN).toPath();

    Icon bkn  = new ImageIcon(black_path.toString());
    Icon wkn  = new ImageIcon(white_path.toString());
    int plx, ply;
    
    public class Button {
        JButton button;
        int status;
        Button() {
            button = new JButton();
            status = 0;
        }

        private void addActionListener(Gui grid) {
            button.addActionListener(ae -> {
                grid.humanchance = false;
                if(status == 3) {
                    button.setEnabled(false);
                    button.setIcon(wkn);
                    button.setBackground(Color.WHITE);
                    status = human;

                    for(int i = 0; i < 8; i++) {
                        int nx = grid.plx + dx[i];
                        int ny = grid.ply + dy[i];
                        if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
                        if(grid.buttons[nx][ny].status == 3 ) {
                            grid.buttons[nx][ny].status = 0;
                            grid.buttons[nx][ny].button.setEnabled(false);
                            grid.buttons[nx][ny].button.setBackground(Color.WHITE);
                        }
                    }
                    grid.buttons[grid.plx][grid.ply].button.setIcon(null);
                    grid.buttons[grid.plx][grid.ply].button.setBackground(Color.LIGHT_GRAY);
                    grid.buttons[grid.plx][grid.ply].status = -1;
                    s.release();
                }
            });
        }

    }
    
    
    static int height = 400;
    static int width = 400;
    JFrame frame;
    JPanel panel;
    Button[][] buttons;
    GridLayout grid;

    public Gui() {
        this.plx = ((int)(Math.random() * 10)) % 8;
        this.ply = ((int)(Math.random() * 10)) % 8;
        frame = new JFrame(ApplicationConstants.NAME);
        panel = new JPanel();
        buttons = new Button[8][8];
        grid = new GridLayout(8, 8);
        panel.setLayout(grid);
        frame.setResizable(false);
        
        for(int i = 0; i < 8; i++) {
           for(int j = 0; j < 8; j++) {
               buttons[i][j] = new Button();
               buttons[i][j].button.setSize(50,50);
               buttons[i][j].addActionListener(this);
               panel.add(buttons[i][j].button);
               buttons[i][j].button.setBackground(Color.WHITE);
               buttons[i][j].button.setEnabled(false);
           }
        }
        
        int rx = ((int)(Math.random() * 10)) % 8;
        int ry = ((int)(Math.random() * 10)) % 8;
        while(rx == plx && ry == ply) {
            rx = ((int)((Math.random() * 10))) % 8;
            ry = ((int)((Math.random() * 10))) % 8;
        }
        //System.out.println(rx + " " + ry + " ");
        buttons[rx][ry].button.setIcon(bkn);
        buttons[rx][ry].button.setEnabled(false);
        buttons[rx][ry].status = robot;
        
        
        buttons[plx][ply].button.setIcon(wkn);
        buttons[plx][ply].button.setEnabled(false);
        buttons[plx][ply].status = human;
        for(int i = 0; i < 8; i++) {
            int nx = plx + dx[i];
            int ny = ply + dy[i];
            if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
            if(buttons[nx][ny].status == 0) {
                buttons[nx][ny].status = 3;
                buttons[nx][ny].button.setBackground(Color.CYAN);
                buttons[nx][ny].button.setEnabled(true);
            }
        }
        frame.add(panel);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    }
    public static void main(String[] args) throws Throwable {

        Gui grid = new Gui();
        Engine session = new Engine();

        boolean humanwins = true;
        while(!grid.gameover) {

            if(grid.humanchance) {
                grid.s.acquire();
            }
            else {
                for(int i = 0; i < 8; i++) {
                    for(int j = 0; j < 8; j++) {
                        if(grid.buttons[i][j].status == human) {
                            grid.plx = i;
                            grid.ply = j;
                        }
                    }
                }
                session.solve(grid);
                boolean flag = false;
                for(int i = 0; i < 8; i++) {
                    int nx = grid.plx + dx[i];
                    int ny = grid.ply + dy[i];
                    if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
                    if(grid.buttons[nx][ny].status == 0) {
                        flag = true;
                        grid.buttons[nx][ny].status = 3;
                        grid.buttons[nx][ny].button.setBackground(Color.CYAN);
                        grid.buttons[nx][ny].button.setEnabled(true);
                    }
                }

                if(!flag) {
                    //System.out.println("You Lose");
                    grid.frame.setEnabled(false);
                    grid.gameover = true;
                    humanwins = false;
                }
                grid.humanchance = true;
            }
        }
        if(humanwins) {
            JOptionPane.showMessageDialog(null,ApplicationConstants.WIN_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, ApplicationConstants.LOSE_MESSAGE);
        }
        System.exit(0);
    }
}
