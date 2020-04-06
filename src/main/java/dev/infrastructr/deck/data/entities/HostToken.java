package dev.infrastructr.deck.data.entities;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "host_tokens")
public class HostToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    private String value;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "host_id")
    private Host host;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
