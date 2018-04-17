package net.emirozturk;

import com.oracle.tools.packager.Log;
import com.sun.tools.javac.util.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    static BigInteger RandomWithGCD1(BigInteger max)
    {
        BigInteger value;
        Random r = new Random();
        do
        {
            value = BigInteger.valueOf(r.nextInt());
        } while (value.gcd(max).equals(BigInteger.ONE));
        return value;
    }

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        List<String> primeStrings = Files.readAllLines(Paths.get("primes.txt"));
        int primeCount = primeStrings.size();

        List<BigInteger> primeNumbers = new ArrayList<>(primeCount);
        List<BigInteger> primeCopy = new ArrayList<>(primeCount);
        List<BigInteger> eulerPhi = new ArrayList<>();
        List<BigInteger> randome = new ArrayList<>();
        List<BigInteger> AValues = new ArrayList<>();

        for (int i = 0; i < primeCount; i++)
            primeNumbers.add(new BigInteger(primeStrings.get(i)));
        primeCopy.addAll(primeNumbers);

        int count = primeCopy.size();

        while (count > 1)
        {
            for (int i = 0; i < count; i += 2)
            {
                BigInteger Nn = primeCopy.get(i).multiply(primeCopy.get(i + 1));
                primeCopy.add(Nn);
                primeNumbers.add(Nn);
                eulerPhi.add(primeCopy.get(i).subtract(BigInteger.ONE).multiply(primeCopy.get(i + 1).subtract(BigInteger.ONE)));
            }
            for(int i=0;i<count;i++)
                primeCopy.remove(0);
            count = primeCopy.size();
        }

        BigInteger N = primeCopy.get(0);

        BigInteger eulerPhiN = BigInteger.ONE;

        for (int i = 0; i < primeCount / 2; i++)
        {
            randome.add(RandomWithGCD1(eulerPhi.get(i)));
            eulerPhiN= eulerPhiN.multiply(eulerPhi.get(i));
        }

        //DEBUGDEBUGDEBUG//
        randome.set(0,BigInteger.valueOf(239));
        randome.set(1,BigInteger.valueOf(151));
        randome.set(2,BigInteger.valueOf(227));
        randome.set(3,BigInteger.valueOf(167));

        int primeCounter = 0;
        for (int i = 0; i < randome.size(); i += 2)
            AValues.add(randome.get(i).modPow(randome.get(i + 1), primeNumbers.get(3 * primeCount / 2 + primeCounter++)));

        count = AValues.size();

        while (count > 1)
        {
            for (int i = 0; i < count; i += 2)
                AValues.add(AValues.get(i).modPow(AValues.get(i + 1), primeNumbers.get(3 * primeCount / 2 + primeCounter++)));
            for(int i=0;i<count;i++)
                AValues.remove(0);
            count = AValues.size();
        }

        BigInteger ESub = AValues.get(0);
        //DEBUG
        ESub = new BigInteger("1581954508210176");
        BigInteger E = RandomWithGCD1(eulerPhi.get(0).multiply(ESub));
        //DEBUG
        E = BigInteger.valueOf(239);
        BigInteger D = E.modInverse(eulerPhiN.multiply(ESub));

        BigInteger input = BigInteger.valueOf(786);

        BigInteger output = input.modPow(E, primeNumbers.get(primeCount));

        BigInteger decrypted = output.modPow(D, primeNumbers.get(primeCount));

        long end = System.currentTimeMillis();

        long diff = end-start;

        System.out.println(diff + " ms");
    }
}
