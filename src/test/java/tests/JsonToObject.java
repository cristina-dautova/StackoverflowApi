package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jsonobjects.Root;

import static io.restassured.RestAssured.given;

public class JsonToObject {

    public static void main(String[] args) {
        Root item = getItemObject();
        System.out.println(item);
    }

    private static Root getItemObject() {

        String rootJson = given().when()
                .get("https://api.stackexchange.com/2.2/answers?site=stackoverflow&page=1&pagesize=10&order=desc&sort=activity&filter=default").then()
                .extract().body().asString();
        Gson gson = new GsonBuilder().create();
        Root root = gson.fromJson(rootJson, Root.class);

        return root;
    }
}
