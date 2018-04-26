package univ_smb.m1.info803;

import org.junit.Test;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.utils.Serializer;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PipeTest {
    @Test
    public void should_Get_55() throws IOException {
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader();
        pipedReader.connect(pipedWriter);

        pipedWriter.write(55);

        assertEquals(pipedReader.read(), 55);
    }

    @Test
    public void should_Get_A_Spec() throws IOException, ClassNotFoundException {
        PipedWriter pipedWriter = new PipedWriter();
        PipedReader pipedReader = new PipedReader(pipedWriter);

        Specification spec = new Specification(new ArrayList<>(), 10, 20, 30);

        Serializer<Specification> specSerializer = new Serializer<>();
        String rawData = specSerializer.serialize(spec);
        pipedWriter.write(rawData + "\n");

        BufferedReader bufferedReader = new BufferedReader(pipedReader);

        Specification result = specSerializer.deserialize(bufferedReader.readLine());
        System.out.println(result);
    }
}
