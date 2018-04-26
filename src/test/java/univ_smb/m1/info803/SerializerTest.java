package univ_smb.m1.info803;

import org.junit.Test;
import univ_smb.m1.info803.model.Specification;
import univ_smb.m1.info803.utils.Serializer;

import java.io.IOException;
import java.util.ArrayList;

public class SerializerTest {
    @Test
    public void should_Serialize_And_Deserialize_Object() throws IOException, ClassNotFoundException {
        // L'objet à traiter
        Specification spec = new Specification(new ArrayList<>(), 1, 1, 1);

        Serializer<Specification> specSerializer = new Serializer<>();
        String serializedObject = specSerializer.serialize(spec);
        System.out.println(serializedObject);

        Specification deserializedSpec = specSerializer.deserialize(serializedObject);
        System.out.println(deserializedSpec);
    }
}
