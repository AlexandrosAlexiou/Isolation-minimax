package uoi.cs.isolation;

import java.awt.*;
import java.net.URL;
import java.util.concurrent.Semaphore;
import javax.swing.*;


public class Gui {

    Semaphore s = new Semaphore(1);
    static int[] dx = {-1, -1, -1, 0, 0, 0, +1, +1, +1};
    static int[] dy = {-1, 0, +1, -1, 0, +1, -1, 0, +1};
    static int human = 1;
    static int computer = 2;
    boolean human_turn = true;
    boolean gameover = false;
    static Icon black_queen;
    static Icon white_queen;
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
                grid.human_turn = false;
                if(status == 3) {
                    button.setEnabled(false);
                    button.setIcon(white_queen);
                    button.setBackground(Color.WHITE);
                    status = human;

                    for(int i = 0; i < 9; i++) {
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
                    grid.buttons[grid.plx][grid.ply].button.setBackground(Color.BLACK);
                    grid.buttons[grid.plx][grid.ply].status = -1;
                    s.release();
                }
            });
        }

    }
    

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
        black_queen = new ImageIcon(getImage(ApplicationConstants.BLACK_QUEEN));
        white_queen = new ImageIcon(getImage(ApplicationConstants.WHITE_QUEEN));
        
        for(int i = 0; i < 8; i++) {
           for(int j = 0; j < 8; j++) {
               buttons[i][j] = new Button();
               buttons[i][j].button.setSize(50,50);
               buttons[i][j].addActionListener(this);
               panel.add(buttons[i][j].button);
               buttons[i][j].button.setBackground(Color.WHITE);
               buttons[i][j].button.setEnabled(false);
               buttons[i][j].button.setOpaque(true);
           }
        }
        
        int rx = ((int)(Math.random() * 10)) % 8;
        int ry = ((int)(Math.random() * 10)) % 8;
        while(rx == plx && ry == ply) {
            rx = ((int)((Math.random() * 10))) % 8;
            ry = ((int)((Math.random() * 10))) % 8;
        }
        buttons[rx][ry].button.setIcon(black_queen);
        buttons[rx][ry].button.setEnabled(false);
        buttons[rx][ry].status = computer;
        
        
        buttons[plx][ply].button.setIcon(white_queen);
        buttons[plx][ply].button.setEnabled(false);
        buttons[plx][ply].status = human;
        for(int i = 0; i < 9; i++) {
            int nx = plx + dx[i];
            int ny = ply + dy[i];
            if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
            if(buttons[nx][ny].status == 0) {
                buttons[nx][ny].status = 3;
                buttons[nx][ny].button.setBackground(Color.GREEN);
                buttons[nx][ny].button.setEnabled(true);
            }
        }
        frame.add(panel);
        frame.setSize(ApplicationConstants.WINDOW_WIDTH, ApplicationConstants.WINDOW_HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public static Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
    }

    public static void main(String[] args) throws Throwable {
        Gui grid = new Gui();
        Engine session = new Engine();

        boolean humanwins = true;
        while(!grid.gameover) {

            if(grid.human_turn) {
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
                for(int i = 0; i < 9; i++) {
                    int nx = grid.plx + dx[i];
                    int ny = grid.ply + dy[i];
                    if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
                    if(grid.buttons[nx][ny].status == 0) {
                        flag = true;
                        grid.buttons[nx][ny].status = 3;
                        grid.buttons[nx][ny].button.setBackground(Color.GREEN);
                        grid.buttons[nx][ny].button.setEnabled(true);
                    }
                }

                if(!flag) {
                    grid.frame.setEnabled(false);
                    grid.gameover = true;
                    humanwins = false;
                }
                grid.human_turn = true;
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
