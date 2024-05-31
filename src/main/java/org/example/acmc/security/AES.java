package org.example.acmc.security;

import jakarta.persistence.AttributeConverter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

@Configuration
public class AES implements AttributeConverter<Object, String> {

    @Value("${aes.encryption.key}")
    private String encryptionKey;

    private final String encryptionCipher = "AES/ECB/PKCS5Padding";

    private Key key;
    private ThreadLocal<Cipher> cipherThreadLocal = ThreadLocal.withInitial(() -> {
        try {
            return Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to initialize Cipher", e);
        }
    });

    private Key getKey() throws GeneralSecurityException, UnsupportedEncodingException {
        if (key == null) {
            if (encryptionKey.length() != 16 && encryptionKey.length() != 24 && encryptionKey.length() != 32) {
                throw new GeneralSecurityException("Invalid key length: must be 16, 24, or 32 bytes");
            }
            key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        }
        return key;
    }

    private void initCipher(int encryptMode) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher cipher = cipherThreadLocal.get();
        cipher.init(encryptMode, getKey());
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }
        initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = SerializationUtils.serialize(attribute);
        return Base64.getEncoder().encodeToString(cipherThreadLocal.get().doFinal(bytes));
    }

    @SneakyThrows
    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        initCipher(Cipher.DECRYPT_MODE);
        byte[] decodedBytes = Base64.getDecoder().decode(dbData);
        byte[] bytes = cipherThreadLocal.get().doFinal(decodedBytes);
        return SerializationUtils.deserialize(bytes);
    }
}
