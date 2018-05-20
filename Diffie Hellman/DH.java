import java.math.*;
import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;


public class DH {

	int bitLength=512;	
	int certainty=20;

    private static final SecureRandom rnd = new SecureRandom();

	public static void main(String [] args) throws Exception
	{
		new DH();
	}

	public DH() throws Exception{
	    Random randomGenerator = new Random();
	    BigInteger generatorValue,primeValue,publicA,publicB,secretA,secretB,sharedKey;

	    primeValue = findPrime();
	     generatorValue	= findPrimeRoot(primeValue);

		// Local Secret Key
	    secretA = new BigInteger(bitLength-2,randomGenerator);
		// FROM OTHER MACHINE, not used in final
		secretB = new BigInteger(bitLength-2,randomGenerator);

	    publicA=generatorValue.modPow(secretA, primeValue);
		// Change this: This key comes from other machine's public A variable in implementation
	    publicB=generatorValue.modPow(secretB, primeValue);
		
		//Final key		
	    sharedKey = publicB.modPow(secretA,primeValue);
		

	    String getValue=sharedKey.toString();

	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(getValue.getBytes());

	    byte byteData[] = md.digest();
	    StringBuffer sb = new StringBuffer();

	    for(int i=0;i<byteData.length;i++)
	    {
	        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));// ??
	    }

	    String getHexValue = sb.toString();

	    byte [] key = getValue.getBytes("UTF-8");

	    MessageDigest sha = MessageDigest.getInstance("SHA-256");
	    key =  sha.digest(key);
	    key = Arrays.copyOf(key, 16);
	    SecretKeySpec secretKeySpec =  new SecretKeySpec(key,"AES");

	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		//This will be changed, encrypts a file called text.jpg and puts it into a file called testEncrypt.jpg
	    CipherInputStream encryptInput = new CipherInputStream(new FileInputStream(new File("test.jpg")),cipher); 
	    FileOutputStream encryptOutput=new FileOutputStream(new File("testEncrypt.jpg"));

	    int i;
	       while((i=encryptInput.read())!= -1)
	           encryptOutput.write(i);

	       cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
		   
		//This will be changed, takes an encrypted file testEncrypt.jpg, decrypts it and puts it into a new file called testDecrypt.jpg
	    CipherInputStream decryptInput = new CipherInputStream(new FileInputStream(new File("testEncrypt.jpg")),cipher); 
	    FileOutputStream decryptOutput = new FileOutputStream(new File("testDecrypt.jpg"));

	    int j;
	    while((j=decryptInput.read())!=-1)
	        decryptOutput.write(j);

	}


	private static boolean miller_rabin_pass(BigInteger a, BigInteger n) {
	    BigInteger n_minus_one = n.subtract(BigInteger.ONE);
	    BigInteger d = n_minus_one;
		int s = d.getLowestSetBit();
		d = d.shiftRight(s);
	    BigInteger a_to_power = a.modPow(d, n);
	    if (a_to_power.equals(BigInteger.ONE)) return true;
	    for (int i = 0; i < s-1; i++) {
	        if (a_to_power.equals(n_minus_one)) return true;
	        a_to_power = a_to_power.multiply(a_to_power).mod(n);
	    }
	    if (a_to_power.equals(n_minus_one)) return true;
	    return false;
	}

	public static boolean miller_rabin(BigInteger n) {
	    for (int repeat = 0; repeat < 20; repeat++) {
	        BigInteger a;
	        do {
	            a = new BigInteger(n.bitLength(), rnd);
	        } while (a.equals(BigInteger.ZERO));
	        if (!miller_rabin_pass(a, n)) {
	            return false;
	        }
	    }
	    return true;
	}

boolean isPrime(BigInteger r){
	return miller_rabin(r);
}

public List<BigInteger> primeFactors(BigInteger number) {
    BigInteger n = number;
	BigInteger i=BigInteger.valueOf(2);
	BigInteger limit=BigInteger.valueOf(10000);
   	List<BigInteger> factors = new ArrayList<BigInteger>();
   	while (!n.equals(BigInteger.ONE)){
		while (n.mod(i).equals(BigInteger.ZERO)){
        factors.add(i);
		n=n.divide(i);
		if(isPrime(n)){
			factors.add(n);
			return factors;
		}
     	}
		i=i.add(BigInteger.ONE);
		if(i.equals(limit)){
			return factors;
		}
	}
   return factors;
 }

boolean isPrimeRoot(BigInteger g, BigInteger p)
{
    BigInteger totient = p.subtract(BigInteger.ONE);
    List<BigInteger> factors = primeFactors(totient);
    int i = 0;
    int j = factors.size();
    for(;i < j; i++)
    {
        BigInteger factor = factors.get(i);
        BigInteger t = totient.divide( factor);
		if(g.modPow(t, p).equals(BigInteger.ONE))return false;
    }
    return true;
}

String download(String address){
	String txt="";
   	URLConnection conn = null;
    InputStream in = null;
    try {
        URL url = new URL(address);
        conn = url.openConnection();
        conn.setReadTimeout(10000);
        in = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int numRead;
		String encoding = "UTF-8";
        while ((numRead = in.read(buffer)) != -1) {
				txt+=new String(buffer, 0, numRead, encoding);
        }
    } catch (Exception exception) {
        exception.printStackTrace();
    }
	return txt;
}

BigInteger findPrimeRoot(BigInteger p){
	int start= 2001;
	for(int i=start;i<100000000;i++)
		if(isPrimeRoot(BigInteger.valueOf(i),p)){
			return BigInteger.valueOf(i);
		}
	return BigInteger.valueOf(0);
}


BigInteger findPrime(){
	Random rnd=new Random();
	BigInteger p=BigInteger.ZERO;
	p= new BigInteger(bitLength, certainty, rnd);
	return p;
}
}