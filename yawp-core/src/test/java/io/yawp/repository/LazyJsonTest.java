package io.yawp.repository;

import static org.junit.Assert.assertEquals;
import io.yawp.commons.utils.EndpointTestCase;
import io.yawp.commons.utils.JsonUtils;
import io.yawp.repository.models.basic.BasicObject;
import io.yawp.repository.models.basic.Pojo;

import java.util.Arrays;


import org.junit.Test;

public class LazyJsonTest extends EndpointTestCase {

    @Test
    public void testSerialize() {
        Pojo pojo = new Pojo("xpto");

        LazyJson<Pojo> lazyJson = LazyJson.create(pojo);

        assertEquals(JsonUtils.to(pojo), JsonUtils.to(lazyJson));
    }

    @Test
    public void testDeserialize() {
        Pojo pojo = new Pojo("xpto");

        LazyJson<Pojo> lazyJson = LazyJson.create(pojo);

        assertEquals("xpto", lazyJson.get().getStringValue());
    }
    
    @Test
    public void testGetCacheChangeJson() {
        Pojo pojo = new Pojo("xpto");
        LazyJson<Pojo> lazyJson = LazyJson.create(pojo);
        
        lazyJson.get().setStringValue("otpx");
        
        assertEquals(JsonUtils.to(new Pojo("otpx")), lazyJson.getJson());
    }


    @Test
    public void testSerializerDeserializeInner() {
        BasicObject object = new BasicObject();
        object.setLazyPojo(new Pojo("xpto"));

        String json = JsonUtils.to(object);
        BasicObject parsedObject = from(json, BasicObject.class);

        assertEquals("xpto", parsedObject.getLazyPojo().getStringValue());
    }

    @Test
    public void testSaveAndLoad() {
        BasicObject object = new BasicObject();
        object.setLazyPojo(new Pojo("xpto"));
        yawp.save(object);

        BasicObject retrievedObject = object.getId().fetch();

        assertEquals("xpto", retrievedObject.getLazyPojo().getStringValue());
    }

}
