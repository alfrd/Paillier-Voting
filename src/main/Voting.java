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
	
	
	public Voting(BigInteger pPrime, BigInteger qPrime, BigInteger gElement, ArrayList<String> encryptedVotes) {
		p = pPrime;
		q = qPrime;
		g = gElement;
		
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
		System.out.println("Efter L-funktion: " + mu);
		mu = mu.modInverse(n);
		System.out.println("Mu is: " + mu);
		//start vote count
		
		BigInteger cipherProd = multiplyCiphertexts(encryptedVotes);
		cipherProd = cipherProd.mod(n2);
		cipherProd = cipherProd.modPow(lambda, n2);
		cipherProd = cipherProd.subtract(BigInteger.ONE).divide(n);
		
		BigInteger voteResult = cipherProd.multiply(mu).mod(n);
		if(voteResult.compareTo(BigInteger.valueOf(encryptedVotes.size())) > 0) {
			voteResult = voteResult.subtract(n);
		}
		System.out.println(cipherProd);
		System.out.println(voteResult);
	}
	
	private BigInteger multiplyCiphertexts(ArrayList<String> ciphertexts) {
		BigInteger cipherProd = BigInteger.ONE;
		for(String s: ciphertexts) {
			cipherProd = cipherProd.multiply(new BigInteger(s));
		}
		return cipherProd;
		
	}
	public static void main(String args[]) throws IOException {
		FileInputStream fileStream = new FileInputStream("input42.txt");
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
		BigInteger p = BigInteger.valueOf(1117);
		BigInteger q = BigInteger.valueOf(1471);
		BigInteger g = new BigInteger("652534095028");
		
		
		Voting v = new Voting(p, q, g, votes);
	}
}
