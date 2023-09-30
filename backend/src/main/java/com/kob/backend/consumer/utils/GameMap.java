package com.kob.backend.consumer.utils;

import java.util.Arrays;
import java.util.Random;

public class GameMap {
    private Integer rows;
    private Integer cols;
    private Integer innerWalls;
    private int[][] g;

    private int[] dx = {1,0,-1,0},dy = {0,1,0,-1};


    public  GameMap(Integer rows,Integer cols,Integer innerWalls){
        this.rows = rows;
        this.cols = cols;
        this.innerWalls = innerWalls;
        this.g = new int[rows][cols];
    }


    public int[][] getGameMap(){
        return g;
    }
    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    public boolean createWalls(){
    //初始化墙
    for(int i = 0;i < this.rows;i++) Arrays.fill(g[i],0);

    //加上四周的墙
    for(int i = 0;i < this.rows;i++){
        for(int j = 0;j < this.cols;j++){
            if(i == 0 || j == 0 || i == this.rows - 1 || j == this.cols - 1) g[i][j] = 1;
        }
    }

    //画墙并且检查连通性
    for(int i = 1;i <= this.innerWalls / 2;i++){
        for(int j = 1;j <= 1000;j++){
            Random random  = new Random();
            int r = random.nextInt(this.rows);
            int c = random.nextInt(this.cols);

//          如果这个点遍历过就跳过
            if(g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;

            if((r == this.rows - 2 && c == 1)||(r == 1 && c == this.cols - 2)) continue;

            g[r][c] = g[this.rows - 1 - r][this.cols - c - 1] = 1;

            break;

        }
    }
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public  void createGameMap(){
        for(int i = 1;i <= 1000;i++){
            if(createWalls()) break;
        }
    }

}
