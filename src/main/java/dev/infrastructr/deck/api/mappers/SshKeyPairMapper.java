package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.data.entities.SshKeyPair;
import org.mapstruct.Mapper;

@Mapper
public interface SshKeyPairMapper {

    SshKeyPair map(dev.infrastructr.deck.ssh.entities.SshKeyPair source);
}
