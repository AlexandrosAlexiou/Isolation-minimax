package uoi.cs.isolation;

import javax.swing.*;
import java.awt.Color;
import java.io.File;
import java.nio.file.Path;


public class Engine {

    static int[] dx = {-1, -1, -1, 0, 0, 0, +1, +1, +1};
    static int[] dy = {-1, 0, +1, -1, 0, +1, -1, 0, +1};
    static int human = 1;
    static int computer = 2;

    static class Position {
        int x, y;
        Position() {
            x = -1;
            y = -1;
        }
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    static class MoveScore {
        Position p;
        int score;
        MoveScore() {
            p = new Position();
            score = 0;
        }
    }
    
    public Position findPosition(int pl, int[][] mat) {
        
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(mat[i][j] == pl) {
                    return new Position(i, j);
                }
            }
        }
        return new Position();
    }
    
    public int findscore(int[][] mat, Position p) {
        int res = 0;
        int x, y;
        for(int i = 0; i < 9; i++) {
            x = p.x + dx[i];
            y = p.y + dy[i];
            if(x < 0 || x > 7 || y < 0 || y > 7) continue;
            if(mat[x][y] == 0) {
                res++;
            }
        }
        return res;
    }
    
    public int minimax(int[][] mat, boolean ismax, int depth, int depthlimit) {
        
        Position hpos = findPosition(human, mat);
        Position rpos = findPosition(computer, mat);
        
        int rscore = findscore(mat, rpos);
        int hscore = findscore(mat, hpos);
        if(hscore == 0) return 100;
        if(rscore == 0) return -100;
        
        if(depth >= depthlimit && ismax) {
            return rscore - hscore;
        }
        else if(depth >= depthlimit) {
            return hscore - rscore;
        }

        int best;
        if (ismax) {

            best = Integer.MIN_VALUE;
            
            int nx, ny;
            boolean pos = false;
            for(int i = 0; i < 9; i++) {
                nx = rpos.x + dx[i];
                ny = rpos.y + dy[i];
                if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
                if(mat[nx][ny] == 0) {
                    pos = true;
                    mat[nx][ny] = computer;
                    mat[rpos.x][rpos.y] = -1;
                    int res = minimax(mat, false, depth + 1, depthlimit);
                    if(res > best) {
                        best = res;
                    }
                    
                    mat[nx][ny] = 0;
                    mat[rpos.x][rpos.y] = computer;
                }
            }
        }
        else {
            best = Integer.MAX_VALUE;
            int nx, ny;
            boolean pos = false;
            for(int i = 0; i < 9; i++) {
                nx = hpos.x + dx[i];
                ny = hpos.y + dy[i];
                if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
                if(mat[nx][ny] == 0) {
                    pos = true;
                    mat[nx][ny] = human;
                    mat[hpos.x][hpos.y] = -1;
                    int res = minimax(mat, true, depth + 1, depthlimit);
                    if(res < best) {
                        best = res;
                    }
                    mat[nx][ny] = 0;
                    mat[hpos.x][hpos.y] = human;
                }
            }
        }
        return best;
    }
    
    
    public MoveScore findBestMove(int[][] mat, int d) {
        MoveScore optimal = new MoveScore();
        Position hpos = findPosition(human, mat);
        Position rpos = findPosition(computer, mat);
        
        int nx, ny, best = Integer.MIN_VALUE;
        
        for(int i = 0; i < 9; i++) {
            nx = rpos.x + dx[i];
            ny = rpos.y + dy[i];
            if(nx < 0 || nx > 7 || ny < 0 || ny > 7) continue;
            
            if(mat[nx][ny] == 0) {
                
                mat[nx][ny] = computer;
                mat[rpos.x][rpos.y] = -1;
                int val = minimax(mat, false, 0, d);


                if(val > best) {
                    best = val;
                    optimal.p = new Position(nx, ny);
                    optimal.score = val;
                }
                
                mat[nx][ny] = 0;
                mat[rpos.x][rpos.y] = computer;
            }
            
        }
        
        return optimal;
    }
    
    public Position iterativeDeepening(int[][] mat) {
        Position res = new Position();
        int best = Integer.MIN_VALUE;
        MoveScore obj;
        for(int d = 1; d <= 7; d += 2) {
            obj = findBestMove(mat, d);
            if(obj.score > best) {
                best = obj.score;
                res = obj.p;
            }
        }
        return res;
    }
    
    
    public void solve(Gui grid) {
        
        int[][] mat = new int[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                mat[i][j] = grid.buttons[i][j].status;
            }
        }
        
        Position nextpos = iterativeDeepening(mat);
        if(nextpos.x == -1) {
              grid.gameover = true;
              grid.frame.setEnabled(false);
              return;
        }
        Position rpos = findPosition(computer, mat);
        grid.buttons[rpos.x][rpos.y].status = -1;
        grid.buttons[rpos.x][rpos.y].button.setBackground(Color.BLACK);
        grid.buttons[rpos.x][rpos.y].button.setIcon(null);
        
        grid.buttons[nextpos.x][nextpos.y].status = computer;
        grid.buttons[nextpos.x][nextpos.y].button.setEnabled(false);
        grid.buttons[nextpos.x][nextpos.y].button.setIcon(grid.black_queen);
    }
  
}
