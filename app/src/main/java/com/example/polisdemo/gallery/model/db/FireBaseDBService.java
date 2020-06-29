package com.example.polisdemo.gallery.model.db;

import java.util.List;
import java.util.Optional;

public interface FireBaseDBService {
    Optional<LabelEntity> getEntity(final long id);

    List<LightLabelEntity> getEntities(final String label);

    List<LightLabelEntity> getEntities(final List<String> labels);

    List<LightLabelEntityV2> getOneEntitiesForEachLabel();

    List<String> getLabels();

    long putEntity(final LabelEntity entity);

    void putAllEntities(final List<LabelEntity> entities);

    long size();

    Optional<LabelEntity> getNewestEntry();
}
