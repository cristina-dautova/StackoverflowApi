package tests;

import jsonobjects.Item;
import jsonobjects.Owner;
import jsonobjects.Root;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetResponseTest extends BaseTest {

    @Test()
    public void assertArraySize() {

        SoftAssert softAssert = new SoftAssert();

        Root newRoot = getResponse(STACKOVERFLOW_PARAMETERS_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Root.class);

        softAssert.assertTrue(newRoot.backoff <= 10, "Item array size is incorrect: " + newRoot.backoff);

        for (Item item : newRoot.items) {
            Owner owner = item.owner;
            softAssert.assertNotNull(owner, "Item doesn't contain owner");

            if (owner == null) {
                continue;
            }

            softAssert.assertEquals(owner.numberFromLink(), owner.user_id, owner.link);
            softAssert.assertTrue(owner.link.contains("/" + owner.user_id + "/"),
                    owner.link + " vs. " + owner.user_id);
            softAssert.assertTrue(owner.link.endsWith("/" + owner.display_name.replace(" ", "-").toLowerCase()),
                    owner.link + " vs. " + owner.display_name);
        }
        softAssert.assertAll();
    }
}


