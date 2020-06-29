package com.example.polisdemo;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.FireBaseDBServiceImpl;
import com.example.polisdemo.gallery.model.db.FireBaseSQLHelper;
import com.example.polisdemo.gallery.model.db.LabelEntity;
import com.example.polisdemo.gallery.model.db.LightLabelEntity;
import com.example.polisdemo.gallery.model.db.LightLabelEntityV2;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(AndroidJUnit4.class)
public class DBSimpleTest {
    private static final String TAG = "DBSimpleTest.class";
    private Context context;
    private FireBaseSQLHelper helper;
    private FireBaseDBService service;


    @Before
    public void createDB() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        helper = new FireBaseSQLHelper(context, 13);
        service = new FireBaseDBServiceImpl(helper);
//        helper.getWritableDatabase().
    }

    @Test
    public void isEmpty() {
        Assert.assertEquals("db not empty", 0, service.size());
    }

    @Test
    public void insertRow() {
        LabelEntity entity = new LabelEntity("l1", "uri1", new Date(), new Date(), 0.5f, 0);
        service.putEntity(entity);
        Assert.assertEquals("Db contains more than 1 entity", 1, service.size());
    }

    @Test
    public void insertAndGetRow() {
        LabelEntity expected = new LabelEntity("l1", "uri1", new Date(), new Date(), 0.5f, 0);
        long id = service.putEntity(expected);
        Optional<LabelEntity> optionalActual = service.getEntity(id);
        Assert.assertTrue("Entity not retrieved by id " + id, optionalActual.isPresent());
        LabelEntity actual = optionalActual.get();
        Assert.assertEquals("Label not equals", expected.getLabel(), actual.getLabel());
        Assert.assertEquals("Content uri not equals", expected.getContentUri(), actual.getContentUri());
        Assert.assertEquals("Date not equals", expected.getDate(), actual.getDate());
        Assert.assertEquals("Confidence not equals", expected.getConfidence(), actual.getConfidence(), 0.01f);
    }

    @Test
    public void insertAndGetManyRows() {
        int count = 10;
        List<LabelEntity> entities = new ArrayList<>(count);
        Set<String> labels = new HashSet<>();
        for (int i = 0; i < count; i++) {
            entities.add(new LabelEntity("l" + (i % 2), "uri" + i, new Date(), new Date(), i, 0));
            labels.add("l" + (i % 2));
        }
        service.putAllEntities(entities);
        Assert.assertEquals("Count of rows not correct", entities.size(), service.size());
        for (String label : labels) {
            List<LightLabelEntity> actual = service.getEntities(label);
            List<LightLabelEntity> expected = entities.stream()
                    .filter(entity -> entity.getLabel().equals(label))
                    .map(entity -> new LightLabelEntity(entity.getContentUri(), entity.getPhotoDate(), 0))
                    .collect(Collectors.toList());
            Assert.assertTrue("service.getEntities did not return entities by label " + label, actual.containsAll(expected));
        }
    }

    @Test
    public void insertAndGetByMultipleLabels() {
        int count = 10;
        List<LabelEntity> entities = new ArrayList<>(count);
        List<String> labels = new ArrayList<>();
        labels.add("l1");
        labels.add("l2");
        labels.add("l3");

        for (int i = 0; i < count; i++) {
            entities.add(new LabelEntity("l1", "uri" + i, new Date(), new Date(), i, 0));
            entities.add(new LabelEntity("l2", "uri" + i, new Date(), new Date(), i, 0));
            entities.add(new LabelEntity("l3", "uri" + i, new Date(), new Date(), i, 0));
        }
        service.putAllEntities(entities);
//        Assert.assertEquals("Count of rows not correct", entities.size(), service.size());
//        List<LightLabelEntity> actual = service.getEntities(labels);
//        Set<LightLabelEntity> expected = entities.stream()
//                .map(entity -> new LightLabelEntity(entity.getContentUri(), entity.getPhotoDate(), 0))
//                .collect(Collectors.toSet());
//        Assert.assertTrue("service.getEntities did not return entities by labels " + labels, actual.containsAll(expected));
        List<LightLabelEntityV2> entityv2s = service.getOneEntitiesForEachLabel();
        System.out.println(entityv2s);
    }

    @After
    public void cleanDB() {
        helper.close();
        context.deleteDatabase(FireBaseSQLHelper.DB_NAME);
    }
}
