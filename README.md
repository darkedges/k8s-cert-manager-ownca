# SSL Demo


## cert-manager

```bash
kubectl create namespace cert-manager
kubectl label namespace cert-manager certmanager.k8s.io/disable-validation=true
kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v0.9.0/cert-manager.yaml
```
### Own CA

```bash
mkdir ca
cd ca
openssl genrsa -out ca.key 2048
openssl req -x509 -new -nodes -key ca.key -subj "//C=AU\ST=Victoria\L=Melbourne\O=DarkEdges\OU=Internal CA\CN=darkedges.local" -days 3650 -reqexts v3_req -extensions v3_ca -out ca.crt
kubectl create secret tls ca-key-pair --cert=ca.crt --key=ca.key --namespace=cert-manager
cat <<EOF | kubectl apply -f -
apiVersion: certmanager.k8s.io/v1alpha1
kind: ClusterIssuer
metadata:
  name: ca-issuer
  namespace: cert-manager
spec:
  ca:
    secretName: ca-key-pair
EOF
```

### Maven

```bash
mvn clean package dockerfile:build
```

### kubernetes deploy

```bash
kubectl apply -f k8s\ssldemo.yaml
```

### Confirm it is working

```bash
kubectl logs ssldemo-0 -n ssldemo -f

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.6.RELEASE)

2019-08-03 03:59:54.816  INFO 1 --- [           main] c.d.s.k8s.ssldemo.SSLDemoApplication     : Starting SSLDemoApplication on ssldemo-0.ssldemo.ssldemo.svc.cluster.local with PID 1 (/app started by root in /)
2019-08-03 03:59:54.825  INFO 1 --- [           main] c.d.s.k8s.ssldemo.SSLDemoApplication     : No active profile set, falling back to default profiles: default
2019-08-03 04:00:00.492  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8443 (https)
2019-08-03 04:00:00.641  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-08-03 04:00:00.647  INFO 1 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.21]
2019-08-03 04:00:01.145  INFO 1 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-08-03 04:00:01.145  INFO 1 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 6079 ms
2019-08-03 04:00:02.214  INFO 1 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-08-03 04:00:03.748  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8443 (https) with context path '' 2019-08-03 04:00:03.764  INFO 1 --- [           main] c.d.s.k8s.ssldemo.SSLDemoApplication     : Started SSLDemoApplication in 10.367 seconds (JVM running for 11.47)
```

### Get Certificate Details

```bash
echo | openssl s_client -showcerts -connect 192.168.99.99:443 2>/dev/null | openssl x509 -text
```

returns

