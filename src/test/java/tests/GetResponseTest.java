package tests;

import org.testng.annotations.Test;

public class GetResponseTest extends BaseTest {
    @Test(priority = 1)
    public static void assertStatusCode() {
        verifyStatusCode(200);
    }

    @Test(priority = 2)
    public static void assertArraySize() {
        verifyResponseArraySize("items.owner", 10);
    }

    @Test(priority = 3)
    public static void assertThatEachItemHasOwner() {
        verifyParentElementContainsChildElement("items", "owner");
    }

    @Test(priority = 4)
    public static void isObjectLinkCorrect() {
        verifyUserLinkIsCorrect("items.owner", "link", "display_name", "user_id");
    }
}
