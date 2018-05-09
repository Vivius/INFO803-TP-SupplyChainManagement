package univ_smb.m1.info803.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.List;

public class Pipe<T> {
    private PipedReader reader;
    private PipedWriter writer;
    private Serializer<T> serializer;
    private BufferedReader buffer;

    public Pipe() throws IOException {
        this.writer = new PipedWriter();
        this.reader = new PipedReader(this.writer);
        this.serializer = new Serializer<>();
        this.buffer = new BufferedReader(this.reader);
    }

    public void write(T object) throws IOException {
        writer.write(serializer.serialize(object) + "\n");
        writer.flush();
    }

    public void writeForEach(List<Runnable> runnables, Class targetRunnable, T object) throws IOException {
        for(Runnable run : runnables) {
            if(run.getClass().equals(targetRunnable)) {
                write(object);
            }
        }
    }

    public T read() throws IOException, ClassNotFoundException {
        String result = buffer.readLine();
        return serializer.deserialize(result);
    }

    public boolean ready() throws IOException {
        return reader.ready();
    }
}
