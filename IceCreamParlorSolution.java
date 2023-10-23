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
        //unassigned friends are represented by `-1`. Start the flavour indexes
        //by marking all 3 friends as unassigned. 
        int[] baseIndexes = new int[] {-1, -1, -1};
        
        //start the result with all -1, just so it'll have a `sumIndexValues(...)` of 0.
        int[] result = new int[] {-1, -1, -1};

        //this will mutate `baseIndexes` to hold the best possible solution
        findClosestBranchToTarget(baseIndexes, result, arr, m);
        
        ArrayList<Integer> listResult = new ArrayList<>();
        
        for(int i = 0; i < result.length; i++) listResult.add(result[i] + 1);
        
        return listResult;
    }
    
    /**
    Main recursive function
    currentIndexes: governs where we are in the tree of possibilities.
    bestCandidateSoFar: the best leaf of the tree. Whenever a leaf is reached, it's checked,
        and if it surpasses, bestCandidateSoFar is overwritten
    amounts: the list of flavour prices
    target: the amount of money to spend
    */
    private static void findClosestBranchToTarget(int[] currentIndexes, int[] bestCandidateSoFar, List<Integer> amounts, int target) {
        //open slots (i.e. unassigned friends) are represented by `-1`. Find the 
        //first unassigned friend's index. If all friends are assigned to a flavour,
        //then this will be `-1`.
        int firstUnfilledIndex = findFirstIndexOfNegative(currentIndexes);
        
        //if all indexes are filled, then don't bother to search branches from this 
        //possibility, since there aren't any!
        //instead, evaluate the leaf of indexes we've found ourselves in
        //this is the base case.
        if(firstUnfilledIndex == -1) {
            //if this leaf is better than the best candidate:
            if(sumIndexValues(currentIndexes, amounts) > sumIndexValues(bestCandidateSoFar, amounts)) {
                // overwrite the best candidate with this leaf
                arrayAssign(bestCandidateSoFar, currentIndexes);
            }
            //and regardless of how good this leaf is, early return
            return;
        };
        
        //search all other possibilities 
        for(int flavourIndex = 0; flavourIndex < amounts.size(); flavourIndex++) {
            //if a previous friend has this flavour, continue, since 
            //selecting the same flavour breaks the 'distinct flavour'
            //requirement
            if(contains(currentIndexes, flavourIndex)) continue;
            
            //check if accepting this flavour would be too expensive 
            //if so, continue
            if(sumIndexValues(currentIndexes, amounts) + amounts.get(flavourIndex) > target) continue;
            
            //tentatively accept this flavour and check further branches down the tree
            //after recursing, `bestCandidateSoFar` holds the largest legal (not too expensive, distinct)
            //possible branch.
            currentIndexes[firstUnfilledIndex] = flavourIndex;
            findClosestBranchToTarget(currentIndexes, bestCandidateSoFar, amounts, target);
            //empty the current friend's slot for the next iteration of the loop
            currentIndexes[firstUnfilledIndex] = -1;
        }
        
        //after looping, `bestCandidateSoFar` holds the largest legal branch over all possibilities
    }
    
    //helper method to copy from one array to another. 
    //arrays must have same length
    private static void arrayAssign(int[] target, int[] source) {
        for(int i = 0; i < target.length; i++) {
            target[i] = source[i];
        }
    }
    
    //helper method to find the first index of a negative value
    private static int findFirstIndexOfNegative(int[] a) {
        for(int i = 0; i < a.length; i++) {
            if(a[i] < 0) return i;
        }
        return -1;
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


