package com.kob.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page,Integer userId) {
        //mybatis plus的语法 第一个传入页面 第二个传入每页多少条记录
        IPage<Record>  recordIPage = new Page<>(page,10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        //用id来降序排序
        queryWrapper.orderByDesc("id").eq("a_id",userId).or().eq("b_id",userId);
        //根据分页和排序规则来获取分页的信息
        List<Record> recordList = recordMapper.selectPage(recordIPage,queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for(Record record:recordList){
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo",userA.getPhoto());
            item.put("a_username",userA.getUsername());
            item.put("b_photo",userB.getPhoto());
            item.put("b_username",userB.getUsername());
            item.put("id",record.getId());
            String result = "平局";
            if(record.getLoser().equals("A")) result = "B赢";
            if(record.getLoser().equals("B")) result = "A赢";
            item.put("result",result);
            item.put("record",record);
            items.add(item);
        }

        resp.put("records",items);
        //因为分页功能需要展示下面的页面栏，需要知道自己在第几页，需要向前端传入总共的页数
        resp.put("records_count",recordMapper.selectCount(queryWrapper));
        return resp ;
    }
}
