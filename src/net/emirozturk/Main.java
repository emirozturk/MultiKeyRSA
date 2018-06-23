package net.emirozturk;

import java.io.IOException;
import java.io.PrintWriter;
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
        double encryptionTime = 0;
        double decryptionTime = 0;
        double keyGenerationTime = 0;

        long start = System.nanoTime();
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

        while (count > 1) {
            for (int i = 0; i < count; i += 2) {
                BigInteger Nn = primeCopy.get(i).multiply(primeCopy.get(i + 1));
                primeCopy.add(Nn);
                primeNumbers.add(Nn);
                eulerPhi.add(primeCopy.get(i).subtract(BigInteger.ONE).multiply(primeCopy.get(i + 1).subtract(BigInteger.ONE)));
            }
            for (int i = 0; i < count; i++)
                primeCopy.remove(0);
            count = primeCopy.size();
        }

        BigInteger N = primeCopy.get(0);

        BigInteger eulerPhiN = BigInteger.ONE;

        for (int i = 0; i < primeCount / 2; i++) {
            randome.add(RandomWithGCD1(eulerPhi.get(i)));
            eulerPhiN = eulerPhiN.multiply(eulerPhi.get(i));
        }

        /* 4 random için deneme
        randome.set(0,BigInteger.valueOf(239));
        randome.set(1,BigInteger.valueOf(151));
        randome.set(2,BigInteger.valueOf(227));
        randome.set(3,BigInteger.valueOf(167));
        */
        /* 8 random için deneme     
        randome.set(0,BigInteger.valueOf(977));
        randome.set(1,BigInteger.valueOf(569));
        randome.set(2,BigInteger.valueOf(827));
        randome.set(3,BigInteger.valueOf(743));
        randome.set(4,BigInteger.valueOf(613));
        randome.set(5,BigInteger.valueOf(997));
        randome.set(6,BigInteger.valueOf(829));
        randome.set(7,BigInteger.valueOf(661));
         */
        int primeCounter = 0;
        for (int i = 0; i < randome.size(); i += 2)
            AValues.add(randome.get(i).modPow(randome.get(i + 1), primeNumbers.get(3 * primeCount / 2 + primeCounter++)));

        count = AValues.size();

        while (count > 1) {
            for (int i = 0; i < count; i += 2)
                AValues.add(AValues.get(i).modPow(AValues.get(i + 1), primeNumbers.get(3 * primeCount / 2 + primeCounter++)));
            for (int i = 0; i < count; i++)
                AValues.remove(0);
            count = AValues.size();
        }
        BigInteger ESub = AValues.get(0);

        BigInteger E = RandomWithGCD1(eulerPhiN.multiply(ESub));

        BigInteger D = E.modInverse(eulerPhiN.multiply(ESub));

        long end = System.nanoTime();
        keyGenerationTime = (double)(end - start) / 1000000;
        start = System.nanoTime();

        BigInteger input = BigInteger.valueOf(Integer.parseInt(args[1]));

        BigInteger output = input.modPow(E, primeNumbers.get(primeCount));

        end = System.nanoTime();
        encryptionTime = (double)(end - start) / 1000000;
        start = System.nanoTime();

        BigInteger decrypted = output.modPow(D, primeNumbers.get(primeCount));
        end = System.nanoTime();
        decryptionTime = (double)(end - start) / 1000000;

        String outputString = "";
        outputString += "N = " + N + "\n";
        outputString += "Φ(N)= " + eulerPhiN + "\n";
        for (int i = 0; i < primeCount / 2; i++)
            outputString += "e" + (i + 1) + "= " + randome.get(i) + "\n";
        outputString += "E'= " + ESub + "\n";
        outputString += "E= " + E + "\n";
        outputString += "D= " + D + "\n";
        outputString += "input= " + input + "\n";
        outputString += "output= " + output + "\n";
        outputString += "decrypted= " + decrypted + "\n";
        outputString += "Anahtar Oluşturma Süresi: " + keyGenerationTime + " ms" + "\n";
        outputString += "Encryption Süresi: " + encryptionTime + " ms" + "\n";
        outputString += "Decryption Süresi: " + decryptionTime + " ms" + "\n";
        System.out.println(outputString);
        PrintWriter writer = new PrintWriter(args[0] + "-output.txt", "UTF-8");
        writer.write(outputString);
        writer.close();
    }
}
