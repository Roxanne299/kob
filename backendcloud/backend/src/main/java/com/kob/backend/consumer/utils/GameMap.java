package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.security.core.parameters.P;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class GameMap extends Thread{
    private Integer rows;
    private Integer cols;
    private Integer innerWalls;
    private Player playerA;
    private Player playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private String status = "playing";//playing -> finishing
    private String loser = "";//all:平局 A:A输 B:B输
    private final static String addBotUrl = "http://127.0.0.1:8083/bot/add/";

    private ReentrantLock lock = new ReentrantLock();

    private int[][] g;

    private int[] dx = {-1,0,1,0},dy = {0,1,0,-1};


    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try{
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try{
            this.nextStepB = nextStepB;

        }finally {
            lock.unlock();
        }
    }


    public  GameMap(Integer rows, Integer cols, Integer innerWalls, Integer idA, Bot aBot, Integer idB, Bot bBot){
        this.rows = rows;
        this.cols = cols;
        this.innerWalls = innerWalls;
        this.g = new int[rows][cols];
        Integer aBotId = -1,bBotId = -1;
        String aBotCode = "",bBotCode = "";
        if(aBot != null){
            aBotId = aBot.getId();
            aBotCode = aBot.getContent();
        }
        if(bBot != null){
            bBotId = bBot.getId();
            bBotCode = bBot.getContent();
        }
        this.playerA = new Player(idA,rows - 2,1,aBotId,aBotCode,new ArrayList<>());
        this.playerB = new Player(idB,1,cols - 2,bBotId,bBotCode,new ArrayList<>());
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

//    input的参数格式是 地图 + # + me.sx + # + me.sy + # + me.操作 + # + you.sx + # + you.sy + # + you.操作
    private String getInput(Player player){// 将当前局面编码成字符串
        Player me,you;
        if(playerA.getId() == player.getId()){
            me = playerA;
            you = playerB;
        }else{
            me  = playerB;
            you = playerA;
        }
        return getMapString() + "#" + me.getSx().toString() + "#" + me.getSy().toString() + "#(" + me.getStepsString()+ ")#" + you.getSx().toString() + "#" + you.getSy().toString() + "(" + you.getStepsString() + ")";
    }
    private void sendBotCode(Player player){
        if(player.getBotId() == -1) return; //亲自出马
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",player.getId().toString());
        data.add("bot_code",player.getBotCode());
        data.add("input",getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl,data,String.class);

    }

    private boolean nextStep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        进行轮询判断 五秒没有操作的话 那么就默认失败
        sendBotCode(playerA);
        sendBotCode(playerB);
        for(int i = 1;i <= 50;i++){
            try {
                Thread.sleep(100);

                lock.lock();
                try{
                    if(nextStepA != null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally{
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n-1);

        if(g[cell.getX()][cell.getY()] == 1) return false;

        for(int i = 0;i < n - 1;i++){
            if(cell.getX() == cellsA.get(i).getX() && cell.getY() == cellsA.get(i).getY())
                return false;
        }

        for(int i = 0;i < n - 1;i++){
            if(cell.getX() == cellsB.get(i).getX() && cell.getY() == cellsB.get(i).getY())
                return false;
        }
        return true;
    }


    private void judge(){
        List<Cell> cellA = playerA.getCells();
        List<Cell> cellB = playerB.getCells();

        boolean validA = check_valid(cellA,cellB);
        boolean validB = check_valid(cellB,cellA);

        if(!validB || !validA){
            status = "finished";
            if(!validB && !validA) loser = "all";
            else if(!validB) loser = "B";
            else loser = "A";
        }
    }
    private void sendAllMessage(String msg){
        if(WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(msg);
        if(WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(msg);
    }

    private void sendMove(){
        lock.lock();
        try{
            JSONObject resp = new JSONObject();
            resp.put("msg", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;

        }finally {
            lock.unlock();
        }
    }

    private void sendResult() {  // 向两个Client公布结果
        JSONObject resp = new JSONObject();
        saveToDatabase();
        resp.put("msg", "result");
        resp.put("loser", loser);
        sendAllMessage(resp.toJSONString());
    }

    private void updateUserRating(Player player,Integer rating){
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i ++ ) {
            for (int j = 0; j < cols; j ++ ) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDatabase() {
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if("A".equals(loser)){
            ratingA += 5;
            ratingB -= 2;
        }

        if("B".equals(loser)){
            ratingB += 5;
            ratingA -= 2;
        }

        updateUserRating(playerB,ratingB);
        updateUserRating(playerA,ratingA);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    @Override
    public void run() {
        for(int i = 1;i <= 1000;i++){
            //是否获取了两条蛇的下一步条件
            if(nextStep()){
                judge();
                if(status.equals("playing")){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }
            else{
                status = "finished";
                lock.lock();
                try{
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }

                }finally {
                    lock.unlock();
                }
                sendResult();
                break;

            }
        }
    }
}
