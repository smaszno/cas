package org.apereo.cas.support.geo.google;

import org.apereo.cas.authentication.adaptive.geo.GeoLocationService;
import org.apereo.cas.support.geo.config.GoogleMapsGeoCodingConfiguration;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link GoogleMapsGeoLocationServiceTests}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@SpringBootTest(classes = {
    RefreshAutoConfiguration.class,
    GoogleMapsGeoCodingConfiguration.class
})
@TestPropertySource(properties = "cas.googleMaps.apiKey=AIzaSyCea6zDOkwJVIOm0vZyAI5eHYrz9Vzlhi9")
public class GoogleMapsGeoLocationServiceTests {
    @Autowired
    @Qualifier("geoLocationService")
    private GeoLocationService geoLocationService;

    @Test
    public void verifyOperation() {
        assertNotNull(geoLocationService);
        val resp = geoLocationService.locate(40.689060, -74.044636);
        assertEquals(40.689060, resp.getLatitude());
        assertEquals(-74.044636, resp.getLongitude());
        assertTrue(resp.getAddresses().isEmpty());
    }
}
