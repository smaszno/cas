package org.apereo.cas.digest.config;

import org.apereo.cas.authentication.adaptive.AdaptiveAuthenticationPolicy;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.digest.DefaultDigestHashedCredentialRetriever;
import org.apereo.cas.digest.DigestHashedCredentialRetriever;
import org.apereo.cas.digest.web.flow.DigestAuthenticationAction;
import org.apereo.cas.digest.web.flow.DigestAuthenticationWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowExecutionPlanConfigurer;
import org.apereo.cas.web.flow.resolver.CasDelegatingWebflowEventResolver;
import org.apereo.cas.web.flow.resolver.CasWebflowEventResolver;

import lombok.val;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.execution.Action;

/**
 * This is {@link DigestAuthenticationConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Configuration("digestAuthenticationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class DigestAuthenticationConfiguration {

    @Autowired
    private CasConfigurationProperties casProperties;

    @Autowired
    @Qualifier("loginFlowRegistry")
    private ObjectProvider<FlowDefinitionRegistry> loginFlowDefinitionRegistry;

    @Autowired
    private ObjectProvider<FlowBuilderServices> flowBuilderServices;

    @Autowired
    @Qualifier("adaptiveAuthenticationPolicy")
    private ObjectProvider<AdaptiveAuthenticationPolicy> adaptiveAuthenticationPolicy;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("serviceTicketRequestWebflowEventResolver")
    private ObjectProvider<CasWebflowEventResolver> serviceTicketRequestWebflowEventResolver;

    @Autowired
    @Qualifier("initialAuthenticationAttemptWebflowEventResolver")
    private ObjectProvider<CasDelegatingWebflowEventResolver> initialAuthenticationAttemptWebflowEventResolver;

    @ConditionalOnMissingBean(name = "digestAuthenticationWebflowConfigurer")
    @Bean
    @DependsOn("defaultWebflowConfigurer")
    public CasWebflowConfigurer digestAuthenticationWebflowConfigurer() {
        return new DigestAuthenticationWebflowConfigurer(flowBuilderServices.getObject(),
            loginFlowDefinitionRegistry.getObject(), applicationContext, casProperties);
    }

    @Autowired
    @Bean
    public Action digestAuthenticationAction(@Qualifier("defaultDigestCredentialRetriever") final DigestHashedCredentialRetriever defaultDigestCredentialRetriever) {
        return new DigestAuthenticationAction(initialAuthenticationAttemptWebflowEventResolver.getIfAvailable(),
            serviceTicketRequestWebflowEventResolver.getIfAvailable(),
            adaptiveAuthenticationPolicy.getIfAvailable(),
            casProperties.getAuthn().getDigest().getRealm(),
            casProperties.getAuthn().getDigest().getAuthenticationMethod(),
            defaultDigestCredentialRetriever);
    }

    @ConditionalOnMissingBean(name = "defaultDigestCredentialRetriever")
    @Bean
    @RefreshScope
    public DigestHashedCredentialRetriever defaultDigestCredentialRetriever() {
        val digest = casProperties.getAuthn().getDigest();
        return new DefaultDigestHashedCredentialRetriever(digest.getUsers());
    }

    @Bean
    @ConditionalOnMissingBean(name = "digestAuthenticationCasWebflowExecutionPlanConfigurer")
    public CasWebflowExecutionPlanConfigurer digestAuthenticationCasWebflowExecutionPlanConfigurer() {
        return plan -> plan.registerWebflowConfigurer(digestAuthenticationWebflowConfigurer());
    }
}
