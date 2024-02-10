import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Celda extends Thread{
    private boolean viva;
    private final CeldaBuffer buffer;
    private List<Celda> vecinos = new ArrayList<>();
    private Integer generaciones;
    private static CyclicBarrier barrera;



    public Celda(boolean estado, CeldaBuffer buzon2, Integer n, Integer d) {
        this.viva = estado;
        this.buffer = buzon2;
        this.generaciones = n;
        Celda.barrera = new CyclicBarrier(d*d);
    }

    public Boolean estaViva(){
        return this.viva;
    }


    public void setViva(boolean viva){
        this.viva = viva;
    }

    public void CambiarEstado(){
        if(this.buffer.getContadorCeldasVivas() == 3 && this.viva == false){
            this.viva = true;
        }
        else if(this.buffer.getContadorCeldasVivas() >3 && this.viva){
            this.viva = false;
        }
        else if(this.buffer.getContadorCeldasVivas() == 0 && this.viva){
            this.viva = false;
        }
        else if(this.buffer.getContadorCeldasVivas()<3 && this.viva){
            this.viva = true;
        }

        this.buffer.setContadorCeldasVivas(0);
        
    }


    public void AgregarVecinos(ArrayList<Celda> vecinos){
        this.vecinos = vecinos;
        
    }

    @Override
    public void run() {
        try {
            Integer n=0;
            while (generaciones > n) {
                
                for (Celda celda : vecinos) {
                    celda.buffer.agregar(this.viva);
                    this.buffer.quitar();
                }
                try {
                    barrera.await();
                    CambiarEstado();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }


                n++;
                
            }
            
           
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    



}