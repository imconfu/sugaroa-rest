package com.sugaroa.rest.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PermissionListSerializer extends StdSerializer<List<Permission>> {

    public PermissionListSerializer() {
        this(null);
    }

    protected PermissionListSerializer(Class<List<Permission>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Permission> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Integer> ids = new ArrayList<>();
        for (Permission item : value) {
            ids.add(item.getId());
        }
        gen.writeObject(ids);

    }
}
