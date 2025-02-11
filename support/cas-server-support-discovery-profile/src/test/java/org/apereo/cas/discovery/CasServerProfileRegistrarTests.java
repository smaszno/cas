package org.apereo.cas.discovery;

import org.apereo.cas.audit.spi.config.CasCoreAuditConfiguration;
import org.apereo.cas.authentication.mfa.TestMultifactorAuthenticationProvider;
import org.apereo.cas.config.CasCoreAuthenticationHandlersConfiguration;
import org.apereo.cas.config.CasCoreAuthenticationPrincipalConfiguration;
import org.apereo.cas.config.CasCoreAuthenticationSupportConfiguration;
import org.apereo.cas.config.CasCoreHttpConfiguration;
import org.apereo.cas.config.CasCoreServicesConfiguration;
import org.apereo.cas.config.CasCoreTicketIdGeneratorsConfiguration;
import org.apereo.cas.config.CasCoreTicketsConfiguration;
import org.apereo.cas.config.CasCoreUtilConfiguration;
import org.apereo.cas.config.CasCoreWebConfiguration;
import org.apereo.cas.config.CasDiscoveryProfileConfiguration;
import org.apereo.cas.config.CasPersonDirectoryConfiguration;
import org.apereo.cas.config.support.CasWebApplicationServiceFactoryConfiguration;
import org.apereo.cas.support.pac4j.config.support.authentication.Pac4jAuthenticationEventExecutionPlanConfiguration;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link CasServerProfileRegistrarTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@SpringBootTest(classes = {
    RefreshAutoConfiguration.class,
    CasWebApplicationServiceFactoryConfiguration.class,
    Pac4jAuthenticationEventExecutionPlanConfiguration.class,
    CasDiscoveryProfileConfiguration.class,
    CasCoreServicesConfiguration.class,
    CasPersonDirectoryConfiguration.class,
    CasCoreUtilConfiguration.class,
    CasCoreAuditConfiguration.class,
    CasCoreTicketIdGeneratorsConfiguration.class,
    CasCoreTicketsConfiguration.class,
    CasCoreAuthenticationPrincipalConfiguration.class,
    CasCoreAuthenticationSupportConfiguration.class,
    CasCoreWebConfiguration.class,
    CasCoreHttpConfiguration.class,
    CasCoreAuthenticationHandlersConfiguration.class
})
@DirtiesContext
public class CasServerProfileRegistrarTests {
    @Autowired
    @Qualifier("casServerProfileRegistrar")
    private CasServerProfileRegistrar casServerProfileRegistrar;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    public void verifyAction() {
        TestMultifactorAuthenticationProvider.registerProviderIntoApplicationContext(applicationContext);
        val profile = casServerProfileRegistrar.getProfile();
        assertNotNull(profile);
        assertNotNull(profile.getAvailableAttributes());
        assertNotNull(profile.getDelegatedClientTypes());
        assertNotNull(profile.getDelegatedClientTypesSupported());
        assertNotNull(profile.getMultifactorAuthenticationProviderTypes());
        assertNotNull(profile.getMultifactorAuthenticationProviderTypesSupported());
        assertNotNull(profile.getRegisteredServiceTypes());
        assertNotNull(profile.getRegisteredServiceTypesSupported());
    }
}
