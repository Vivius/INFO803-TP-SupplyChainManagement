package univ_smb.m1.info803.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Transmission<T> {
    private PipedReader reader;
    private PipedWriter writer;
    private Serializer<T> serializer;

    public Transmission() throws IOException {
        this.writer = new PipedWriter();
        this.reader = new PipedReader(this.writer);
        this.serializer = new Serializer<>();
    }

    public void write(T object) throws IOException {
        writer.write(serializer.serialize(object) + "\n");
        writer.flush();
    }

    public T read() throws IOException, ClassNotFoundException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String result = bufferedReader.readLine();
        return serializer.deserialize(result);
    }
}
