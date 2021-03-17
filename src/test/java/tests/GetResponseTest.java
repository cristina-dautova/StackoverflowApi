package tests;

import jsonobjects.Root;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class GetResponseTest extends BaseTest {

    @Test(priority = 1)
    public void assertStatusCode() {
        verifyStatusCode(200);
    }

    @Test(priority = 2)
    public void assertArraySize() {

        verifyResponseArraySize(root.backoff, 10);
    }

    @Test(priority = 3)
    public void assertThatEachItemHasOwner() {

        getResponse().then().assertThat().body("items", everyItem(hasKey("owner")));

        //verifyParentElementContainsChildElement("items", "owner");
    }

    @Test(priority = 4)
    public void isObjectLinkCorrect() {

//        assertThat(root.items.stream().map(e -> e.owner.link)).as("Owner link doesn't contain [" + "]")
//                .contains(root.items.stream().map(e -> e.owner.display_name.toLowerCase().replace(" ", "-")).collect(Collectors.toList()).toString());

        System.out.println(root.items.stream().map(e -> e.owner.link).collect(Collectors.toList()));



        assertThat(root.items.stream().map(e -> e.owner.link).collect(Collectors.toList())).as("Owner link doesn't contain [" + "]")
                .contains(root.items.stream().map(e -> e.owner.user_id).collect(Collectors.toList()).toString());

        for (int i = 0; i < root.items.size(); i++) {
            String link = root.items.get(i).owner.link;
            System.out.println(link);
            String userId = root.items.get(i).owner.user_id;
            System.out.println(userId);
            String displayName = root.items.get(i).owner.display_name;
            System.out.println(displayName);
        }


        //verifyUserLinkIsCorrect("items.owner", "link", "display_name", "user_id");
    }
}
