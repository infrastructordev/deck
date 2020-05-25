package dev.infrastructr.deck.ssh.sevices;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import dev.infrastructr.deck.ssh.entities.SshKeyPair;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class SshKeyPairService {

    private final JSch jSch = new JSch();

    public SshKeyPair generate() {
        try {
            com.jcraft.jsch.KeyPair keyPair = com.jcraft.jsch.KeyPair.genKeyPair(jSch, com.jcraft.jsch.KeyPair.RSA);

            SshKeyPair sshKeyPair = new SshKeyPair();
            sshKeyPair.setPublicKey(getPublicKeyAsStr(keyPair));
            sshKeyPair.setPrivateKey(getPrivateKeyAsStr(keyPair));

            return sshKeyPair;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate SSH key pair.", e);
        }
    }

    private String getPrivateKeyAsStr(KeyPair keyPair) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        keyPair.writePrivateKey(byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }

    private String getPublicKeyAsStr(KeyPair keyPair) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        keyPair.writePublicKey(byteArrayOutputStream, "");
        return byteArrayOutputStream.toString();
    }
}
