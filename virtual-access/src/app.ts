import * as express from 'express';

class App {

    public express: express.Application;

    constructor() {
        this.express = express();
        this.middleware();
    }

    private middleware(): void {

        this.express.use("/usuarios", (req, res, nex) => {
            res.send(
                {
                    nome: "Danylo Macelai"
                }
            );
        });

    }
}

export default new App().express;