//存放所有的游戏对象
const AC_GAME_OBJECTS = [];

export class AcGameObject {
    constructor(){
        //在游戏对象列表中加入当前对象
        AC_GAME_OBJECTS.push(this);

        //下面存放的就是对象的属性

        //记录是否执行过
        this.has_called_start = false;
        //记录这一帧到上一帧的时间间隔 对应速度
        this.timedelta = 0;
    }

    start(){//只执行一次 第一帧执行

    }

    update(){// 每一帧执行一次，第一帧除外

    }

    on_destroy(){//删除之前还行

    }

    destroy(){
        this.on_destroy();
        //删除调用这个函数的对象，其实就是删除自己
        for(let i in AC_GAME_OBJECTS){
            const obj = AC_GAME_OBJECTS[i];
            if(obj===this){
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}


//实现动画的一直执行

let last_timestamp; //上一次执行的时刻

const step = (timestamp)=>{
    for(let obj in AC_GAME_OBJECTS){
        //如果没有执行过
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }else{//执行过的话
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step);