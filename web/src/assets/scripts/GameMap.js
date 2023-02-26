import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        //一定要先构造基类的构造函数
        super();
        this.ctx = ctx;
        this.parent = parent;
        //存放每一个单位的绝对距离
        this.L = 0;
        this.rows = 13;
        this.cols = 13;
        //存放内部障碍物数量
        this.inner_walls_counts = 30;
        //存放墙
        this.walls = [];
    }

    start() {//只执行一次 第一帧执行
        //每次一开始就创建墙 直到创建成功的那就是成功
        for (let i = 0; i < 1000; i++)
            if (this.create_walls() == true)
                break;
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
                g[x][y] = g[y][x] = true;
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
        this.render();
    }
    update_size() {
        //计算每一个格子的大小 返回的是浮点数，但是后面计算的时候就是整数，所以会有白缝隙
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        //设置canvas的属性
        this.ctx.canvas.width = this.L * this.rows;
        this.ctx.canvas.height = this.L * this.cols;

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