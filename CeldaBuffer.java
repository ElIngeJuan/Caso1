 import java.util.ArrayList;


public class CeldaBuffer {

    private ArrayList<Boolean> buzon;
    private Integer cantidad;

    public CeldaBuffer(int tamanio){
        this.cantidad = tamanio+1;
        this.buzon = new ArrayList<>(cantidad);

    }

    public ArrayList<Boolean> getBuzon() {
        return buzon;
    }


    public synchronized void agregar(Boolean elemento) throws InterruptedException{
        while(buzon.size() == cantidad){
            wait();
        }
        if (elemento != false) {
            buzon.add(elemento);
        }
        notify();
    }


    
    
}
