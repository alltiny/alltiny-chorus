package org.alltiny.chorus.model.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DOMOperation {

    private final UUID uuid;
    private final String description;

    private final List<Consumer<DOMOperation>> conclusionListener = new ArrayList<>();

    private boolean concluded = false;

    public DOMOperation(String description) {
        this(UUID.randomUUID(), description);
    }

    public DOMOperation(UUID uuid, String description) {
        this.uuid = uuid;
        this.description = description;
    }

    public DOMOperation addConclusionListener(Consumer<DOMOperation> listener) {
        if (listener != null && !conclusionListener.contains(listener)) {
            conclusionListener.add(listener);
        }
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDescription() {
        return description;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public DOMOperation conclude() {
        if (!concluded) {
            concluded = true;
            conclusionListener.forEach(l -> l.accept(this));
        }
        return this;
    }

    @Override
    public String toString() {
        return "DOMOperation{" +
            "uuid=" + uuid +
            ", description='" + description + '\'' +
            '}';
    }
}
