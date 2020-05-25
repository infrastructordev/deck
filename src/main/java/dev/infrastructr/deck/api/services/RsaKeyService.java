package dev.infrastructr.deck.api.services;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;

@Service
public class RsaKeyService {




    private static byte[] encode(RSAPublicKey key)
        throws IOException
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] name = "ssh-rsa".getBytes(StandardCharsets.US_ASCII);
        write(name, buf);
        write(key.getPublicExponent().toByteArray(), buf);
        write(key.getModulus().toByteArray(), buf);
        return buf.toByteArray();
    }

    private static void write(byte[] str, OutputStream os)
        throws IOException
    {
        for (int shift = 24; shift >= 0; shift -= 8)
            os.write((str.length >>> shift) & 0xFF);
        os.write(str);
    }
}
