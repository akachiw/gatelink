/*
 */
package com.airhacks.gatelink.health;

import com.airhacks.gatelink.SystemTest;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author airhacks.com
 */
public class HealthCheckIT {

    private WebTarget tut;

    @BeforeEach
    public void init() {
        this.tut = SystemTest.context("health");
    }

    /**
     * {"checks":[{"data":{},"name":"pushserver","state":"UP"}],"outcome":"UP"}
     */
    @Test
    public void health() {
        Response response = this.tut.request().get();
        assertThat(response.getStatus(), is(200));
        JsonObject health = response.readEntity(JsonObject.class);
        JsonArray checks = health.getJsonArray("checks");
        List<JsonObject> checkList = checks.getValuesAs(JsonObject.class);
        long checkCount = checkList.
                stream().
                filter(check -> "pushserver".equals(check.getString("name"))).count();
        assertThat(checkCount, is(1l));
    }


}
