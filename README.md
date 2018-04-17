# MultiKeyRSA
-Key generation with 2^k prime numbers.  
-The application is developed for testing the performance of using 2^n primes for key generation of RSA.  
-Application first creates a key, encodes output and decodes the output with the same key to measure total ellapsed time.  
-Also some generated intermediate values are printed to console.  
-Keys are defined as BigInteger so big numbers could also be used (The actual development purpose was big numbers).  
-To use jar file, use command below:  
    java -jar MultiKeyRSA.jar [filename] [input]  
      *filename should contain 2^n prime numbers with each number per line. For example:  
      101  
      103  
      107  
      ...etc.  
      *input should be a positive integer number  
