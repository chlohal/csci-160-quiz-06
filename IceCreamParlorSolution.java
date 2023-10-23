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
        int[] baseIndexes = new int[] {-1, -1, -1};
        
        int[] result = findClosestBranchToTarget(baseIndexes, arr, m);
        
        ArrayList<Integer> listResult = new ArrayList<>(3);
        
        for(int i = 0; i < result.length; i++) listResult.add(result[i]);
        
        return listResult;
    }
    
    private static int[] findClosestBranchToTarget(int[] currentIndexes, List<Integer> amounts, int target) {
        //currentIndexes will always have length 3, since there are 3 friends
        int firstUnfilledIndex = currentIndexes[0] == -1 ? 0 
                                : currentIndexes[1] == -1 ? 1
                                : currentIndexes[2] == -1 ? 2
                                : -1;        
        //if all indexes are filled, then don't bother to search branches from this 
        //possibility, since there aren't any!
        //this is the base case.
        if(firstUnfilledIndex == -1) return currentIndexes;
        
        int largestAmountSuccessfulIndex = 0;
        int largestAmountSuccessfulAmount = 0;
        
        //search all other possibilities 
        for(int flavourIndex = 0; flavourIndex < amounts.size(); flavourIndex++) {
            //if a previous friend has this flavour, continue, since 
            //selecting the same flavour breaks the 'distinct flavour'
            //requirement
            if(hasValueBefore(currentIndexes, flavourIndex, firstUnfilledIndex)) continue;
            
            //check if accepting this flavour would be too expensive 
            //if so, continue
            if(sumIndexValues(currentIndexes, amounts) + amounts.get(flavourIndex) > target) continue;
            
            //tentatively accept this flavour, and check further branches
            //find the largest legal combination from all current branches
            currentIndexes[firstUnfilledIndex] = flavourIndex;
            int[] largestBranch = findClosestBranchToTarget(currentIndexes, amounts, target);
            int largestBranchValue = sumIndexValues(largestBranch, amounts);
            
            if(largestBranchValue > largestAmountSuccessfulAmount) {
                largestAmountSuccessfulAmount = largestBranchValue;
                largestAmountSuccessfulIndex = flavourIndex;
            }
        }
        
        //after scanning all branches, set the index to the largest one found
        currentIndexes[firstUnfilledIndex] = largestAmountSuccessfulIndex;
        
        //and return the result array
        return currentIndexes;
    }
    
    private static int sumIndexValues(int[] indexes, List<Integer> amounts) {
        int sum = 0;
        for(int i : indexes) {
            if(i > -1) sum += amounts.get(i);
        }
        return sum;
    }
    
    private static boolean hasValueBefore(int[] arr, int value, int index) {
        for(int i = 0; i < index; i++) {
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
