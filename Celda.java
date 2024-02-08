import java.util.ArrayList;
import java.util.List;

public class Celda extends Thread{
    private boolean viva;
    private final CeldaBuffer buffer;
    private List<Celda> vecinos = new ArrayList<>();
    


    public Celda(boolean estado, CeldaBuffer buzon2) {
        this.viva = estado;
        this.buffer = buzon2;
    }

    public Boolean estaViva(){
        return this.viva;
    }


    public void setViva(boolean viva){
        this.viva = viva;
    }

    public void CambiarEstado(){
        if(this.buffer.getBuzon().size() == 0){
            this.viva = false;
        }
        else if(this.buffer.getBuzon().size() > 3 ){
            this.viva = false;
        }

        else if(this.buffer.getBuzon().size() == 3){
            this.viva = true;
        }

        else if((this.buffer.getBuzon().size()<3 || this.buffer.getBuzon().size()>1) && this.viva == true){
            this.viva = true;
        }
    }


    public void AgregarVecinos(ArrayList<Celda> vecinos){
        this.vecinos = vecinos;
        
    }

    @Override
    public void run() {
        try {
            for (Celda celda : vecinos) {
                celda.buffer.agregar(this.viva);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
    }
    



}