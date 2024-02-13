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
                for (Celda vecinos : celda.getVecinos()) {
                    this.celda.getBuffer().quitar();
                }

                Celda.getBarrera().await();
                
                celda.CambiarEstado();

                Celda.getBarrera().await();
                n++;
            }

            Celda.setTerminado(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
