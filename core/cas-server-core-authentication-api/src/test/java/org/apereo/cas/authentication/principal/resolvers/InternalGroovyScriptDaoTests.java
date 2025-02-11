package org.apereo.cas.authentication.principal.resolvers;

import org.apereo.cas.configuration.CasConfigurationProperties;

import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link InternalGroovyScriptDaoTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Tag("Groovy")
@SpringBootTest(classes = RefreshAutoConfiguration.class)
@EnableConfigurationProperties(CasConfigurationProperties.class)
@TestPropertySource(properties = "cas.authn.attributeRepository.groovy[0].location=classpath:GroovyAttributeDao.groovy")
public class InternalGroovyScriptDaoTests {
    @Autowired
    private CasConfigurationProperties casProperties;

    @Test
    public void verifyAction() {
        val d = new InternalGroovyScriptDao(new StaticApplicationContext(), casProperties);
        val results = d.getAttributesForUser("casuser");
        assertFalse(results.isEmpty());
        assertFalse(d.getPersonAttributesFromMultivaluedAttributes(new HashMap(results)).isEmpty());
    }
}
