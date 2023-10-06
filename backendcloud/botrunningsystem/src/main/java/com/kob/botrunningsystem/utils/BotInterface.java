package com.kob.botrunningsystem.utils;


//前端用户编写ai的借口 必须实现
public interface BotInterface {
    Integer nextMove(String input);
}
