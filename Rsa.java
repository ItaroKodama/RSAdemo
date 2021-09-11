import java.util.*;
import java.lang.Math;
import java.math.BigInteger;

public class Rsa{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("��������f���̃r�b�g������͂��Ă��������F");
        int bitLength = sc.nextInt(); //�f���̃r�b�g��
        long plainText = 21;

        while(bitLength>0){
            //���J������
            long publicKeyE = 65537; //http://post1.s105.xrea.com/�Q��
            //2�̈قȂ�f���𐶐�
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
            System.out.print("�f��P:" + primeNumberP + " �f��Q:" + primeNumberQ + " ");
        
            //Enter���͑҂�
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
            System.out.print("���J��N(=p�~q)�F" + publicKeyN + " ���J��E((p-1)(q-1)�ƌ݂��ɑf�Ȏ��R��)�F" + publicKeyE);

            //Enter���͑҂�
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

            //�閧������
            long lcm = (primeNumberP-1)*(primeNumberQ-1) / euclidean_Algorithm(primeNumberP-1, primeNumberQ-1); //�v�Z�ʂ𗎂Ƃ����߂̍ŏ����{��
            long secretKeyD = extended_Euclidean_Algorithm(lcm, publicKeyE);
            System.out.println("�閧��d(ed mod(p-1)(q-1) = 1�𖞂������R��d):" + secretKeyD);

            //Enter���͑҂�
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
            
            //�Í���
            long cipherText = highSpeed_Exponentiation(plainText, publicKeyE, publicKeyN);
            System.out.print("����(���M���������b�Z�[�W):" + plainText);
            //Enter���͑҂�
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
            System.out.print(" �Í���(���J���ɂ���ĈÍ������ꂽ����)�F" + cipherText);

            //Enter���͑҂�
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

            //����
            long decryptionText = highSpeed_Exponentiation(cipherText, secretKeyD, publicKeyN);
            System.out.print("������(=�����ƂȂ��Ă���ΐ����I�I)" + decryptionText);

            //Enter���͑҂�
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

            //���
            long start = System.currentTimeMillis();
            long decordingText = decording(cipherText, publicKeyN, publicKeyE);
            long end = System.currentTimeMillis();
            System.out.print("���:" + decordingText + " ��ǂɂ�����������:" + (end -start) + "(ms) ");

            bitLength = sc.nextInt();
        }
    }

    //���[�N���b�h�ݏ��@�i�߂�l�Fx��y�̍ő���񐔁j
    static long euclidean_Algorithm(long a, long b){
        if(b == 0){
            return a;
        }else{
            return euclidean_Algorithm(b, a%b);
        }
    }

    //�g�����[�N���b�h�ݏ��@�i�߂�l�Fa��@�Ƃ���b�̋t���j
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
        //�t�������Ȃ琳�ɒ���
        if(x0<0){
            x0 += a;
        }
        //�t�����@���傫���Ȃ�]����Ƃ�
        if(x0>a){
            x0 %= a;
        }
        return x0;
    }

    //�����ׂ���v�Z�i�߂�l�Fx^e mod n)
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

        long lcm = (p-1)*(q-1) / euclidean_Algorithm(p-1, q-1); //�v�Z�ʂ𗎂Ƃ����߂̍ŏ����{��
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


