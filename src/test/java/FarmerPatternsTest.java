import farmers.model.FarmerPatterns;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cvasquez on 30.09.15.
 */
public class FarmerPatternsTest {

    @Test
    public void testFindMAP_NAME() throws Exception {
        String input = "initializeSolo();showAddressSolo(\"PCG\",-50.9436628,+3.5279977,15,\"Karreweg 6 9770 Kruishoutem\",\"2709\",\"v\");";
        Assert.assertEquals("PCG", FarmerPatterns.MAPNAME.findOne(input));
    }

    @Test
    public void testFindCOORDINATES() throws Exception {
        String input = "initializeSolo();showAddressSolo(\"PCG\",-50.9436628,+3.5279977,15,\"Karreweg 6 9770 Kruishoutem\",\"2709\",\"v\");";
        List<String> result = FarmerPatterns.COORDINATES.find(input);
        Assert.assertTrue(result.equals(Arrays.asList("-50.9436628", "3.5279977")));
    }

    @Test
    public void testFindADDRESS() throws Exception {
        String input = "initializeSolo();showAddressSolo(\"PCG\",-50.9436628,+3.5279977,15,\"Karreweg 6 9770 Kruishoutem\",\"2709\",\"v\");";
        Assert.assertEquals("Karreweg 6 9770 Kruishoutem", FarmerPatterns.ADDRESS.findOne(input));
    }

}