package net.emirozturk;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static BigInteger RandomWithGCD1(BigInteger max)
    {
        BigInteger value;
        Random r = new Random();
        do
        {
            value = BigInteger.valueOf(Math.abs(r.nextInt()));
        } while (!value.gcd(max).equals(BigInteger.ONE));
        return value;
    }

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();
        List<String> primeStrings = Files.readAllLines(Paths.get(args[0]));
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

        BigInteger E = RandomWithGCD1(eulerPhiN.multiply(ESub));

        BigInteger D = E.modInverse(eulerPhiN.multiply(ESub));

        BigInteger input = BigInteger.valueOf(Integer.parseInt(args[1]));

        BigInteger output = input.modPow(E, primeNumbers.get(primeCount));

        BigInteger decrypted = output.modPow(D, primeNumbers.get(primeCount));

        long end = System.currentTimeMillis();

        long diff = end-start;

        System.out.println("N = " + N);
        System.out.println("Φ(N)= " + eulerPhiN);
        for (int i = 0; i < primeCount / 2; i++)
             System.out.println("e" + (i + 1) + "= " + randome.get(i));
        System.out.println("E'= " + ESub);
        System.out.println("E= " + E);
        System.out.println("D= " + D);
        System.out.println("input= " + input);
        System.out.println("output= " + output);
        System.out.println("decrypted= " + decrypted);
        System.out.println("Süre:" + diff + " ms");
    }
}
