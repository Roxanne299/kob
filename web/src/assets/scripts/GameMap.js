import { AcGameObject } from "./AcGameObject";

export class GameMap extends AcGameObject{
    constructor(ctx,parent){
        //一定要先构造基类的构造函数
        super();
        this.ctx = ctx;
        this.parent = parent;
        //存放每一个单位的绝对距离
        this.L = 0;
    }

    start(){//只执行一次 第一帧执行

    }

    update(){// 每一帧执行一次，第一帧除外
        this.render();
    }
    //渲染画面
    render(){

    }

}