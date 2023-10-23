import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class PlusMinusResult {

    /*
     * Complete the 'plusMinus' function below.
     *
     * The function accepts INTEGER_ARRAY arr as parameter.
     */

    public static void plusMinus(List<Integer> arr) {
        int pos = 0;
        int neg = 0;
        int nil = 0;
        
        plusMinusRec(arr, 0, pos, neg, nil);
    }
    
    private static void plusMinusRec(List<Integer> arr, int i, int pos, int neg, int nil) {
        //base case: at end of list, print results
        if(i == arr.size()) {
            double posFrac = (double) pos / arr.size();
            double negFrac = (double) neg / arr.size();
            double nilFrac = (double) nil / arr.size();
            
            System.out.printf("%.6f\n", posFrac);
            System.out.printf("%.6f\n", negFrac);
            System.out.printf("%.6f\n", nilFrac);
            return;
        }
        
        int n = arr.get(i);
        if(n == 0) nil++;
        else if(n > 0) pos++;
        else neg++;
        
        plusMinusRec(arr, i+1, pos, neg, nil);
    }

}

public class PlusMinusSolution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(toList());

        PlusMinusResult.plusMinus(arr);

        bufferedReader.close();
    }
}

