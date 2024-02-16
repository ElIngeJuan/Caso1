import java.util.concurrent.BrokenBarrierException;

public class Consumidor extends Thread{
    private Celda celda;

    public Consumidor(Celda celda){
        this.celda = celda;
    }

    @Override
    public void run() {
        try {
            Integer n=0;
            while (celda.getGeneraciones() > n) {
                for(int i=0; i<celda.getVecinos().size(); i++){
                    celda.getBuffer().quitar();
                    Thread.yield();
                }

                Celda.getBarrera().await();
                
                celda.CambiarEstado();

                Celda.getBarrera().await();
                n++;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
