package api.spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class CinescopeSpecs {


    private static final String authBaseUri = "https://auth.cinescope.t-qa.ru";
    private static final String apiBaseUri = "https://auth.cinescope.t-qa.ru";
    private static final String contentType = "application/json";

    public static RequestSpecification authRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(authBaseUri)
                .addHeader("accept", contentType)
                .addHeader("Content-Type", contentType)
                .build();
    }

    public static RequestSpecification apiRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(apiBaseUri)
                .addHeader("accept", contentType)
                .addHeader("Content-Type", contentType)
                .build();
    }

    public static ResponseSpecification responseSpecOk() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(contentType)
                .build();

    }

    public static ResponseSpecification responseSpecCreatedOk() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(contentType)
                .build();
    }

    public static ResponseSpecification responseSpecNotFound() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectContentType(contentType)
                .build();
    }

    public static ResponseSpecification responseSpecConflict() {
        return new ResponseSpecBuilder()
                .expectStatusCode(409)
                .expectContentType(contentType)
                .build();
    }

    public static ResponseSpecification responseSpecUnauthorized() {
        return new ResponseSpecBuilder()
                .expectStatusCode(403)
                .expectContentType(contentType)
                .build();
    }
}
