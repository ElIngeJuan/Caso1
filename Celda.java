import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Celda{
    private boolean viva;
    private final CeldaBuffer buffer;
    private List<Celda> vecinos = new ArrayList<>();
    private Integer generaciones;

    private static CyclicBarrier barrera;


    private Consumidor consumidor;
    private Productor productor;

    private static boolean terminado = false;



    public static void setTerminado(boolean terminado) {
        Celda.terminado = terminado;
    }

    public static boolean isTerminado() {
        return terminado;
    }

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public Productor getProductor() {
        return productor;
    }

    public Celda(boolean estado, CeldaBuffer buzon2, Integer n, Integer d) {
        this.viva = estado;
        this.buffer = buzon2;
        this.generaciones = n;
        Celda.barrera = new CyclicBarrier(2*(d*d));
    
        this.consumidor = new Consumidor(this);
        this.productor = new Productor(this);
    }

    public Boolean estaViva(){
        return this.viva;
    }


    public void setViva(boolean viva){
        this.viva = viva;
    }


    public static CyclicBarrier getBarrera() {
        return barrera;
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

    public List<Celda> getVecinos() {
        return vecinos;
    }

    public CeldaBuffer getBuffer() {
        return buffer;
    }

    public Integer getGeneraciones() {
        return generaciones;
    }

    public void iniciarHilos(){
        this.consumidor.start();
        this.productor.start();
    }

}