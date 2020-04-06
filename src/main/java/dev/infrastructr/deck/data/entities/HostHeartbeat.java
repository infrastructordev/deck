package dev.infrastructr.deck.data.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "host_heartbeats")
public class HostHeartbeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "host_id")
    private Host host;

    @Type(type = "jsonb")
    private Map<String, Object> value;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
}