```bash
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            21:66:65:a5:3f:0a:1e:5b:85:4a:03:a9:83:7a:07:7b
    Signature Algorithm: sha256WithRSAEncryption
        Issuer: C=AU, ST=Victoria, L=Melbourne, O=DarkEdges, OU=Internal CA, CN=darkedges.local
        Validity
            Not Before: Aug  3 03:59:46 2019 GMT
            Not After : Nov  1 03:59:46 2019 GMT
        Subject: O=DarkEdges, CN=ssldemo.svc.cluster.local
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    00:a5:52:61:e2:4c:68:55:f0:32:c2:0d:db:9f:d8:
                    b1:e8:36:b2:44:fc:9b:c7:db:bc:0f:b4:a9:4e:c0:
                    35:3d:81:6e:30:ae:15:a6:87:b5:4d:e7:7f:6b:53:
                    59:95:40:6a:9e:f6:6a:f5:1b:bf:40:57:ee:9e:3e:
                    91:f8:47:b5:b1:68:3f:e1:49:2a:b4:87:d5:75:67:
                    a5:05:c3:26:ea:2f:55:7b:44:ec:61:7f:63:c2:1c:
                    9b:2c:c7:45:b0:43:0a:61:3c:5a:3e:d5:60:59:b4:
                    be:ff:16:3f:2d:c4:75:db:31:44:3e:3d:8e:ce:e2:
                    1f:56:21:91:56:ce:47:53:eb:86:d4:f5:59:6d:54:
                    72:f3:8c:2a:cc:b5:a8:0b:b7:3d:62:15:e3:69:6a:
                    91:6c:da:59:9c:7e:95:ec:b1:12:1c:18:e3:c0:c4:
                    c5:a0:bc:72:04:06:2f:d8:d1:59:30:64:47:e0:f6:
                    cb:bf:39:29:a5:ab:60:c4:6a:91:77:cc:1e:f6:0a:
                    79:44:e7:85:8d:52:bb:02:96:ec:1b:33:1e:61:1e:
                    69:fd:8a:a7:b6:3c:ee:df:12:e4:6f:59:66:02:de:
                    71:a8:94:fe:e1:17:6d:28:b5:07:77:c0:fb:b1:7d:
                    67:22:37:f7:52:61:73:f8:4f:78:e7:3e:d5:d0:aa:
                    1f:9f
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature, Key Encipherment
            X509v3 Basic Constraints: critical
                CA:FALSE
            X509v3 Authority Key Identifier:
                keyid:FF:1F:97:4F:85:B0:ED:B7:B1:AA:28:E7:15:C0:13:BB:AB:F7:54:15

            X509v3 Subject Alternative Name:
                DNS:ssldemo.svc.cluster.local, DNS:*.ssldemo.svc.cluster.local
    Signature Algorithm: sha256WithRSAEncryption
         3e:b6:cd:20:d4:75:c3:86:32:82:9e:51:d1:15:e9:d0:9d:62:
         62:27:ec:eb:df:4a:42:a5:47:7a:2f:d8:ed:0a:d9:1b:26:66:
         a0:83:52:5a:c9:ca:13:5e:50:30:8c:92:ca:56:54:a6:c2:f6:
         ed:bd:8d:a9:5e:7e:6a:52:a6:da:44:f5:ce:8b:f6:e2:35:ea:
         20:9f:e3:6e:11:97:66:5c:bb:be:ec:56:ea:4f:21:68:d3:2e:
         68:4e:6d:9b:03:4f:10:98:2f:9b:84:d0:d4:dc:eb:bf:1f:a4:
         2a:de:a9:55:f5:ed:ca:84:59:e1:79:4f:19:66:88:ca:da:f3:
         8c:bf:db:3d:48:24:16:0a:e6:c8:41:04:b2:fd:76:42:33:0f:
         28:5f:69:15:fc:5a:22:24:ee:54:0b:75:50:8e:70:a1:b6:df:
         6e:bc:4a:04:df:d9:89:36:de:d7:b5:69:85:e6:68:64:51:7b:
         f4:c5:76:8f:53:b2:b5:7f:81:01:e7:df:ad:d2:c5:58:50:eb:
         29:aa:89:04:d9:02:6c:47:f3:84:a7:83:03:48:be:9e:a9:cb:
         c6:83:67:32:2f:98:5e:45:31:b2:54:fb:6e:0b:63:e5:f4:91:
         f5:f5:48:30:b7:12:2f:53:61:18:6b:96:13:ef:fc:d4:35:68:
         19:8d:f6:bf
-----BEGIN CERTIFICATE-----
MIIDwDCCAqigAwIBAgIQIWZlpT8KHluFSgOpg3oHezANBgkqhkiG9w0BAQsFADB4
MQswCQYDVQQGEwJBVTERMA8GA1UECAwIVmljdG9yaWExEjAQBgNVBAcMCU1lbGJv
dXJuZTESMBAGA1UECgwJRGFya0VkZ2VzMRQwEgYDVQQLDAtJbnRlcm5hbCBDQTEY
MBYGA1UEAwwPZGFya2VkZ2VzLmxvY2FsMB4XDTE5MDgwMzAzNTk0NloXDTE5MTEw
MTAzNTk0NlowODESMBAGA1UEChMJRGFya0VkZ2VzMSIwIAYDVQQDExlzc2xkZW1v
LnN2Yy5jbHVzdGVyLmxvY2FsMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC
AQEApVJh4kxoVfAywg3bn9ix6DayRPybx9u8D7SpTsA1PYFuMK4Vpoe1Ted/a1NZ
lUBqnvZq9Ru/QFfunj6R+Ee1sWg/4UkqtIfVdWelBcMm6i9Ve0TsYX9jwhybLMdF
sEMKYTxaPtVgWbS+/xY/LcR12zFEPj2OzuIfViGRVs5HU+uG1PVZbVRy84wqzLWo
C7c9YhXjaWqRbNpZnH6V7LESHBjjwMTFoLxyBAYv2NFZMGRH4PbLvzkppatgxGqR
d8we9gp5ROeFjVK7ApbsGzMeYR5p/Yqntjzu3xLkb1lmAt5xqJT+4RdtKLUHd8D7
sX1nIjf3UmFz+E945z7V0KofnwIDAQABo4GFMIGCMA4GA1UdDwEB/wQEAwIFoDAM
BgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFP8fl0+FsO23saoo5xXAE7ur91QVMEEG
A1UdEQQ6MDiCGXNzbGRlbW8uc3ZjLmNsdXN0ZXIubG9jYWyCGyouc3NsZGVtby5z
dmMuY2x1c3Rlci5sb2NhbDANBgkqhkiG9w0BAQsFAAOCAQEAPrbNINR1w4Yygp5R
0RXp0J1iYifs699KQqVHei/Y7QrZGyZmoINSWsnKE15QMIySylZUpsL27b2NqV5+
alKm2kT1zov24jXqIJ/jbhGXZly7vuxW6k8haNMuaE5tmwNPEJgvm4TQ1Nzrvx+k
Kt6pVfXtyoRZ4XlPGWaIytrzjL/bPUgkFgrmyEEEsv12QjMPKF9pFfxaIiTuVAt1
UI5wobbfbrxKBN/ZiTbe17VpheZoZFF79MV2j1OytX+BAeffrdLFWFDrKaqJBNkC
bEfzhKeDA0i+nqnLxoNnMi+YXkUxslT7bgtj5fSR9fVIMLcSL1NhGGuWE+/81DVo
GY32vw==
-----END CERTIFICATE-----
```

### kubernetes clean up

```bash
kubectl delete -f k8s\ssldemo.yaml
```