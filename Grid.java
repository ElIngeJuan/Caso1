import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Grid {
    
    private Map<Point, Celda> tablero;
    private static Integer generaciones;

    public Grid( Integer n) {
        Grid.generaciones = n;
    }

    public static Map<Point, Celda> generarTablero(String nombreArchivo) {
        Map<Point, Celda> tablero = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            int dimension = Integer.parseInt(br.readLine());// Lee la configuración del tablero
            for (int i = 0; i < dimension; i++) {
                String[] valores = br.readLine().split(",");
                for (int j = 0; j < dimension; j++) {
                    CeldaBuffer buffer = new CeldaBuffer(i);
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
    
    public void iniciarJuego() throws InterruptedException {
        for (Map.Entry<Point, Celda> entry : tablero.entrySet()) {
            int i = entry.getKey().x;
            int j = entry.getKey().y;
            
            ArrayList<Celda> vecinos = (ArrayList<Celda>) CalcularVecinos(i, j);
            entry.getValue().AgregarVecinos(vecinos);
        }
        for (Celda celda : tablero.values()) {
            celda.iniciarHilos();
        }

        for (Celda celda : tablero.values()) {
            celda.getConsumidor().join();
            celda.getProductor().join();
        }

        ImprimirTablero();


    }

    public void ImprimirTablero() {
        int maxX = 0, maxY = 0;

        // Encontrar las coordenadas máximas
        for (Point point : tablero.keySet()) {
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("Resultado.out"))) {
            // Imprimir en el archivo
            for (int y = 0; y <= maxY; y++) {
                for (int x = 0; x <= maxX; x++) {
                    Point currentPoint = new Point(y, x);
                    Celda celda = tablero.get(currentPoint);

                    if (celda != null) {
                        writer.print(celda.estaViva() + " ");
                    } else {
                        writer.print("0 "); // O cualquier valor por defecto si la celda está vacía
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introduce el nombre del archivo: ");
        String nombreArchivo = sc.nextLine();

        System.out.println("Ingrese el número de generaciones");
        Integer gens = sc.nextInt();


        Grid grid = new Grid(gens);
        grid.tablero = generarTablero(nombreArchivo);
        grid.iniciarJuego();

        sc.close();
    }


}
