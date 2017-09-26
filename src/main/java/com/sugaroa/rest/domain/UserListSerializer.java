package com.sugaroa.rest.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListSerializer extends StdSerializer<List<User>> {

    public UserListSerializer() {
        this(null);
    }

    protected UserListSerializer(Class<List<User>> t) {
        super(t);
    }

    @Override
    public void serialize(List<User> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Integer> ids = new ArrayList<>();
        for (User item : value) {
            ids.add(item.getId());
        }
        gen.writeObject(ids);

    }
}
