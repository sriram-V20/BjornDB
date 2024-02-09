package com.Bjorn.table;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializableTable implements Table, Serializable {
    private static final Kryo kryo = new Kryo();

    private final Map<String, Pointer> table;

    public SerializableTable() {
        kryo.register(SerializableTable.class);
        kryo.register(HashMap.class);
        kryo.register(Pointer.class);
        this.table = new HashMap<>();
    }

    public static SerializableTable fromEmpty() {
        return new SerializableTable();
    }

    public static SerializableTable fromFile(String filePath) throws Exception {
        kryo.register(SerializableTable.class);
        kryo.register(HashMap.class);
        kryo.register(Pointer.class);
        try (Input input = new Input(new FileInputStream(filePath))) {
            return kryo.readObject(input, SerializableTable.class);
        } catch (KryoException e) {
            throw new Exception("Failed to load FileTable from disk: " + e.getMessage());
        }
    }

    @Override
    public void put(byte[] key, Pointer value) {
        if (key != null && value != null) {
            table.put(new String(key), value);
        }
    }



    @Override
    public Pointer get(byte[] key) {
        if (key != null) {
            return table.get(new String(key));
        }
        return null;
    }

    @Override
    public void saveToDisk(String filePath) throws FileNotFoundException {
        Output output = new Output(new FileOutputStream(filePath));
        kryo.writeObject(output, this);
        output.close();
    }

}
