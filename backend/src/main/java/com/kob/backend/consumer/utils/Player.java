package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    public boolean check_increasing(int step){
        if(step <= 10) return true;
        return (step % 3) == 1;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d: steps) {
            res.append(d);
        }
        return res.toString();
    }

    public List<Cell> getCells(){
        List<Cell> cells = new ArrayList<>();
        int x = sx,y = sy;
        cells.add(new Cell(x,y));
        int[] dx = {-1,0,1,0},dy = {0,1,0,-1};
        int step = 0;
        for(int i : steps){
            x += dx[i];
            y += dy[i];
            cells.add(new Cell(x,y));
            if(!check_increasing(++step)) cells.remove(0);
        }
        return cells;
    }

}
