package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

public class Voting {
	private BigInteger p;
	private BigInteger q;
	private BigInteger g;
	private BigInteger n;
	private BigInteger n2;
	private BigInteger lambda;
	private BigInteger mu;
	
	
	public Voting(int pPrime, int qPrime, int gElement, ArrayList<String> encryptedVotes) {
		p = BigInteger.valueOf(pPrime);
		q = BigInteger.valueOf(qPrime);
		g = BigInteger.valueOf(gElement);
		
		n = p.multiply(q);
		n2 = n.pow(2);

		//Calculate lamdba
		BigInteger p1 = p.subtract(BigInteger.ONE);
		BigInteger q1 = q.subtract(BigInteger.ONE);
		
		lambda = p1.multiply(q1).divide(p1.gcd(q1));
		System.out.println("Lambda is: " + lambda);
		
		//Calculate mu
		BigInteger mu = g.modPow(lambda, n2);
		mu = mu.subtract(BigInteger.ONE).divide(n);
		mu = mu.modInverse(n);
		System.out.println("Mu is: " + mu);
		//start vote count
		
		BigInteger cipherProd = multiplyCiphertexts(encryptedVotes);
		cipherProd = cipherProd.mod(n2);
		cipherProd = cipherProd.modPow(lambda, n2);
		cipherProd = cipherProd.subtract(BigInteger.ONE).divide(n);
		
		BigInteger voteResult = cipherProd.multiply(mu).mod(n);
		System.out.println(cipherProd);
		System.out.println(voteResult);
	}
	
	private BigInteger multiplyCiphertexts(ArrayList<String> ciphertexts) {
		BigInteger cipherProd = BigInteger.ONE;
		for(String s: ciphertexts) {
			cipherProd = cipherProd.multiply(BigInteger.valueOf(Integer.valueOf(s)));
		}
		return cipherProd;
		
	}
	public static void main(String args[]) throws IOException {
		FileInputStream fileStream = new FileInputStream("encryptedvotes.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
		String vote;
		ArrayList<String> votes = new ArrayList<String>();
		try {
			while((vote = br.readLine()) != null){
				votes.add(vote);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("There is no file with that name.");
		}
		int p = 5;
		int q = 7;
		int g = 867;
		
		
		Voting v = new Voting(p, q, g, votes);
	}
}
