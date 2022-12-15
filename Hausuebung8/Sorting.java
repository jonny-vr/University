import java.util.Arrays;

public class Sorting {

    public static void main(String[] args) {
        // QuickSort für random Array der Größe 100
        int[] array = new int[100];
        fillArray(array,100);
        quickSort(array);
        System.out.println("Array nach QuickSort: " + Arrays.toString(array));

        // SelectionSort für random Array der Größe 100
        int[] array2 = new int[100];
        fillArray(array2,100);
        selectionSort(array2);
        System.out.println("Array2 nach SelectionSort: " + Arrays.toString(array2));
        System.out.println();

        // RunTimes der Algos
        getExecutionTime();
    }

    public static void getExecutionTime() {
        // messung für verschiedenen array-längen
        int[] arrar_sizes =  {10, 100, 500, 1000, 1500, 2000, 2500, 3000, 5000, 7500, 10000};
        int n = arrar_sizes.length;
        double[] s_times = new double[n];
        double[] q_times = new double[n];

        for(int i = 0 ; i < n ; i++) {
            // for QuickSort
            // creating random int array
            int[] toBeSorted = new int[arrar_sizes[i]];
            fillArray(toBeSorted,arrar_sizes[i]);

            // finding the time before the operation is executed
            long start = System.currentTimeMillis();
            quickSort(toBeSorted);
            // finding the time after the operation is executed
            long end = System.currentTimeMillis();
            //finding the time difference and converting it into seconds
            double sec = (end - start) / 1000d;
            q_times[i] = sec;

            // for Selection Sort
            int[] toBeSorted2 = new int[arrar_sizes[i]];
            fillArray(toBeSorted,arrar_sizes[i]);

            // finding the time before the operation is executed
            long start2 = System.currentTimeMillis();
            selectionSort(toBeSorted);
            // finding the time after the operation is executed
            long end2 = System.currentTimeMillis();
            //finding the time difference and converting it into seconds
            double sec2 = (end2 - start2) / 1000d;

            s_times[i] = sec2;
        }

        System.out.println("Selection Sort brauchte: " +  Arrays.toString(s_times));
        System.out.println("Quick Sort brauchte: " + Arrays.toString(q_times));
    }

    public static void selectionSort(int arr[]) {
        int n = arr.length;
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (arr[j] < arr[min_idx])
                    min_idx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
    }

    // start QuickSort
    public static void quickSort(int[] array) {
        quickSortRecursive(array, 0, array.length-1);
    }

    /*
    Methode, die unsere arrayTeilen methode solange aufruft, bis das Array sortiert ist
     */
    public static void quickSortRecursive(int[] array, int left, int right) {
        /*
        Man muss es sich so vorstellen: Wir haben einen Baum mit ganz vielen Ästen, die sich immer weiter verzweigen.
        Nun geht der Code an den linkesten Ast des Baums und geht solange alle verzweigungen nach links
         (rekursion(array,left,pivotStelle-1) ) bis man am Ende
        angekommen ist. Also in unserer analogie bei dem Blatt des linkesten Zweiges. Wenn wir dort angekommen sind
        gilt unsere Bedingung von (left<right) nicht mehr und der Code geht an die letzte Verzweigung zurück. Nun geht
        er nacht rechts weiter (rekursion(array,pivotStelle +1, right)) und gelangt an das Blatt. Nun geht er wieder eine
        Verzweigung weiter nach hinten und geht wieder nach rechts. Da er nun wieder nach links gehen kann, geht er nach
        links und gelangt wieder an das Blatt. Nun geht er wieder zurück und geht nach rechts.
         */
        if (left < right) {
            int pivotStelle = arrayTeilen(array, left, right);
            quickSortRecursive(array, left, pivotStelle - 1);
            quickSortRecursive(array, pivotStelle + 1, right);
        }

    }


    /* die methode quicksort sortiert alle elemente zwischen first und last so dass alle elemente die kleiner
    als der Pivot sind, links neben dem Pivot liegen und alle Elemente, die größer sind rechts daneben liegen
     */
    public static int arrayTeilen(int[] unsorted, int first, int last) {
        //Der Einfachheit wegen nehmen wir immer das letzte Element des Intervalls als Pivot
        int pivot = unsorted[last];
        //nur zum tauschen da
        int zwischenspeicher = 0;

        /* Wir legen nun zwei wichtige variablen fest, einmal "kleinere" welches die Stelle angibt, an dem das letzte
        kleinere Element (kleiner als Pivot) positioniert ist. Nach dem durchlaufen des Arrays legen wir unsren pivot
        dann rechts daneben, damit links vom
         pivot alle elemente kleiner sind, also dann an "kleinere + 1"
        Die andere wichtige Variable ist "current", welches die Stelle des elementes angibt, das wir gerade betrachten
         */
        //Wir beginnen mit kleinere bei first-1, da ja schon das erste element an unsorted[first] größer als der pivot
        // sein kann
        int kleinere = first - 1;
        //for schleife die einmal durch das ganze array geht
        for (int current = first; current < last; current++) {
            //Wenn nun das element an current größer ist, als der Pivot, dann soll nichts passieren, da unsere kleinere
            //noch an der stelle bleiben soll. Wenn jedoch nun unsorted[current] kleiner ist, dann erhöhen wir
            // kleinere um eins und tauschen unsorted[current] und unsorted[kleinere] mit einander, damit das Element
            //was ja kleiner ist als der Pivot an die Stelle von kleinere kommt, da ja kleinere die Stelle des letzten
            //Element angeben soll welches kleiner ist als der pivot
            /*
            Das heißt wir setzten nach und nach alle elemente die kleiner als der Pivot sind ans anfang vom Array
            und das letzte dieser kleineren liegt an "kleinere". Wenn zum Beispiel das erste Element größer ist, dann
            geschieht ja nichts und wenn das zweite element nun kleiner ist, dann tauschen wir ja das zweite und das erste
            element miteinander und erhöhen kleinere um eins. Somit liegt das letzte kleinere an der Stelle first.
             */
            if (unsorted[current] < pivot) {
                kleinere++;
                zwischenspeicher = unsorted[current];
                unsorted[current] = unsorted[kleinere];
                unsorted[kleinere] = zwischenspeicher;
            }
        }
        //nachdem wir jetzt die Zahlen nach links und rechts verteilt haben verschieben wir alle zahlen die rechts
        //von "kleinere" sind eins nach rechts, um dann den pivot an seine richtige position zu setzen(an kleinere + 1)

        for (int current = last - 1; current > kleinere; current--) {
            unsorted[current + 1] = unsorted[current];
        }
        //pivot an richtige Stelle setzen
        unsorted[kleinere + 1] = pivot;

        int pivotstelle = kleinere + 1;

        return pivotstelle;
    }

    public static void fillArray(int[] array, int anzahlAnZahlen) {
        int random;
        //kommazahl zwischen 0 und 1 wird multipliziert mit einer zahl und so viele verschieden zahlen kann es dann geben
        // da wir noch negative zahlen haben wollen, verschieben wir den Intervall um die hälfte der Zahlen
        //also bei 500, werden es dann zahlen zwischen -250 und 250
        for (int i = 0; i < array.length; i++) {
            random = (int) ((Math.random() * anzahlAnZahlen) - anzahlAnZahlen / 2);
            array[i] = random;
        }
    }
}
