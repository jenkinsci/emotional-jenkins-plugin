package org.jenkinsci.plugins.emotional_jenkins;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hudson.model.FreeStyleProject;
import org.htmlunit.Page;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

/**
 * The Emotional Jenkins post-build action has help text, shown by the ? icon in the job configuration and in the
 * Pipeline Syntax snippet generator.
 */
@WithJenkins
class PublisherHelpTest {

    @Test
    void theHelpFileExplainsTheMoodsAndThePipelineStep(JenkinsRule j) throws Exception {
        EmotionalJenkinsPublisher.DescriptorImpl descriptor =
                j.jenkins.getDescriptorByType(EmotionalJenkinsPublisher.DescriptorImpl.class);
        try (JenkinsRule.WebClient wc = j.createWebClient()) {
            // the URL behind the ? icon of the post-build action and of the snippet generator
            Page page = wc.goTo("descriptor/" + descriptor.getId() + "/help", null);
            assertTrue(page.getWebResponse().getStatusCode() == 200, "the post-build action has no help");
            String help = page.getWebResponse().getContentAsString();
            for (String expected : new String[] {"happy", "worried", "angry", "disappointed", "emotionalJenkins()"}) {
                assertTrue(help.contains(expected), "the help should mention " + expected);
            }
        }
    }

    @Test
    void theJobConfigurationLinksTheHelp(JenkinsRule j) throws Exception {
        FreeStyleProject p = j.createFreeStyleProject();
        p.getPublishersList().add(new EmotionalJenkinsPublisher());
        try (JenkinsRule.WebClient wc = j.createWebClient()) {
            HtmlPage config = wc.getPage(p, "configure");
            assertNotNull(
                    config.querySelector("[helpURL$='EmotionalJenkinsPublisher/help']"),
                    "the post-build action in the job configuration has no ? help link");
        }
    }
}
