import java.util.*;
import java.lang.Math;
import java.math.BigInteger;

public class Rsa{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("生成する素数のビット長を入力してください：");
        int bitLength = sc.nextInt(); //素数のビット長
        long plainText = 21;

        while(bitLength>0){
            //公開鍵生成
            long publicKeyE = 65537; //http://post1.s105.xrea.com/参照
            //2つの異なる素数を生成
            long primeNumberP;
            long primeNumberQ;
            do{
                Random rnd = new Random();
                primeNumberP = BigInteger.probablePrime(bitLength, rnd).intValue();
                primeNumberQ = BigInteger.probablePrime(bitLength, rnd).intValue();
                while(primeNumberP == primeNumberQ){
                    primeNumberQ = BigInteger.probablePrime(bitLength, rnd).intValue();
                }
            }while(euclidean_Algorithm(publicKeyE,(primeNumberP - 1) * (primeNumberQ - 1)) != 1);
            System.out.print("素数P:" + primeNumberP + " 素数Q:" + primeNumberQ + " ");
        
            //Enter入力待ち
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }

            long publicKeyN = primeNumberP * primeNumberQ;
            System.out.print("公開鍵N(=p×q)：" + publicKeyN + " 公開鍵E((p-1)(q-1)と互いに素な自然数)：" + publicKeyE);

            //Enter入力待ち
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }

            //秘密鍵生成
            long lcm = (primeNumberP-1)*(primeNumberQ-1) / euclidean_Algorithm(primeNumberP-1, primeNumberQ-1); //計算量を落とすための最小公倍数
            long secretKeyD = extended_Euclidean_Algorithm(lcm, publicKeyE);
            System.out.println("秘密鍵d(ed mod(p-1)(q-1) = 1を満たす自然数d):" + secretKeyD);

            //Enter入力待ち
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            
            //暗号化
            long cipherText = highSpeed_Exponentiation(plainText, publicKeyE, publicKeyN);
            System.out.print("平文(送信したいメッセージ):" + plainText);
            //Enter入力待ち
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.print(" 暗号文(公開鍵によって暗号化された平文)：" + cipherText);

            //Enter入力待ち
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }

            //復号
            long decryptionText = highSpeed_Exponentiation(cipherText, secretKeyD, publicKeyN);
            System.out.print("復号文(=平文となっていれば成功！！)" + decryptionText);

            //Enter入力待ち
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                System.in.read();
            }catch(Exception e){
                e.printStackTrace();
            }

            //解読
            long start = System.currentTimeMillis();
            long decordingText = decording(cipherText, publicKeyN, publicKeyE);
            long end = System.currentTimeMillis();
            System.out.print("解読:" + decordingText + " 解読にかかった時間:" + (end -start) + "(ms) ");

            bitLength = sc.nextInt();
        }
    }

    //ユークリッド互除法（戻り値：xとyの最大公約数）
    static long euclidean_Algorithm(long a, long b){
        if(b == 0){
            return a;
        }else{
            return euclidean_Algorithm(b, a%b);
        }
    }

    //拡張ユークリッド互除法（戻り値：aを法としたbの逆元）
    static long extended_Euclidean_Algorithm(long a, long b){
        long x0 = 0L, x1 = 1L, x2;
        long y0 = 1L, y1 = 0L, y2;
        long r0 = a,  r1 = b,  r2;
        long q;
        
        while(r1>0){
            r2 = r0%r1;
            q  = r0/r1;
            x2 = x0-q*x1;
            y2 = y0-q*y1;
            
            r0 = r1;
            r1 = r2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }
        //逆元が負なら正に直す
        if(x0<0){
            x0 += a;
        }
        //逆元が法より大きいなら余りをとる
        if(x0>a){
            x0 %= a;
        }
        return x0;
    }

    //高速べき乗計算（戻り値：x^e mod n)
    static long highSpeed_Exponentiation(long x, long e, long n){
        String binary = Integer.toBinaryString((int)e);
        long ret = 1;
        for(int i=binary.length()-1; i>=0; i--){
            if(binary.charAt(i) == '1'){
                ret = ret * x % n;
            }
            x = x * x % n;
        }
        return ret;
    }

    static long decording(long cipherText, long n, long e){        
        long p = prime_factorization(n);
        long q = n / p;

        long lcm = (p-1)*(q-1) / euclidean_Algorithm(p-1, q-1); //計算量を落とすための最小公倍数
        long d = extended_Euclidean_Algorithm(lcm, e);

        return highSpeed_Exponentiation(cipherText, d, n);
    }

    static long prime_factorization(long n){
        long p = 1;
        if(n%2 == 0){
            p = 2;
        }
        int i = 3;
        while(i<(int)Math.sqrt(n)){
            if(n % i == 0){
                p = (long)i;
            }
            i+=2;
        }
        return p;
    }
}


