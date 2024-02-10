import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Grid {
    
    private Map<Point, Celda> tablero;
    private int n;
    private static Integer generaciones;

    public Grid( Integer n) {
        this.generaciones = n;
    }

    public static Map<Point, Celda> generarTablero(String nombreArchivo) {
        Map<Point, Celda> tablero = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            int dimension = Integer.parseInt(br.readLine());// Lee la configuración del tablero
            for (int i = 0; i < dimension; i++) {
                String[] valores = br.readLine().split(" ");
                for (int j = 0; j < dimension; j++) {
                    CeldaBuffer buffer = new CeldaBuffer(i+2);
                    boolean estado = Boolean.parseBoolean(valores[j]);
                    Point coordenada = new Point(i, j);
                    Celda celda = new Celda(estado, buffer, generaciones, dimension);
                    tablero.put(coordenada, celda);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tablero;
    }


    private List<Celda> CalcularVecinos(int i, int j){
        ArrayList<Celda> vecinos = new ArrayList<>();
    
        if (tablero.containsKey(new Point(i-1, j-1))) {
            vecinos.add(tablero.get(new Point(i-1, j-1)));
        }
        if (tablero.containsKey(new Point(i-1, j))) {
            vecinos.add(tablero.get(new Point(i-1, j)));
        }
        if (tablero.containsKey(new Point(i-1, j+1))) {
            vecinos.add(tablero.get(new Point(i-1, j+1)));
        }
        if (tablero.containsKey(new Point(i, j-1))) {
            vecinos.add(tablero.get(new Point(i, j-1)));
        }
        if (tablero.containsKey(new Point(i, j+1))) {
            vecinos.add(tablero.get(new Point(i, j+1)));
        }
        if (tablero.containsKey(new Point(i+1, j-1))) {
            vecinos.add(tablero.get(new Point(i+1, j-1)));
        }
        if (tablero.containsKey(new Point(i+1, j))) {
            vecinos.add(tablero.get(new Point(i+1, j)));
        }
        if (tablero.containsKey(new Point(i+1, j+1))) {
            vecinos.add(tablero.get(new Point(i+1, j+1)));
        }
    
        return vecinos;
    }
    
    public Boolean estaViva(int fila, int columna) {
        return tablero.get(new Point(fila, columna)).estaViva();
    }

    public void matarCelula(int fila, int columna) {
        tablero.get(new Point(fila, columna)).setViva(false);
    }

    public void revivirCelula(int fila, int columna) {
        tablero.get(new Point(fila, columna)).setViva(true);
    }
    
    public void iniciarJuego() {
        for (Map.Entry<Point, Celda> entry : tablero.entrySet()) {
            int i = entry.getKey().x;
            int j = entry.getKey().y;
            
            ArrayList<Celda> vecinos = (ArrayList<Celda>) CalcularVecinos(i, j);
            entry.getValue().AgregarVecinos(vecinos);
        }
        for (Celda celda : tablero.values()) {
            celda.start();
        }


        for(Celda celda : tablero.values()){
            try {
                celda.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        
    
        ImprimirTablero();


    }


public void ImprimirTablero(){
        int maxX = 0, maxY = 0;

        // Encontrar las coordenadas máximas
        for (Point point : tablero.keySet()) {
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        // Imprimir la matriz
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                Point currentPoint = new Point(y, x);
                Celda celda = tablero.get(currentPoint);

                if (celda != null) {
                    System.out.print(celda.estaViva() + " ");
                } else {
                    System.out.print("0 "); // O cualquier valor por defecto si la celda está vacía
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Grid grid = new Grid(3);
        grid.tablero = generarTablero("a.in");
        grid.iniciarJuego();
    }


}
