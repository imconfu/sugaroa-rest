package com.sugaroa.rest.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoleListSerializer extends StdSerializer<List<Role>> {

    public RoleListSerializer() {
        this(null);
    }

    protected RoleListSerializer(Class<List<Role>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Role> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Integer> ids = new ArrayList<>();
        for (Role item : value) {
            ids.add(item.getId());
        }
        gen.writeObject(ids);

    }
}
