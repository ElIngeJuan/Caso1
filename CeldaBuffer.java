 import java.util.ArrayList;


public class CeldaBuffer {

    private ArrayList<Boolean> buzon;
    private Integer cantidad;
    private Integer contadorCeldasVivas;

    public CeldaBuffer(int tamanio){
        this.cantidad = tamanio+1;
        this.buzon = new ArrayList<>(tamanio+1);
        this.contadorCeldasVivas = 0;

    }

    public ArrayList<Boolean> getBuzon() {
        return buzon;
    }

    public Integer getContadorCeldasVivas() {
        return contadorCeldasVivas;
    }

    public void setContadorCeldasVivas(Integer contadorCeldasVivas) {
        this.contadorCeldasVivas = contadorCeldasVivas;
    }

    public synchronized void agregar(Boolean elemento) throws InterruptedException{
        while(buzon.size() == cantidad){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buzon.add(elemento);
        notify();
    }
    
    public synchronized Boolean quitar() throws InterruptedException{
        Boolean sigue = true;
        while(buzon.size() == 0){
            return sigue = null;
        }
        if(buzon.get(0)){
            contadorCeldasVivas++;
            buzon.remove(0);
        }
        else{
            buzon.remove(0);
        }
        
        notifyAll();
        return sigue;
        
    }
}
