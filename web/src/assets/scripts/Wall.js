import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject {
    constructor(x, y, gamemap) {
        //一定要先构造基类的构造函数
        super();
        this.x = x;
        this.y = y;
        //存放每一个单位的绝对距离
        this.gamemap = gamemap;
        this.color = "#B37226";
    }

    start() {//只执行一次 第一帧执行

    }

    update() {// 每一帧执行一次，第一帧除外
        this.render();
    }

    //渲染画面
    render() {
        //需要动态获取L
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect(this.y * L, this.x * L, L, L);
    }

}