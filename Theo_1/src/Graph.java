import java.io.*;
import java.util.*;
import java.util.LinkedList;

class Graph
{
    private int V;   // No. of vertices
    private LinkedList<Integer> adj[]; //Adjacency List

    //Constructor
    Graph(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }

    public static void main(String args[]) throws IOException {
        // Auskommentieren für den Beispielgraphen aus dem Bild
        /*
        int[][] beispiel = new int[6][6];
        beispiel[0][1] = 1;
        beispiel[1][2] = 1;
        beispiel[2][0] = 1;
        beispiel[2][3] = 1;
        beispiel[3][4] = 1;
        beispiel[4][3] = 1;
        beispiel[4][5] = 1;
         */



        // Wähle aus :) (gegebene Graphen)
        String big_graph_path = "src/big_graph.txt"; int big_graph_size = 1000;
        String small_graph_path = "src/small_graph.txt"; int small_graph_size = 7;

        // setze hier entweder big oder small graph ein
        int[][] graph_matrix = createAdjacencyMatrix(big_graph_path, big_graph_size);
        LinkedList<Integer> adj_list[] = convertMatrixToList(graph_matrix);

        // Hier werden die SCCs bestimmt --> übergebe Graph Konstruktor die Größe |V| des Graphen (Anzahl an Knoten)
        Graph g = new Graph(big_graph_size);
        g.adj = convertMatrixToList(graph_matrix);
        displayAdjacencyList(g.adj);
        g.printSCCs();

        // printe adj_Matrix
        // displayAdjacencyMatrix(graph_matrix);
    }

    // Funktion fügt eine Kante hinzu
    void addEdge(int v, int w)  { adj[v].add(w); }

    // Hier wird DFS rekursiv aufgerufen
    public void DFS(int knoten , boolean visited[])
    {
        // Markiere die current node als visited und printe (node + 1, da arrays bei 0 starten)
        visited[knoten] = true;

        System.out.print((knoten + 1) + " ");

        int neighbor;

        // Wiederhole für alle benachbarten Knoten
        Iterator<Integer> i = adj[knoten].iterator();
        while (i.hasNext())
        {
            neighbor = i.next();
            if (!visited[neighbor])
                // hier passiert der rekursive Aufruf --> wir gehen in die Tiefe
                DFS(neighbor,visited);
        }
    }

    // Transponierte Matrix
    Graph getTranspose()
    {
        Graph g = new Graph(V);
        // hier werden quasi einfach i und j vertauscht
        for (int zeile = 0; zeile < V; zeile++)
        {
            // für alle benachbarten Knoten zu v
            Iterator<Integer> i = adj[zeile].listIterator();
            while(i.hasNext())
                g.adj[i.next()].add(zeile);
        }
        return g;
    }

    // Die Funktion geht quasi für eine Node, alle Adjacent Nodes rein und pusht dann die auf den Stack, die
    // ganz am Ende liegt, also z.B. 1 -> 5 -> 2 -> 10 , dann würde 10 auf den Stack gepusht werden(liegt ganz unten) ,
    //  da die 10 ja die längste visiting time hat
    void fillOrder(int v, boolean visited[], Stack stack)
    {
        // Der Knoten wurde besucht
        visited[v] = true;

        // Wieder für alle benachbarten Knoten wiederholen
        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext())
        {
            int next_node = i.next();
            if(!visited[next_node])
                fillOrder(next_node, visited, stack);
        }

        // Jetzt wurden alle benachbarten Knoten abgearbeitet, also pushen wir

        stack.push(v);
    }

    // The main function that finds and prints all strongly
    // connected components
    void printSCCs()
    {
        Stack stack = new Stack();

        // erstmal alles auf false setzen zu Beginn
        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;

        // jz die Knoten nach deren visiting times auffüllen auf den Stack
        // (alle einmal durch, wenn noch nicht gevisited)
        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                fillOrder(i, visited, stack);

        // Transponierter Graph
        Graph gr = getTranspose();

        // Wieder alle als nicht-besucht markieren für den zweiten Durchlauf von DFS
        for (int i = 0; i < V; i++)
            visited[i] = false;

        // Und jz alles vom Stack ausgeben in der richtigen Reihenfolge
        System.out.println("------------------------------------------------");
        System.out.println("            STRONGLY CONNECTED COMPONENTS       ");
        System.out.println("------------------------------------------------");
        while (stack.empty() == false)
        {
            int node = (int) stack.pop();
            if (visited[node] == false)
            {
                System.out.print("Component: ");
                gr.DFS(node, visited);
                System.out.println();
            }
        }
    }

    // Excel Files auslesen (habe auf .txt geswitched, weils sonst nicht funktioniert hat)
    public static int[][] createAdjacencyMatrix(String file_path, int size) throws IOException {
        int [][] adjacency_matrix = new int[size][size];

        String fileName= file_path;
        File file= new File(fileName);

        // this gives you a 2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        Scanner inputStream;

        try{
            inputStream = new Scanner(file);

            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }

            inputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // the following code adds the input strings into the integer 2d array
        int i = 0;
        for(List<String> line: lines) {
            int j = 0;
            for (String value: line) {

                adjacency_matrix[i][j] = Integer.parseInt(value);
                j++;
            }

            i++;
        }


        return adjacency_matrix;
    }


    // convert Adjacency Matrix to Adjacency List
    public static LinkedList<Integer>[] convertMatrixToList(int[][] matrix) {
        int size = matrix.length;
        LinkedList<Integer> adj_list[] = new LinkedList[size]; // neue Liste

        for (int i=0; i<size; ++i)
            adj_list[i] = new LinkedList();

        for(int i = 0 ; i < size ; i++) {
            for(int j = 0 ; j < size ; j++) {
                // falls es eine Kante gibt, fügen wir sie hinzu --> so sparen wir uns sehr viel space
                if(matrix[i][j] != 0) {
                    adj_list[i].add(j);
                }
            }
        }
        return adj_list;
    }

    // Display Adjacency List nicely :)
    public static void displayAdjacencyList(LinkedList<Integer> adj[]) {
        System.out.println("------------------------------------------------");
        System.out.println("                 ADJACENCY LIST                 ");
        System.out.println("------------------------------------------------");
        for(int i = 0 ; i < adj.length ; i++) {
            System.out.print("Element " + (i + 1) + ": ");
            for(int edge : adj[i]) {
                System.out.print(" -> " + (edge + 1));
            }
            System.out.println();
        }
    }


    // Display Adjacency Matrix nicely :)
    public static void displayAdjacencyMatrix(int[][] graph) {
        System.out.println("------------------------------------------------");
        System.out.println("               ADJACENCY MATRIX                 ");
        System.out.println("------------------------------------------------");
        try {
            int rows = graph.length;
            int columns = graph[0].length;


            for (int i = 0; i < rows; i++) {
                System.out.print("Line " + i + ":   ");
                System.out.print("| ");
                for (int j = 0; j < columns; j++) {
                    System.out.print(graph[i][j]);
                }
                System.out.println(" |");

            }
        } catch (Exception e) {
            System.out.println("Matrix is empty!!");
        }
    }
}
