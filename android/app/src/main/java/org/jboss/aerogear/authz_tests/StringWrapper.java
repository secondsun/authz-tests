package org.jboss.aerogear.authz_tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.jboss.aerogear.android.core.RecordId;

import java.io.IOException;

/**
 * Created by summers on 7/7/17.
 */

public class StringWrapper {
    public static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(StringWrapper.class, new TypeAdapter<StringWrapper>() {
            @Override
            public void write(JsonWriter out, StringWrapper value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public StringWrapper read(JsonReader in) throws IOException {
                in.beginObject();
                in.nextName();

                StringWrapper wrapper = new StringWrapper(in.nextString());
                in.endObject();
                return wrapper;

            }
        });

        GSON = builder.create();

    }

    @RecordId
    private Integer id = null;

    private String string;

    public StringWrapper(String s) {
        this.string = s;
    }

    public String getString() {
        return string;
    }

    public StringWrapper setString(String string) {
        this.string = string;
        return this;
    }

    @Override
    public String toString() {
        return string;
    }

    public Integer  getId() {
        return null;
    }

    public void setId(Integer  id) {

    }



}
