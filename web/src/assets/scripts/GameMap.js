import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";
export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        //一定要先构造基类的构造函数
        super();
        this.ctx = ctx;
        this.parent = parent;
        //存放每一个单位的绝对距离
        this.L = 0;
        //行号和列号设置一奇一偶是因为这样就不存在两个蛇下一步同时装上 （奇偶奇偶奇偶）
        this.rows = 14;
        this.cols = 13;
        //存放内部障碍物数量
        this.inner_walls_counts = 30;
        //存放墙
        this.walls = [];
        //存放蛇
        this.snakes = [
            new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this)
        ]
    }

    start() {//只执行一次 第一帧执行
        //每次一开始就创建墙 直到创建成功的那就是成功
        for (let i = 0; i < 1000; i++)
            if (this.create_walls() == true)
                break;
        this.add_listening_events();
    }

    check_ready() {
        //只有在蛇的状态是静止并且两条蛇都收到了指令才可以移动
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    next_step() { //让两条蛇进入下回合
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }
    add_listening_events() {
        const [s0, s1] = this.snakes;
        this.ctx.canvas.focus();
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === 'w') s0.set_direction(0);
            else if (e.key === 'd') s0.set_direction(1);
            else if (e.key === 's') s0.set_direction(2);
            else if (e.key === 'a') s0.set_direction(3);
            else if (e.key === 'ArrowUp') s1.set_direction(0);
            else if (e.key === 'ArrowRight') s1.set_direction(1);
            else if (e.key === 'ArrowDown') s1.set_direction(2);
            else if (e.key === 'ArrowLeft') s1.set_direction(3);
        });

    }
    check_valid(cell) {
        //检测目标位置是不是合法 会不会撞到墙或者两个蛇的身体

        for (const wall of this.walls) {
            if (wall.x === cell.r && wall.y === cell.c)
                return false;
        }
        for (const snake of this.snakes) {
            let k = snake.cells.length;
            //需要特判的是如果当前蛇尾会前进 那么蛇尾就不用判断
            if (!snake.check_tail_increasing())
                k--;

            for (let i = 0; i < k; i++) {
                if (snake.cells[i].c === cell.c && snake.cells[i].r === cell.r)
                    return false;
            }
        }

        return true;
    }
    check_conective(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = true;
        let dx = [-1, 0, 1, 0];
        let dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i++) {
            let x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && !g[x][y])
                return this.check_conective(g, x, y, tx, ty);
        }
        return false;

    }
    create_walls() {
        const g = [];
        //初始化墙
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        //给墙四周加上
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (r == 0 || c == 0 || r == this.rows - 1 || c == this.cols - 1)
                    g[r][c] = true;
            }
        }

        //创建内部墙体
        for (let i = 0; i < this.inner_walls_counts / 2; i++) {
            for (let j = 0; j < 100010; j++) {
                let x = parseInt(Math.random() * this.rows);
                let y = parseInt(Math.random() * this.cols);
                if (g[x][y] == true || x == this.rows - 2 && y == 1 || y == this.cols - 2 && x == 1) continue;
                g[x][y] = g[this.rows - x - 1][this.cols - y - 1] = true;
                break;
            }
        }
        let copy_g = JSON.parse(JSON.stringify(g));
        if (!this.check_conective(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;

        //花墙
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c] == true) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
        this.walls.push(new Wall(0, 0, this));
        return true;

    }

    update() {// 每一帧执行一次，第一帧除外
        this.update_size();
        //当两条蛇准备好了就进入下一回合
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }
    update_size() {
        //计算每一个格子的大小 返回的是浮点数，但是后面计算的时候就是整数，所以会有白缝隙
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        //设置canvas的属性
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;

    }
    //渲染画面
    render() {
        //参数分别是前面是起点，后面是宽高
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let x = 0; x < this.cols; x++) {
            for (let y = 0; y < this.rows; y++) {
                if ((x + y) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(this.L * x, this.L * y, this.L, this.L);
            }
        }
    }

}