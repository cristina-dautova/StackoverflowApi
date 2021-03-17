package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import jsonobjects.Item;
import jsonobjects.Root;
import utils.PropertyReader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class BaseTest {

    public static String STACKOVERFLOW_ANSWERS_URL = buildURL(PropertyReader.getBaseURL(), PropertyReader.getEndPoint(), PropertyReader.getParameters());
    private static RequestSpecification requestSpecification;
    Root root = getItemObject();

    public BaseTest() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(STACKOVERFLOW_ANSWERS_URL);
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();
    }

    public static String buildURL(String baseURL, String endPoint, String parameters) {
        StringBuilder completeURL = new StringBuilder(baseURL);
        return completeURL.append(endPoint).append(parameters).toString();
    }

    public static Response getResponse() {
        return given(requestSpecification).when().get(STACKOVERFLOW_ANSWERS_URL);
    }

    public static void verifyStatusCode(Integer code) {
        getResponse().then().assertThat().statusCode(code);
    }

    public static void verifyResponseArraySize(int arrayResponseSize, int expectedArraySize) {
        getResponse().then().assertThat().body(String.valueOf(arrayResponseSize), lessThanOrEqualTo(expectedArraySize));
    }

    public static void verifyParentElementContainsChildElement(String pathToList, String jsonItem) {
        List<LinkedHashMap> item = getListFromJson(getResponse(), pathToList);
        for (LinkedHashMap itemNode : item) {
            Object ownerObj = itemNode.get(jsonItem);
            assertThat(ownerObj).as("HashMap element doesn't contain key [" + jsonItem + "]").isNotEqualTo(null);
        }
    }

    private static List<LinkedHashMap> getListFromJson(Response response, String path) {
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.getList(path);
    }

    public void verifyUserLinkIsCorrect(List<Item> listOfItems, String link1, String... keysToCheck) {
        //listOfItems root.items
        assertThat(root.items.stream().map(e -> e.owner.link)).as("Owner link doesn't contain [" + "]")
                       .contains(root.items.stream().map(e -> e.owner.display_name).collect(Collectors.toList()).toString());

        for (int i = 0; i < listOfItems.size(); i++) {
            String link = listOfItems.get(i).owner.link;
            System.out.println(link);
            String userId = root.items.get(i).owner.user_id;
            System.out.println(userId);
            String displayName = root.items.get(i).owner.display_name;
            System.out.println(displayName);
        }
    }

//    public static void verifyUserLinkIsCorrect(String path, String link, String... keysToCheck) {
//        List<LinkedHashMap> ownerValues = getListFromJson(getResponse(), path);
//        for (LinkedHashMap ownerNode : ownerValues) {
//            if (ownerNode == null) continue;
//            String ownerLink = ownerNode.get(link).toString();
//            for (String pathElement : keysToCheck) {
//                String value = ownerNode.get(pathElement).toString().replace(" ", "-");
//                assertThat(ownerLink).as("Owner link doesn't contain [" + pathElement + "]")
//                        .containsIgnoringCase(value);
//            }
//        }
//    }

    static Root getItemObject() {

        String rootJson = given().when()
                .get("https://api.stackexchange.com/2.2/answers?site=stackoverflow&page=1&pagesize=10&order=desc&sort=activity&filter=default").then()
                .extract().body().asString();
        Gson gson = new GsonBuilder().create();
        Root root = gson.fromJson(rootJson, Root.class);

        return root;
    }
}
