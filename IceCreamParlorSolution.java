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

class IceCreamParlorResult {

    /*
     * Complete the 'icecreamParlor' function below.
     *
     * The function is expected to return an INTEGER_ARRAY.
     * The function accepts following parameters:
     *  1. INTEGER m
     *  2. INTEGER_ARRAY arr
     */

    public static List<Integer> icecreamParlor(int m, List<Integer> arr) {
        int[] result = findBestPathToTarget(arr, m, new int[0]);
        
        ArrayList<Integer> listResult = new ArrayList<>();
        
        for(int i = 0; i < result.length; i++) listResult.add(result[i] + 1);
        
        return listResult;
    }


    public static int[] findBestPathToTarget(List<Integer> amounts, int target, int[] previousIndexes) {
        if(previousIndexes.length == 3) return previousIndexes;

        int[] maxChoicePath = new int[0];
        int maxChoiceValue = 0;
        for(int i = 0; i < amounts.size(); i++) {
            if(contains(previousIndexes, i)) continue;
            if(sumIndexValues(previousIndexes, amounts) + amounts.get(i) > target) continue;

            int[] branch = findBestPathToTarget(amounts, target, arrayWithAddition(previousIndexes, i));

            if(sumIndexValues(branch, amounts) > maxChoiceValue) {
                maxChoiceValue = sumIndexValues(branch, amounts);
                maxChoicePath = branch;
            }
        }

        return maxChoicePath;
    }

    //helper method to add an integer to the end of an array
    private static int[] arrayWithAddition(int[] base, int addition) {
        int[] result = new int[base.length + 1];
        System.arraycopy(base, 0, result, 0, base.length);
        result[result.length - 1] = addition;
        return result;
    }

    //helper method to sum values, given an array of indexes into a list of amounts
    private static int sumIndexValues(int[] indexes, List<Integer> amounts) {
        int sum = 0;
        for(int i : indexes) {
            if(i > -1) sum += amounts.get(i);
        }
        return sum;
    }
    
    //helper method: does array contain value?
    private static boolean contains(int[] arr, int value) {
        for(int i = 0; i < arr.length; i++) {
            if (arr[i] == value) return true;
        }
        return false;
    }

}

public class IceCreamParlorSolution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = Integer.parseInt(bufferedReader.readLine().trim());

        IntStream.range(0, t).forEach(tItr -> {
            try {
                int m = Integer.parseInt(bufferedReader.readLine().trim());

                int n = Integer.parseInt(bufferedReader.readLine().trim());

                List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                    .map(Integer::parseInt)
                   .collect(toList());

                List<Integer> result = IceCreamParlorResult.icecreamParlor(m, arr);

                bufferedWriter.write(
                    result.stream()
                        .map(Object::toString)
                        .collect(joining(" "))
                    + "\n"
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        bufferedReader.close();
        bufferedWriter.close();
    }
}


