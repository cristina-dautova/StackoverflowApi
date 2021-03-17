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

public static String STACKOVERFLOW_PARAMETERS_URL = "https://api.stackexchange.com/2.2/answers?site=stackoverflow&page=1&pagesize=10&order=desc&sort=activity&filter=default";
    private static RequestSpecification requestSpecification;

    public BaseTest() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(STACKOVERFLOW_PARAMETERS_URL);
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();
    }

    public Response getResponse(String url) {
        return given(requestSpecification).when().get(url);
    }

    private Root getItemObject() {
        String rootJson = getResponse(STACKOVERFLOW_PARAMETERS_URL).then()
                .extract().body().asString();
        Gson gson = new GsonBuilder().create();
        Root root = gson.fromJson(rootJson, Root.class);
        return root;
    }


}
