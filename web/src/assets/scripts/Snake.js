import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";
export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        //一定要先构造基类的构造函数
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        //存放蛇身体里面的每一部分 cell[0]是舌头
        this.cells = [new Cell(info.r, info.c)];
        this.next_cell = null;//记录下一步的目的地

        this.speed = 5; //蛇每秒走五个格子

        //存收到的指令，-1就是没有 0,1,2,3,分别是上右下左
        this.direction = -1;
        //存放蛇的状态 idle 表示静止，move表示移动，die表示死亡
        this.status = "idle";

        this.dr = [-1, 0, 1, 0];//表示四个方向行的偏移量 上右下左
        this.dc = [0, 1, 0, -1];

        this.step = 0;//表示回合数

        this.eps = 0.01;//表示蛇头到目的地允许的距离误差

        //配置蛇的眼方向
        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2;

        //蛇眼睛不同方向的偏移量
        this.eye_dx = [
            [-1, 1], [1, 1], [1, -1], [-1, -1]
        ];
        this.eye_dy = [
            [-1, -1], [-1, 1], [1, 1], [1, -1]
        ];

    }

    start() {//只执行一次 第一帧执行

    }

    update() {// 每一帧执行一次，第一帧除外
        if (this.status === 'move') {
            this.update_move();
        }
        this.render();
    }

    update_move() {
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        const move_distance = this.speed * this.gamemap.timedelta / 1000;//表示每两帧之间走的距离

        if (distance < this.eps) {
            //表示达到终点了 将蛇头设置为新头，并且设置和状态
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            this.status = 'idle';
            if (!this.check_tail_increasing()) {//到达终点，蛇不变长，砍掉蛇尾
                this.cells.pop();
            }
        } else {
            this.cells[0].x += move_distance * dx / distance;//假设是斜线走 那么就是距离成sinx
            this.cells[0].y += move_distance * dy / distance;
        }

        if (!this.check_tail_increasing()) {
            const k = this.cells.length;
            const cs = this.cells[k - 1];
            const ct = this.cells[k - 2];
            const tail_dx = ct.x - cs.x;
            const tail_dy = ct.y - cs.y;
            const tail_distance = Math.sqrt(tail_dx * tail_dx + tail_dy * tail_dy);

            this.cells[k - 1].x += move_distance * tail_dx / tail_distance;
            this.cells[k - 1].y += move_distance * tail_dy / tail_distance;
        }

    }

    //用来改变蛇运动方向
    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() {
        //检测当前回合蛇尾部是否增加
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }
    next_step() {
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1;//清空操作
        this.status = "move";
        this.step++;
        this.eye_direction = d;
        //将新头加入
        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            //防止引用混乱 使用更深层复制
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

    }

    //渲染画面
    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        if (this.status === "die") this.color = "white";
        ctx.fillStyle = this.color;
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        //填充蛇身
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++) {
            const eyex = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eysy = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eyex, eysy, L * 0.1, 0, Math.PI * 2);
            ctx.fill();
        }

    }

}