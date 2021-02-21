package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.PropertyReader;

import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class BaseTest {

    public static String STACKOVERFLOW_ANSWERS_URL = buildURL(PropertyReader.getBaseURL(), PropertyReader.getEndPoint(), PropertyReader.getParameters());
    private static RequestSpecification requestSpecification;

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

    public static void verifyResponseArraySize(String jsonPath, Integer maxUsersNumber) {
        getResponse().then().assertThat().body(jsonPath + ".size()", lessThanOrEqualTo(maxUsersNumber));
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

    public static void verifyUserLinkIsCorrect(String path, String link, String... keysToCheck) {
        List<LinkedHashMap> ownerValues = getListFromJson(getResponse(), path);
        for (LinkedHashMap ownerNode : ownerValues) {
            if (ownerNode == null) continue;
            String ownerLink = ownerNode.get(link).toString();
            for (String pathElement : keysToCheck) {
                String value = ownerNode.get(pathElement).toString().replace(" ", "-");
                assertThat(ownerLink).as("Owner link doesn't contain [" + pathElement + "]")
                        .containsIgnoringCase(value);
            }
        }
    }
}
