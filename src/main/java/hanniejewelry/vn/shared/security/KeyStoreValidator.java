////package hanniejewelry.vn.shared.security;
////
////import org.springframework.boot.ApplicationArguments;
////import org.springframework.boot.ApplicationRunner;
////import org.springframework.stereotype.Component;
////import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
////import org.springframework.vault.core.VaultTemplate;
////
////import java.io.InputStream;
////import java.security.KeyStore;
////
////@Component
////public class KeyStoreValidator implements ApplicationRunner {
////
////    private final KeystoreProperties keystoreProperties;
////    private final VaultTemplate vaultTemplate;
////
////    public KeyStoreValidator(KeystoreProperties keystoreProperties, VaultTemplate vaultTemplate) {
////        this.keystoreProperties = keystoreProperties;
////        this.vaultTemplate = vaultTemplate;
////    }
////
////    @Override
////    public void run(ApplicationArguments args) throws Exception {
////        try {
////            vaultTemplate.opsForKeyValue("secret", KeyValueBackend.KV_2).get("keystore", Object.class);
////        } catch (Exception e) {
////            System.err.println("Cannot connect to Vault server: " + e.getMessage());
////            System.exit(1);
////        }
////
////        String keystorePath = "/keystore.p12";
////        String keystorePassword = keystoreProperties.getPassword();
////
////        try (InputStream is = getClass().getResourceAsStream(keystorePath)) {
////            if (is == null) {
////                throw new IllegalStateException("Keystore file not found: " + keystorePath);
////            }
////            KeyStore ks = KeyStore.getInstance("PKCS12");
////            ks.load(is, keystorePassword.toCharArray());
////            System.out.println("Keystore loaded successfully.");
////        } catch (Exception ex) {
////            System.err.println("Keystore validation failed: " + ex.getMessage());
////            System.exit(1);
////        }
////    }
////}
//package hanniejewelry.vn.shared.security;
//
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//import java.security.KeyStore;
//
//@Component
//public class KeyStoreValidator implements ApplicationRunner {
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        String keystorePath = "/keystore.p12";
//        String keystorePassword = "nguyenhauweb";
//
//        try (InputStream is = getClass().getResourceAsStream(keystorePath)) {
//            if (is == null) {
//                throw new IllegalStateException("Keystore file not found: " + keystorePath);
//            }
//            KeyStore ks = KeyStore.getInstance("PKCS12");
//            ks.load(is, keystorePassword.toCharArray());
//        } catch (Exception ex) {
//            System.err.println("Keystore validation failed: " + ex.getMessage());
//            // Dừng app ngay
//            System.exit(1);
//        }
//    }
//}
