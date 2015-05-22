package io.mikael.cors;

import org.testng.annotations.Test;

public class BuilderTest {

    @Test
    public void createBuilder() {

        final CorsOriginFilter f1 = new CorsFilterBuilder()
                .forAnyOrigin()
                    .allowMethods("GET", "POST")
                    .done()
                .build();

    }

}
