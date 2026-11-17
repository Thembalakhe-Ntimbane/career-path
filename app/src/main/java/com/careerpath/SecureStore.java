package com.careerpath;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

final class SecureStore {
    private static final String PREFS = "career_path_secure";
    private static final String AES_ALIAS = "career_path_aes_v1";
    private static final String RSA_ALIAS = "career_path_rsa_v1";
    private final Context context;
    private final SharedPreferences preferences;

    SecureStore(Context context) {
        this.context = context.getApplicationContext();
        this.preferences = this.context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    synchronized void put(String name, String value) {
        try {
            byte[] plaintext = value.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cipher.init(Cipher.ENCRYPT_MODE, getOrCreateAesKey());
                preferences.edit()
                        .putString(name + ".iv", encode(cipher.getIV()))
                        .putString(name + ".data", encode(cipher.doFinal(plaintext)))
                        .remove(name + ".wrapped")
                        .apply();
            } else {
                byte[] keyBytes = new byte[32];
                new SecureRandom().nextBytes(keyBytes);
                SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                Cipher wrapper = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                wrapper.init(Cipher.ENCRYPT_MODE, getRsaKeyStore().getCertificate(RSA_ALIAS).getPublicKey());
                preferences.edit()
                        .putString(name + ".iv", encode(cipher.getIV()))
                        .putString(name + ".data", encode(cipher.doFinal(plaintext)))
                        .putString(name + ".wrapped", encode(wrapper.doFinal(keyBytes)))
                        .apply();
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Could not encrypt local data.", exception);
        }
    }

    synchronized String get(String name) {
        String encodedData = preferences.getString(name + ".data", null);
        String encodedIv = preferences.getString(name + ".iv", null);
        if (encodedData == null || encodedIv == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cipher.init(Cipher.DECRYPT_MODE, getOrCreateAesKey(),
                        new GCMParameterSpec(128, decode(encodedIv)));
            } else {
                String wrappedKey = preferences.getString(name + ".wrapped", null);
                if (wrappedKey == null) {
                    throw new IllegalStateException("Encrypted data is missing its wrapped key.");
                }
                Cipher wrapper = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                wrapper.init(Cipher.DECRYPT_MODE, getRsaKeyStore().getKey(RSA_ALIAS, null));
                byte[] keyBytes = wrapper.doFinal(decode(wrappedKey));
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"),
                        new GCMParameterSpec(128, decode(encodedIv)));
            }
            return new String(cipher.doFinal(decode(encodedData)), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not decrypt local data.", exception);
        }
    }

    synchronized void remove(String name) {
        preferences.edit().remove(name + ".iv").remove(name + ".data")
                .remove(name + ".wrapped").apply();
    }

    private SecretKey getOrCreateAesKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (keyStore.containsAlias(AES_ALIAS)) {
            return (SecretKey) keyStore.getKey(AES_ALIAS, null);
        }
        KeyGenerator generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        generator.init(new KeyGenParameterSpec.Builder(AES_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build());
        return generator.generateKey();
    }

    private KeyStore getRsaKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (!keyStore.containsAlias(RSA_ALIAS)) {
            Calendar start = Calendar.getInstance();
            Calendar end = (Calendar) start.clone();
            end.add(Calendar.YEAR, 30);
            AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(RSA_ALIAS)
                    .setSubject(new X500Principal("CN=CareerPathLocalStore"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            generator.initialize(spec);
            generator.generateKeyPair();
            keyStore.load(null);
        }
        return keyStore;
    }

    private static String encode(byte[] bytes) {
        return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP);
    }

    private static byte[] decode(String value) {
        return android.util.Base64.decode(value, android.util.Base64.NO_WRAP);
    }
}
