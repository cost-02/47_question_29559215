package com.example;

import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import javax.net.ssl.*;

public class RetrievePublicKey {

    private static PublicKey getKey(String hostname, int port) throws Exception {
        // Configura il SSLContext per utilizzare un TrustManager che non verifica i certificati
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLSocketFactory factory = sslContext.getSocketFactory();

        // Crea un socket SSL usando il SSLSocketFactory che non verifica i certificati
        SSLSocket socket = (SSLSocket) factory.createSocket(hostname, port);
        socket.startHandshake();

        // Ottieni i certificati dal server
        Certificate[] certs = socket.getSession().getPeerCertificates();
        PublicKey key = certs[0].getPublicKey();

        System.out.println("Public Key: " + key);
        socket.close();
        return key;
    }

    public static void main(String[] args) throws Exception {
        String hostname = "example.com"; // Sostituisci con il nome host del server
        int port = 443; // Porta SSL standard
        PublicKey publicKey = getKey(hostname, port);
        System.out.println("Public Key: " + publicKey);
    }
}
