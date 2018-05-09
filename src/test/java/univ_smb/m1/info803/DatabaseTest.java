package univ_smb.m1.info803;

import org.junit.Test;
import univ_smb.m1.info803.model.Specification;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DatabaseTest {
    @Test
    public void should_Get_Specification() {
        Database db = Database.getInstance();

        Specification spec_1 = db.addSpecification(new Specification(new ArrayList<>(), 10, 20, 30));
        Specification spec_2 = db.addSpecification(new Specification(new ArrayList<>(), 10, 20, 30));

        System.out.println(db.getSpecification(spec_1.getId()));
        System.out.println(db.getSpecification(spec_2.getId()));

        assertEquals(spec_1.getId(), 1);
        assertEquals(spec_2.getId(), 2);
    }
}
