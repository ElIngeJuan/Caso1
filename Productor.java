import java.util.concurrent.BrokenBarrierException;

public class Productor extends Thread{
    private Celda celda;

    public Productor(Celda celda){
        this.celda = celda;
    }

    @Override
    public void run() {
        try {
            Integer n=0;
            while (celda.getGeneraciones() > n) {
                
                for (Celda vecinos : celda.getVecinos()) {
                    vecinos.getBuffer().agregar(celda.estaViva());
                }
                
                Celda.getBarrera().await();
                
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
